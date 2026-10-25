#!/usr/bin/env python3
"""Writes Chronofall's assets and data into every workspace, each in the format of its Minecraft version. Edit the
tables here, not the generated JSON. Textures and sounds come from tools/assets (see gen_textures.py, gen_sounds.py
and gen_models.py, which writes the entity textures straight into the workspaces).

  python3 tools/gen_textures.py && python3 tools/gen_sounds.py && python3 tools/gen_models.py && python3 tools/gen_assets.py

Format differences handled here (checked against each version's vanilla data):
  1.20.1   plural folders (loot_tables, recipes, advancements, tags/blocks, tags/items), {"item"/"tag"} ingredients,
           {"item": id} results, forge:add_features biome modifiers, armour layers under textures/models/armor
  1.21.1   singular folders, {"id": id} results, neoforge:add_features biome modifiers
  26.x     plain-string ingredients, item model definitions (assets/<ns>/items), equipment assets (armour textures under
           textures/entity/equipment), min_format/max_format pack metadata
  26.3     loot "condition"/"modifier" objects with "type", worldgen/feature with a flat ore config and string states,
           recipe_unlocked "recipes"
"""
from __future__ import annotations

import json
import shutil
from pathlib import Path

REPO = Path(__file__).resolve().parents[1]
TOOLS = Path(__file__).resolve().parent
NS = 'chronofall'
VERSIONS = ['1.20.1', '1.21.1', '26.1.2', '26.2', '26.3']
PACK = {
    '1.20.1': {'pack_format': 15},
    '1.21.1': {'pack_format': 34},
    '26.1.2': {'min_format': 84, 'max_format': 101},
    '26.2': {'min_format': 88, 'max_format': 121},
    '26.3': {'min_format': 88, 'max_format': 121},
}
METALS = ['scrap', 'titanium', 'cobalt', 'orichalcum']
MATERIAL = {'scrap': f'{NS}:scrap', 'titanium': f'{NS}:titanium_ingot', 'cobalt': f'{NS}:cobalt_ingot', 'orichalcum': f'{NS}:orichalcum_ingot'}
TOOLS_LIST = ['sword', 'pickaxe', 'axe', 'shovel', 'hoe']
ARMOR = ['helmet', 'chestplate', 'leggings', 'boots']
DIRS = {'north': 'North', 'north_east': 'North-East', 'east': 'East', 'south_east': 'South-East', 'south': 'South',
        'south_west': 'South-West', 'west': 'West', 'north_west': 'North-West', 'here': 'right here'}

# Names ---------------------------------------------------------------------------------------------------------------

NAMES = {
    'scrap_block': 'Block of Scrap', 'scrap_slab': 'Scrap Slab', 'scrap_stairs_block': 'Scrap Stairs', 'scrap_debris': 'Scrap Debris',
    'titanium_ore': 'Titanium Ore', 'cobalt_ore': 'Cobalt Ore', 'orichalcum_ore': 'Orichalcum Ore', 'titanium_block': 'Block of Titanium',
    'cobalt_block': 'Block of Cobalt', 'orichalcum_block': 'Block of Orichalcum', 'rift_glass': 'Rift Glass', 'fabricator': 'Fabricator',
    'flux_charger': 'Flux Charger', 'rift_gate': 'Rift Gate', 'rift_cache': 'Rift Cache', 'rift_beacon': 'Rift Beacon',
    'scrap': 'Scrap', 'rift_shard': 'Rift Shard', 'raw_titanium': 'Raw Titanium', 'titanium_ingot': 'Titanium Ingot', 'raw_cobalt': 'Raw Cobalt',
    'cobalt_ingot': 'Cobalt Ingot', 'raw_orichalcum': 'Raw Orichalcum', 'orichalcum_ingot': 'Orichalcum Ingot',
    'electrical_conductors': 'Copper Wiring', 'blank_printed_circuit_board': 'Blank Circuit Board', 'printed_circuit_board': 'Circuit Board',
    'central_processing_unit': 'Processor', 'flux_cell': 'Flux Cell', 'blank_card': 'Blank Card', 'repair_card': 'Repair Card',
    'bulk_upgrade_card': 'Bulk Upgrade Card', 'damage_upgrade_card': 'Damage Upgrade Card', 'defense_upgrade_card': 'Defense Upgrade Card',
    'speed_upgrade_card': 'Speed Upgrade Card', 'collector_chip': 'Collector Chip', 'guard_chip': 'Guard Chip', 'lamp_chip': 'Lamp Chip',
    'jetpack': 'Jetpack', 'plasma_cutter': 'Plasma Cutter', 'pulse_rifle': 'Pulse Rifle', 'rift_tuner': 'Rift Tuner', 'drone': 'Drone',
    'mech': 'Mech', 'skiff': 'Hover Skiff', 'sentinel_spawn_egg': 'Sentinel Spawn Egg',
}
for _m in METALS:
    for _t in TOOLS_LIST:
        NAMES[f'{_m}_{_t}'] = f'{_m.capitalize()} {_t.capitalize()}'
    for _p in ARMOR:
        NAMES[f'{_m}_armor_{_p}'] = f'{_m.capitalize()} {_p.capitalize()}'
