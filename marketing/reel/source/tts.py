"""Voiceover lines with Kokoro-82M: a 60/40 blend of the am_onyx and am_fenrir voices (deep but clear)."""
import json
import numpy as np
import soundfile as sf
from kokoro_onnx import Kokoro

E = "/tmp/claude-0/cf/edit"
M = "/tmp/claude-0/-home-user-Elementals/3b676e2b-c649-5d55-a750-c5b258d7a571/scratchpad/ttsmodels"
k = Kokoro(f"{M}/kokoro-v1.0.onnx", f"{M}/voices-v1.0.bin")
style = 0.6 * k.get_voice_style("am_onyx") + 0.4 * k.get_voice_style("am_fenrir")
lines = json.load(open(f"{E}/lines.json"))
for i, line in enumerate(lines):
    # "ph" pins the pronunciation where the default phonemes are off (mech, Chronofall, tear)
    if "ph" in line:
        audio, sr = k.create(line["ph"], voice=style, speed=line.get("speed", 1.08), lang="en-us", is_phonemes=True)
    else:
        audio, sr = k.create(line["say"], voice=style, speed=line.get("speed", 1.08), lang="en-us")
    nz = np.where(np.abs(audio) > 0.01)[0]
    audio = audio[max(0, nz[0] - int(0.02 * sr)): nz[-1] + int(0.06 * sr)]
    sf.write(f"{E}/vo/line_{i:02d}.wav", audio, sr)
    print(i, round(len(audio) / sr, 2), line["say"])
