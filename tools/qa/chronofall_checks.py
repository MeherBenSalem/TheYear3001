#!/usr/bin/env python3
"""In-game checks for Chronofall against a running dev server (see devrun.sh), optionally with the QA client.

Server checks (RCON only):
  * registry: every block, item and entity in the language file exists in the game;
  * config: config/chronofall.json was written with comments;
  * legacy: robotica: and theyear3001: block, item and entity ids load as their Chronofall successors (commands and
    container contents use the same registry lookups as saved chunks); with --fixture, a world saved by Robotica 1.0.1
    loads with its blocks, chest contents and robots migrated;
  * data: loot tables, the ore features, recipes (through the log), block tags (a pickaxe is the right tool);
  * rift: /chronofall rift_at opens a rift, a Chrono Pod lands, digs a crater, places a Rift Cache and its Sentinels;
    with the QA config (chance 1 per minute) a rift also opens on its own at night near the player;
  * charger: coal and Rift Shards burn into Flux, the charge slot, a drone in range and a worn jetpack are charged,
    a comparator reads it, hoppers feed it;
  * fabricator: hoppers take its output and cannot push into it;
  * sentinel: a Sentinel shoots an iron golem with laser bolts;
  * mech: landing from a height slams the ground and hurts mobs around it;
  * gate: a player standing on a linked Rift Gate arrives on its partner.
Client checks (--client, real mouse and keyboard): jetpack flight (height, Flux used, no fall damage, flames and HUD),
the skiff (mount, fly forward and up, Flux used), the mech (ride, walk, upgrade card), the drone (deploy, guard and
collector chips, pack up), the Fabricator screen (select, Fabricate, output, hologram), the Plasma Cutter, the Pulse
Rifle, the Rift Tuner, opening a Rift Cache, the Rift Beacon, and that the client loaded every model and texture.
The server and client logs are then scanned for errors.

  python3 tools/qa/chronofall_checks.py --log /tmp/cf-qa/fabric-server.log --ws 1.21.1 --loader fabric [--client --shots DIR] [--fixture]
"""
from __future__ import annotations

import argparse
import json
import re
import sys
import time
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
from rcon import wait_for  # noqa: E402

ROOT = Path(__file__).resolve().parents[2]
NS = 'chronofall'
PLAYER = 'Builder'
G = -61          # top layer of the flat world (grass); things stand on GY
GY = G + 1
FAILURES: list[str] = []
COUNT = [0]


def check(name, ok, detail=''):
    COUNT[0] += 1
    print(('PASS ' if ok else 'FAIL ') + name + (f'  [{str(detail)[:400]}]' if detail and not ok else ''), flush=True)
    if not ok:
        FAILURES.append(name)
    return ok


class Server:
    def __init__(self, port, legacy):
        self.rcon = wait_for(port)
        self.legacy = legacy  # 1.20.1 NBT and item formats

    def cmd(self, command):
        return self.rcon.command(command)

    def test(self, x, y, z, block):
        return 'passed' in self.cmd(f'execute if block {x} {y} {z} {block}')

    def count(self, selector):
        self.cmd('scoreboard objectives add cfqa dummy')
        self.cmd(f'execute store result score #n cfqa if entity {selector}')
        m = re.search(r'has (\d+)', self.cmd('scoreboard players get #n cfqa'))
        return int(m.group(1)) if m else 0

    def number(self, command):
        """The first number in a `data get` reply (or None)."""
        m = re.search(r'data:\s*(-?[\d.]+)', self.cmd(command))
        return float(m.group(1)) if m else None

    def entity_number(self, selector, path):
        return self.number(f'data get entity {selector} {path}')

    def block_number(self, x, y, z, path):
        return self.number(f'data get block {x} {y} {z} {path}')

    def items(self, x, y, z):
        return count_items(self.cmd(f'data get block {x} {y} {z} Items'))

    def blocks(self, x0, y0, z0, x1, y1, z1, block):
        """How many of `block` are in the box. Counting replaces them with air (fill only counts changed blocks)."""
        out = self.cmd(f'fill {x0} {y0} {z0} {x1} {y1} {z1} minecraft:air replace {block}')
        m = re.search(r'filled (\d+)', out)
        return int(m.group(1)) if m else 0

    def stack(self, item, count=1, flux=None):
        """An item stack in this version's NBT syntax, for container and entity data."""
        if self.legacy:
            tag = f',tag:{{Flux:{flux}}}' if flux is not None else ''
            return f'{{id:"{item}",Count:{count}b{tag}}}'
        comp = f',components:{{"minecraft:custom_data":{{Flux:{flux}}}}}' if flux is not None else ''
        return f'{{id:"{item}",count:{count}{comp}}}'

    def item_arg(self, item, flux=None):
        """An item argument for /give and /item with Flux on it."""
        if flux is None:
            return item
        if self.legacy:
            return f'{item}{{Flux:{flux}}}'
        return f'{item}[minecraft:custom_data={{Flux:{flux}}}]'

    def chest_item(self, player):
        """The item worn in the chest slot (26.x keeps armour in `equipment`, older versions in Inventory)."""
        if ARGS.ws.startswith('26.'):
            return self.cmd(f'data get entity {player} equipment.chest')
        return self.cmd(f'data get entity {player} Inventory[{{Slot:102b}}]')

    def held_item(self, player):
        """The item in the main hand (26.x has no SelectedItem: read the selected hotbar slot)."""
        if ARGS.ws.startswith('26.'):
            slot = self.entity_number(player, 'SelectedItemSlot')
            return self.cmd(f'data get entity {player} Inventory[{{Slot:{int(slot or 0)}b}}]')
        return self.cmd(f'data get entity {player} SelectedItem')

    def clear_area(self, x0, z0, x1, z1, height=8):
        for y in range(GY, GY + height, 4):
            self.cmd(f'fill {x0} {y} {z0} {x1} {min(y + 3, GY + height)} {z1} minecraft:air')
        self.cmd(f'fill {x0} {G} {z0} {x1} {G} {z1} minecraft:grass_block')


