"""Single source of truth for the Chronofall reel: voiceover placement, shots, time maps and every overlay/audio cue.

Everything sits on a 128 BPM grid anchored on the drop ("Every night, rifts tear open the sky"). The reel is
exactly 67 beats long and starts on a beat, so the music loops with the picture. Writes timeline_plan.json,
edl.json and cues.json.
"""
import json

import numpy as np
from scipy.interpolate import PchipInterpolator

E = "/tmp/claude-0/cf/edit"
BPM = 128.0
BEAT = 60.0 / BPM
DROP = 7 * BEAT          # 3.281 s: beat 0 of the grid; the reel starts exactly 7 beats earlier
DURATION = DROP + 60 * BEAT  # 31.406 s = 67 beats


def b(k):
    return round(DROP + k * BEAT, 4)


LW = {l["i"]: l for l in json.load(open(f"{E}/lineword.json"))}


def word_at(i, k):
    """Offset of word k inside voice line i."""
    return LW[i]["words"][k]["s"]


# ------------------------------------------------------------------ voiceover (start times)
SLAM = b(31)
VO_START = {
    0: 0.12,
    1: DROP + 0.04,
    2: 5.40,
    3: b(9) + 0.02,
    4: b(12) + 0.08,
    5: b(15) + 0.08,
    6: b(19) + 0.05,
    7: b(22) + 0.05,
    8: b(25) + 0.05,
    9: round(SLAM - word_at(9, 5) - 0.05, 3),    # "ground" lands just before the slam
    10: round(b(35) + 0.05 - word_at(10, 2), 3),  # "Pulse" on the cut to the rifle
    11: b(38) + 0.05,
    12: b(42) + 0.05,
    13: round(b(47) - word_at(13, 2), 3),         # "Chronofall" on the beat the title word lands
    14: b(52) + 0.2,
}
lines = json.load(open(f"{E}/lines.json"))

# ------------------------------------------------------------------ shots
# map: [[local_out_time, src_game_time], ...], "smooth" (monotone cubic, eased speed ramps) or "linear".
# cam: [[abs_t, scale, cx, cy], ...], a constant drift; the loop seam keeps the camera moving at about the same speed.
# No shot plays slower than real time: many particles only update on game ticks (20 a second), and slow motion
# would show those steps as judder.
shots = []


def shot(name, src, start, end, mp, cam, interp="smooth", punch=0.06, shake=(), **kw):
    s = {"name": name, "src": src, "start": round(start, 4), "end": round(end, 4), "map": mp, "interp": interp,
         "cam": cam, "punch": punch, "shake": [list(x) for x in shake]}
    s.update(kw)
    shots.append(s)
    return s


IMPACT_HOOK = 0.45
HOOK_SRC0 = 3.80
shot("hook_impact", "hook_ground", 0.0, b(-3), [[0, HOOK_SRC0], [IMPACT_HOOK, 4.30], [b(-3), 5.75]],
     [[0.0, 1.08, 540, 940], [b(-3), 1.13, 532, 925]], punch=0.0, shake=[(IMPACT_HOOK, 30, 0.75)])
shot("crater", "crash2", b(-3), b(0), [[0, 4.55], [b(0) - b(-3), 6.6]],
     [[b(-3), 1.45, 590, 1000], [b(0), 1.6, 590, 1000]])
shot("rift_opens", "up_rift", b(0), b(4), [[0, 0.5], [b(4) - b(0), 3.95]],
     [[b(0), 1.25, 540, 860], [b(4), 1.6, 540, 860]], punch=0.08)
IMPACT_POD = b(8)
shot("pod_falls", "hook_sky", b(4), b(9), [[0, 2.4], [IMPACT_POD - b(4), 4.33], [b(9) - b(4), 4.95]],
     [[b(4), 1.15, 540, 900], [b(9), 1.24, 540, 940]], interp="linear", shake=[(IMPACT_POD, 24, 0.6)])
shot("sentinels", "sentinel", b(9), b(12), [[0, 2.45], [b(12) - b(9), 4.85]], [[b(9), 1.25, 470, 880], [b(12), 1.4, 470, 880]],
     interp="linear")
