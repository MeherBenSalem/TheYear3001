#!/usr/bin/env python3
"""Synthesises every Chronofall sound effect (mono, 44.1 kHz) into tools/assets/sounds/*.ogg, from oscillators,
filtered noise and envelopes. Every sound is original; nothing is sampled. Needs numpy and ffmpeg with libvorbis.

  python3 tools/gen_sounds.py
"""
from __future__ import annotations

import subprocess
import tempfile
import wave
from pathlib import Path

import numpy as np

RATE = 44100
OUT = Path(__file__).resolve().parent / 'assets' / 'sounds'
rng = np.random.default_rng(3001)


def t(seconds):
    return np.arange(int(RATE * seconds)) / RATE


def noise(seconds):
    return rng.uniform(-1, 1, int(RATE * seconds))


def lowpass(x, cutoff):
    """One-pole low-pass; cutoff may be a number or an array (a sweep)."""
    cutoff = np.broadcast_to(np.asarray(cutoff, dtype=float), x.shape)
    a = 1 - np.exp(-2 * np.pi * cutoff / RATE)
    y = np.empty_like(x)
    acc = 0.0
    for i in range(len(x)):
        acc += a[i] * (x[i] - acc)
        y[i] = acc
    return y


def bandpass(x, low, high):
    return lowpass(x, high) - lowpass(x, low)


def sweep(f0, f1, seconds, shape='sin', curve=1.0):
    tt = t(seconds)
    f = f0 + (f1 - f0) * (tt / seconds) ** curve
    phase = 2 * np.pi * np.cumsum(f) / RATE
    if shape == 'saw':
        return 2 * ((phase / (2 * np.pi)) % 1) - 1
    if shape == 'square':
        return np.sign(np.sin(phase))
    return np.sin(phase)


def env(seconds, attack=0.005, release=None, decay=None):
    tt = t(seconds)
    e = np.minimum(1, tt / max(attack, 1e-4))
    if decay is not None:
        e *= np.exp(-tt / decay)
    if release is not None:
        e *= np.clip((seconds - tt) / release, 0, 1)
    return e


def mixdown(*parts):
    n = max(len(p) for p in parts)
    out = np.zeros(n)
    for p in parts:
        out[:len(p)] += p
    return out


def normalize(x, peak=0.9):
    m = np.max(np.abs(x))
    return x if m == 0 else x * (peak / m)


def loopable(x, fade=0.03):
    """Cross-fades the end into the start so the file loops without a click."""
    n = int(RATE * fade)
    head, tail = x[:n].copy(), x[-n:].copy()
    ramp = np.linspace(0, 1, n)
    x = x[:-n].copy()
    x[:n] = head * ramp + tail * (1 - ramp)
    return x


def save(name, x):
    OUT.mkdir(parents=True, exist_ok=True)
    data = (np.clip(normalize(x), -1, 1) * 32767).astype(np.int16)
    with tempfile.NamedTemporaryFile(suffix='.wav', delete=False) as tmp:
        with wave.open(tmp.name, 'wb') as w:
            w.setnchannels(1)
            w.setsampwidth(2)
            w.setframerate(RATE)
            w.writeframes(data.tobytes())
        subprocess.run(['ffmpeg', '-loglevel', 'error', '-y', '-i', tmp.name, '-c:a', 'libvorbis', '-q:a', '5', str(OUT / f'{name}.ogg')],
                       check=True)
        Path(tmp.name).unlink()


def thud(seconds, f0=70, f1=35, decay=0.18):
    return sweep(f0, f1, seconds) * env(seconds, 0.002, decay=decay)


