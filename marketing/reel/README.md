# Instagram Reel: "The year 3001 just crashed into Minecraft"

| File | Use |
| --- | --- |
| `chronofall-reel.mp4` | The Reel: 1080×1920, 30 fps, 36.1 s, H.264 + AAC, about -15 LUFS. Not tracked in git. Rebuild it with the steps below |
| `cover.jpg` | Cover image. The title sits inside the centre 3:4 area, so it survives the profile-grid crop |
| `caption.txt` | Post caption with hashtags |

## Structure

Short-form retention practice: the payoff in the first half second, one idea per shot, burned-in captions for
sound-off viewers, a label on every gadget, and an ending that loops seamlessly into the first frame. Every cut,
voice line, label and sound sits on a 128 BPM grid; the Reel is exactly 77 beats long and starts on a beat, so the
music loops with the picture.

| Time | Shot | Voiceover |
| --- | --- | --- |
| 0.0–1.9 | A Chrono Pod slams down in front of the camera; flash, shake, the year counter rolls 2026 → 3001 | "The year 3001 just crashed into Minecraft!" |
| 1.9–4.2 | The crater from above, the Rift Cache and its Sentinels | |
| 4.2–6.6 | The drop: looking straight up as a Chrono Rift tears open | "Every night, rifts tear open the sky," |
| 6.6–9.8 | The pod falls the full height of the frame and lands on the beat, right after "tech" | "and drop pods of future tech!" |
| 9.8–11.7 | Sentinels firing laser bolts at the camera | "Guarded by Sentinels." |
| 11.7–13.1 | A Rift Cache cracking open | "Crack them open!" |
| 13.1–15.0 | The Fabricator printing a jetpack, its hologram spinning | "Print your own gear." |
| 15.0–25.8 | Jetpack take-off, Hover Skiff over the coast, Drone shooting zombies, the Mech's leap and ground slam (on "ground"), Plasma Cutter, Pulse Rifle (the cut lands on "Pulse") | "Jetpacks! Hover skiffs! Combat drones! A mech that shakes the ground! Plasma cutters! Pulse rifles!" |
| 25.8–27.7 | Stepping onto a Rift Gate and arriving at the other one | "Rift gates, to anywhere!" |
| 27.7–29.5 | A Flux Charger beaming charge into a drone, a mech and a skiff | "And zero cables." |
| 29.5–32.3 | Title card over the open rift; the name lands on "Chronofall" | "It's called Chronofall." |
| 32.3–36.1 | The quiet field before the first shot, an "answer below" prompt; the pod drops in as the Reel loops | "Would you survive the year 3001?" |

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
- **Voice:** the NightBeam channel voice ("gamer"): [Chatterbox](https://github.com/resemble-ai/chatterbox) (MIT)
  cloning the channel's reference clip with the channel's fixed settings (exaggeration 0.55, cfg_weight 0.3,
  temperature 0.7, seed 1000 × take + line number), run with the channel's own runner. The reference clip lives in the
  private channel repository, not here. Every line was generated in 4 to 8 takes (`source/voice/script.json`,
  `script2.json` spells "Chrono Fall" so the name is said clearly); `source/voice/pick.py` keeps only takes where
  Whisper hears exactly the script and the pitch matches the voice, then picks the most energetic one (pace, pitch
  movement, punch). Nothing is sped up or time-stretched: the edit gives each shot the beats its line needs
  (`BEATS` in `source/plan.py`, which refuses a plan where a line would not fit). Word offsets inside each line
  (`source/lineword.py`) put the mech's slam on "ground", the cut to the rifle on "Pulse" and the title on
  "Chronofall". The mix gives the voice only the channel narration chain (low cut at 75 Hz, +1.5 dB at 3.5 kHz,
  de-ess, 2.5:1 compression) and ducks the music under it. Captions are timed per word from a Whisper transcript of
  the finished voiceover (`source/align.py`, `source/captions.py`).
- **Sound effects:** 37 professional recordings from [Mixkit](https://mixkit.co/free-sound-effects/) under the Mixkit
  Sound Effects Free License (free for commercial use in videos, no attribution required), placed by
  `source/sfx_pro.py`: cinematic impacts and a rock-debris explosion on the opening crash, a trailer riser and hit on
  the drop, a rocket whoosh into a metal explosion and an underground echo when the pod lands, laser shots on the
  Sentinels' bolts and the rifle, engine starts and a fly-by for the jetpack and skiff, servos and an apocalyptic
  stomp for the mech, a plasma buzz, a warp for the gate, an electric power-up for the charger, and a movie-logo hit
  on the title. Each recording's main hit is measured and lands on its exact frame; the swell before the opening
  impact wraps to the end of the Reel and carries the loop. `source/fetch_sfx.sh` downloads them by their Mixkit ids
  (the files are not stored here). Mixed in stereo, ducked lightly under the voice so the hits keep their punch.
- **Music:** an original bed synthesized in `source/synth.py` and `source/audio.py` (a D harmonic minor 128 BPM
  groove with drop-outs before the big hits), sidechained under the voice (`source/final.sh`).
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
`colors.py <clip>` per clip, the channel runner over `voice/script.json` (and `script2.json` for the extra takes),
`voice/pick.py`, `lineword.py`, `plan.py`, `build_vo.py`, `align.py`, `captions.py`, `audio.py`,
`fetch_sfx.sh` then `sfx_pro.py` (set `LIB` to the download folder),
`compose.py`, `node render_overlay.cjs`, `final.sh`, and `verify.py reel.mp4 edl.json`.