CRACK = b(12) + 0.53
shot("cache", "cache", b(12), b(15), [[0, 0.2], [0.53, 0.75], [b(15) - b(12), 2.2]],
     [[b(12), 1.22, 520, 840], [b(15), 1.34, 520, 840]], interp="linear", punch=0.04, shake=[(CRACK, 10, 0.3)])
shot("fabricator", "fabricator", b(15), b(19), [[0, 0.6], [b(19) - b(15), 3.4]],
     [[b(15), 1.4, 500, 960], [b(19), 1.55, 500, 940]], interp="linear")
LIFTOFF = b(19) + 0.2
shot("jetpack", "jetpack", b(19), b(22), [[0, 0.5], [0.2, 0.72], [b(22) - b(19), 2.9]],
     [[b(19), 1.15, 540, 1000], [b(22), 1.25, 540, 960]], interp="linear")
shot("skiff", "skiff", b(22), b(25), [[0, 2.4], [b(25) - b(22), 4.6]], [[b(22), 1.15, 540, 1020], [b(25), 1.26, 540, 1020]],
     interp="linear", punch=0.04)
shot("drone", "drone", b(25), b(28), [[0, 0.4], [b(28) - b(25), 2.65]], [[b(25), 1.2, 470, 860], [b(28), 1.32, 470, 860]],
     interp="linear", punch=0.04)
shot("mech", "mech", b(28), b(33), [[0, 1.40], [SLAM - b(28), 2.86], [b(33) - b(28), 3.85]],
     [[b(28), 1.2, 540, 1080], [b(33), 1.36, 540, 1080]], punch=0.04, shake=[(SLAM, 34, 0.8)])
shot("cutter", "cutter", b(33), b(35), [[0, 0.9], [b(35) - b(33), 2.6]], [[b(33), 1.2, 600, 1000], [b(35), 1.32, 600, 1000]],
     interp="linear")
shot("rifle", "rifle", b(35), b(38), [[0, 1.05], [b(38) - b(35), 3.2]], [[b(35), 1.25, 540, 1000], [b(38), 1.38, 540, 1000]],
     interp="linear")
TELEPORT = b(40)
shot("gate", "gate", b(38), b(42), [[0, 1.0], [TELEPORT - b(38), 2.99], [b(42) - b(38), 4.3]],
     [[b(38), 1.2, 540, 1050], [b(42), 1.3, 540, 1050]], shake=[(TELEPORT, 12, 0.35)])
shot("charger", "charger", b(42), b(46), [[0, 0.3], [b(46) - b(42), 3.4]], [[b(42), 1.15, 540, 950], [b(46), 1.3, 520, 950]],
     interp="linear")
TITLE0, TITLE1 = b(46), b(52)
shot("title", "up_rift", TITLE0, TITLE1, [[0, 0.75], [TITLE1 - TITLE0, 3.55]], [[TITLE0, 1.05, 540, 940], [TITLE1, 1.25, 540, 940]],
     interp="linear", punch=0.0, shake=[(TITLE0 + 0.03, 14, 0.5)])
# The loop: the CTA plays the hook's own clip up to the frame the reel starts on, with the camera at rest there.
shot("cta_loop", "hook_ground", TITLE1, DURATION, [[0, HOOK_SRC0 - (DURATION - TITLE1)], [DURATION - TITLE1, HOOK_SRC0]],
     [[TITLE1, 1.0, 540, 960], [DURATION, 1.08, 540, 940]], interp="linear", punch=0.0)


# ------------------------------------------------------------------ cue helpers
def out_time(shot_name, src_t):
    s = next(x for x in shots if x["name"] == shot_name)
    xs, ys = zip(*s["map"])
    f = PchipInterpolator(xs, ys) if s["interp"] == "smooth" else (lambda u: np.interp(u, xs, ys))
    grid = np.linspace(0, xs[-1], 6000)
    vals = np.asarray(f(grid))
    k = int(np.searchsorted(vals, src_t))
    return round(s["start"] + float(grid[min(k, len(grid) - 1)]), 4)


