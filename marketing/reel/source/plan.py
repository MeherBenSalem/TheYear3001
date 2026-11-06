"""Single source of truth for the Chronofall reel: voiceover placement, shots, time maps and every overlay/audio cue.

Every shot lasts a whole number of beats on a 128 BPM grid (BEATS below). Beat 0 is the drop ("Every night, rifts
tear open the sky"); the reel starts on a beat and lasts a whole number of beats, so the music loops with the
picture. Each voice line is pinned to its shot or to a word anchor (the slam lands on "ground", the cut to the
rifle on "Pulse", the title on "Chronofall"); if a line does not fit before the next one, the plan stops and says
which shot needs another beat. Narration is never sped up. Writes timeline_plan.json, edl.json and cues.json.
"""
import json
import sys

import numpy as np
from scipy.interpolate import PchipInterpolator

E = "/tmp/claude-0/cf/edit"
BPM = 128.0
BEAT = 60.0 / BPM

# Shot order and length in beats. The hook (the first two) runs before the drop.
BEATS = {
    "hook_impact": 4, "crater": 5,
    "rift_opens": 5, "pod_falls": 7, "sentinels": 4, "cache": 3, "fabricator": 4,
    "jetpack": 3, "skiff": 3, "drone": 4, "mech": 6, "cutter": 4, "rifle": 3, "gate": 4, "charger": 4,
    "title": 6, "cta_loop": 8,
}
PRE = BEATS["hook_impact"] + BEATS["crater"]
DROP = PRE * BEAT
STARTS, t = {}, 0.0
for name, n in BEATS.items():
    STARTS[name] = (round(t, 4), round(t + n * BEAT, 4))
    t += n * BEAT
DURATION = round(t, 4)

LW = {l["i"]: l for l in json.load(open(f"{E}/lineword.json"))}
lines = json.load(open(f"{E}/lines.json"))


def word_at(i, word):
    """Offset of a word (by its start, case-insensitive) inside voice line i."""
    for w in LW[i]["words"]:
        if w["w"].lower().strip(".,!?").startswith(word):
            return w["s"]
    raise KeyError(f"line {i}: no word {word!r} in {[w['w'] for w in LW[i]['words']]}")


def S(name):
    return STARTS[name][0]


def L(name):
    return STARTS[name][1] - STARTS[name][0]


# ------------------------------------------------------------------ shots
# map: [[local_out_time, src_game_time], ...], "smooth" (monotone cubic) or "linear".
# cam: [[abs_t, scale, cx, cy], ...], a constant drift; the loop seam keeps the camera moving at about the same speed.
# No shot plays slower than real time: many particles only update on game ticks (20 a second), and slow motion
# would show those steps as judder.
shots = []


def shot(name, src, mp, cam, interp="smooth", punch=0.06, shake=(), **kw):
    a, z = STARTS[name]
    s = {"name": name, "src": src, "start": a, "end": z, "map": mp, "interp": interp,
         "cam": [[a + u * (z - a), sc, cx, cy] for u, sc, cx, cy in cam], "punch": punch,
         "shake": [list(x) for x in shake]}
    s.update(kw)
    shots.append(s)
    return s


IMPACT_HOOK = 0.45
HOOK_SRC0 = 3.80
n = L("hook_impact")
shot("hook_impact", "hook_ground", [[0, HOOK_SRC0], [IMPACT_HOOK, 4.30], [n, 4.30 + (n - IMPACT_HOOK) * 1.02]],
     [[0, 1.08, 540, 940], [1, 1.13, 532, 925]], punch=0.0, shake=[(IMPACT_HOOK, 30, 0.75)])
n = L("crater")
shot("crater", "crash2", [[0, 4.55], [n, 4.55 + min(1.46, (7.9 - 4.55) / n) * n]], [[0, 1.45, 590, 1000], [1, 1.6, 590, 1000]],
     interp="linear")
n = L("rift_opens")
shot("rift_opens", "up_rift", [[0, 0.5], [n, 3.95]], [[0, 1.25, 540, 860], [1, 1.6, 540, 860]], punch=0.08)
n = L("pod_falls")
IMPACT_POD = round(S("pod_falls") + n - 2 * BEAT, 4)  # lands two beats before the cut, then the smoke
li = IMPACT_POD - S("pod_falls")
shot("pod_falls", "hook_sky", [[0, 4.33 - li * 1.03], [li, 4.33], [n, 4.33 + (n - li) * 1.32]],
     [[0, 1.15, 540, 900], [1, 1.24, 540, 940]], interp="linear", shake=[(IMPACT_POD, 24, 0.6)])
n = L("sentinels")
shot("sentinels", "sentinel", [[0, 2.45], [n, 2.45 + min(1.71, (6.8 - 2.45) / n) * n]], [[0, 1.25, 470, 880], [1, 1.4, 470, 880]],
     interp="linear")
n = L("cache")
CRACK = round(S("cache") + 0.53, 4)
shot("cache", "cache", [[0, 0.2], [0.53, 0.75], [n, 0.75 + (n - 0.53) * 1.65]], [[0, 1.22, 520, 840], [1, 1.34, 520, 840]],
     interp="linear", punch=0.04, shake=[(CRACK, 10, 0.3)])