BLOCKS = ['scrap_block', 'scrap_slab', 'scrap_stairs_block', 'scrap_debris', 'titanium_ore', 'cobalt_ore', 'orichalcum_ore', 'titanium_block',
          'cobalt_block', 'orichalcum_block', 'rift_glass', 'fabricator', 'flux_charger', 'rift_gate', 'rift_cache', 'rift_beacon']

DESCRIPTIONS = {
    'rift_shard': 'Crystallised time from a Chrono Rift. Burns in a Flux Charger for 40,000 Flux.',
    'flux_cell': 'Use on a drone, mech or skiff to add 4,000 Flux.',
    'repair_card': 'Use on a Mech: repairs 20 health.',
    'bulk_upgrade_card': 'Use on a Mech: +10 max health (up to 5).',
    'damage_upgrade_card': 'Use on a Mech: +2 damage for its rider (up to 5).',
    'defense_upgrade_card': 'Use on a Mech: +4 armour (up to 5).',
    'speed_upgrade_card': 'Use on a Mech: +10% speed (up to 3).',
    'collector_chip': 'Install in a Drone: it brings items lying nearby to you.',
    'guard_chip': 'Install in a Drone: it shoots hostile mobs near you.',
    'lamp_chip': 'Install in a Drone: it lights the area around it.',
    'jetpack': 'Wear it and hold jump to fly. No fall damage while charged.',
    'plasma_cutter': 'Hold use to cut the block you look at, up to 12 blocks away.',
    'pulse_rifle': 'Fires laser bolts (7 damage).',
    'rift_tuner': 'Right-click two Rift Gates to link them.',
    'drone': 'Use on a block to deploy. Sneak-use an empty hand on it to pack it up.',
    'mech': 'Use on a block to deploy, right-click to ride. Hold jump to leap; land to slam.',
    'skiff': 'Use on a block to deploy. W/S speed, look to steer, jump to climb, sprint to descend.',
}

LANG = {
    'itemGroup.chronofall': 'Chronofall',
    'entity.chronofall.sentinel': 'Sentinel', 'entity.chronofall.drone': 'Drone', 'entity.chronofall.mech': 'Mech',
    'entity.chronofall.skiff': 'Hover Skiff', 'entity.chronofall.chrono_pod': 'Chrono Pod', 'entity.chronofall.laser_bolt': 'Laser Bolt',
    'tooltip.chronofall.flux': 'Flux: %s',
    'tooltip.chronofall.tuner.stored': 'First gate: %s %s %s',
    'tooltip.chronofall.upgrade.bulk': 'Bulk upgrades: %s', 'tooltip.chronofall.upgrade.damage': 'Damage upgrades: %s',
    'tooltip.chronofall.upgrade.defense': 'Defense upgrades: %s', 'tooltip.chronofall.upgrade.speed': 'Speed upgrades: %s',
    'message.chronofall.tuner.stored': 'Gate at %s %s %s stored. Now use the tuner on the second gate.',
    'message.chronofall.tuner.same': 'That is the same gate. Tuner cleared.',
    'message.chronofall.tuner.no_cross_dimension': 'Rift Gates cannot be linked across dimensions on this server.',
    'message.chronofall.tuner.missing': 'The first gate is gone. Tuner cleared.',
    'message.chronofall.tuner.linked': 'Linked with the gate at %s %s %s.',
    'message.chronofall.robot.no_room': 'Not enough room here.',
    'message.chronofall.drone.has_chip': 'This drone already has a %s.',
    'message.chronofall.drone.chip': '%s installed.',
    'message.chronofall.drone.status': 'Drone: %s%% Flux · %s',
    'message.chronofall.drone.no_chips': 'no chips',
    'message.chronofall.flux': 'Flux: %s%%',
    'message.chronofall.no_flux': 'No Flux: charge it at a Flux Charger or use a Flux Cell on it.',
    'message.chronofall.mech.healthy': 'The mech is not damaged.',
    'message.chronofall.mech.repaired': 'Mech repaired: %s / %s health.',
    'message.chronofall.mech.capped': '%s: this mech already has the maximum of %s.',
    'message.chronofall.mech.upgraded': '%s installed (%s / %s).',
    'message.chronofall.fabricator.missing': 'You do not have the ingredients.',
    'message.chronofall.gate.unlinked': 'Unlinked. Use a Rift Tuner on this gate and another one.',
    'message.chronofall.gate.linked': 'Linked to %s %s %s (%s).',
    'message.chronofall.gate.lost': 'The linked gate is gone.',
    'message.chronofall.beacon.need': 'Use %s Rift Shards on the beacon to call a Chrono Rift.',
    'message.chronofall.beacon.started': 'The beacon is calling a rift...',
    'message.chronofall.rift.opening': 'A Chrono Rift is tearing open to the %s!',
    'commands.chronofall.rift': 'Chrono Rift opening above %s %s %s',
    'commands.chronofall.no_landing': 'No Chrono Pod has landed since the server started.',
    'commands.chronofall.last_landing': 'Last Chrono Pod landed at %s %s %s',
    'gui.chronofall.fabricator.fabricate': 'Fabricate', 'gui.chronofall.fabricator.printing': 'Printing...',
    'gui.chronofall.fabricator.full': 'Output full',
    'hud.chronofall.skiff': 'SKIFF', 'hud.chronofall.mech': 'MECH', 'hud.chronofall.jetpack': 'JETPACK',
    'hud.chronofall.skiff_stats': '%s m/s · Y %s', 'hud.chronofall.mech_stats': 'Hull %s / %s',
}
for _k, _v in DIRS.items():
    LANG[f'direction.chronofall.{_k}'] = _v.lower() if _k == 'here' else _v.lower().replace('-', '-')
