"""Production sound design for the Chronofall reel: real recorded sound effects, timed from cues.json.

Every sound is a professional recording from Mixkit (Mixkit Sound Effects Free License: free for commercial use in
videos, no attribution required; https://mixkit.co/license/), listed in SFX below by its Mixkit id. No synthesized
effects. Each cue places a recording so its main hit (its loudest 10 ms, measured per file) lands exactly on the
frame it belongs to; whooshes and risers are placed so they peak into the cut or hit they lead to. Anything that
starts before 0 wraps around to the end of the reel, so the swell and whoosh before the opening impact play at the
end and carry the loop. Writes sfx.wav (48 kHz stereo).

The library itself is not stored in the repository: fetch.sh downloads the files from Mixkit by id.
"""
import json
import os

import numpy as np
import soundfile as sf
from scipy.signal import resample_poly, butter, sosfilt

E = "/tmp/claude-0/cf/edit"
LIB = "/tmp/claude-0/sfxlib/wav"
SR = 48000
C = json.load(open(f"{E}/cues.json"))
EV = C["events"]
DUR = C["duration"]
N = int(round(DUR * SR))

# Mixkit id -> what it is (titles as published on mixkit.co)
SFX = {
    3024: "Meteor whoosh", 1703: "Explosion with rocks debris", 788: "Big cinematic impact",
    1022: "Cinematic sci fi glitch", 1492: "Cinematic whoosh fast transition", 790: "Cinematic trailer riser",
    2908: "Movie trailer epic impact", 2687: "Time travel warp", 2297: "Bass rumble hum", 1714: "Fast rocket whoosh",
    1687: "Dramatic metal explosion impact", 1686: "Underground explosion impact echo", 1681: "Laser weapon shot",
    3110: "Laser gun shot", 2952: "Break tech impact", 3108: "Crystal chime", 2352: "Magic glitter or particles",
    3184: "Sci Fi power", 2530: "Futuristic robotic mechanism", 3163: "Sci fi engine short start",
    2738: "Futuristic engine sounds", 2733: "Sci-fi flyby wobble", 1670: "Short laser gun shot",
    2743: "Drone spaceship sound", 3165: "Steam powered mech suit activate", 1417: "Robot step",
    3057: "Apocalyptic stomp impact", 904: "Electricity reactor buzz", 2598: "Futuristic electricity sparks",
    1678: "Laser cannon shot", 3113: "Sci fi warp slide", 2614: "Electric gamma ray whoosh",
    2600: "Electricity static power up", 1144: "Short space stutter intro riser", 2901: "Epic movie impact",
    2950: "Intro text glitch", 1481: "Cinematic swoosh",
}

_cache = {}


def load(iid):
    """A recording at 48 kHz stereo, normalized to 0 dBFS peak, and the time of its main hit."""
    if iid not in _cache:
        a, sr = sf.read(os.path.join(LIB, f"{iid}.wav"), dtype="float32", always_2d=True)
        if a.shape[1] == 1:
            a = np.repeat(a, 2, axis=1)
        a = a[:, :2]
        if sr != SR:
            a = np.stack([resample_poly(a[:, c], SR, sr) for c in range(2)], axis=1).astype(np.float32)
        a /= max(1e-6, np.abs(a).max())
        m = a.mean(1)
        win = int(0.01 * SR)
        env = np.sqrt(np.convolve(m ** 2, np.ones(win) / win, "same"))
        _cache[iid] = (a, int(np.argmax(env)) / SR)
    return _cache[iid]


out = np.zeros((N, 2), np.float32)


def place(iid, at, db=0.0, align="peak", start=0.0, length=None, fade_in=0.0, fade_out=0.08, hp=None, lp=None):
    """Places recording iid so its `align` point ("peak", "start" or "end") lands at `at` seconds.

    start/length pick a segment of the file (seconds); fades are in seconds; hp/lp are filter corners in Hz."""
    a, peak = load(iid)
    seg = a[int(start * SR): int((start + length) * SR) if length else None].copy()
    if hp:
        seg = sosfilt(butter(2, hp, "high", fs=SR, output="sos"), seg, axis=0).astype(np.float32)
    if lp:
        seg = sosfilt(butter(2, lp, "low", fs=SR, output="sos"), seg, axis=0).astype(np.float32)
    n = len(seg)
    if fade_in:
        k = min(n, int(fade_in * SR))
        seg[:k] *= np.linspace(0, 1, k)[:, None]
    if fade_out:
        k = min(n, int(fade_out * SR))
        seg[n - k:] *= np.linspace(1, 0, k)[:, None]
    ref = {"peak": peak - start, "start": 0.0, "end": n / SR}[align]
    i0 = int(round((at - ref) * SR))
    g = 10 ** (db / 20)
    idx = (np.arange(n) + i0) % N  # wraps around the loop
    np.add.at(out, idx, seg * g)