n = L("fabricator")
shot("fabricator", "fabricator", [[0, 0.6], [n, 0.6 + min(1.49, 3.9 / n) * n]], [[0, 1.4, 500, 960], [1, 1.55, 500, 940]],
     interp="linear")
n = L("jetpack")
LIFTOFF = round(S("jetpack") + 0.2, 4)
shot("jetpack", "jetpack", [[0, 0.5], [0.2, 0.72], [n, 0.72 + (n - 0.2) * 1.8]], [[0, 1.15, 540, 1000], [1, 1.25, 540, 960]],
     interp="linear")
n = L("skiff")
shot("skiff", "skiff", [[0, 2.4], [n, 2.4 + 1.56 * n]], [[0, 1.15, 540, 1020], [1, 1.26, 540, 1020]], interp="linear", punch=0.04)
n = L("drone")
shot("drone", "drone", [[0, 0.4], [n, 0.4 + 1.6 * n]], [[0, 1.2, 470, 860], [1, 1.32, 470, 860]], interp="linear", punch=0.04)
n = L("mech")
SLAM = round(S("mech") + 3 * BEAT, 4)
ls = SLAM - S("mech")
shot("mech", "mech", [[0, 2.86 - ls * 1.04], [ls, 2.86], [n, 2.86 + (n - ls) * 1.05]],
     [[0, 1.2, 540, 1080], [1, 1.36, 540, 1080]], punch=0.04, shake=[(SLAM, 34, 0.8)])
n = L("cutter")
shot("cutter", "cutter", [[0, 0.9], [n, 0.9 + min(1.81, 3.0 / n) * n]], [[0, 1.2, 600, 1000], [1, 1.32, 600, 1000]],
     interp="linear")
n = L("rifle")
shot("rifle", "rifle", [[0, 1.05], [n, 1.05 + 1.53 * n]], [[0, 1.25, 540, 1000], [1, 1.38, 540, 1000]], interp="linear")
n = L("gate")
TELEPORT = round(S("gate") + 2 * BEAT, 4)
lt = TELEPORT - S("gate")
shot("gate", "gate", [[0, 2.99 - lt * 2.1], [lt, 2.99], [n, 2.99 + (n - lt) * 1.4]], [[0, 1.2, 540, 1050], [1, 1.3, 540, 1050]],
     shake=[(TELEPORT, 12, 0.35)])
n = L("charger")
shot("charger", "charger", [[0, 0.3], [n, 0.3 + min(1.65, 3.9 / n) * n]], [[0, 1.15, 540, 950], [1, 1.3, 520, 950]],
     interp="linear")
TITLE0, TITLE1 = STARTS["title"]
n = L("title")
shot("title", "up_rift", [[0, 0.75], [n, 0.75 + n]], [[0, 1.05, 540, 940], [1, 1.25, 540, 940]], interp="linear", punch=0.0,
     shake=[(TITLE0 + 0.03, 14, 0.5)])
# The loop: the CTA plays the hook's own clip up to the frame the reel starts on (the field before the pod).
n = L("cta_loop")
shot("cta_loop", "hook_ground", [[0, max(0.0, HOOK_SRC0 - n)], [n, HOOK_SRC0]], [[0, 1.0, 540, 960], [1, 1.08, 540, 940]],
     interp="linear", punch=0.0)
TITLE_WORD = round(TITLE0 + BEAT, 4)

# ------------------------------------------------------------------ voiceover (start times) and the fit check
VO_START = {
    0: 0.12,
    1: round(DROP + 0.04, 3),
    3: round(S("sentinels") + 0.02, 3),
    4: round(S("cache") + 0.06, 3),
    5: round(S("fabricator") + 0.06, 3),
    6: round(S("jetpack") + 0.05, 3),
    7: round(S("skiff") + 0.05, 3),
    8: round(S("drone") + 0.05, 3),
    9: round(SLAM - word_at(9, "ground") - 0.05, 3),     # "ground" lands just before the slam
    10: round(S("rifle") + 0.03 - word_at(10, "pulse"), 3),  # "Pulse" on the cut to the rifle
    11: round(S("gate") + 0.05, 3),
    12: round(S("charger") + 0.05, 3),
    13: round(TITLE_WORD - word_at(13, "chrono"), 3),     # "Chronofall" on the beat the title word lands
    14: round(S("cta_loop") + 0.15, 3),
}
VO_START[2] = round(VO_START[1] + LW[1]["dur"] + 0.1, 3)  # straight on from line 1
VO_START = dict(sorted(VO_START.items()))
problems = []
for i in range(len(lines) - 1):
    end = VO_START[i] + LW[i]["dur"]
    if end + 0.06 > VO_START[i + 1]:
        problems.append(f"line {i} ends {end:.2f} but line {i + 1} starts {VO_START[i + 1]:.2f} (short by "
                        f"{end + 0.06 - VO_START[i + 1]:.2f} s)")