def count_items(text):
    """Item counts in a `data get` reply listing stacks; each stack is a top-level {...} (1.20.1 nests a tag in it)."""
    totals: dict[str, int] = {}
    depth, begin = 0, None
    objects = []
    for i, ch in enumerate(text):
        if ch == '{':
            if depth == 0:
                begin = i
            depth += 1
        elif ch == '}' and depth:
            depth -= 1
            if depth == 0:
                objects.append(text[begin:i + 1])
    for obj in objects:
        m = re.search(r'\bid: "([^"]+)"', obj)
        if not m:
            continue
        n = re.search(r'\b[cC]ount: (\d+)', obj)
        totals[m.group(1)] = totals.get(m.group(1), 0) + (int(n.group(1)) if n else 1)
    return totals


def lang():
    path = ROOT / ARGS.ws / 'common' / 'src' / 'main' / 'resources' / 'assets' / NS / 'lang' / 'en_us.json'
    return json.loads(path.read_text())


def uuid_array(s):
    out = s.cmd(f'data get entity {PLAYER} UUID')
    m = re.search(r'\[I;\s*(-?\d+),\s*(-?\d+),\s*(-?\d+),\s*(-?\d+)\]', out)
    return f'[I;{m.group(1)},{m.group(2)},{m.group(3)},{m.group(4)}]' if m else None


# Server checks -------------------------------------------------------------------------------------------------------

def check_registry(s):
    names = lang()
    items = sorted(k.split('.')[2] for k in names if k.startswith('item.chronofall.') and not k.endswith('.desc'))
    blocks = sorted(k.split('.')[2] for k in names if k.startswith('block.chronofall.'))
    entities = sorted(k.split('.')[2] for k in names if k.startswith('entity.chronofall.'))
    x, z = 0, -40
    s.cmd(f'setblock {x} {GY} {z} minecraft:chest')
    missing = [i for i in items if 'Replaced' not in s.cmd(f'item replace block {x} {GY} {z} container.0 with {NS}:{i}')]
    check(f'registry: all {len(items)} items exist', not missing, missing)
    bad = [b for b in blocks if 'Changed' not in s.cmd(f'setblock {x + 2} {GY} {z} {NS}:{b}')]
    check(f'registry: all {len(blocks)} blocks exist', not bad, bad)
    s.cmd(f'setblock {x + 2} {GY} {z} minecraft:air')
    bad = [e for e in entities if 'Summoned' not in s.cmd(f'summon {NS}:{e} {x + 6} {GY + 30} {z} {{NoGravity:1b}}')]
    s.cmd(f'kill @e[type=!minecraft:player,x={x + 6},y={GY + 30},z={z},distance=..4]')
    check(f'registry: all {len(entities)} entities exist', not bad, bad)


def check_config():
    server = Path(ARGS.server_dir) if ARGS.server_dir else ROOT / ARGS.ws / ARGS.loader / 'runs' / 'server'
    cfg = server / 'config' / 'chronofall.json'
    text = cfg.read_text() if cfg.exists() else ''
    check('config: written with comments', text.startswith('// Chronofall.') and '// Rift Gates' in text, str(cfg))
    check('config: QA values kept (chance 1 per minute)', '"chance_per_minute": 1' in text, text[:300])


def check_legacy(s):
    if ARGS.loader == 'forge':
        # Forge maps old ids when it loads a world (registry aliases and missing-mapping remaps), but commands only take
        # current ids; the --fixture checks cover Forge with a world saved by Robotica 1.0.1.
        print('SKIP legacy: old ids in commands (Forge: covered by the fixture world)', flush=True)
        check('legacy: removed content (oil) is not resurrected', 'Unknown' in s.cmd(f'setblock 20 {GY} -34 robotica:oil')
              or 'Invalid' in s.cmd(f'setblock 20 {GY} -34 robotica:oil'))
        return
    x, z = 20, -40
    pairs = [('robotica:titanium_ore', f'{NS}:titanium_ore'), ('theyear3001:cobalt_ore', f'{NS}:cobalt_ore'),
             ('robotica:crafting_workstation', f'{NS}:fabricator'), ('theyear3001:scrap_stairs_block', f'{NS}:scrap_stairs_block')]
    for i, (old, new) in enumerate(pairs):
        s.cmd(f'setblock {x + i} {GY} {z} {old}')
        check(f'legacy: block {old} loads as {new}', s.test(x + i, GY, z, new))
    s.cmd(f'setblock {x} {GY + 2} {z} minecraft:chest')
    olds = ['robotica:titanium_ingot', 'theyear3001:central_processing_unit', 'robotica:empty_card', 'robotica:mech_mk_1_egg',
            'theyear3001:space_ship_capsule', 'robotica:boggie_mk_1_egg', 'robotica:orichalcum_armor_chestplate']
    news = [f'{NS}:titanium_ingot', f'{NS}:central_processing_unit', f'{NS}:blank_card', f'{NS}:mech', f'{NS}:skiff', f'{NS}:drone',
            f'{NS}:orichalcum_armor_chestplate']
    for slot, old in enumerate(olds):
        s.cmd(f'item replace block {x} {GY + 2} {z} container.{slot} with {old}')
    got = s.items(x, GY + 2, z)
    check('legacy: old item ids load as their successors', all(got.get(n, 0) == 1 for n in news), got)
    for old, new in (('robotica:boggie', 'drone'), ('robotica:space_ship', 'skiff'), ('theyear3001:sentinel_mk_1', 'sentinel'),
                     ('theyear3001:mech_mk_1', 'mech')):
        s.cmd(f'summon {old} {x + 8} {GY + 20} {z} {{NoAI:1b,NoGravity:1b,Tags:["cf_legacy"]}}')
        check(f'legacy: entity {old} loads as {NS}:{new}', s.count(f'@e[type={NS}:{new},tag=cf_legacy]') == 1)
        s.cmd('kill @e[tag=cf_legacy]')
    check('legacy: removed content (oil) is not resurrected', 'Unknown' in s.cmd(f'setblock {x + 6} {GY} {z} robotica:oil')
          or not s.test(x + 6, GY, z, f'{NS}:scrap_block'))


