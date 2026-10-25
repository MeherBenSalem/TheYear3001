#!/usr/bin/env python3
"""Builds a legacy-world fixture: run against a dev server of the OLD mod (Robotica 1.0.1, mod id `robotica`, built
from the repository's history) on the QA flat world. It places old blocks, a chest of old items and old robots with
the old ids, saves the world and zips it. chronofall_checks.py --fixture then checks that Chronofall loads them.

  # in a checkout of the 1.0.1 sources (git archive fc4e6c1 <ws>), with the QA server.properties:
  python3 tools/qa/make_fixture.py --world <server dir>/qa --out tools/qa/fixtures/legacy-1.21.1.zip [--legacy-nbt]
"""
from __future__ import annotations

import argparse
import sys
import time
import zipfile
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
from rcon import wait_for  # noqa: E402

GY = -60
BLOCKS = ['titanium_ore', 'cobalt_ore', 'orichalcum_ore', 'scrap_block', 'crafting_workstation', 'scrap_debris', 'scrap_slab',
          'scrap_stairs_block']
CHEST = [('titanium_ingot', 5), ('scrap', 12), ('repair_card', 1), ('mech_mk_1_egg', 1), ('space_ship_capsule', 1),
         ('titanium_armor_chestplate', 1)]
ENTITIES = ['mech', 'sentinel', 'boggie', 'space_ship']


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument('--world', required=True)
    ap.add_argument('--out', required=True)
    ap.add_argument('--legacy-nbt', action='store_true', help='1.20.1 item NBT (Count:1b)')
    ap.add_argument('--port', type=int, default=25575)
    args = ap.parse_args()
    r = wait_for(args.port)
    for i, block in enumerate(BLOCKS):
        print(r.command(f'setblock {i} {GY} 30 robotica:{block}'))
    print(r.command(f'setblock 0 {GY} 32 minecraft:chest'))
    for slot, (item, count) in enumerate(CHEST):
        print(r.command(f'item replace block 0 {GY} 32 container.{slot} with robotica:{item} {count}'))
    for i, entity in enumerate(ENTITIES):
        print(r.command(f'summon robotica:{entity} {10 + 3 * i} {GY} 30 {{NoAI:1b,PersistenceRequired:1b,Tags:["cf_fixture"]}}'))
    print(r.command('save-all flush'))
    time.sleep(5)
    print(r.command('stop'))
    time.sleep(10)
    world = Path(args.world)
    out = Path(args.out)
    out.parent.mkdir(parents=True, exist_ok=True)
    with zipfile.ZipFile(out, 'w', zipfile.ZIP_DEFLATED) as z:
        for f in sorted(world.rglob('*')):
            if f.is_file() and f.name != 'session.lock':
                z.write(f, f.relative_to(world))
    print('wrote', out, out.stat().st_size, 'bytes')


if __name__ == '__main__':
    main()
