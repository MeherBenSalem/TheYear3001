# Chronofall 2.0: audit, market gaps and plan

Chronofall is the remake of the mod published as **The Year 3001** (0.x, mod id `theyear3001`) and **Robotica**
(1.0.x, mod id `robotica`). This document records what 1.0.1 actually did (read from the code and data in every
workspace, not from the README), what players of futuristic and tech mods ask for, where 1.0.1 fell short, and what
2.0 builds instead. Every 2.0 claim is implemented in all ten builds and checked in game by
`tools/qa/chronofall_checks.py` (see [CONTRIBUTING](../CONTRIBUTING.md#testing-in-game) and
[QA-2.0.0](QA-2.0.0.md)).

## 1. Audit of 1.0.1

| Area | What the code and data did |
|------|----------------------------|
| Code base | MCreator output, hand-moved into MultiLoader workspaces: `procedures/` classes that compare slot indices one by one, `new Object() { ... }.getAmount(20)` helpers, every entity with copy-pasted `tick`/`travel` bodies, no tests. Three workspaces (1.20.1, 1.21.1, 26.2) whose code had drifted apart. |
| Mod id | `robotica` since 1.0.0. 0.x used `theyear3001`; 1.0.0 renamed every id without any remapping, so 0.x worlds lost every item, block and robot. The language file still carried 70+ dead `theyear3001` keys. |
| 1.21.1 data | Recipes and loot tables were in the pre-1.21 folders (`recipes/`, `loot_tables/`) with the pre-1.21 `"item"` result key: **nothing in the mod could be crafted and no block dropped anything** on 1.21.1. |
| Ores | The configured/placed features existed, but no build added them to biomes: Fabric had no biome modification at all and the NeoForge 1.21.1 modifiers used the removed `forge:add_features` type in a `forge/` folder. **Titanium, cobalt, orichalcum and scrap debris never generated on 1.21.1 or 26.2.** |
| Crafting Workstation | A 5×5 grid where three hard-coded shapes (mech, boggie, ship) were matched slot by slot when a button was pressed. No recipe book, no feedback; wrong items were silently kept. |
| Robots | **Boggie** and **R1-D1 Mech**: spawned from "egg" items, extended `Monster` (so they despawned in peaceful and counted as hostile), had no owner, no goals for the mech, and the mech accepted any rider. Upgrade cards added a **random** amount (`RandomSource.create()` per click) to max health, damage, speed or armour **with no cap**, and printed raw doubles in chat. |
| Sentinel | A melee zombie with a 128×128 texture of noise; no way to meet it except a spawn egg (no natural spawns, no structures). |
| Flying capsule / spaceship | A 7-block-wide "submarine" model, `setNoGravity(true)` even with no rider, three separate key packets (up, down, forward), and forward flight set the velocity straight to the look vector every tick. On 26.2 the renderers were replaced by the **player model** with the robot texture. |
| Oil | A fluid and a bucket with no use. |
| Tools and armour | Four sets (scrap, titanium, cobalt, orichalcum), several hoes named "Shovel", all armour named "Armor Helmet". |
| Sounds | Two robot sounds; stereo files that did not attenuate until a late fix. |
| Versions | 1.20.1 (Fabric, Forge), 1.21.1 (Fabric, NeoForge, plus an unused `forge/` folder and a stray `src/`), 26.2 (Fabric, NeoForge). No 26.1.2 or 26.3. **The 26.2 builds crashed on startup on both loaders** ("Block id not set": 26.x needs the id on the block and item properties before construction), so 1.0.1 never ran on 26.2. |
| Tooling | Leftover generator scripts (`scripts/gen_*.py`, `scopes/`), an `upload_platforms.mjs`, a publish workflow that guessed the loader from the file name and published every jar with the same Minecraft versions; no tests, no QA, no release jars. |

## 2. What players of futuristic and tech mods ask for

Read from the most-downloaded technology, transportation and equipment projects on CurseForge and Modrinth, their
comment sections and issue trackers, and from long-running community requests:

1. **Flight without a power grid.** Jetpacks are among the most requested items, but almost every jetpack today is
   an add-on to a large energy or machine ecosystem. Players want to craft one and fly.
2. **Robots that do something.** "Robot" mods are mostly hostile mobs or pets that only fight. The requests are for
   helpers: something that picks up items, guards you, lights the way, and that you can pick up and carry.
3. **Vehicles with good controls on servers.** Rideable mechs and flying craft exist, but players complain about
   jittery multiplayer movement, vehicles that fly away unridden, and no feedback on fuel or speed.
4. **Fast travel that feels like technology.** Waypoint teleporters are among the most downloaded mods of all, almost
   always themed as magic.
5. **Reasons to explore.** Ore-and-tools mods get the same review: "another ore mod". Players ask for events and
   places that reward going out at night.
6. **Spectacle.** The comments under trailers are about how things look and sound: beams, thruster flames, glowing
   eyes in the dark, animations. Recipe-only tech is skipped.
7. **Approachable progression.** Standalone sci-fi mods that do not need a 300-page guide, a power network and pipes.
8. **Basics done right:** every loader on the versions people play, working recipes and drops, stable ids so worlds
   survive updates, a readable config, no hard dependencies.

The market itself: the technology category is dominated by large industrial ecosystems and their add-ons, and by
utilities. Standalone futuristic mods with robots, flight and vehicles are rare and small (1.0.1 itself sat at a few
thousand downloads), so a polished one has little direct competition.

## 3. Gap table

| Expectation | 1.0.1 | 2.0 |
|-------------|-------|-----|
| Something to explore for | Ores (that never generated on 1.21.1/26.2) | **Chrono Rifts**: a rift tears open in the night sky and a **Chrono Pod** crashes nearby, leaving a crater, a **Rift Cache** of salvage and Sentinels guarding it; a **Rift Beacon** calls one on demand. Ores generate again on every loader. |
| Enemies | A melee Sentinel from a spawn egg | **Sentinels** come out of the rifts, keep their distance and fire **laser bolts**; their eyes glow in the dark. They drop scrap and Rift Shards. |
| Crafting tech | A 5×5 grid matching three hard-coded shapes | The **Fabricator**: a recipe list with ingredient counts, one click to print, and a **hologram** of the item spinning above the block while it works. |
| Energy | none | **Flux**, without cables: the **Flux Charger** burns fuel (or a Rift Shard) and charges the device in its slot, the gear players wear nearby, and any drone, mech or skiff within 8 blocks through visible **charge beams**. Every device shows its charge as a bar. |
| Flight | A spaceship that set velocity to the look vector | **Jetpack** (hold jump; thruster flames and sound; no "flying is not enabled" kicks) and the **Hover Skiff**: a 1-seat flying vehicle with inertia, banking, engine hum and a HUD. |
| Robots | Boggie and Mech, no owner, random uncapped cards | **Drone** companion with an owner and three **chips** (Collector, Guard, Lamp), picked up into an item that keeps its charge and chips. **Mech**: a rideable 3-block walker that steps up blocks, leaps and **slams** the ground; upgrade cards give fixed, capped bonuses. |
| Tools and weapons | Four metal tool sets | The sets stay (named properly, tiers rebalanced), plus the **Plasma Cutter** (a mining beam that cuts blocks 12 blocks away) and the **Pulse Rifle** (laser bolts). |
| Travel | none | **Rift Gates**: link two gates with a **Rift Tuner**, stand on one and arrive at the other, across dimensions. |
| Feedback | Chat messages with raw doubles | Charge bars, a flux gauge on the HUD while flying or riding, tooltips with numbers, actionbar messages. |
| Visuals and sound | Two sounds, one model replaced by the player model on 26.2 | New code models for every robot and vehicle with animations, glowing emissive layers, three custom particles (flux, rift, thruster), animated textures, a hologram renderer and fourteen sound events. |
| Old worlds | 0.x worlds broke at 1.0.0 | Every kept item, block and entity loads from **both** old namespaces (`robotica:` and `theyear3001:`), including renamed ids (`crafting_workstation` → `fabricator`, `boggie` → `drone`, `space_ship` → `skiff`). |
| Versions | 1.20.1, 1.21.1, 26.2 | 1.20.1 (Fabric, Forge), 1.21.1, 26.1.2, 26.2 and 26.3 (Fabric, NeoForge), identical features and ids |
| Tests / QA | none | JUnit for the Minecraft-free rules; a real dev server and client on all ten builds, driven by RCON and real input; the remapped release jars run on real servers |

## 4. Identity

**Name: Chronofall.** The year 3001 did not arrive slowly: it fell. Pieces of the future crash through rifts in the
night sky, and you build your tech from what you salvage. The name keeps the old theme (the year 3001, robots,
salvage) and gives the mod a single hook that the rifts, the pods and the shards all hang from. The mod id is
`chronofall`; the GitHub repository keeps its name.

**Look.** Deep night navy, **flux cyan** for anything powered, **rift magenta** for anything from the rift, and
**thruster amber** for heat and motion. Machines are dark gunmetal panels with cyan light strips; rift things are
cracked black glass with magenta glow. The same three colours run through the particles, the HUD, the GUIs and the
marketing art.

**Version: 2.0.0, "Impact".** A full remake, so a new major version.

## 5. The feature set and why

### The loop

1. **Salvage.** Mine titanium, cobalt, orichalcum and scrap debris (ores generate again). At night, watch the sky:
   a Chrono Rift opens above a spot 24 to 48 blocks from you, a pod streaks down trailing fire, and the impact leaves
   a crater with a Rift Cache and two or three Sentinels.
2. **Fabricate.** Crack the cache for Rift Shards, circuits, scrap and metal. The Fabricator turns them into flux
   cells, processors and devices.
3. **Power.** The Flux Charger burns coal (or a Rift Shard for a lot of flux) and charges your gear and robots.
4. **Gear up and go.** Jetpack, Plasma Cutter, Pulse Rifle, a Drone at your side, a Mech for fights, a Skiff for
   travel, and Rift Gates between your bases. With enough shards, a Rift Beacon calls rifts on demand, so the loop
   scales into the late game.

Each step uses the previous one, every device runs on the same Flux, and the rift is both the source of the best
loot and the main danger.

### Chrono Rifts (the signature event)

* Every minute of night (time 13000 to 23000) each player in the Overworld has a **4 % chance** (config) of a rift,
  with at least 15 minutes between rifts for the same player. About one night in three brings one.
* The rift opens about 40 blocks above a surface spot 24 to 48 blocks away: a ring of magenta rift particles and a
  rumble everyone nearby hears. Two seconds later a **Chrono Pod** falls from it with a fire-and-rift trail.
* On impact: an explosion effect (no block damage, config), a 3-block crater lined with scorched blocks, the
  **Rift Cache** in the middle, and **2 to 3 Sentinels** (none on Peaceful). Nearby players get an actionbar warning
  with the direction.
* The **Rift Cache** cracks open on right-click and drops its salvage: 2 to 4 Rift Shards, scrap, a metal, circuit
  parts and, rarely, a processor. The rolls live in Minecraft-free code with unit tests.
* The **Rift Beacon** takes 4 Rift Shards on right-click and calls a pod near itself after a short countdown.
* `/chronofall rift [player]` (operators) starts an event, for servers and testing.

Reasoning: a world event gives players something to do at night, makes Sentinels feel like they belong, and makes
Rift Shards (the key ingredient) come from play rather than from mining a ninth ore.

### Sentinels

Hostile robots from the rift. They keep 6 to 12 blocks away and fire a **laser bolt** every two seconds (5 damage,
visible cyan-red bolt with a trail), and punch when cornered. Their visor glows red in the dark through an emissive
layer. 30 health, 4 armour. Drops: 1 to 3 scrap, a 25 % chance of a Rift Shard. The old Sentinel id is kept.

### The Fabricator

Replaces the Crafting Workstation (old blocks in worlds become Fabricators). The screen lists every recipe; selecting
one shows its ingredients with "have / need" counts in green or red, and **Fabricate** takes them from the player's
inventory and prints the item over a few seconds into the output slot. While it prints, a **hologram** of the item
spins above the block in flux cyan and the block hums. Recipes are a table in Minecraft-free code (tested: every
ingredient and result id exists, counts are positive).

### Flux and the Flux Charger

Flux is an integer stored on each device (as item data or entity data). The Flux Charger burns any furnace fuel at
4 flux per burn tick (coal: 6,400 flux) or a Rift Shard for 40,000, keeps up to 100,000, and every tick:

* charges the device in its slot (400 flux per tick),
* charges flux devices worn or held by players within 4 blocks,
* charges drones, mechs and skiffs within 8 blocks (200 per tick each), drawing a **charge beam** of flux particles
  to each one.

A comparator reads its buffer. No cables, no networks: one block, one idea.

### Jetpack

Chest-slot armour (titanium armour value). Hold jump to thrust up; forward input while thrusting gives a horizontal
push; release to fall, with no fall damage while the jetpack has charge. 8 flux per tick of thrust (12,000 capacity:
75 seconds of thrust). Thruster flames and smoke come out of both nozzles and a thrust sound loops. The server stops
counting jetpack flight toward the "flying is not enabled" kick, so it works on servers with `allow-flight=false`.

### Plasma Cutter and Pulse Rifle

* **Plasma Cutter**: hold use to cut the block you look at, up to 12 blocks away, at a speed set by the block's
  hardness (a diamond-tier tool: obsidian takes about 4 seconds, stone a fraction of one). A flux beam runs from the
  tool to the block, the crack overlay shows progress, and the block drops as if mined. Respects spawn protection and
  claims (it mines through the player's own game mode). 30 flux per block.
* **Pulse Rifle**: fires a laser bolt (7 damage) every 10 ticks for 60 flux.

### The Drone

A small hovering robot with spinning rotors and a glowing eye, bound to the player who deploys it. It follows its
owner at shoulder height. Right-click it with a chip to install it (one of each):

* **Collector chip**: flies to items within 10 blocks and delivers them to the owner's inventory.
* **Guard chip**: fires laser bolts at hostile mobs within 10 blocks of the owner.
* **Lamp chip**: lights the area around it like a torch (a light block that moves with it).

Sneak-right-click picks it up as an item that keeps its charge and chips. It uses 1 flux per tick plus 40 per shot;
at 0 it lands and powers down until charged (by a Charger or by right-clicking it with a Flux Cell). It never
attacks its owner and takes no damage from its owner. The old Boggie id now loads as a Drone.

### The Mech

A rideable 3-block walker (the old R1-D1 Mech, new model and animations). It walks up full blocks, **leaps** on jump,
and landing after a leap **slams** the ground: 8 damage and knockback to mobs within 4 blocks, with a shockwave of
particles. The rider's melee attacks get +6 damage while seated. 2 flux per tick while moving. The **upgrade cards**
work on the mech with fixed steps and caps: Bulk (+10 max health, up to 5 times), Damage (+2 rider bonus damage, up
to 5), Defense (+4 armour, up to 5), Speed (+10 % speed, up to 3), Repair (heals 20). Sneak-right-click with an empty
hand packs it into an item that keeps its upgrades and charge.

### The Hover Skiff

A one-seat flying craft (new model, much smaller than the old 7-block ship). W/S accelerate and brake, A/D turn,
jump climbs, sprint (Ctrl) descends; speed builds up and decays smoothly and the hull banks into turns. Unridden it
settles to the ground. Engine hum, thruster particles, and a HUD gauge with flux, speed and altitude. 6 flux per tick
while flying (30,000 capacity: about 4 minutes). Movement is computed by the rider's client, as vanilla does for
boats, so it is smooth on servers. Old spaceships load as skiffs.

### Rift Gates

A gate pad of black glass and magenta light. Right-click a gate with a **Rift Tuner**, then another: they are linked
both ways (any distance, any dimension, config). Stand on a linked gate: rift particles spiral up for two seconds and
you arrive on top of the other gate, with a sound at both ends and a short cooldown. A broken partner unlinks the
gate.

### Materials, ores, tools and armour

Titanium, cobalt, orichalcum and scrap debris generate again on every loader (titanium from Y -64 to 16, most
common around -24; cobalt below -16; orichalcum below -40; scrap debris below 0). The four tool and armour sets stay with proper names and a
clear tier order: scrap (between stone and iron) < titanium (between iron and diamond) < cobalt (diamond) <
orichalcum (between diamond and netherite). Rift Shards, Flux Cells, wiring, circuit boards and processors are the
tech components.

### What was removed

* **Oil** and the oil bucket: a fluid with no use, and a fluid needs a lot of per-loader code. Oil blocks in old
  worlds turn into air.
* The **flying capsule keys** (fly up / fly down / forward): the skiff uses the vanilla movement keys.

## 6. Ids and migration

The mod id changes from `robotica` to `chronofall`. Old worlds are migrated, not restarted:

* Every kept id is also accepted under `robotica:` and `theyear3001:`. Lookups of an id that does not exist in the
  registry are redirected to the new id before the game gives up on it, for blocks (chunks), items (inventories,
  containers, item entities), entities and block entities. On NeoForge and Forge this uses the loader's registry
  alias and missing-mapping support; on Fabric a small mixin does the redirect.
* Renamed ids: `crafting_workstation` → `fabricator`, `boggie` and `boggie_mk_1_egg` → `drone`, `mech_mk_1_egg` →
  `mech`, `space_ship` and `space_ship_capsule` → `skiff`, `empty_card` → `blank_card`, plus the 0.x names
  (`mech_mk_1`, `sentinel_mk_1`, `flying_capsule_mech`, `flying_capsule`). The table is Minecraft-free code with unit
  tests.
* Oil (`oil`, `oil_bucket`) has no new id and disappears.

## 7. The plan as built

* Five MultiLoader workspaces (`1.20.1/`, `1.21.1/`, `26.1.2/`, `26.2/`, `26.3/`), each with `common/` and its two
  loaders. `common/.../core/` is Minecraft-free (config, flux maths, Fabricator recipes, rift scheduling and loot,
  upgrade-card rules, legacy ids) and is identical in every workspace; JUnit covers it.
* Content, behaviour and rendering live in `common/`; the loaders only register, hook events, send the one control
  packet (jetpack thrust and vehicle keys) and register renderers, screens, particles and the HUD.
* Assets are generated: `tools/gen_assets.py` draws the textures and writes models, blockstates, loot tables,
  recipes, tags and worldgen in each version's format; `tools/gen_models.py` turns the entity model specs into Java
  layer definitions and paints their textures; `tools/gen_sounds.py` synthesises the sound effects.
* QA: `tools/qa/` runs a dev server and a real client under Xvfb for every build and checks every feature (see
  [QA-2.0.0](QA-2.0.0.md)).

### Out of scope for 2.0

* Interop with other mods' energy systems.
* A space dimension or rockets: a different mod.
* Multi-seat vehicles and drone pathfinding through caves.