if VO_START[2] + LW[2]["dur"] > IMPACT_POD + 0.6:
    problems.append(f"line 2 runs to {VO_START[2] + LW[2]['dur']:.2f}, long after the pod lands at {IMPACT_POD:.2f}")
last = VO_START[14] + LW[14]["dur"]
if last > DURATION - 0.45:
    problems.append(f"the closing line ends {last:.2f}, too close to the loop at {DURATION:.2f}")
if problems:
    print("VOICE DOES NOT FIT:\n  " + "\n  ".join(problems))
    sys.exit(1)


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
LABELS = [("sentinels", "Sentinels", "Laser-firing guards", C_RED), ("cache", "Rift Cache", "Shards · circuits · salvage", C_RIFT),
          ("fabricator", "Fabricator", "Prints your tech", C_FLUX), ("jetpack", "Jetpack", "Fly on any server", C_AMBER),
          ("skiff", "Hover Skiff", "Rideable aircraft", C_FLUX), ("drone", "Drone", "Collect · guard · light", C_FLUX),
          ("mech", "Mech", "Leap and ground slam", C_AMBER), ("cutter", "Plasma Cutter", "Mines from 12 blocks", C_FLUX),
          ("rifle", "Pulse Rifle", "Laser bolts", C_RED), ("gate", "Rift Gates", "Any distance · any dimension", C_RIFT),
          ("charger", "Flux Charger", "Wireless power", C_FLUX)]
labels = [{"t0": STARTS[k][0], "t1": STARTS[k][1], "name": nm, "sub": sub, "color": c} for k, nm, sub, c in LABELS]

ev = {
    "impact_hook": IMPACT_HOOK, "drop": round(DROP, 4), "impact_pod": IMPACT_POD, "crack": CRACK, "liftoff": LIFTOFF,
    "slam": SLAM, "teleport": TELEPORT, "title": TITLE0, "cta": TITLE1,
    "pod_appears": out_time("pod_falls", 2.45),
    "sentinel_bolts": [out_time("sentinels", t_) for t_ in (2.86 - 0.2, 4.61 - 0.2) if t_ < shots[4]["map"][-1][1]],
    "drone_shots": [out_time("drone", t_) for t_ in (0.57, 1.08, 2.57, 4.95) if t_ < shots[9]["map"][-1][1]],
    "rifle_shots": [out_time("rifle", t_) for t_ in (1.18, 1.78, 2.38, 2.98)],
    "mech_leap": out_time("mech", 1.62),
    "cutter_on": S("cutter"), "charger": S("charger"), "fabricator": S("fabricator"), "skiff": S("skiff"), "rift_open": S("rift_opens"),
    "whooshes": [[S("crater") - 0.16, True], [S("jetpack") - 0.18, True], [TITLE1 - 0.12, False]],
}

cues = {
    "duration": DURATION, "bpm": BPM, "drop": round(DROP, 4), "beat": BEAT, "events": ev, "labels": labels,
    "flashes": [[IMPACT_HOOK, 0.6], [round(DROP, 4), 0.3], [IMPACT_POD, 0.55], [CRACK, 0.25], [SLAM, 0.45], [TELEPORT, 0.35],
                [TITLE0, 0.6]],
    "year": {"t0": round(0.12 + word_at(0, "three") if any(w["w"].lower().startswith("three") for w in LW[0]["words"])
                         else 0.12 + word_at(0, "3001"), 3), "t1": round(S("crater") + 0.8, 3)},
    "brand": [],  # no corner tag: it would sit on the rift; the title card carries the name
    "capTop": [],
    "capBig": [0.0, DROP],
    "title": [TITLE0, TITLE1],
    "titleWord": TITLE_WORD,
    # the prompt comes in while the closing question is still being asked, and clears before the loop seam
    "comment": [round(min(VO_START[14] + LW[14]["dur"] + 0.05, DURATION - 1.4), 3), round(DURATION - 0.12, 3)],
}

timeline = [{"i": i, "start": VO_START[i], "cap": lines[i]["cap"]} for i in range(len(lines))]
json.dump({"duration": DURATION, "lines": timeline}, open(f"{E}/timeline_plan.json", "w"), indent=1)
json.dump({"fps": 30, "size": [1080, 1920], "shots": shots}, open(f"{E}/edl.json", "w"), indent=1)
json.dump(cues, open(f"{E}/cues.json", "w"), indent=1)
print(f"duration {DURATION:.3f} s = {round(DURATION / BEAT)} beats, drop at {DROP:.3f}")
for s in shots:
    print(f'{s["start"]:7.3f}-{s["end"]:7.3f} {s["end"] - s["start"]:5.2f}s  {s["name"]:12s} {s["src"]}  src {s["map"][0][1]:.2f}->{s["map"][-1][1]:.2f}')
for i in range(len(lines)):
    print(f'VO {i:2d} {VO_START[i]:6.2f}-{VO_START[i] + LW[i]["dur"]:6.2f} {lines[i]["cap"]}')
print(json.dumps(ev))