for _path, _name in NAMES.items():
    LANG[f'item.{NS}.{_path}'] = _name
    if _path in BLOCKS:
        LANG[f'block.{NS}.{_path}'] = _name
for _path, _desc in DESCRIPTIONS.items():
    LANG[f'item.{NS}.{_path}.desc'] = _desc

SOUNDS = {
    'rift_open': ('Chrono Rift opens', 160), 'pod_fall': ('Chrono Pod falls', 160), 'pod_impact': ('Chrono Pod crashes', 160),
    'cache_open': ('Rift Cache cracks open', 16), 'laser_shot': ('Laser fires', 24), 'laser_hit': ('Laser hits', 16),
    'jetpack_loop': ('Jetpack thrusts', 24), 'skiff_loop': ('Skiff engine hums', 24), 'cutter_loop': ('Plasma Cutter cuts', 16),
    'mech_step': ('Mech stomps', 20), 'mech_slam': ('Mech slams the ground', 32), 'gate_charge': ('Rift Gate charges', 16),
    'gate_warp': ('Rift Gate warps', 24), 'fabricator_print': ('Fabricator prints', 12), 'fabricator_done': ('Fabricator finishes', 16),
    'charger_hum': ('Flux Charger hums', 10), 'drone_beep': ('Drone beeps', 12), 'sentinel_ambient': ('Sentinel buzzes', 16),
    'sentinel_hurt': ('Sentinel hurts', 16), 'sentinel_death': ('Sentinel shuts down', 16), 'robot_hurt': ('Robot hurts', 16),
}
for _s, (_sub, _) in SOUNDS.items():
    LANG[f'subtitles.{NS}.{_s}'] = _sub

# Recipes -------------------------------------------------------------------------------------------------------------

C = f'{NS}:'
SHAPED = {
    # id: (pattern, key, result, count, unlocked by)
    'scrap_block': (['###', '###', '###'], {'#': C + 'scrap'}, C + 'scrap_block', 1, C + 'scrap'),
    'scrap_slab': (['###'], {'#': C + 'scrap_block'}, C + 'scrap_slab', 6, C + 'scrap_block'),
    'scrap_stairs_block': (['#  ', '## ', '###'], {'#': C + 'scrap_block'}, C + 'scrap_stairs_block', 4, C + 'scrap_block'),
    'fabricator': (['tct', 'rbr', 'sss'], {'t': C + 'titanium_ingot', 'c': 'minecraft:crafting_table', 'r': 'minecraft:redstone',
                                         'b': C + 'scrap_block', 's': C + 'scrap'}, C + 'fabricator', 1, C + 'titanium_ingot'),
    'flux_charger': (['tct', 'cfc', 'tbt'], {'t': C + 'titanium_ingot', 'c': C + 'cobalt_ingot', 'f': 'minecraft:furnace',
                                           'b': 'minecraft:redstone_block'}, C + 'flux_charger', 1, C + 'cobalt_ingot'),
    'rift_glass': (['ggg', 'gsg', 'ggg'], {'g': 'minecraft:black_stained_glass', 's': C + 'rift_shard'}, C + 'rift_glass', 8, C + 'rift_shard'),
}
for _m in ('titanium', 'cobalt', 'orichalcum'):
    SHAPED[f'{_m}_block'] = (['###', '###', '###'], {'#': MATERIAL[_m]}, C + f'{_m}_block', 1, MATERIAL[_m])
for _m in METALS:
    _x = MATERIAL[_m]
    for _t, _p in (('sword', ['X', 'X', '#']), ('pickaxe', ['XXX', ' # ', ' # ']), ('axe', ['XX', 'X#', ' #']), ('shovel', ['X', '#', '#']),
                   ('hoe', ['XX', ' #', ' #'])):
        SHAPED[f'{_m}_{_t}'] = (_p, {'X': _x, '#': 'minecraft:stick'}, C + f'{_m}_{_t}', 1, _x)
    for _a, _p in (('helmet', ['XXX', 'X X']), ('chestplate', ['X X', 'XXX', 'XXX']), ('leggings', ['XXX', 'X X', 'X X']), ('boots', ['X X', 'X X'])):
        SHAPED[f'{_m}_armor_{_a}'] = (_p, {'X': _x}, C + f'{_m}_armor_{_a}', 1, _x)
