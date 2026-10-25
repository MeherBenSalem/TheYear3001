# Marketing

Listing copy and artwork for CurseForge and Modrinth.

| Path | What it is |
|------|------------|
| `curseforge/description.md` | The project description (Markdown; also works on Modrinth). Images load from this repo's `main` branch. |
| `curseforge/summary.txt` | The one-line summary (under 250 characters). |
| `images/banner.png` | 1800×600 header banner. |
| `images/icon.png` | 512×512 project icon (also the mod's in-jar `logo.png`). |
| `images/card-*.png` | 1600×900 feature cards used in the description and as gallery images. |
| `images/header-*.png`, `images/divider.png` | Transparent section headers and a divider for the description. |
| `images/screenshots/*.jpg` | Real in-game captures (Minecraft 26.3, Fabric) used by the cards and the gallery. |
| `source/render.cjs` | Renders every PNG above from the screenshots and the bundled fonts. |
| `source/config-excerpt.json` | The config file as the mod writes it on first start, shown on the config card. |
| `source/fonts/` | Orbitron and Exo 2, both under the SIL Open Font License (licence files included). |

The look: the night the rifts open. Deep night navy behind everything with a faint cyan grid, titles in Orbitron with
a flux-cyan gradient, glowing cyan frames around the captures, diamond bullets, and one colour per kind of thing: flux
cyan for anything powered (the Fabricator, the drone, the gates' links), rift magenta for anything from the rift (the
rift, the crater, the gates), thruster amber for heat and motion (the charger, the jetpack, the mech) and laser red
for the Sentinels. The icon is a flux-cyan "C" inside a magenta rift ring with an amber pod streaking past, over a
real capture of a rift.

## Regenerating the images

Needs Node.js and Playwright with Chromium:

```bash
NODE_PATH="$(npm root -g)" node marketing/source/render.cjs             # everything
NODE_PATH="$(npm root -g)" node marketing/source/render.cjs card-rifts  # one asset
```

The release name and the supported Minecraft versions and loaders are at the top of `render.cjs`. Supporting a new
Minecraft version means adding one line there, plus one row to the download table in `description.md`. After
changing the icon, copy `images/icon.png` to every workspace's `common/src/main/resources/logo.png`.

## Retaking the screenshots

`tools/qa/marketing_shots.py` captures every screenshot from a dev client (see `tools/qa/` and `CONTRIBUTING.md`): a
1920×1080 window under Xvfb, on a 26.3 Fabric dev server whose `server.properties` uses `level-name=showcase`,
`level-type=minecraft:normal`, the seed given at the top of the script and `view-distance=12`:

```bash
tools/qa/devrun.sh prepare-server 26.3 fabric showcase minecraft:normal 7214950366214
sed -i 's/view-distance=8/view-distance=12/' 26.3/fabric/runs/server/server.properties
tools/qa/devrun.sh start-server 26.3 fabric && tools/qa/devrun.sh start-client 26.3 fabric
python3 tools/qa/rcon.py "op Builder"
python3 tools/qa/marketing_shots.py              # or a few: rift crater jetpack
```

The script finds a plains biome near spawn, flattens a pad for the machines and sets each scene up with commands
(blocks, robots, time of day). Everything the mod does in the pictures is the mod running: the rift and the falling
pod, the crater and its Sentinels, the laser bolts, the charge beams, the hologram, the thruster flames, the HUD and
the cutter beam. Every screenshot is a single unedited frame: a screen capture, or for `rift.jpg` and `sentinel.jpg`
(the pod and a laser bolt are on screen for a moment only) one frame of a lossless ffmpeg screen recording, the one
with the most rift-magenta or laser-red pixels.

| Screenshot | Scene |
|------------|-------|
| `rift.jpg` | A Chrono Rift open in the night sky with the pod falling out of it |
| `crater.jpg` | The crash site at dawn: crater, Rift Cache and Sentinels (seen in spectator mode) |
| `fabricator.jpg` | The Fabricator printing a Jetpack, its hologram above it, beside a Flux Charger and a Rift Beacon |
| `fabricator_gui.jpg` | The Fabricator's screen with the Jetpack selected |
| `charger.jpg` | A Flux Charger at night beaming charge into a drone and a mech |
| `sentinel.jpg` | A Sentinel at night firing at an iron golem |
| `jetpack.jpg` | Flying with the jetpack, third person, with the flux gauge |
| `skiff.jpg` | Flying the Hover Skiff, third person, banking, with its HUD |
| `mech.jpg` | Riding the mech towards two Sentinels, seen from the front |
| `drone.jpg` | A drone with its Lamp chip beside its owner at night |
| `gate.jpg` | Two Rift Gates just linked with the Rift Tuner |
| `cutter.jpg` | The Plasma Cutter's beam cutting into a stone wall with titanium ore |