def check_fixture(s):
    """The world was saved by Robotica 1.0.1 (tools/qa/fixtures): see make_fixture.py for what it contains."""
    expect = {(0, GY, 30): 'titanium_ore', (1, GY, 30): 'cobalt_ore', (2, GY, 30): 'orichalcum_ore', (3, GY, 30): 'scrap_block',
              (4, GY, 30): 'fabricator', (5, GY, 30): 'scrap_debris', (6, GY, 30): 'scrap_slab', (7, GY, 30): 'scrap_stairs_block'}
    for (x, y, z), block in expect.items():
        check(f'fixture: saved robotica block at {x} {y} {z} is now {NS}:{block}', s.test(x, y, z, f'{NS}:{block}'))
    got = s.items(0, GY, 32)
    want = {f'{NS}:titanium_ingot': 5, f'{NS}:scrap': 12, f'{NS}:repair_card': 1, f'{NS}:mech': 1, f'{NS}:skiff': 1,
            f'{NS}:titanium_armor_chestplate': 1}
    check('fixture: chest contents migrated', all(got.get(k, 0) == v for k, v in want.items()), got)
    for kind in ('mech', 'sentinel', 'drone', 'skiff'):
        check(f'fixture: saved robotica entity is now a {NS}:{kind}', s.count(f'@e[type={NS}:{kind},tag=cf_fixture]') >= 1)
    s.cmd('kill @e[tag=cf_fixture]')


def check_data(s):
    x, z = 40, -40
    out = s.cmd(f'loot spawn {x} {GY + 1} {z} loot {NS}:blocks/titanium_ore')
    check('data: ore loot table drops raw titanium', 'Dropped' in out and 'raw' in out.lower(), out)
    out = s.cmd(f'loot spawn {x} {GY + 1} {z} loot {NS}:entities/sentinel')
    check('data: sentinel loot table drops scrap', 'Dropped' in out and 'scrap' in out.lower(), out)
    s.cmd(f'kill @e[type=minecraft:item,x={x},y={GY},z={z},distance=..4]')
    s.cmd(f'fill {x} {GY} {z} {x + 10} {GY + 10} {z + 10} minecraft:deepslate')
    out = s.cmd(f'place feature {NS}:ore_titanium {x + 5} {GY + 5} {z + 5}')
    found = s.blocks(x, GY, z, x + 10, GY + 10, z + 10, f'{NS}:titanium_ore')
    check('data: the titanium ore feature places ore in deepslate', found > 0, (out, found))
    s.cmd(f'fill {x} {GY} {z} {x + 10} {GY + 10} {z + 10} minecraft:air')
    s.cmd(f'fill {x} {G} {z} {x + 10} {G} {z + 10} minecraft:grass_block')


def check_rift(s):
    s.cmd('time set 6000')
    x, z = 120, 0
    s.clear_area(x - 8, z - 8, x + 8, z + 8, 10)
    out = s.cmd(f'chronofall rift_at {x} {GY} {z}')
    check('rift: /chronofall rift_at opens a rift', 'opening' in out.lower(), out)
    time.sleep(8)
    landing = s.cmd('chronofall last_landing')
    m = re.search(r'at (-?\d+) (-?\d+) (-?\d+)', landing)
    check('rift: a Chrono Pod landed', m is not None, landing)
    if not m:
        return
    lx, ly, lz = (int(v) for v in m.groups())
    check('rift: it landed where the rift opened', abs(lx - x) <= 1 and abs(lz - z) <= 1, (lx, lz))
    check('rift: a Rift Cache stands at the landing', s.test(lx, ly, lz, f'{NS}:rift_cache'))
    check('rift: the impact dug a crater', s.test(x, G, z, 'minecraft:air') and s.test(x + 2, G, z, 'minecraft:air'))
    scorched = sum(s.blocks(x - 5, max(G - 4, -64), z - 5, x + 5, G + 1, z + 5, b) for b in
                   ('minecraft:blackstone', 'minecraft:magma_block', f'{NS}:rift_glass', f'{NS}:scrap_debris'))
    check('rift: the crater is lined with scorched blocks', scorched >= 10, scorched)
    logged = re.findall(r'Chrono Pod landed at .* with (\d+) Sentinels', Path(ARGS.log).read_text(errors='replace'))
    spawned = int(logged[-1]) if logged else -1
    nearby = s.count(f'@e[type={NS}:sentinel,x={lx},y={ly},z={lz},distance=..20]')
    check('rift: two or three Sentinels guard the pod', 2 <= spawned <= 3 and nearby >= spawned, (spawned, nearby))
    s.cmd(f'kill @e[type={NS}:sentinel]')
    return lx, ly, lz


