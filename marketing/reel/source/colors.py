"""Per-render colour counts (laser red, flux cyan, rift magenta, white) of a clip, to time sound effects."""
import json, sys
import cv2
import numpy as np
name = sys.argv[1]
J = json.load(open(f"idx/{name}.json"))
want = dict(zip(J["idx"], J["t"]))
cap = cv2.VideoCapture(f"/tmp/claude-0/cf/raw/{name}.mkv")
n, rows = 0, []
while True:
    ok, f = cap.read()
    if not ok:
        break
    if n in want:
        s = cv2.resize(f, (270, 480), interpolation=cv2.INTER_AREA).astype(np.int16)
        b, g, r = s[..., 0], s[..., 1], s[..., 2]
        red = int(((r > 200) & (g < 130) & (b < 150)).sum())
        cyan = int(((g > 190) & (b > 190) & (r < 170)).sum())
        mag = int(((r > 170) & (b > 150) & (g < 140)).sum())
        white = int(((r > 230) & (g > 230) & (b > 230)).sum())
        rows.append([want[n], red, cyan, mag, white])
    n += 1
json.dump(rows, open(f"idx/{name}_col.json", "w"))
