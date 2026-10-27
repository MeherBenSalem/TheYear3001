# Instagram Reel: "The year 3001 just crashed into Minecraft"

| File | Use |
| --- | --- |
| `chronofall-reel.mp4` | The Reel: 1080×1920, 30 fps, 31.4 s, H.264 + AAC, about -15 LUFS. Not tracked in git. Rebuild it with the steps below |
| `cover.jpg` | Cover image. The title sits inside the centre 3:4 area, so it survives the profile-grid crop |
| `caption.txt` | Post caption with hashtags |

## Structure

Short-form retention practice: the payoff in the first half second, one idea per shot, burned-in captions for
sound-off viewers, a label on every gadget, and an ending that loops seamlessly into the first frame. Every cut,
voice line, label and sound sits on a 128 BPM grid; the Reel is exactly 67 beats long and starts on a beat, so the
music loops with the picture.

| Time | Shot | Voiceover |
| --- | --- | --- |
| 0.0–1.9 | A Chrono Pod slams down in front of the camera; flash, shake, the year counter rolls 2026 → 3001 | "The year 3001 just crashed into Minecraft." |
| 1.9–3.3 | The crater from above, the Rift Cache and its Sentinels | |
| 3.3–5.2 | The drop: looking straight up as a Chrono Rift tears open | "Every night, rifts tear open the sky," |
| 5.2–7.5 | The pod falls the full height of the frame and lands on the beat | "and drop pods of future tech." |
| 7.5–8.9 | Sentinels firing laser bolts at the camera | "Guarded by Sentinels." |
| 8.9–10.3 | A Rift Cache cracking open | "Crack them open." |
| 10.3–12.2 | The Fabricator printing a jetpack, its hologram spinning | "Print your own gear." |
| 12.2–21.1 | Jetpack take-off, Hover Skiff over the coast, Drone shooting zombies, the Mech's leap and ground slam (on the beat), Plasma Cutter, Pulse Rifle | "Jetpacks. Hover skiffs. Combat drones. A mech that shakes the ground. Plasma cutters. Pulse rifles." |
| 21.1–23.0 | Stepping onto a Rift Gate and arriving at the other one | "Rift gates, to anywhere." |
| 23.0–24.8 | A Flux Charger beaming charge into a drone, a mech and a skiff | "And zero cables." |
| 24.8–27.7 | Title card over the open rift | "It's called Chronofall." |
| 27.7–31.4 | The quiet field before the first shot, an "answer below" prompt; the pod drops in as the Reel loops | "Would you survive the year 3001?" |

## How it was made

- **Footage:** unedited captures of the 1.21.1 Fabric development client in a 1080×1920 window on a portrait virtual
  display, playing on a dedicated server that runs the 2.0.0 release jar. Everything on screen is the mod running:
  rifts and pods come from `/chronofall rift_at`, the Sentinels shoot for real, the drone, mech, skiff and jetpack are
  driven with real key presses (`source/capture/shots.py` sets up and records every shot). The world is the
  showcase seed from `marketing/README.md`, with the same flattened pad.
- **Smooth playback:** both game processes ran on a clock slowed 10× with libfaketime (`source/capture/`), so the
  software renderer still drew about 50 frames per second of game time, and the screen was grabbed often enough to
  keep every one of them. `source/prep.py` indexes which captured frames are new renders and their game time;
  `source/compose.py` asks for a game time per output frame and cross-fades the two renders around it, so no frame
  repeats or skips at any speed, and fast speed ramps get a 180° shutter blur. `source/verify.py` checks the result
  for repeated frames and uneven motion.
- **Voice:** [Kokoro-82M](https://huggingface.co/hexgrad/Kokoro-82M) (Apache-2.0), a 60/40 blend of the `am_onyx`
  and `am_fenrir` voices, one line at a time so each line can be placed on its beat (`source/tts.py`; the phonemes are
  pinned for "mech", "Chronofall" and "tear"). Word offsets inside each line (`source/lineword.py`) put "ground" on the
  mech's slam, "Pulse" on the cut to the rifle and "Chronofall" on the title. Captions are timed per word from a
  Whisper transcript of the finished voiceover (`source/align.py`, `source/captions.py`).
- **Music and sound effects:** original, synthesized in `source/synth.py` and `source/audio.py`: a D harmonic minor
  trailer bed with braams on the impacts, and one sound per gadget (laser bolts timed to the bolts on screen,
  thrusters, servo stomps, the gate's sweep, the charger's hum). Only three soft whooshes. No samples or licensed
  tracks, so there are no copyright claims. The music is sidechained under the voice (`source/final.sh`).
- **Edit:** `source/plan.py` is the single source of truth (beat grid, shots, time maps, cues). `source/overlay.html`
  is rendered frame by frame with Playwright (`source/render_overlay.cjs`) in the listing's look: Orbitron and Exo 2,
  flux cyan, rift magenta, thruster amber and laser red. The fonts are the ones in `marketing/source/fonts/`.

## Posting checklist

- Upload the MP4 as a Reel and set `cover.jpg` as the cover. Keep the original audio; a trending track on top would
  bury the voiceover.
- Paste `caption.txt`. Put the Modrinth link in your bio before posting, since the caption points there.
- Add alt text, for example: "Minecraft gameplay: a rift opens in the night sky and a pod crashes down, then a
  jetpack, a hover skiff, a drone, a mech and laser weapons."
- Pin a first comment that answers the question yourself ("I'd last one night, max") so replies start.
- Post when your audience is most active (Insights → Most active times) and reply to early comments quickly.
- Cross-post the same file to TikTok and YouTube Shorts.

## Rebuilding

The scripts were run from a scratch working folder (`E` / `R` at the top of each script; the raw captures, the
voice lines and the rendered frames live there, not in git). Copy `source/` there with `marketing/source/fonts/` as
`fonts/` and `marketing/images/icon.png`, then: `capture/shots.py <shot>...` for the footage, `prep.py <clip>` and
`colors.py <clip>` per clip, `tts.py`, `lineword.py`, `plan.py`, `build_vo.py`, `align.py`, `captions.py`, `audio.py`,
`compose.py`, `node render_overlay.cjs`, `final.sh`, and `verify.py reel.mp4 edl.json`.