def check_natural_rift(s, log):
    before = log.read_text(errors='replace').count('for player')
    s.cmd('time set 14000')
    deadline = time.time() + 75
    seen = False
    while time.time() < deadline and not seen:
        time.sleep(5)
        seen = log.read_text(errors='replace').count('for player') > before
    s.cmd('time set 6000')
    check('rift: a rift opens on its own at night near the player', seen)
    time.sleep(8)
    s.cmd(f'kill @e[type={NS}:sentinel]')


def check_charger(s):
    x, z = 160, 0
    s.clear_area(x - 2, z - 2, x + 16, z + 10)
    # Charge slot.
    s.cmd(f'setblock {x} {GY} {z} {NS}:flux_charger')
    s.cmd(f'item replace block {x} {GY} {z} container.0 with minecraft:coal 2')
    s.cmd(f'item replace block {x} {GY} {z} container.1 with {s.item_arg(NS + ":jetpack", 0)}')
    # A drone in range, wirelessly.
    s.cmd(f'setblock {x + 12} {GY} {z} {NS}:flux_charger')
    s.cmd(f'item replace block {x + 12} {GY} {z} container.0 with minecraft:coal 2')
    s.cmd(f'summon {NS}:drone {x + 12} {GY + 1} {z + 5} {{NoAI:1b,Tags:["cf_charge"]}}')
    # Comparator output.
    s.cmd(f'setblock {x} {GY} {z + 6} {NS}:flux_charger')
    s.cmd(f'item replace block {x} {GY} {z + 6} container.0 with minecraft:coal 1')
    s.cmd(f'setblock {x + 1} {GY} {z + 6} minecraft:comparator[facing=west]')
    time.sleep(4)
    m = re.search(r'Flux: (\d+)', s.cmd(f'data get block {x} {GY} {z} Items[1]'))
    check('charger: burns coal and charges the jetpack in its slot', m is not None and int(m.group(1)) > 1000, m and m.group(0))
    drone = s.entity_number('@e[tag=cf_charge,limit=1]', 'Flux')
    check('charger: charges a drone 5 blocks away (wirelessly)', drone is not None and drone > 1000, drone)
    check('charger: a comparator reads its stored Flux', s.test(x + 1, GY, z + 6, 'minecraft:comparator[powered=true]'))
    s.cmd('kill @e[tag=cf_charge]')
    s.cmd(f'setblock {x + 4} {GY} {z + 4} {NS}:flux_charger')
    s.cmd(f'item replace block {x + 4} {GY} {z + 4} container.0 with {NS}:rift_shard')
    time.sleep(3)
    burn = s.block_number(x + 4, GY, z + 4, 'BurnTotal')
    check('charger: a Rift Shard burns for 40,000 Flux', burn is not None and burn * 32 >= 39_000, burn)
    s.cmd(f'setblock {x + 8} {GY} {z + 8} {NS}:flux_charger')
    s.cmd(f'setblock {x + 8} {GY + 1} {z + 8} minecraft:hopper[facing=down]')
    s.cmd(f'item replace block {x + 8} {GY + 1} {z + 8} container.0 with minecraft:charcoal 3')
    time.sleep(3)
    fed = s.items(x + 8, GY, z + 8).get('minecraft:charcoal', 0) + (1 if (s.block_number(x + 8, GY, z + 8, 'BurnLeft') or 0) > 0 else 0)
    check('charger: a hopper feeds it fuel', fed >= 1, s.items(x + 8, GY, z + 8))


def check_fabricator_io(s):
    x, z = 200, 0
    s.clear_area(x - 2, z - 2, x + 4, z + 4)
    s.cmd(f'setblock {x} {GY + 1} {z} {NS}:fabricator')
    s.cmd(f'setblock {x} {GY} {z} minecraft:hopper[facing=down]')
    s.cmd(f'item replace block {x} {GY + 1} {z} container.0 with {NS}:flux_cell 2')
    time.sleep(2)
    check('fabricator: a hopper below takes its output', s.items(x, GY, z).get(f'{NS}:flux_cell', 0) >= 1, s.items(x, GY, z))
    s.cmd(f'setblock {x} {GY + 2} {z} minecraft:hopper[facing=down]')
    s.cmd(f'item replace block {x} {GY + 2} {z} container.0 with minecraft:apple 3')
    time.sleep(2)
    check('fabricator: a hopper above cannot push into it', s.items(x, GY + 2, z).get('minecraft:apple', 0) == 3, s.items(x, GY + 2, z))


def check_sentinel(s):
    x, z = 240, 0
    s.clear_area(x - 2, z - 2, x + 12, z + 4)
    s.cmd(f'summon {NS}:sentinel {x} {GY} {z} {{Tags:["cf_sentinel"],PersistenceRequired:1b}}')
    s.cmd(f'summon minecraft:iron_golem {x + 9} {GY} {z} {{NoAI:1b,Tags:["cf_golem"]}}')
    shots = 0
    for _ in range(80):
        time.sleep(0.08)
        shots = max(shots, s.count(f'@e[type={NS}:laser_bolt]'))
        if shots:
            break
    time.sleep(2)
    health = s.entity_number('@e[tag=cf_golem,limit=1]', 'Health')
    check('sentinel: fires laser bolts at a target', shots > 0, shots)
    check('sentinel: its bolts hurt the target', health is not None and health < 100, health)
    s.cmd('kill @e[tag=cf_sentinel]')
    s.cmd('kill @e[tag=cf_golem]')