SHAPELESS = {
    'scrap_from_block': ([C + 'scrap_block'], C + 'scrap', 9, C + 'scrap_block'),
    'electrical_conductors': (['minecraft:copper_ingot', 'minecraft:copper_ingot', 'minecraft:redstone'], C + 'electrical_conductors', 3,
                              'minecraft:copper_ingot'),
    'blank_printed_circuit_board': (['minecraft:iron_ingot', 'minecraft:green_dye', C + 'scrap'], C + 'blank_printed_circuit_board', 2, C + 'scrap'),
    'blank_card': (['minecraft:paper', 'minecraft:paper', 'minecraft:iron_nugget', 'minecraft:redstone'], C + 'blank_card', 2, C + 'scrap'),
}
for _m in ('titanium', 'cobalt', 'orichalcum'):
    SHAPELESS[f'{_m}_ingot_from_block'] = ([C + f'{_m}_block'], MATERIAL[_m], 9, C + f'{_m}_block')
SMELTING = {
    # id: (input, result, xp)
    'titanium_ingot_from_raw': (C + 'raw_titanium', C + 'titanium_ingot', 0.8),
    'cobalt_ingot_from_raw': (C + 'raw_cobalt', C + 'cobalt_ingot', 0.9),
    'orichalcum_ingot_from_raw': (C + 'raw_orichalcum', C + 'orichalcum_ingot', 1.0),
    'titanium_ingot_from_ore': (C + 'titanium_ore', C + 'titanium_ingot', 0.8),
    'cobalt_ingot_from_ore': (C + 'cobalt_ore', C + 'cobalt_ingot', 0.9),
    'orichalcum_ingot_from_ore': (C + 'orichalcum_ore', C + 'orichalcum_ingot', 1.0),
    'scrap_from_debris': (C + 'scrap_debris', C + 'scrap', 0.5),
}

ORES = {
    # placed feature id: (block, vein size, veins per chunk, min y, max y)
    'ore_titanium': ('titanium_ore', 8, 9, -64, 16),
    'ore_cobalt': ('cobalt_ore', 6, 5, -64, -16),
    'ore_orichalcum': ('orichalcum_ore', 4, 3, -64, -40),
    'ore_scrap_debris': ('scrap_debris', 3, 4, -64, 0),
}

# Helpers -------------------------------------------------------------------------------------------------------------


def modern(v):
    return v.startswith('26.')


def write(path: Path, data):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, indent=2) + '\n')


def folder(v, kind):
    plural = {'loot_table': 'loot_tables', 'recipe': 'recipes', 'advancement': 'advancements', 'tags/block': 'tags/blocks',
              'tags/item': 'tags/items'}
    return plural[kind] if v == '1.20.1' else kind


def ingredient(v, value):
    if modern(v):
        return value
    return {'tag': value[1:]} if value.startswith('#') else {'item': value}


def result(v, item, count):
    if v == '1.20.1':
        return {'item': item, 'count': count}
    return {'id': item, 'count': count}


def unlock(v, recipe_id, item):
    if v == '1.20.1':
        predicate = {'items': [item]}
    else:
        predicate = {'items': item}
    return {
        'parent': 'minecraft:recipes/root',
        'criteria': {
            'has_item': {'trigger': 'minecraft:inventory_changed', 'conditions': {'items': [predicate]}},
            'has_the_recipe': {'trigger': 'minecraft:recipe_unlocked', 'conditions': {('recipes' if v == '26.3' else 'recipe'): recipe_id}},
        },
        'requirements': [['has_item', 'has_the_recipe']],
        'rewards': {'recipes': [recipe_id]},
    }


# Loot (26.3 writes conditions and functions as "condition"/"modifier" objects with "type").

def cond(v, kind, **fields):
    if v == '26.3':
        return {'type': f'minecraft:{kind}', **fields}
    return {'condition': f'minecraft:{kind}', **fields}


def conds(v, *items):
    if v == '26.3':
        return {'condition': items[0] if len(items) == 1 else {'type': 'minecraft:all_of', 'terms': list(items)}}
    return {'conditions': list(items)}


def func(v, kind, **fields):
    if v == '26.3':
        return {'type': f'minecraft:{kind}', **fields}
    return {'function': f'minecraft:{kind}', **fields}


def funcs(v, *items):
    return {'modifier': list(items)} if v == '26.3' else {'functions': list(items)}


def silk(v):
    if v == '26.3':
        return {'condition': 'minecraft:tool/can_silk_touch'}
    return {'conditions': [{'condition': 'minecraft:match_tool', 'predicate': (
        {'enchantments': [{'enchantment': 'minecraft:silk_touch', 'levels': {'min': 1}}]} if v == '1.20.1'
        else {'predicates': {'minecraft:enchantments': [{'enchantments': 'minecraft:silk_touch', 'levels': {'min': 1}}]}})}]}


def pool(v, entries, conditions=None):
    p = {'rolls': 1 if v == '26.3' else 1.0, 'entries': entries}
    if conditions:
        p.update(conditions)
    return p


def table(v, kind, path, pools):
    return {'type': f'minecraft:{kind}', 'random_sequence': f'{NS}:{path}', 'pools': pools}


def drop_self(v, block):
    return table(v, 'block', f'blocks/{block}', [pool(v, [{'type': 'minecraft:item', 'name': f'{NS}:{block}'}],
                                                      conds(v, cond(v, 'survives_explosion')))])