def sounds():
    s = {}
    # The rift tears open: a deep rumble, a rising shimmer chord and crackles.
    d = 3.2
    rumble = lowpass(noise(d), 90) * 6 * env(d, 0.6, release=0.8)
    shimmer = sum(sweep(f, f * 2.4, d, curve=1.6) * 0.25 for f in (220, 277, 330, 415)) * env(d, 1.2, release=0.6)
    shimmer *= 0.6 + 0.4 * np.sin(2 * np.pi * 7 * t(d))
    crackle = (rng.random(int(RATE * d)) > 0.9985) * rng.uniform(-1, 1, int(RATE * d))
    s['rift_open'] = mixdown(rumble, shimmer * 0.5, lowpass(crackle, 3000) * 3)
    # The pod falls: a descending whistle over a roaring band of noise.
    d = 3.0
    roar = bandpass(noise(d), 200, 1500) * env(d, 0.4, release=0.3)
    whistle = sweep(1400, 380, d, curve=0.7) * 0.35 * env(d, 0.3, release=0.4)
    s['pod_fall'] = mixdown(roar * 1.6, whistle)
    # Impact: a heavy thump, a blast of noise and falling debris.
    d = 2.6
    blast = lowpass(noise(d), np.linspace(4000, 200, int(RATE * d))) * env(d, 0.002, decay=0.35)
    debris = lowpass((rng.random(int(RATE * d)) > 0.995) * rng.uniform(-1, 1, int(RATE * d)), 2500) * env(d, 0.2, decay=0.8) * 4
    s['pod_impact'] = mixdown(thud(d, 80, 28, 0.5) * 1.4, blast * 1.6, debris)
    # Rift Cache: a latch, a hiss and a rising chime.
    d = 1.2
    clank = bandpass(noise(0.08), 800, 5000) * env(0.08, 0.001, decay=0.02)
    hiss = bandpass(noise(d), 2000, 8000) * env(d, 0.05, decay=0.25) * 0.5
    chime = mixdown(*[np.concatenate([np.zeros(int(RATE * 0.12 * i)), np.sin(2 * np.pi * f * t(0.6)) * env(0.6, 0.005, decay=0.2)])
                      for i, f in enumerate((660, 880, 1320))])
    s['cache_open'] = mixdown(clank * 2, hiss, chime * 0.5)
    # Laser shot: a fast falling zap.
    d = 0.28
    s['laser_shot'] = mixdown(sweep(2200, 260, d, curve=0.5) * env(d, 0.002, decay=0.07),
                              sweep(1100, 130, d, 'square', 0.5) * 0.25 * env(d, 0.002, decay=0.05),
                              bandpass(noise(d), 3000, 9000) * env(d, 0.001, decay=0.02) * 0.4)
    d = 0.22
    s['laser_hit'] = mixdown(bandpass(noise(d), 600, 6000) * env(d, 0.001, decay=0.04), np.sin(2 * np.pi * 1800 * t(d)) * env(d, 0.001, decay=0.03) * 0.3)
    # Jetpack: a steady roaring burn (loops).
    d = 0.9
    burn = bandpass(noise(d), 150, 2200) * (0.8 + 0.2 * np.sin(2 * np.pi * 13 * t(d)))
    s['jetpack_loop'] = loopable(mixdown(burn, lowpass(noise(d), 120) * 3))
    # Skiff engine: a humming chord with a little vibrato and air (loops; whole cycles).
    d = 2.0
    vib = 1 + 0.004 * np.sin(2 * np.pi * 3 * t(d))
    hum = sum(np.sin(2 * np.pi * f * np.cumsum(vib) / RATE) * a for f, a in ((90, 1.0), (180, 0.5), (270, 0.3), (540, 0.12)))
    s['skiff_loop'] = loopable(mixdown(hum * 0.6, bandpass(noise(d), 400, 3000) * 0.25))
    # Plasma cutter: a crackling high buzz (loops).
    d = 1.0
    buzz = sweep(880, 880, d, 'saw') * 0.3 + sweep(1320, 1320, d, 'square') * 0.08
    sizzle = bandpass(noise(d), 2500, 9000) * (0.5 + 0.5 * (rng.random(int(RATE * d)) > 0.7))
    s['cutter_loop'] = loopable(mixdown(lowpass(buzz, 4000), sizzle * 0.5))
    # Mech: footstep and ground slam.
    d = 0.45
    s['mech_step'] = mixdown(thud(d, 90, 40, 0.09) * 1.5, bandpass(noise(d), 300, 3000) * env(d, 0.002, decay=0.03) * 0.6,
                             bandpass(noise(d), 1500, 6000) * env(d, 0.08, decay=0.08) * 0.3)
    d = 1.4
    s['mech_slam'] = mixdown(thud(d, 70, 25, 0.35) * 1.8, lowpass(noise(d), np.linspace(5000, 300, int(RATE * d))) * env(d, 0.002, decay=0.2),
                             np.sin(2 * np.pi * 410 * t(d)) * env(d, 0.002, decay=0.4) * 0.15)
    # Rift Gate: a rising charge and the warp.
    d = 2.0
    s['gate_charge'] = mixdown(sweep(180, 1300, d, curve=1.5) * (0.6 + 0.4 * np.sin(2 * np.pi * np.linspace(4, 22, int(RATE * d)) * t(d))) * env(d, 0.3, release=0.1),
                               bandpass(noise(d), 1000, 6000) * env(d, 1.5, release=0.1) * 0.3)
    d = 1.1
    whoosh = bandpass(noise(d), np.linspace(3000, 200, int(RATE * d)), np.linspace(9000, 1200, int(RATE * d))) * env(d, 0.01, decay=0.3)
    s['gate_warp'] = mixdown(whoosh * 1.4, sweep(1600, 200, d, curve=0.6) * env(d, 0.005, decay=0.25) * 0.4, thud(d, 120, 50, 0.15) * 0.6)
    # Fabricator: printing servo with beeps, and the done chime.
    d = 0.9
    servo = sweep(300, 520, d, 'saw') * 0.2 * env(d, 0.05, release=0.1)
    beeps = mixdown(*[np.concatenate([np.zeros(int(RATE * (0.1 + 0.18 * i))), np.sin(2 * np.pi * f * t(0.06)) * env(0.06, 0.003, release=0.02)])
                      for i, f in enumerate((1568, 1760, 1318, 1975))])
    s['fabricator_print'] = mixdown(lowpass(servo, 2000), beeps * 0.35)
    d = 0.7
    s['fabricator_done'] = mixdown(np.sin(2 * np.pi * 988 * t(d)) * env(d, 0.003, decay=0.15) * 0.5,
                                   np.concatenate([np.zeros(int(RATE * 0.12)), np.sin(2 * np.pi * 1480 * t(d - 0.12)) * env(d - 0.12, 0.003, decay=0.2)]) * 0.5)
    # Flux Charger hum.
    d = 1.6
    s['charger_hum'] = mixdown(sum(np.sin(2 * np.pi * f * t(d)) * a for f, a in ((120, 1.0), (240, 0.4), (360, 0.2))) * env(d, 0.4, release=0.5),
                               bandpass(noise(d), 3000, 8000) * 0.05)
    # Drone beep-boop.
    d = 0.45
    s['drone_beep'] = mixdown(np.sin(2 * np.pi * 1760 * t(0.09)) * env(0.09, 0.003, release=0.03),
                              np.concatenate([np.zeros(int(RATE * 0.14)), sweep(1320, 2100, 0.12) * env(0.12, 0.003, release=0.04)]))
    # Sentinel voice: a modulated robotic warble; hurt clank; power-down death.
    d = 1.4
    carrier = 2 * np.pi * np.cumsum(140 + 60 * np.sin(2 * np.pi * 5.5 * t(d)) + 40 * np.sin(2 * np.pi * 0.8 * t(d))) / RATE
    voice = np.sin(carrier + 2.5 * np.sin(carrier * 2.01))
    gate = (np.sin(2 * np.pi * 9 * t(d)) > -0.3).astype(float)
    s['sentinel_ambient'] = lowpass(voice * gate, 2500) * env(d, 0.05, release=0.3)
    d = 0.4
    s['sentinel_hurt'] = mixdown(bandpass(noise(d), 500, 6000) * env(d, 0.001, decay=0.05) * 1.2,
                                 sweep(420, 210, d, 'square') * env(d, 0.002, decay=0.1) * 0.35)
    d = 1.6
    s['sentinel_death'] = mixdown(sweep(600, 40, d, 'saw', 0.6) * env(d, 0.01, release=0.4) * 0.5,
                                  lowpass((rng.random(int(RATE * d)) > 0.993) * rng.uniform(-1, 1, int(RATE * d)), 4000) * 4 * env(d, 0.2, decay=0.6),
                                  thud(d, 90, 30, 0.25) * 0.8)
    d = 0.3
    s['robot_hurt'] = mixdown(bandpass(noise(d), 900, 7000) * env(d, 0.001, decay=0.04), np.sin(2 * np.pi * 620 * t(d)) * env(d, 0.001, decay=0.08) * 0.4)
    return s


def main():
    for name, signal in sounds().items():
        save(name, signal)
        print('wrote', name)


if __name__ == '__main__':
    main()