C_FLUX, C_RIFT, C_AMBER, C_RED = "#38f3ff", "#ff3fd2", "#ffb547", "#ff5a6e"
labels = [
    {"t0": b(9), "t1": b(12), "name": "Sentinels", "sub": "Laser-firing guards", "color": C_RED},
    {"t0": b(12), "t1": b(15), "name": "Rift Cache", "sub": "Shards · circuits · salvage", "color": C_RIFT},
    {"t0": b(15), "t1": b(19), "name": "Fabricator", "sub": "Prints your tech", "color": C_FLUX},
    {"t0": b(19), "t1": b(22), "name": "Jetpack", "sub": "Fly on any server", "color": C_AMBER},
    {"t0": b(22), "t1": b(25), "name": "Hover Skiff", "sub": "Rideable aircraft", "color": C_FLUX},
    {"t0": b(25), "t1": b(28), "name": "Drone", "sub": "Collect · guard · light", "color": C_FLUX},
    {"t0": b(28), "t1": b(33), "name": "Mech", "sub": "Leap and ground slam", "color": C_AMBER},
    {"t0": b(33), "t1": b(35), "name": "Plasma Cutter", "sub": "Mines from 12 blocks", "color": C_FLUX},
    {"t0": b(35), "t1": b(38), "name": "Pulse Rifle", "sub": "Laser bolts", "color": C_RED},
    {"t0": b(38), "t1": b(42), "name": "Rift Gates", "sub": "Any distance · any dimension", "color": C_RIFT},
    {"t0": b(42), "t1": b(46), "name": "Flux Charger", "sub": "Wireless power", "color": C_FLUX},
]

ev = {
    "impact_hook": IMPACT_HOOK, "drop": DROP, "impact_pod": IMPACT_POD, "crack": round(CRACK, 4), "liftoff": round(LIFTOFF, 4),
    "slam": SLAM, "teleport": TELEPORT, "title": TITLE0, "cta": TITLE1,
    "pod_appears": out_time("pod_falls", 2.45),
    "sentinel_bolts": [out_time("sentinels", 2.86 - 0.2), out_time("sentinels", 4.61 - 0.2)],
    "drone_shots": [out_time("drone", t) for t in (0.57, 1.08, 2.57)],
    "rifle_shots": [out_time("rifle", t) for t in (1.18, 1.78, 2.38, 2.98)],
    "mech_leap": out_time("mech", 1.62),
    "cutter_on": b(33), "charger": b(42), "fabricator": b(15), "skiff": b(22), "rift_open": b(0),
    "whooshes": [[b(-3) - 0.16, True], [b(19) - 0.18, True], [TITLE1 - 0.12, False]],
}

cues = {
    "duration": round(DURATION, 4), "bpm": BPM, "drop": DROP, "beat": BEAT, "events": ev, "labels": labels,
    "flashes": [[IMPACT_HOOK, 0.6], [DROP, 0.3], [IMPACT_POD, 0.55], [round(CRACK, 4), 0.25], [SLAM, 0.45], [TELEPORT, 0.35],
                [TITLE0, 0.6]],
    "year": {"t0": round(0.12 + word_at(0, 2), 3), "t1": b(-3) - 0.05},
    "brand": [],  # no corner tag: it would sit on the rift; the title card carries the name
    "capTop": [],
    "capBig": [0.0, 3.3],
    "title": [TITLE0, TITLE1],
    "titleWord": b(47),
    "comment": [round(VO_START[14] + LW[14]["dur"] + 0.05, 3), round(DURATION - 0.12, 3)],
}

timeline = [{"i": i, "start": VO_START[i], "cap": lines[i]["cap"]} for i in range(len(lines))]
json.dump({"duration": round(DURATION, 4), "lines": timeline}, open(f"{E}/timeline_plan.json", "w"), indent=1)
json.dump({"fps": 30, "size": [1080, 1920], "shots": shots}, open(f"{E}/edl.json", "w"), indent=1)
json.dump(cues, open(f"{E}/cues.json", "w"), indent=1)
for s in shots:
    print(f'{s["start"]:7.3f}-{s["end"]:7.3f} {s["end"] - s["start"]:5.2f}s  {s["name"]:12s} {s["src"]}')
for i in range(len(lines)):
    print(f'VO {i:2d} {VO_START[i]:6.2f}-{VO_START[i] + LW[i]["dur"]:6.2f} {lines[i]["cap"]}')
print(json.dumps(ev))