def check_mech_slam(s):
    x, z = 280, 0
    s.clear_area(x - 6, z - 6, x + 6, z + 6, 12)
    for dx in (3, -3):
        s.cmd(f'summon minecraft:zombie {x + dx} {GY} {z} {{NoAI:1b,Tags:["cf_slam"],PersistenceRequired:1b,Health:20f}}')
    s.cmd(f'summon {NS}:mech {x} {GY + 7} {z} {{Flux:30000,Tags:["cf_mech"],Passengers:[{{id:"minecraft:armor_stand",Invisible:1b}}]}}')
    time.sleep(4)
    hurt = [s.entity_number(f'@e[tag=cf_slam,limit=1,sort=nearest,x={x + dx},y={GY},z={z}]', 'Health') for dx in (3, -3)]
    check('mech: landing from a height slams the ground and hurts mobs around it', all(h is not None and h < 20 for h in hurt), hurt)
    check('mech: takes no fall damage', (s.entity_number('@e[tag=cf_mech,limit=1]', 'Health') or 0) >= 80)
    s.cmd('kill @e[tag=cf_slam]')
    s.cmd('kill @e[type=minecraft:armor_stand]')
    s.cmd('kill @e[tag=cf_mech]')


def check_gate(s):
    a, b = (320, 0), (340, 0)
    s.clear_area(a[0] - 2, a[1] - 2, b[0] + 2, b[1] + 2)
    for (x, z), (tx, tz) in ((a, b), (b, a)):
        s.cmd(f'setblock {x} {GY} {z} {NS}:rift_gate[linked=true]{{TargetDim:"minecraft:overworld",TargetX:{tx},TargetY:{GY},TargetZ:{tz}}}')
    s.cmd(f'tp {PLAYER} {a[0]}.5 {GY}.25 {a[1]}.5')
    time.sleep(4)
    px = s.entity_number(PLAYER, 'Pos[0]')
    check('gate: standing on a linked gate warps the player to its partner', px is not None and abs(px - (b[0] + 0.5)) < 1.5, px)


# Client checks -------------------------------------------------------------------------------------------------------

