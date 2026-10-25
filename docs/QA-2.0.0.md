# QA record: 2.0.0

`tools/qa/run_matrix.sh` (see [CONTRIBUTING](../CONTRIBUTING.md#testing-in-game)) on a fresh flat world per build: a
dev server and a real game client under Xvfb (software rendering), driven over RCON and with real mouse and keyboard
input by `tools/qa/chronofall_checks.py`. Every build also loads a world saved by **Robotica 1.0.1** (built from this
repository's history) and checks that its blocks, chest contents and robots came back as Chronofall content. The
unit tests (JUnit, `common/src/test`) pass in every workspace's `./gradlew build`.

## Results

Final run, on the code in this release:

| Minecraft | Loader | Checks | Failures |
|-----------|--------|--------|----------|
| 1.20.1 | Fabric | 84 | 0 |
| 1.20.1 | Forge | 75 | 0 |
| 1.21.1 | Fabric | 84 | 0 |
| 1.21.1 | NeoForge | 84 | 0 |
| 26.1.2 | Fabric | 84 | 0 |
| 26.1.2 | NeoForge | 84 | 0 |
| 26.2 | Fabric | 84 | 0 |
| 26.2 | NeoForge | 84 | 0 |
| 26.3 | Fabric | 84 | 0 |
| 26.3 | NeoForge | 84 | 0 |

The first final run of 26.1.2 NeoForge failed one check (packing the drone): 26.x renamed the gamerules the QA
script used to turn off mob spawning, a slime spawned on the flat test world, and the drone's Collector chip put its
slime ball into the player's empty hand. The script now sets both spellings of the rules and clears the player right
before packing; the rerun passed all 84. The mod was not changed.

The 84 checks of a build:

| Group | Checks | What |
|-------|--------|------|
| registry | 3 | Every item (82), block (16) and entity (6) in the language file exists in the game |
| config | 2 | `config/chronofall.json` is written with its comments and keeps the values already in it |
| legacy | 10 | `robotica:` and `theyear3001:` blocks (including `crafting_workstation` → Fabricator and the stairs), items (including `empty_card`, `mech_mk_1_egg`, `space_ship_capsule`, `boggie_mk_1_egg`, an armour piece) and entities (`boggie`, `space_ship`, `sentinel_mk_1`, `mech_mk_1`) resolve to their successors, and removed oil stays removed |
| fixture | 13 | The Robotica 1.0.1 world: eight placed blocks, a chest of old items and four old robots are Chronofall blocks, items and entities |
| data | 3 | The titanium ore and Sentinel loot tables drop their items; the titanium ore feature places ore in deepslate |
| rift | 8 | `/chronofall rift_at` opens a rift, a Chrono Pod lands where it opened, digs a crater lined with scorched blocks, places a Rift Cache and two or three Sentinels; with the QA config a rift also opens on its own at night next to the player |
| charger | 5 | Coal charges the jetpack in the slot, a drone 5 blocks away is charged wirelessly, a comparator reads the charger, a Rift Shard burns for 40,000 Flux, a hopper feeds it fuel |
| fabricator | 2 | A hopper below takes the output; a hopper above cannot push into it |
| sentinel | 2 | A Sentinel fires laser bolts at an iron golem and they hurt it |
| mech | 2 | A mech dropped from a height slams the ground (mobs around it are hurt) and takes no fall damage |
| gate | 1 | A player standing on a linked Rift Gate arrives on its partner |
| client | 31 | Jetpack (flies up when jump is held, uses Flux, no fall damage); skiff (mounts, flies forward, climbs, uses Flux, hovering past the server's 4-second limit does not kick the rider, sneaking gets off); mech (mounts, walks, uses Flux, sneaking gets off, a Bulk Upgrade Card upgrades it); drone (deploys from its item, takes chips, a Flux Cell powers it, the Guard chip shoots a zombie, the Collector chip brings items, sneak-using packs it with its chips); the Fabricator screen (prints a Flux Cell, takes the ingredients); the Plasma Cutter cuts a block 7 blocks away and it drops; the Pulse Rifle hurts a target; the Rift Tuner links two gates both ways; a Rift Cache cracks open and drops Rift Shards; 4 Rift Shards on a Rift Beacon call a rift; the client loaded every Chronofall model, texture, sound and particle |
| log | 2 | The server log has no errors about the mod and no errors at all |

On **Forge 1.20.1** the 10 legacy checks are not run: Forge maps old ids when a world loads (registry aliases, and
a missing-mappings handler for worlds that list the old ids), but its commands only accept current ids, so typing
`robotica:...` in a command is not a meaningful test there. The fixture checks cover Forge with the real Robotica
world, so Forge runs 75 checks.

Screenshots taken by the runs (jetpack flight, skiff and mech HUDs, the drone, the Fabricator screen and its output)
were looked at for every version; the listing screenshots are from 26.3 Fabric (see `marketing/README.md`).

## Production jars

The dev runs use deobfuscated Minecraft, so the three release jars that are remapped for obfuscated Minecraft were
also run on real servers: the Fabric server launcher (loader 0.16.9) with Fabric API for 1.20.1 and 1.21.1, and the
Forge 1.20.1 installer's server (Forge 47.4.0), each with the jar from `releases/` and the Robotica fixture world.
`chronofall_checks.py --fixture --server-dir ...` without the client:

| Jar | Checks | Failures |
|-----|--------|----------|
| `chronofall-fabric-1.20.1-2.0.0.jar` | 51 | 0 |
| `chronofall-fabric-1.21.1-2.0.0.jar` | 51 | 0 |
| `chronofall-forge-1.20.1-2.0.0.jar` | 42 | 0 |

The Forge server loaded the Robotica world without any "unidentified mapping" error. The 26.x builds run
unobfuscated Minecraft and NeoForge runs Mojang names at runtime, so their dev runs already use the production
names. `.github/scripts/detect-jar-meta.js` reads the loader and Minecraft version of all ten jars correctly.

## Found and fixed by this QA

* **Skiff riders were kicked for "floating a vehicle too long".** The skiff set no-gravity in its constructor, but a
  skiff loaded from a save or a command reads `NoGravity` from its data (false when absent), and the server then
  counted a hovering skiff as floating. The skiff now always reports no gravity. A check hovers past the server's
  limit.
* **26.x: old worlds did not load their Robotica content on Fabric.** The registry mixin that maps old ids was not
  listed in the 26.x Fabric mixin configuration.
* **26.x: rift craters left the grass in place.** Grass blocks left the `#dirt` tag in 26.x; craters now dig the
  `#substrate_overworld` blocks there.
* **The pod could miss the ground block it hit** when its last move ended inside the ground (seen on 26.x), leaving a
  crater one block too low. It now finds the top of the ground.
* **Forge 1.20.1 logged "unidentified mapping" errors for Robotica worlds.** Old ids are now remapped through Forge's
  missing-mappings event; removed content (oil) is dropped.
* **The rift was hard to see from the ground** (found while taking the listing screenshots): it now opens wider with
  three dense rings and rift motes raining towards the landing spot.
* **The QA run caught its own mistakes too**: an item-list parser that missed 1.20.1's nested item tags, a quick
  sneak tap that a slow software-rendered client could miss (dismounts now hold sneak), a Rift Tuner click sent before
  the client had the gate's chunk, and the missing Mesa EGL libraries for 26.3 clients. Those were fixed in the
  script and the environment, not the mod.

## Known limits

* On **1.20.1 Fabric** the mech rider's +2 reach does not apply (that version has no reach attribute); everything else
  is the same on every build.
* Robotica 1.0.1 for **26.2** crashed on startup on both loaders, so no world was ever saved with it; the 26.x runs
  load the 1.21.1 Robotica world (upgraded by Minecraft's own data fixers), the path a player updating both would take.
