"""Picks the best take of every voice line: the words must match the script, then the most energetic read wins.

Energy = speaking pace (syllables per second of speech), pitch movement (spread of f0 in semitones) and punch
(loudness range across the line), each z-scored across the takes of the same line. A take is rejected if Whisper
does not hear the script's words, or if it has a long pause or trailing noise. The winners are trimmed of silence
(never sped up) and written to ../vo/line_XX.wav, the names the rest of the pipeline reads.
"""
import json
import re
import sys

import numpy as np
import soundfile as sf
from faster_whisper import WhisperModel
from scipy.signal import resample_poly

D = "/tmp/claude-0/cf/edit/vo_cb"
OUT = "/tmp/claude-0/cf/edit/vo"
script = json.load(open(f"{D}/script.json"))
takes = [int(t) for t in (sys.argv[1:] or ["1", "2", "3", "4"])]
EXTRA = {"s05": [5, 6, 7, 8], "s07": [5, 6, 7, 8], "s08": [5, 6, 7, 8], "s13": [5, 6, 7, 8], "s14": [5, 6, 7, 8]}
m = WhisperModel("small.en", device="cpu", compute_type="int8")

NUM = {"3001": "three thousand and one", "3,001": "three thousand and one"}


def norm(s):
    s = " ".join(s.lower().split())
    for k, v in NUM.items():
        s = s.replace(k, v)
    s = s.replace("chrono fall", "chronofall").replace("chrono-fall", "chronofall").replace("jet packs", "jetpacks")
    s = s.replace("jet paks", "jetpacks").replace("jet pack", "jetpack")
    return re.sub(r"[^a-z ]", " ", s).split()


def syllables(word):
    return max(1, len(re.findall(r"[aeiouy]+", word)))


def trim(a, sr):
    env = np.convolve(np.abs(a), np.ones(int(0.01 * sr)) / int(0.01 * sr), "same")
    on = np.where(env > 0.012)[0]
    return a[max(0, on[0] - int(0.03 * sr)): on[-1] + int(0.08 * sr)]


def f0_track(a, sr):
    f0 = []
    hop, win = int(0.01 * sr), int(0.04 * sr)
    for i in range(0, len(a) - win, hop):
        fr = a[i:i + win] - a[i:i + win].mean()
        if np.sqrt((fr ** 2).mean()) < 0.02:
            continue
        ac = np.correlate(fr, fr, "full")[win - 1:]
        lo, hi = int(sr / 350), int(sr / 70)
        lag = lo + int(np.argmax(ac[lo:hi]))
        if ac[lag] > 0.35 * ac[0]:
            f0.append(sr / lag)
    return np.array(f0)


ref, rsr = sf.read("/home/user/moneymaker/voice/channel_ref.wav", dtype="float32")
REF_F0 = float(np.median(f0_track(ref, rsr)))
print(f"reference voice median pitch {REF_F0:.0f} Hz")
rows = {}
for sid, text in script:
    want = norm(text)
    res = []
    for k in takes + [x for x in EXTRA.get(sid, []) if x not in takes]:
        try:
            a, sr = sf.read(f"{D}/takes/{sid}_t{k}.wav", dtype="float32")
        except Exception:
            continue
        a = trim(a, sr)
        segs, _ = m.transcribe(resample_poly(a, 16000, sr).astype(np.float32), word_timestamps=True, beam_size=5)
        words = [w for s in segs for w in s.words]
        heard = norm(" ".join(w.word for w in words))
        ok = heard == want
        gaps = [b.start - a_.end for a_, b in zip(words, words[1:])]
        # a sentence break inside the line ("Plasma cutters! Pulse rifles!") may pause longer
        long_gap = max(gaps, default=0) > (0.65 if re.search(r"[.!?,] \w", text) else 0.45)
        tail = len(a) / sr - (words[-1].end if words else 0)
        speech = (words[-1].end - words[0].start) if words else len(a) / sr
        rate = sum(syllables(w) for w in want) / max(0.2, speech)
        f0 = f0_track(a, sr)
        pitch = float(np.std(12 * np.log2(f0 / np.median(f0)))) if len(f0) > 5 else 0.0
        env = np.sqrt(np.convolve(a ** 2, np.ones(int(0.05 * sr)) / int(0.05 * sr), "same"))
        voiced = env[env > 0.02]
        punch = float(np.percentile(20 * np.log10(voiced), 95) - np.percentile(20 * np.log10(voiced), 30)) if len(voiced) else 0
        res.append(dict(take=k, ok=bool(ok and not long_gap and tail < 0.6), heard=" ".join(heard), dur=round(len(a) / sr, 3),
                        rate=round(rate, 2), pitch=round(pitch, 2), punch=round(punch, 1), f0=round(float(np.median(f0)) if len(f0) else 0),
                        audio=a, sr=sr))
    for r in res:
        if r["f0"] and abs(np.log2(r["f0"] / REF_F0)) > 0.3:  # more than ~4 semitones off the voice
            r["ok"] = False
            r["heard"] += f"  (pitch {r['f0']} Hz, off the voice)"
    good = [r for r in res if r["ok"]] or res
    for key in ("rate", "pitch", "punch"):
        v = np.array([r[key] for r in good], float)
        z = (v - v.mean()) / (v.std() + 1e-6)
        for r, zz in zip(good, z):
            r["score"] = r.get("score", 0) + float(zz) * (1.5 if key == "rate" else 1.0)
    best = max(good, key=lambda r: r.get("score", 0))
    i = int(sid[1:]) - 1
    sf.write(f"{OUT}/line_{i:02d}.wav", best["audio"], best["sr"])
    rows[sid] = [{k: v for k, v in r.items() if k not in ("audio", "sr")} for r in res]
    print(f"{sid} pick t{best['take']}  " + "  ".join(
        f"t{r['take']}:{'OK' if r['ok'] else 'NO'} {r['dur']}s r{r['rate']} p{r['pitch']} k{r['punch']} {r['f0']}Hz" for r in res))
    for r in res:
        if not r["ok"]:
            print(f"      t{r['take']} heard: {r['heard']}")
json.dump(rows, open(f"{D}/picks.json", "w"), indent=1)