# ---------------------------------------------------------------- the opening crash (and the loop into it)
HOOK = EV["impact_hook"]
place(788, HOOK, db=-3)                        # its swell starts before 0: it plays under the end of the reel
place(3024, HOOK - 0.04, db=-7)                 # the pod's whoosh peaks into the hit
place(1703, HOOK, db=-1)                        # rock-debris explosion
place(1022, C["year"]["t0"], db=-14, hp=400)    # the year counter flicks over
place(1492, EV["whooshes"][0][0] + 0.16, db=-12, align="peak")  # into the crater shot

# ---------------------------------------------------------------- the drop: the rift tears open
DROP = EV["drop"]
place(790, DROP, db=-8, align="end")            # trailer riser into the drop
place(2908, DROP, db=-2)                        # trailer hit on the drop
place(2297, DROP + 0.05, db=-13, align="start", length=2.3, fade_out=0.6)  # the rift's low rumble
place(2687, EV["rift_open"] + 1.2, db=-10)      # the rift's warp at its widest

# ---------------------------------------------------------------- the pod falls and lands
POD = EV["impact_pod"]
place(1714, POD - 0.06, db=-6)                  # incoming rocket whoosh peaks into the landing
place(1687, POD, db=-2)                         # metal explosion
place(1686, POD + 0.02, db=-5)                  # deep underground echo

# ---------------------------------------------------------------- Sentinels, the cache, the Fabricator
for k, t in enumerate(EV["sentinel_bolts"]):
    place(1681 if k % 2 == 0 else 3110, t, db=-9, fade_out=0.25)
place(2952, EV["crack"], db=-7)                 # the cache breaks
place(3108, EV["crack"] + 0.06, db=-14)         # crystal ring of the shards
place(2352, EV["crack"] + 0.35, db=-18, align="start", start=1.0, length=1.6, fade_out=0.5)
place(3184, EV["fabricator"] + 0.25, db=-11)    # the Fabricator powers up
place(2530, EV["fabricator"] + 0.3, db=-20, align="start", length=1.5, fade_out=0.4)  # printing mechanism

# ---------------------------------------------------------------- the gadget run
place(3163, EV["liftoff"], db=-8)               # jetpack ignition
place(2738, EV["liftoff"] + 0.05, db=-11, align="start", fade_out=0.3)
place(2733, EV["skiff"] + 0.05, db=-10, align="start", length=1.45, fade_out=0.25)  # hover skiff fly-by
place(2743, EV["drone_shots"][0] - 0.1, db=-19, align="start", fade_out=0.3)        # drone hum
for t in EV["drone_shots"]:
    place(1670, t, db=-13)
place(3165, EV["mech_leap"], db=-9)             # mech servos
place(1417, EV["mech_leap"] + 0.5, db=-12)
place(3057, EV["slam"], db=-3)                  # the ground slam
place(1686, EV["slam"] + 0.01, db=-6)
place(904, EV["cutter_on"] + 0.05, db=-13, align="start", start=6.6, length=1.8, fade_in=0.08, fade_out=0.3)  # plasma
place(2598, EV["cutter_on"] + 0.45, db=-15, align="start")
for k, t in enumerate(EV["rifle_shots"]):
    place(1678, t, db=-9 - (k % 2), fade_out=0.15)

# ---------------------------------------------------------------- rift gate, charger, title, closing question
TP = EV["teleport"]
place(2614, TP - 0.04, db=-12)                  # the gate charges up into the jump
place(3113, TP, db=-8)                          # the jump
place(2600, EV["charger"] + 0.2, db=-12)        # wireless charge
place(904, EV["charger"] + 0.1, db=-19, align="start", start=2.0, length=1.7, fade_in=0.2, fade_out=0.4)
TITLE = EV["title"]
place(1144, TITLE, db=-15, align="end", fade_in=1.5)  # riser under the charger into the title
place(2901, TITLE, db=-3)                       # title hit
place(2950, C["titleWord"], db=-17, hp=300)     # the name glitches in
place(1481, EV["whooshes"][2][0] + 0.12, db=-14)  # out of the title into the closing shot

peak = np.abs(out).max()
out *= 0.9 / max(1e-6, peak)
sf.write(f"{E}/sfx.wav", out, SR)
print(f"sfx.wav: {len(SFX)} recordings, {DUR:.2f} s, peak normalized from {20 * np.log10(peak):.1f} dBFS")