def client_checks(s, shots):
    from client import Session
    c = Session(display=':1')
    s.cmd('time set 6000')
    s.cmd('difficulty peaceful')
    s.cmd('difficulty normal')
    s.cmd(f'gamemode survival {PLAYER}')
    s.cmd(f'effect give {PLAYER} minecraft:resistance 600 4 true')
    s.cmd(f'clear {PLAYER}')

    def hold_item(item, flux=None, count=1):
        s.cmd(f'clear {PLAYER}')
        s.cmd(f'item replace entity {PLAYER} weapon.mainhand with {s.item_arg(item, flux)} {count}')
        time.sleep(0.5)

    def dismount():
        # Hold sneak for a second: a short tap can fall between two client ticks on a slow software-rendered client.
        c.focus()
        c.xdo('keydown', 'shift')
        time.sleep(1.0)
        c.xdo('keyup', 'shift')
        time.sleep(1.0)

    def health():
        return s.entity_number(PLAYER, 'Health')

    def pos():
        return [s.entity_number(PLAYER, f'Pos[{i}]') for i in range(3)]

    # Jetpack ---------------------------------------------------------------------------------------------------------
    x, z = 400, 0
    s.clear_area(x - 4, z - 4, x + 4, z + 4, 20)
    s.cmd(f'item replace entity {PLAYER} armor.chest with {s.item_arg(NS + ":jetpack", 12000)}')
    c.tp(x + 0.5, GY, z + 0.5, yaw=0, pitch=-10)
    time.sleep(1.5)
    y0 = pos()[1]
    c.focus()
    c.xdo('keydown', 'space')
    time.sleep(1.2)
    if shots:
        c.shot(Path(shots) / 'jetpack.png')
    time.sleep(1.0)
    c.xdo('keyup', 'space')
    y1 = pos()[1]
    check('client: holding jump with a jetpack flies up', y1 is not None and y0 is not None and y1 - y0 > 4, (y0, y1))
    time.sleep(4)
    m = re.search(r'Flux: (\d+)', s.chest_item(PLAYER))
    check('client: the jetpack used Flux', m is not None and int(m.group(1)) < 12000, m and m.group(0))
    check('client: no fall damage after the flight', (health() or 0) >= 20, health())
    s.cmd(f'item replace entity {PLAYER} armor.chest with minecraft:air')

    # Skiff -----------------------------------------------------------------------------------------------------------
    x, z = 440, 0
    s.clear_area(x - 4, z - 30, x + 4, z + 4, 12)
    s.cmd(f'summon {NS}:skiff {x}.5 {GY} {z + 2}.5 {{Flux:30000,Tags:["cf_skiff"],Rotation:[0f,0f]}}')
    c.tp(x + 0.5, GY, z - 0.5, facing=(x + 0.5, GY + 0.5, z + 2))
    time.sleep(1)
    c.click(3)
    time.sleep(1.5)
    riding = 'cf_skiff' in s.cmd(f'data get entity {PLAYER} RootVehicle')
    check('client: right-clicking a skiff mounts it', riding)
    sx0 = s.entity_number('@e[tag=cf_skiff,limit=1]', 'Pos[2]')
    sy0 = s.entity_number('@e[tag=cf_skiff,limit=1]', 'Pos[1]')
    c.focus()
    c.xdo('keydown', 'w')
    c.xdo('keydown', 'space')
    time.sleep(2.5)
    if shots:
        c.shot(Path(shots) / 'skiff.png')
    c.xdo('keyup', 'space')
    c.xdo('keyup', 'w')
    time.sleep(0.5)
    sx1 = s.entity_number('@e[tag=cf_skiff,limit=1]', 'Pos[2]')
    sy1 = s.entity_number('@e[tag=cf_skiff,limit=1]', 'Pos[1]')
    check('client: the skiff flies forward', sx0 is not None and sx1 is not None and sx1 - sx0 > 5, (sx0, sx1))
    check('client: the skiff climbs with jump', sy0 is not None and sy1 is not None and sy1 - sy0 > 1.5, (sy0, sy1))
    flux = s.entity_number('@e[tag=cf_skiff,limit=1]', 'Flux')
    check('client: the skiff used Flux', flux is not None and flux < 30000, flux)
    # Hovering longer than the server's 4-second "floating a vehicle" limit must not kick the rider.
    time.sleep(5)
    check('client: hovering in the skiff does not get the rider kicked', PLAYER in s.cmd('list') and
          'cf_skiff' in s.cmd(f'data get entity {PLAYER} RootVehicle'))
    dismount()
    check('client: sneaking gets off the skiff', 'cf_skiff' not in s.cmd(f'data get entity {PLAYER} RootVehicle'))
    s.cmd('kill @e[tag=cf_skiff]')

    # Mech ------------------------------------------------------------------------------------------------------------
    x, z = 480, 0
    s.clear_area(x - 4, z - 4, x + 4, z + 20, 8)
    s.cmd(f'clear {PLAYER}')
    s.cmd(f'summon {NS}:mech {x}.5 {GY} {z + 2}.5 {{Flux:30000,Tags:["cf_mech"],Rotation:[0f,0f]}}')
    c.tp(x + 0.5, GY, z - 0.5, facing=(x + 0.5, GY + 1.0, z + 2.5))
    time.sleep(1)
    c.click(3)
    time.sleep(1.5)
    check('client: right-clicking a mech mounts it', 'cf_mech' in s.cmd(f'data get entity {PLAYER} RootVehicle'))
    mz0 = s.entity_number('@e[tag=cf_mech,limit=1]', 'Pos[2]')
    c.focus()
    c.xdo('keydown', 'w')
    time.sleep(2.0)
    if shots:
        c.shot(Path(shots) / 'mech.png')
    c.xdo('keyup', 'w')
    time.sleep(0.5)
    mz1 = s.entity_number('@e[tag=cf_mech,limit=1]', 'Pos[2]')
    check('client: the mech walks', mz0 is not None and mz1 is not None and mz1 - mz0 > 2, (mz0, mz1))
    flux = s.entity_number('@e[tag=cf_mech,limit=1]', 'Flux')
    check('client: the mech used Flux', flux is not None and flux < 30000, flux)
    dismount()
    check('client: sneaking gets off the mech', 'cf_mech' not in s.cmd(f'data get entity {PLAYER} RootVehicle'))
    mx, my, mz = (s.entity_number('@e[tag=cf_mech,limit=1]', f'Pos[{i}]') for i in range(3))
    hold_item(f'{NS}:bulk_upgrade_card')
    c.tp(mx, GY, mz - 2.2, facing=(mx, GY + 1.0, mz))
    time.sleep(1)
    c.click(3)
    time.sleep(1)
    upgraded = s.cmd('data get entity @e[tag=cf_mech,limit=1] Upgrades')
    check('client: a Bulk Upgrade Card upgrades the mech', 'bulk: 1' in upgraded, upgraded)
    s.cmd('kill @e[tag=cf_mech]')

    # Drone -----------------------------------------------------------------------------------------------------------
    # Deployed without Flux the drone rests on the ground, so the chip and pack clicks have a still target.
    x, z = 520, 0
    s.clear_area(x - 12, z - 12, x + 12, z + 12, 8)
    hold_item(f'{NS}:drone', 0)
    c.tp(x + 0.5, GY, z + 0.5, facing=(x + 0.5, G + 0.5, z + 2.5))
    time.sleep(1)
    c.click(3)
    time.sleep(2)
    check('client: using the drone item deploys a drone', s.count(f'@e[type={NS}:drone]') == 1)
    drone = [s.entity_number(f'@e[type={NS}:drone,limit=1]', f'Pos[{i}]') for i in range(3)]
    if None in drone:
        drone = [x + 0.5, GY, z + 2.5]  # no drone: the checks below fail instead of stopping the run
    for item in ('guard_chip', 'collector_chip', 'flux_cell'):
        hold_item(f'{NS}:{item}')
        c.tp(drone[0], GY, drone[2] - 1.6, facing=(drone[0], drone[1] + 0.3, drone[2]))
        time.sleep(1)
        c.click(3)
        time.sleep(1)
    data = s.cmd(f'data get entity @e[type={NS}:drone,limit=1]')
    check('client: chips install into the drone', 'guard_chip: 1b' in data and 'collector_chip: 1b' in data, data[:300])
    flux = s.entity_number(f'@e[type={NS}:drone,limit=1]', 'Flux')
    check('client: a Flux Cell powers the drone up', flux is not None and flux > 3000, flux)
    s.cmd(f'clear {PLAYER}')
    c.tp(x + 0.5, GY, z + 0.5, yaw=0, pitch=0)
    time.sleep(2)
    s.cmd(f'summon minecraft:zombie {x + 5} {GY} {z + 2} {{NoAI:1b,Tags:["cf_target"],PersistenceRequired:1b}}')
    s.cmd(f'summon minecraft:item {x - 6} {GY} {z - 4} {{Item:{s.stack("minecraft:diamond", 3)},PickupDelay:0}}')
    time.sleep(8)
    zhealth = s.entity_number('@e[tag=cf_target,limit=1]', 'Health')
    check('client: a guard drone shoots a hostile mob near its owner', zhealth is None or zhealth < 20, zhealth)
    inv = count_items(s.cmd(f'data get entity {PLAYER} Inventory'))
    check('client: a collector drone brings items to its owner', inv.get('minecraft:diamond', 0) == 3, inv)
    if shots and s.count(f'@e[type={NS}:drone]'):
        drone = [s.entity_number(f'@e[type={NS}:drone,limit=1]', f'Pos[{i}]') for i in range(3)]
        c.tp(drone[0] + 1.5, GY, drone[2] + 2.5, facing=tuple(drone))
        time.sleep(1.5)
        c.shot(Path(shots) / 'drone.png')
    s.cmd('kill @e[tag=cf_target]')
    s.cmd(f'clear {PLAYER}')
    s.cmd(f'data merge entity @e[type={NS}:drone,limit=1] {{Flux:0}}')
    time.sleep(3)
    drone = [s.entity_number(f'@e[type={NS}:drone,limit=1]', f'Pos[{i}]') for i in range(3)]
    if None in drone:
        drone = [x + 0.5, GY, z + 2.5]
    c.tp(drone[0], GY, drone[2] - 1.6, facing=(drone[0], drone[1] + 0.3, drone[2]))
    time.sleep(1)
    c.focus()
    c.xdo('keydown', 'shift')
    time.sleep(0.6)
    c.click(3)
    time.sleep(0.6)
    c.xdo('keyup', 'shift')
    time.sleep(0.8)
    packed = s.held_item(PLAYER)
    check('client: sneak-using an empty hand packs the drone with its chips', f'{NS}:drone' in packed and 'guard_chip' in packed
          and s.count(f'@e[type={NS}:drone]') == 0, packed[:300])

    # Fabricator ------------------------------------------------------------------------------------------------------
    x, z = 560, 0
    s.clear_area(x - 4, z - 4, x + 4, z + 4)
    s.cmd(f'setblock {x} {GY} {z + 2} {NS}:fabricator[facing=north]')
    s.cmd(f'clear {PLAYER}')
    for item, n in ((f'{NS}:cobalt_ingot', 2), ('minecraft:redstone', 4), (f'{NS}:electrical_conductors', 1)):
        s.cmd(f'give {PLAYER} {item} {n}')
    c.tp(x + 0.5, GY, z - 0.5, facing=(x + 0.5, GY + 0.5, z + 2.5))
    time.sleep(1)
    c.click(3)
    time.sleep(1.5)
    if shots:
        c.shot(Path(shots) / 'fabricator_gui.png')
    # The screen is centred: 256x222 at GUI scale (1920x1080 -> scale 4 by default, capped by the window).
    scale = 4
    left = (1920 // scale - 256) // 2
    top = (1080 // scale - 222) // 2
    c.click(1, (left + 10 + 9) * scale, (top + 20 + 9) * scale)
    time.sleep(0.5)
    c.click(1, (left + 124 + 40) * scale, (top + 96 + 9) * scale)
    time.sleep(4.5)
    if shots:
        c.shot(Path(shots) / 'fabricator_done.png')
    c.key('Escape')
    out = s.items(x, GY, z + 2)
    check('client: the Fabricator prints a Flux Cell from the ingredients', out.get(f'{NS}:flux_cell', 0) == 1, out)
    inv = count_items(s.cmd(f'data get entity {PLAYER} Inventory'))
    check('client: fabricating used the ingredients', inv.get(f'{NS}:cobalt_ingot', 0) == 0 and inv.get('minecraft:redstone', 0) == 0, inv)

    # Plasma Cutter and Pulse Rifle -----------------------------------------------------------------------------------
    x, z = 600, 0
    s.clear_area(x - 4, z - 4, x + 4, z + 12)
    s.cmd(f'setblock {x} {GY + 1} {z + 7} minecraft:stone')
    hold_item(f'{NS}:plasma_cutter', 6000)
    c.tp(x + 0.5, GY, z + 0.5, facing=(x + 0.5, GY + 1.5, z + 7))
    time.sleep(1)
    c.hold(3, 2.5)
    check('client: the Plasma Cutter cuts a block 7 blocks away', s.test(x, GY + 1, z + 7, 'minecraft:air'))
    dropped = s.count(f'@e[type=minecraft:item,x={x},y={GY + 1},z={z + 7},distance=..3]')
    check('client: the cut block drops', dropped >= 1, dropped)
    s.cmd(f'summon minecraft:zombie {x} {GY} {z + 8} {{NoAI:1b,Tags:["cf_target"],PersistenceRequired:1b}}')
    hold_item(f'{NS}:pulse_rifle', 6000)
    c.tp(x + 0.5, GY, z + 0.5, facing=(x + 0.5, GY + 1.2, z + 8.5))
    time.sleep(1)
    for _ in range(3):
        c.click(3)
        time.sleep(0.7)
    time.sleep(1)
    zhealth = s.entity_number('@e[tag=cf_target,limit=1]', 'Health')
    check('client: the Pulse Rifle hurts its target', zhealth is None or zhealth < 20, zhealth)
    s.cmd('kill @e[tag=cf_target]')

    # Rift Tuner ------------------------------------------------------------------------------------------------------
    x, z = 640, 0
    s.clear_area(x - 4, z - 4, x + 8, z + 4)
    s.cmd(f'setblock {x} {GY} {z + 2} {NS}:rift_gate')
    s.cmd(f'setblock {x + 4} {GY} {z + 2} {NS}:rift_gate')
    hold_item(f'{NS}:rift_tuner')
    time.sleep(1)  # the gates' chunk reaches the client
    linked = (None, None)
    for _ in range(2):  # a second try if a click went out before the client had the gate
        for gx in (x, x + 4):
            c.tp(gx + 0.5, GY, z - 0.5, facing=(gx + 0.5, GY + 0.2, z + 2.5))
            time.sleep(1.5)
            c.click(3)
            time.sleep(0.8)
        linked = (s.block_number(x, GY, z + 2, 'TargetX'), s.block_number(x + 4, GY, z + 2, 'TargetX'))
        if linked == (x + 4, x):
            break
        hold_item(f'{NS}:rift_tuner')
    check('client: the Rift Tuner links two gates both ways', linked == (x + 4, x), linked)

    # Rift Cache and Rift Beacon ----------------------------------------------------------------------------------------
    x, z = 680, 0
    s.clear_area(x - 4, z - 4, x + 4, z + 4)
    s.cmd(f'setblock {x} {GY} {z + 2} {NS}:rift_cache')
    s.cmd(f'clear {PLAYER}')
    c.tp(x + 0.5, GY, z - 0.5, facing=(x + 0.5, GY + 0.5, z + 2.5))
    time.sleep(1)
    c.click(3)
    time.sleep(1.5)
    check('client: right-clicking a Rift Cache cracks it open', s.test(x, GY, z + 2, 'minecraft:air'))
    shards = s.count(f'@e[type=minecraft:item,nbt={{Item:{{id:"{NS}:rift_shard"}}}},x={x},y={GY},z={z + 2},distance=..4]')
    check('client: the cache drops Rift Shards', shards >= 1, shards)
    s.cmd(f'kill @e[type=minecraft:item]')
    s.cmd(f'setblock {x} {GY} {z + 2} {NS}:rift_beacon')
    hold_item(f'{NS}:rift_shard', None, 4)
    before = Path(ARGS.log).read_text(errors='replace').count('Chrono Rift opening')
    c.tp(x + 0.5, GY, z - 0.5, facing=(x + 0.5, GY + 0.5, z + 2.5))
    time.sleep(1)
    c.click(3)
    time.sleep(5)
    after = Path(ARGS.log).read_text(errors='replace').count('Chrono Rift opening')
    check('client: 4 Rift Shards on a Rift Beacon call a rift', after > before, (before, after))
    inv = count_items(s.cmd(f'data get entity {PLAYER} Inventory'))
    check('client: the beacon took the shards', inv.get(f'{NS}:rift_shard', 0) == 0, inv)
    time.sleep(6)
    s.cmd(f'kill @e[type={NS}:sentinel]')

    # Assets --------------------------------------------------------------------------------------------------------
    client_log = Path(ARGS.log).with_name(f'{ARGS.loader}-client.log').read_text(errors='replace')
    missing = [line for line in client_log.splitlines() if NS in line and re.search(r'Missing|Unable to|Failed to|Couldn\'t|not found', line)]
    check('client: every Chronofall model, texture, sound and particle loaded', not missing, missing[:5])


def check_log():
    text = Path(ARGS.log).read_text(errors='replace')
    # Logged for every modded entity type by vanilla's data fixer lookup; and by vanilla's ore feature when the QA's
    # /place feature runs it outside world generation.
    allowed = ('No data fixer registered for chronofall:', 'Unprimed heightmap: OCEAN_FLOOR_WG')
    ours = [line for line in text.splitlines() if NS in line.lower() and ('ERROR' in line or 'Exception' in line)
            and not any(a in line for a in allowed)]
    check('log: no errors about the mod', not ours, ours[:5])
    errors = [line for line in text.splitlines() if '/ERROR]' in line and not any(a in line for a in allowed)]
    check('log: no errors at all', not errors, errors[:5])


def main():
    s = Server(ARGS.port, ARGS.ws == '1.20.1')
    s.cmd('gamerule doMobSpawning false')
    s.cmd('gamerule doDaylightCycle false')
    s.cmd('gamerule doWeatherCycle false')
    s.cmd('time set 6000')
    s.cmd('difficulty normal')
    # The tests run along a strip far from spawn; keep it loaded and ticking without a player nearby.
    for x0, x1 in ((-48, 336), (336, 720)):
        s.cmd(f'forceload add {x0} -48 {x1} 48')
    check_config()
    check_registry(s)
    check_legacy(s)
    if ARGS.fixture:
        check_fixture(s)
    check_data(s)
    check_rift(s)
    check_charger(s)
    check_fabricator_io(s)
    check_sentinel(s)
    check_mech_slam(s)
    if ARGS.client:
        s.cmd(f'op {PLAYER}')
        check_gate(s)
        check_natural_rift(s, Path(ARGS.log))
        client_checks(s, ARGS.shots)
    check_log()
    print(f'{COUNT[0]} checks, {len(FAILURES)} failures' + (': ' + '; '.join(FAILURES) if FAILURES else ''))
    sys.exit(1 if FAILURES else 0)


if __name__ == '__main__':
    ap = argparse.ArgumentParser()
    ap.add_argument('--log', required=True)
    ap.add_argument('--ws', required=True)
    ap.add_argument('--loader', required=True)
    ap.add_argument('--port', type=int, default=25575)
    ap.add_argument('--client', action='store_true')
    ap.add_argument('--fixture', action='store_true')
    ap.add_argument('--shots')
    ap.add_argument('--server-dir', help='server directory (default: the dev run of --ws/--loader), for release jars on real servers')
    ARGS = ap.parse_args()
    main()