def drop_ore(v, block, raw, extra_pool=None):
    pools = [pool(v, [{'type': 'minecraft:alternatives', 'children': [
        {'type': 'minecraft:item', 'name': f'{NS}:{block}', **silk(v)},
        {'type': 'minecraft:item', 'name': raw, **funcs(v, func(v, 'apply_bonus', enchantment='minecraft:fortune', formula='minecraft:ore_drops'),
                                                         func(v, 'explosion_decay'))}]}])]
    if extra_pool:
        pools.append(extra_pool)
    return table(v, 'block', f'blocks/{block}', pools)


def drop_slab(v, block):
    if v == '26.3':
        double = {'type': 'minecraft:set_count', 'count': 2,
                  'condition': {'type': 'minecraft:match_block', 'blocks': f'{NS}:{block}', 'state': {'type': 'double'}}}
    else:
        double = {'function': 'minecraft:set_count', 'count': 2.0, 'add': False,
                  'conditions': [{'condition': 'minecraft:block_state_property', 'block': f'{NS}:{block}', 'properties': {'type': 'double'}}]}
    entry = {'type': 'minecraft:item', 'name': f'{NS}:{block}', **funcs(v, double, func(v, 'explosion_decay'))}
    return table(v, 'block', f'blocks/{block}', [pool(v, [entry])])


def sentinel_loot(v):
    scrap = {'type': 'minecraft:item', 'name': f'{NS}:scrap',
             **funcs(v, func(v, 'set_count', count={'type': 'minecraft:uniform', 'min': 1, 'max': 3}))}
    shard = {'type': 'minecraft:item', 'name': f'{NS}:rift_shard'}
    return table(v, 'entity', 'entities/sentinel', [
        pool(v, [scrap]),
        pool(v, [shard], conds(v, cond(v, 'killed_by_player'), cond(v, 'random_chance', chance=0.25))),
    ])


# Models --------------------------------------------------------------------------------------------------------------

def cube_all(texture):
    return {'parent': 'minecraft:block/cube_all', 'textures': {'all': texture}}


GATE_MODEL = {
    'parent': 'minecraft:block/block',
    'textures': {'top': f'{NS}:block/rift_gate_top', 'side': f'{NS}:block/rift_gate_side', 'particle': f'{NS}:block/rift_gate_side'},
    'elements': [{'from': [0, 0, 0], 'to': [16, 4, 16], 'faces': {
        'up': {'uv': [0, 0, 16, 16], 'texture': '#top'},
        'down': {'uv': [0, 0, 16, 16], 'texture': '#side', 'cullface': 'down'},
        **{f: {'uv': [0, 12, 16, 16], 'texture': '#side', 'cullface': f} for f in ('north', 'south', 'east', 'west')}}}],
}
CACHE_MODEL = {
    'parent': 'minecraft:block/block',
    'textures': {'top': f'{NS}:block/rift_cache_top', 'side': f'{NS}:block/rift_cache_side', 'particle': f'{NS}:block/rift_cache_side'},
    'elements': [{'from': [1, 0, 1], 'to': [15, 13, 15], 'faces': {
        'up': {'uv': [1, 1, 15, 15], 'texture': '#top'},
        'down': {'uv': [1, 1, 15, 15], 'texture': '#side', 'cullface': 'down'},
        **{f: {'uv': [1, 3, 15, 16], 'texture': '#side'} for f in ('north', 'south', 'east', 'west')}}}],
}


def stairs_states(model):
    variants = {}
    for facing, y in (('east', 0), ('south', 90), ('west', 180), ('north', 270)):
        for half in ('bottom', 'top'):
            for shape in ('straight', 'inner_left', 'inner_right', 'outer_left', 'outer_right'):
                m = model + ('' if shape == 'straight' else '_inner' if 'inner' in shape else '_outer')
                rot = y
                if shape in ('inner_left', 'outer_left'):
                    rot = (y + 270) % 360
                if half == 'top':
                    if shape in ('inner_right', 'outer_right'):
                        rot = (y + 90) % 360
                    elif shape in ('inner_left', 'outer_left'):
                        rot = y
                v = {'model': m}
                if half == 'top':
                    v['x'] = 180
                if rot:
                    v['y'] = rot
                if half == 'top' or rot:
                    v['uvlock'] = True
                variants[f'facing={facing},half={half},shape={shape}'] = v
    return {'variants': variants}


def facing_variants(model_for, extra=None):
    variants = {}
    for facing, y in (('north', 0), ('east', 90), ('south', 180), ('west', 270)):
        for suffix, model in (extra or {'': model_for}).items():
            key = f'facing={facing}' + suffix
            variants[key] = {'model': model, **({'y': y} if y else {})}
    return {'variants': variants}


HANDHELD = {f'{m}_{t}' for m in METALS for t in TOOLS_LIST} | {'plasma_cutter', 'pulse_rifle'}
ITEM_TEXTURE = {}


