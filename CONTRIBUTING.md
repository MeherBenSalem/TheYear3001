# Contributing

Thanks for helping improve Chronofall.

## How to contribute

1. Fork the repository and create a feature branch from `main`.
2. Make focused changes in the relevant Minecraft version workspace (`1.20.1/`, `1.21.1/`, `26.1.2/`, `26.2/` or
   `26.3/`). A gameplay change belongs in all five: the `common/` code is the same design everywhere and only
   version-specific APIs differ (see "Porting between versions" below).
3. Build the workspace you changed (`./gradlew build` from that folder, with Java 17 for 1.20.1, 21 for 1.21.1 and 25
   for 26.x). This also runs the unit tests.
4. Open a pull request describing the change and how you tested it.

## Layout

Each workspace is a MultiLoader Gradle build:

- `common/`: everything that matters.
  - `core/` is Minecraft-free and unit-tested (`common/src/test`): the config file, Flux maths, the Fabricator
    recipes, rift scheduling and Rift Cache loot, the upgrade-card rules and the table of old ids. It is identical in
    every workspace.
  - `content/` registers everything, `block/`, `item/` and `entity/` hold the blocks, devices and robots, `rift/` the
    Chrono Rift event, `menu/` and `client/` the screens, models, renderers, particles and HUD, `legacy/` the old-id
    aliases, `network/` the one control packet (jetpack thrust and vehicle keys).
- `fabric/`, `neoforge/` (`forge/` on 1.20.1): entry points that register the content, events, the control packet,
  renderers, screens, particles and the HUD. Old ids load through registry aliases on NeoForge and Forge and through a
  small registry mixin on Fabric.

Assets and data are generated, not edited by hand:

```bash
python3 tools/gen_textures.py   # block, item, GUI and particle textures, into tools/assets/textures
python3 tools/gen_sounds.py     # the synthesised sound effects, into tools/assets/sounds
python3 tools/gen_models.py     # entity models: ModelMeshes.java and the entity textures, into every workspace
python3 tools/gen_assets.py     # lang, models, blockstates, sounds.json, loot tables, recipes, advancements, tags, worldgen
```

`tools/gen_assets.py` knows each version's format (plural data folders and `forge:` biome modifiers on 1.20.1, item
model definitions and equipment assets on 26.x, loot and worldgen syntax on 26.3, pack formats); change the tables at
its top and run it, then rebuild.

## Porting between versions

The differences you will meet when carrying a change across workspaces:

| Area | 1.20.1 | 1.21.1 | 26.x |
|------|--------|--------|------|
| Item data | NBT tag (`ItemData`) | `minecraft:custom_data` component | same, `getIntOr`-style getters |
| Saving | `load`/`saveAdditional(CompoundTag)` | `+ HolderLookup.Provider` | `ValueInput`/`ValueOutput` |
| Block use | `use(...)` | `useItemOn` / `useWithoutItem` | same, one `InteractionResult` type |
| Rendering | `renderToBuffer(..., r, g, b, a)` | `renderToBuffer(..., color)` | render states and `submit` |
| Screens | `GuiGraphics` | `GuiGraphics` | `GuiGraphicsExtractor` (`extract*`) |
| Networking | Fabric buffers / Forge `SimpleChannel` | `CustomPacketPayload` | same |

## Testing in game

`tools/qa/` drives dev servers over RCON and a real client under Xvfb (software rendering). It needs `xdotool`,
ImageMagick (`import`), `ffmpeg` (for the marketing captures) and an X server on `:1`
(`Xvfb :1 -screen 0 1920x1080x24 &`); 26.x clients also need Mesa EGL (`libegl1 libegl-mesa0 libgl1-mesa-dri
libgles2`).

```bash
tools/qa/devrun.sh prepare-server 26.3 fabric     # eula, RCON on 25575 (password qa), a fresh flat world, a QA config
tools/qa/devrun.sh start-server 26.3 fabric
tools/qa/devrun.sh start-client 26.3 fabric       # joins localhost:25565 as "Builder"
python3 tools/qa/chronofall_checks.py --log /tmp/cf-qa/fabric-server.log --ws 26.3 --loader fabric --client --shots /tmp/cf-qa/shots
tools/qa/devrun.sh stop-client; tools/qa/devrun.sh stop-server
```

`chronofall_checks.py` sets every scene up with commands and checks it through RCON and real mouse and keyboard
input: every block, item and entity is registered; the config file; old `robotica:` and `theyear3001:` block, item and
entity ids; loot tables and ore features; a Chrono Rift (pod landing, crater, Rift Cache, Sentinels, and a rift
opening on its own at night); the Flux Charger (fuel, Rift Shards, the charge slot, wireless charging of a drone and a
worn jetpack, comparator, hoppers); the Fabricator's hoppers; Sentinel laser bolts; the mech's ground slam; Rift Gate
travel. With `--client` the player flies the jetpack, the skiff and the mech, upgrades the mech, deploys a drone and
installs its chips, powers it with a Flux Cell, lets it guard and collect, packs it up, fabricates a Flux Cell in the
Fabricator screen, cuts a block with the Plasma Cutter, shoots the Pulse Rifle, links two gates with the Rift Tuner,
cracks a Rift Cache and calls a rift with a Rift Beacon; the client log is checked for every model, texture, sound and
particle. It ends by scanning the server log for errors. With `--fixture` it also checks a world saved by Robotica
1.0.1 (blocks, chest contents and robots under the old ids).

`tools/qa/run_matrix.sh` does all of that on a fresh world for every build (ten: five versions, two loaders each),
loading the Robotica fixture from `tools/qa/fixtures/` where there is one:

```bash
tools/qa/run_matrix.sh                       # all builds; results in /tmp/cf-qa/results/
tools/qa/run_matrix.sh 26.3:fabric 1.20.1:forge
```

The fixtures were saved by `tools/qa/make_fixture.py` on dev servers of Robotica 1.0.1 built from this repository's
history (`git archive fc4e6c1 <workspace>`). Stop dev runs with `devrun.sh stop-client` / `stop-server`, which only
look at Java processes.

If you change gameplay or data, run the checks on every workspace you touched, for both loaders.

## Releasing

`tools/build_release.sh` builds every workspace with its own JDK and copies the jars into `releases/`
(`chronofall-{loader}-{minecraft}-{version}.jar`). Release notes go in `patchnotes/<version>.md` and a summary in
`PATCH_NOTES.md`. The publish workflow (`.github/workflows/publish.yml`) uploads `releases/` to Modrinth and
CurseForge when a `v*.*.*` tag is pushed; the maintainers run it.

## Coding expectations

- Prefer small, reviewable PRs over large mixed changes.
- Keep ids stable: block, item, entity and block entity ids are what saved worlds refer to. A renamed id needs an
  entry in `core/LegacyIds`.
- Keep logic Minecraft-free where it can be, and test it in `common/src/test`.
- Match existing naming, package layout, and MultiLoader patterns.
- Do not commit build output, run directories, IDE metadata, or local secrets.
- When fixing a bug, say which versions and loaders you verified.

## Issues

Use GitHub Issues for bugs and feature requests. Include the Minecraft version, mod loader, mod version and steps to
reproduce when reporting bugs, and the log when the game crashed.

## Licensing

By contributing, you agree that your contributions are licensed under the Apache License, Version 2.0, unless you
state otherwise in the pull request.
