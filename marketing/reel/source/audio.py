"""Music bed and sound effects for the Chronofall reel, timed entirely from cues.json.

A dark 128 BPM trailer bed in D harmonic minor: a tense drone and the pod's whistle before the drop, trailer braams
on the big impacts, a driving 808/arp groove through the gadget run, a breakdown under the title and a riser that
leads back into the first frame. Each gadget gets its own sound (laser bolts, thrusters, servo stomps, the gate's
sweep, the charger's hum); only three soft whooshes mark section changes. Writes music.wav and sfx.wav at 48 kHz.
"""
import json
import sys

import numpy as np
import soundfile as sf

sys.path.insert(0, "/tmp/claude-0/cf/edit")
from synth import (SR, bass808, blip, braam, chime, clap, earth, fireburst, glitch, hat, hum, impact, incoming,  # noqa: E402
                   kick, laser, pad, place, pluck, riser, shimmer, sizzle, stomp, swirl, teleport, thruster, whoosh)

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

# Hook: low drone, the impact's braam, a riser into the drop.
place(music, pad([38, 45, 50], DROP + 0.3, 700, 0.55), 0.0)
place(music, braam(26, 2.6, 1.0), EV["impact_hook"])
place(music, riser(DROP - 1.6, 0.6), 1.6)

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
place(music, braam(26, 2.4, 0.9), DROP)
place(music, braam(22, 2.2, 0.85), EV["impact_pod"])
place(music, braam(29, 2.0, 0.75), EV["slam"])
place(music, braam(26, 3.0, 1.0), TITLE)
place(music, riser(2.0, 0.4), TITLE - 2.0)
place(music, riser(DURATION - (CTA + 0.8), 0.35, 300, 5000), CTA + 0.8)  # back into the loop
place(music, incoming(0.7, 0.5), DURATION - 0.7)  # the pod is already falling when the reel restarts

fi, fo = int(0.03 * SR), int(0.08 * SR)
music[:fi] *= np.linspace(0, 1, fi)
music[-fo:] *= np.linspace(1, 0.4, fo)

# ---------------------------------------------------------------- sound effects
sfx = np.zeros(N)
place(sfx, incoming(0.45, 0.7), EV["impact_hook"] - 0.45)
place(sfx, impact(1.0, 2.4), EV["impact_hook"])
place(sfx, glitch(0.35, 0.8), C["year"]["t0"])
place(sfx, impact(0.4, 1.4), DROP)
place(sfx, swirl(1.6, 0.6), EV["rift_open"] + 0.1)
place(sfx, incoming(1.6, 0.9), EV["impact_pod"] - 1.6)
place(sfx, impact(1.0, 2.4), EV["impact_pod"])
for t in EV["sentinel_bolts"]:
    place(sfx, laser(1.0, 2600, 350), t)
place(sfx, impact(0.35, 1.0), EV["crack"])
place(sfx, chime(80, 0.7), EV["crack"] + 0.05)
place(sfx, shimmer(1.6, 1.0), EV["fabricator"] + 0.1)
place(sfx, blip(1500, 0.06, 0.6), EV["fabricator"] + 0.15)
place(sfx, thruster(1.3, 1.0), EV["liftoff"])
place(sfx, thruster(1.3, 0.6), EV["skiff"] + 0.05)
for t in EV["drone_shots"]:
    place(sfx, laser(0.7, 3200, 900, 0.15), t)
place(sfx, stomp(0.8), EV["mech_leap"])
place(sfx, stomp(0.7), EV["mech_leap"] + 0.47)
place(sfx, impact(1.0, 2.0), EV["slam"])
place(sfx, earth(0.8), EV["slam"])
place(sfx, sizzle(0.9, 1.0), EV["cutter_on"])
for t in EV["rifle_shots"]:
    place(sfx, laser(0.9, 1800, 220, 0.25), t)
place(sfx, teleport(1.0, 0.9), EV["teleport"] - 1.0)
place(sfx, impact(0.4, 1.0), EV["teleport"])
place(sfx, hum(1.8, 0.8), EV["charger"])
place(sfx, impact(1.0, 2.6), TITLE)
place(sfx, shimmer(1.8, 1.2), TITLE + 0.1)
place(sfx, glitch(0.3, 0.6), C["titleWord"])
for t, up in EV["whooshes"]:
    place(sfx, whoosh(0.34, 0.5, up=up), t)


def norm(x, peak):
    return x / max(1e-9, np.abs(x).max()) * peak


sf.write(f"{E}/music.wav", norm(music, 0.9).astype(np.float32), SR)
sf.write(f"{E}/sfx.wav", norm(sfx, 0.9).astype(np.float32), SR)
print("music/sfx written", DURATION)
