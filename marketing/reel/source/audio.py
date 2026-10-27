"""Music bed for the Chronofall reel, timed from cues.json: a dark 128 BPM groove in D harmonic minor.

A tense drone before the drop, a driving 808/arp groove through the gadget run, a breakdown under the title, and
short drop-outs before the big hits. The hits, risers and every sound effect are real recordings placed by
sfx_pro.py, not part of this bed. Writes music.wav at 48 kHz.
"""
import json
import sys

import numpy as np
import soundfile as sf

sys.path.insert(0, "/tmp/claude-0/cf/edit")
from synth import SR, bass808, clap, hat, kick, pad, place, pluck  # noqa: E402

E = "/tmp/claude-0/cf/edit"
C = json.load(open(f"{E}/cues.json"))
EV = C["events"]
DURATION = C["duration"]
N = int(round(DURATION * SR))
BEAT = C["beat"]
DROP = C["drop"]
TITLE, CTA = C["title"]

music = np.zeros(N)

# D harmonic minor: Dm - Bb - Gm - A (the A major chord gives the trailer lift)
CHORDS = [[62, 65, 69], [58, 62, 65], [55, 58, 62], [57, 61, 64]]
ROOTS = [38, 34, 31, 33]
ARP = [[74, 77, 81, 77], [70, 74, 77, 74], [67, 70, 74, 70], [69, 73, 76, 73]]

# Hook: a low drone up to the drop.
place(music, pad([38, 45, 50], DROP + 0.3, 700, 0.55), 0.0)

for k in range(0, int(round((DURATION - DROP) / BEAT))):
    bt = DROP + k * BEAT
    bar, pos = k // 4, k % 4
    chord = bar % 4
    in_title = TITLE - 0.05 <= bt < CTA - 0.02
    in_cta = bt >= CTA - 0.02
    intense = EV["fabricator"] - 0.01 <= bt < TITLE - 0.05  # the gadget run drives harder
    if not in_title:
        if pos in (0, 2) or (intense and pos == 3):
            place(music, kick(0.95 if pos == 0 else 0.8), bt)
        if pos == 2:
            place(music, clap(0.8 if not in_cta else 0.5), bt)
        for h in range(2):
            place(music, hat(False, 0.85 if h == 0 else 0.5), bt + h * BEAT / 2)
        if intense:
            place(music, hat(True, 0.35), bt + BEAT / 2)
        if pos == 3 and bar % 2 == 1:
            for r in range(4):
                place(music, hat(False, 0.5 + r * 0.1), bt + BEAT / 2 + r * BEAT / 8)
        if not in_cta or bar % 2 == 0:
            for s in range(4):
                place(music, pluck(ARP[chord][(pos * 4 + s) % 4] - (12 if not intense else 0), 0.2, 0.7), bt + s * BEAT / 4)
    if pos == 0:
        place(music, bass808(ROOTS[chord], BEAT * 4 - 0.02, 1.0), bt)
        place(music, pad(CHORDS[chord], BEAT * 4, 2000 if not in_title else 1000, 0.3), bt)


def gap(at, length):
    """Short drop-out right before a big moment."""
    i0, i1 = int((at - length) * SR), int(at * SR)
    fade = np.linspace(1, 0.12, max(1, int(0.03 * SR)))
    music[i0:i0 + len(fade)] *= fade
    music[i0 + len(fade):i1] *= 0.12


gap(DROP, 0.2)
gap(EV["impact_pod"] - 0.01, 0.16)
gap(EV["slam"] - 0.01, 0.14)

fi, fo = int(0.03 * SR), int(0.08 * SR)
music[:fi] *= np.linspace(0, 1, fi)
music[-fo:] *= np.linspace(1, 0.4, fo)

def norm(x, peak):
    return x / max(1e-9, np.abs(x).max()) * peak


sf.write(f"{E}/music.wav", norm(music, 0.9).astype(np.float32), SR)
print("music written", DURATION)