def generate(v):
    res = REPO / v / 'common' / 'src' / 'main' / 'resources'
    assets = res / 'assets' / NS
    data = res / 'data'
    keep_entity = assets / 'textures' / 'entity'
    entity_backup = None
    if keep_entity.exists():
        entity_backup = REPO / v / 'build' / 'entity-textures'
        if entity_backup.exists():
            shutil.rmtree(entity_backup)
        shutil.copytree(keep_entity, entity_backup)
    for stale in (assets, data):
        if stale.exists():
            shutil.rmtree(stale)
    write(res / 'pack.mcmeta', {'pack': {'description': 'Chronofall resources', **PACK[v]}})

    # Assets
    write(assets / 'lang' / 'en_us.json', dict(sorted(LANG.items())))
    write(assets / 'sounds.json', {name: {'subtitle': f'subtitles.{NS}.{name}', 'sounds': [
        {'name': f'{NS}:{name}', 'attenuation_distance': distance}]} for name, (_, distance) in SOUNDS.items()})
    shutil.copytree(TOOLS / 'assets' / 'sounds', assets / 'sounds')
    shutil.copytree(TOOLS / 'assets' / 'textures', assets / 'textures')
    if entity_backup:
        shutil.copytree(entity_backup, keep_entity, dirs_exist_ok=True)
    if modern(v):
        armor = assets / 'textures' / 'models' / 'armor'
        for metal in METALS + ['jetpack']:
            for layer, kind in (('1', 'humanoid'), ('2', 'humanoid_leggings')):
                src = armor / f'{metal}_layer_{layer}.png'
                if src.exists():
                    dst = assets / 'textures' / 'entity' / 'equipment' / kind / f'{metal}.png'
                    dst.parent.mkdir(parents=True, exist_ok=True)
                    shutil.copy(src, dst)
            layers = {'humanoid': [{'texture': f'{NS}:{metal}'}]}
            if metal != 'jetpack':
                layers['humanoid_leggings'] = [{'texture': f'{NS}:{metal}'}]
            write(assets / 'equipment' / f'{metal}.json', {'layers': layers})
        shutil.rmtree(assets / 'textures' / 'models')
    for particle in ('flux', 'rift', 'laser', 'thruster'):
        write(assets / 'particles' / f'{particle}.json', {'textures': [f'{NS}:{particle}']})

    bs = assets / 'blockstates'
    bm = assets / 'models' / 'block'
    for block in ('scrap_block', 'scrap_debris', 'titanium_ore', 'cobalt_ore', 'orichalcum_ore', 'titanium_block', 'cobalt_block',
                  'orichalcum_block', 'rift_glass'):
        write(bs / f'{block}.json', {'variants': {'': {'model': f'{NS}:block/{block}'}}})
        write(bm / f'{block}.json', cube_all(f'{NS}:block/{block}'))
    scrap = f'{NS}:block/scrap_block'
    tex = {'bottom': scrap, 'top': scrap, 'side': scrap}
    write(bm / 'scrap_slab.json', {'parent': 'minecraft:block/slab', 'textures': tex})
    write(bm / 'scrap_slab_top.json', {'parent': 'minecraft:block/slab_top', 'textures': tex})
    write(bs / 'scrap_slab.json', {'variants': {'type=bottom': {'model': f'{NS}:block/scrap_slab'},
                                                'type=top': {'model': f'{NS}:block/scrap_slab_top'},
                                                'type=double': {'model': f'{NS}:block/scrap_block'}}})
    write(bm / 'scrap_stairs_block.json', {'parent': 'minecraft:block/stairs', 'textures': tex})
    write(bm / 'scrap_stairs_block_inner.json', {'parent': 'minecraft:block/inner_stairs', 'textures': tex})
    write(bm / 'scrap_stairs_block_outer.json', {'parent': 'minecraft:block/outer_stairs', 'textures': tex})
    write(bs / 'scrap_stairs_block.json', stairs_states(f'{NS}:block/scrap_stairs_block'))
    write(bm / 'fabricator.json', {'parent': 'minecraft:block/orientable_with_bottom', 'textures': {
        'top': f'{NS}:block/fabricator_top', 'front': f'{NS}:block/fabricator_front', 'side': f'{NS}:block/fabricator_side',
        'bottom': f'{NS}:block/fabricator_bottom'}})
    write(bs / 'fabricator.json', facing_variants(f'{NS}:block/fabricator'))
    for lit, suffix in ((False, ''), (True, '_on')):
        write(bm / f'flux_charger{suffix}.json', {'parent': 'minecraft:block/orientable_with_bottom', 'textures': {
            'top': f'{NS}:block/flux_charger_top{suffix}', 'front': f'{NS}:block/flux_charger_front{suffix}',
            'side': f'{NS}:block/flux_charger_side{suffix}', 'bottom': f'{NS}:block/fabricator_bottom'}})
    write(bs / 'flux_charger.json', facing_variants(None, {',lit=false': f'{NS}:block/flux_charger', ',lit=true': f'{NS}:block/flux_charger_on'}))
    write(bm / 'rift_gate.json', GATE_MODEL)
    write(bs / 'rift_gate.json', {'variants': {'linked=false': {'model': f'{NS}:block/rift_gate'}, 'linked=true': {'model': f'{NS}:block/rift_gate'}}})
    write(bm / 'rift_cache.json', CACHE_MODEL)
    write(bs / 'rift_cache.json', {'variants': {'': {'model': f'{NS}:block/rift_cache'}}})
    write(bm / 'rift_beacon.json', {'parent': 'minecraft:block/cube_column', 'textures': {
        'side': f'{NS}:block/rift_beacon_side', 'end': f'{NS}:block/rift_beacon_top'}})
    write(bs / 'rift_beacon.json', {'variants': {'': {'model': f'{NS}:block/rift_beacon'}}})

    im = assets / 'models' / 'item'
    for path in NAMES:
        if path in BLOCKS:
            model = {'parent': f'{NS}:block/{path}'}
        else:
            model = {'parent': 'minecraft:item/handheld' if path in HANDHELD else 'minecraft:item/generated',
                     'textures': {'layer0': f'{NS}:item/{path}'}}
        write(im / f'{path}.json', model)
        if modern(v):
            write(assets / 'items' / f'{path}.json', {'model': {'type': 'minecraft:model', 'model': f'{NS}:item/{path}'}})

    # Data
    d = data / NS
    for block in ('scrap_block', 'titanium_block', 'cobalt_block', 'orichalcum_block', 'rift_glass', 'fabricator', 'flux_charger', 'rift_gate',
                  'rift_beacon', 'scrap_stairs_block'):
        write(d / folder(v, 'loot_table') / 'blocks' / f'{block}.json', drop_self(v, block))
    write(d / folder(v, 'loot_table') / 'blocks' / 'scrap_slab.json', drop_slab(v, 'scrap_slab'))
    for metal in ('titanium', 'cobalt', 'orichalcum'):
        write(d / folder(v, 'loot_table') / 'blocks' / f'{metal}_ore.json', drop_ore(v, f'{metal}_ore', f'{NS}:raw_{metal}'))
    shard_pool = pool(v, [{'type': 'minecraft:item', 'name': f'{NS}:rift_shard'}], conds(v, cond(v, 'random_chance', chance=0.05)))
    write(d / folder(v, 'loot_table') / 'blocks' / 'scrap_debris.json', drop_ore(v, 'scrap_debris', f'{NS}:scrap', shard_pool))
    write(d / folder(v, 'loot_table') / 'entities' / 'sentinel.json', sentinel_loot(v))

    rf = d / folder(v, 'recipe')
    adv = d / folder(v, 'advancement') / 'recipes'
    for rid, (pattern, key, out, count, unlocked) in SHAPED.items():
        recipe = {'type': 'minecraft:crafting_shaped', 'category': 'equipment' if any(x in rid for x in ('armor', 'sword', 'pickaxe', 'axe', 'shovel', 'hoe'))
                  else 'building' if 'block' in rid or 'slab' in rid or 'stairs' in rid or rid == 'rift_glass' else 'misc',
                  'pattern': pattern, 'key': {k: ingredient(v, i) for k, i in key.items()}, 'result': result(v, out, count)}
        write(rf / f'{rid}.json', recipe)
        write(adv / f'{rid}.json', unlock(v, f'{NS}:{rid}', unlocked))
    for rid, (inputs, out, count, unlocked) in SHAPELESS.items():
        write(rf / f'{rid}.json', {'type': 'minecraft:crafting_shapeless', 'category': 'misc',
                                   'ingredients': [ingredient(v, i) for i in inputs], 'result': result(v, out, count)})
        write(adv / f'{rid}.json', unlock(v, f'{NS}:{rid}', unlocked))
    for rid, (source, out, xp) in SMELTING.items():
        for kind, time, suffix in (('smelting', 200, ''), ('blasting', 100, '_blasting')):
            r = {'type': f'minecraft:{kind}', 'ingredient': ingredient(v, source), 'experience': xp, 'cookingtime': time}
            if v != '26.3':
                r['category'] = 'misc'
            r['result'] = {'item': out} if v == '1.20.1' else {'id': out}
            if v == '1.20.1':
                r['result'] = out
            write(rf / f'{rid}{suffix}.json', r)
            write(adv / f'{rid}{suffix}.json', unlock(v, f'{NS}:{rid}{suffix}', source))

    # Tags
    pick = ['scrap_block', 'scrap_slab', 'scrap_stairs_block', 'scrap_debris', 'titanium_ore', 'cobalt_ore', 'orichalcum_ore', 'titanium_block',
            'cobalt_block', 'orichalcum_block', 'rift_glass', 'fabricator', 'flux_charger', 'rift_gate', 'rift_beacon']
    btags = {
        'minecraft:mineable/pickaxe': [f'{NS}:{b}' for b in pick],
        'minecraft:needs_stone_tool': [f'{NS}:scrap_debris', f'{NS}:scrap_block', f'{NS}:scrap_slab', f'{NS}:scrap_stairs_block'],
        'minecraft:needs_iron_tool': [f'{NS}:{b}' for b in ('titanium_ore', 'cobalt_ore', 'titanium_block', 'cobalt_block', 'rift_gate', 'rift_beacon')],
        'minecraft:needs_diamond_tool': [f'{NS}:orichalcum_ore', f'{NS}:orichalcum_block'],
        'minecraft:beacon_base_blocks': [f'{NS}:titanium_block', f'{NS}:cobalt_block', f'{NS}:orichalcum_block'],
        'minecraft:slabs': [f'{NS}:scrap_slab'], 'minecraft:stairs': [f'{NS}:scrap_stairs_block'],
        'minecraft:dragon_immune': [f'{NS}:rift_cache'], 'minecraft:wither_immune': [f'{NS}:rift_cache'],
    }
    itags = {
        'minecraft:beacon_payment_items': [MATERIAL[m] for m in ('titanium', 'cobalt', 'orichalcum')],
        'minecraft:slabs': [f'{NS}:scrap_slab'], 'minecraft:stairs': [f'{NS}:scrap_stairs_block'],
    }
    for tool in TOOLS_LIST:
        itags[f'minecraft:{tool}s'] = [f'{NS}:{m}_{tool}' for m in METALS]
    if v != '1.20.1':
        for piece, tag in (('helmet', 'head_armor'), ('chestplate', 'chest_armor'), ('leggings', 'leg_armor'), ('boots', 'foot_armor')):
            itags[f'minecraft:{tag}'] = [f'{NS}:{m}_armor_{piece}' for m in METALS]
    itags['minecraft:trimmable_armor'] = [f'{NS}:{m}_armor_{p}' for m in METALS for p in ARMOR]
    for metal in METALS:
        itags[f'{NS}:repairs_{metal}_armor'] = [MATERIAL[metal]]
    itags[f'{NS}:repairs_jetpack'] = [MATERIAL['titanium']]
    for metal in ('titanium', 'cobalt', 'orichalcum'):
        if v == '1.20.1':
            itags[f'forge:ingots/{metal}'] = [MATERIAL[metal]]
            itags[f'forge:raw_materials/{metal}'] = [f'{NS}:raw_{metal}']
            itags[f'forge:ores/{metal}'] = [f'{NS}:{metal}_ore']
            itags[f'forge:storage_blocks/{metal}'] = [f'{NS}:{metal}_block']
            itags[f'c:{metal}_ingots'] = [MATERIAL[metal]]
            btags[f'forge:ores/{metal}'] = [f'{NS}:{metal}_ore']
        else:
            itags[f'c:ingots/{metal}'] = [MATERIAL[metal]]
            itags[f'c:raw_materials/{metal}'] = [f'{NS}:raw_{metal}']
            itags[f'c:ores/{metal}'] = [f'{NS}:{metal}_ore']
            itags[f'c:storage_blocks/{metal}'] = [f'{NS}:{metal}_block']
            btags[f'c:ores/{metal}'] = [f'{NS}:{metal}_ore']
            btags[f'c:storage_blocks/{metal}'] = [f'{NS}:{metal}_block']
    for tid, values in btags.items():
        ns, path = tid.split(':')
        write(data / ns / folder(v, 'tags/block') / f'{path}.json', {'values': values})
    for tid, values in itags.items():
        ns, path = tid.split(':')
        write(data / ns / folder(v, 'tags/item') / f'{path}.json', {'values': values})

    # Worldgen: ores in the deepslate layer and the stone above it.
    wg = d / 'worldgen'
    for fid, (block, size, count, low, high) in ORES.items():
        targets = [('minecraft:stone_ore_replaceables'), ('minecraft:deepslate_ore_replaceables')]
        if v == '26.3':
            feature = {'type': 'minecraft:ore', 'discard_chance_on_air_exposure': 0.0, 'size': size, 'targets': [
                {'state': f'{NS}:{block}', 'target': {'predicate_type': 'minecraft:tag_match', 'tag': t}} for t in targets]}
            write(wg / 'feature' / f'{fid}.json', feature)
        else:
            feature = {'type': 'minecraft:ore', 'config': {'discard_chance_on_air_exposure': 0.0, 'size': size, 'targets': [
                {'state': {'Name': f'{NS}:{block}'}, 'target': {'predicate_type': 'minecraft:tag_match', 'tag': t}} for t in targets]}}
            write(wg / 'configured_feature' / f'{fid}.json', feature)
        write(wg / 'placed_feature' / f'{fid}.json', {'feature': f'{NS}:{fid}', 'placement': [
            {'type': 'minecraft:count', 'count': count}, {'type': 'minecraft:in_square'},
            {'type': 'minecraft:height_range', 'height': {'type': 'minecraft:trapezoid' if fid == 'ore_titanium' else 'minecraft:uniform',
                                                          'min_inclusive': {'absolute': low}, 'max_inclusive': {'absolute': high}}},
            {'type': 'minecraft:biome'}]})
        if v == '1.20.1':
            write(d / 'forge' / 'biome_modifier' / f'{fid}.json', {'type': 'forge:add_features', 'biomes': '#minecraft:is_overworld',
                                                                     'features': f'{NS}:{fid}', 'step': 'underground_ores'})
        else:
            write(d / 'neoforge' / 'biome_modifier' / f'{fid}.json', {'type': 'neoforge:add_features', 'biomes': '#minecraft:is_overworld',
                                                                        'features': f'{NS}:{fid}', 'step': 'underground_ores'})


def main():
    for v in VERSIONS:
        if (REPO / v).is_dir():
            generate(v)
            print('generated', v)


if __name__ == '__main__':
    main()
