#!/usr/bin/env python3
"""Draws Chronofall's item, block, GUI and particle textures into tools/assets/textures (the source that
tools/gen_assets.py copies into every workspace). Pixel art is drawn from small primitives in the mod's palette:
gunmetal panels, flux cyan, rift magenta and thruster amber. Textures kept from Robotica 1.0 (ores, metals, tools,
armour, circuits, cards) live in tools/assets/legacy and are copied under their new names.

  python3 tools/gen_textures.py
"""
from __future__ import annotations

import math
import random
import shutil
from pathlib import Path

from PIL import Image, ImageDraw

TOOLS = Path(__file__).resolve().parent
OUT = TOOLS / 'assets' / 'textures'
LEGACY = TOOLS / 'assets' / 'legacy'

CYAN = (56, 243, 255)
CYAN_D = (27, 140, 154)
CYAN_L = (200, 252, 255)
MAG = (255, 63, 210)
MAG_D = (140, 20, 120)
MAG_L = (255, 196, 240)
AMBER = (255, 181, 71)
AMBER_D = (200, 100, 30)
AMBER_L = (255, 240, 196)
RED = (255, 51, 68)
STEEL = (123, 135, 152)
STEEL_D = (70, 79, 94)
STEEL_L = (178, 189, 204)
GUN = (58, 66, 80)
GUN_D = (34, 39, 49)
GUN_L = (92, 102, 118)
DARK = (20, 23, 30)
OUTLINE = (14, 16, 22)
WHITE = (226, 232, 240)

# Old texture -> new path (relative to textures/).
LEGACY_MAP = {
    'item/bd1b1275fdc0ac1.png': 'item/scrap.png',
    'item/circuit_empty.png': 'item/blank_printed_circuit_board.png',
    'item/circuit.png': 'item/printed_circuit_board.png',
    'item/controller.png': 'item/central_processing_unit.png',
    'item/copper_wires.png': 'item/electrical_conductors.png',
    'item/upgrade_card_empty.png': 'item/blank_card.png',
    'item/upgrade_card_health.png': 'item/bulk_upgrade_card.png',
    'item/upgrade_card_damage.png': 'item/damage_upgrade_card.png',
    'item/upgrade_card_speed.png': 'item/speed_upgrade_card.png',
    'item/armor_card.png': 'item/defense_upgrade_card.png',
    'item/repair_card.png': 'item/repair_card.png',
    'item/mech.png': 'item/mech.png',
    'block/scrap.png': 'block/scrap_block.png',
    'block/junk.png': 'block/scrap_debris.png',
    'block/titanium_ore_deepslate.png': 'block/titanium_ore.png',
    'block/cobalt_ore_deepslate.png': 'block/cobalt_ore.png',
    'block/orichalcum_ore_deepslate.png': 'block/orichalcum_ore.png',
}
for metal in ('titanium', 'cobalt', 'orichalcum'):
    LEGACY_MAP[f'item/{metal}_ingot.png'] = f'item/{metal}_ingot.png'
    LEGACY_MAP[f'item/raw_{metal}.png'] = f'item/raw_{metal}.png'
for metal in ('scrap', 'titanium', 'cobalt', 'orichalcum'):
    for tool in ('sword', 'pickaxe', 'axe', 'shovel', 'hoe'):
        LEGACY_MAP[f'item/{metal}_{tool}.png'] = f'item/{metal}_{tool}.png'
    for piece in ('helmet', 'chestplate', 'leggings', 'boots'):
        LEGACY_MAP[f'item/{metal}_{piece}.png'] = f'item/{metal}_armor_{piece}.png'
    LEGACY_MAP[f'models/armor/{metal}_layer_1.png'] = f'models/armor/{metal}_layer_1.png'
    LEGACY_MAP[f'models/armor/{metal}_layer_2.png'] = f'models/armor/{metal}_layer_2.png'


class Canvas:
    def __init__(self, w=16, h=16):
        self.img = Image.new('RGBA', (w, h), (0, 0, 0, 0))
        self.px = self.img.load()
        self.w, self.h = w, h

    def set(self, x, y, c, a=255):
        if 0 <= x < self.w and 0 <= y < self.h:
            self.px[x, y] = tuple(c[:3]) + (a,)

    def get(self, x, y):
        return self.px[x, y]

    def rect(self, x0, y0, x1, y1, c):
        for x in range(x0, x1 + 1):
            for y in range(y0, y1 + 1):
                self.set(x, y, c)

    def line(self, x0, y0, x1, y1, c):
        n = max(abs(x1 - x0), abs(y1 - y0))
        for i in range(n + 1):
            t = i / n if n else 0
            self.set(round(x0 + (x1 - x0) * t), round(y0 + (y1 - y0) * t), c)

    def outline(self, c=OUTLINE):
        """Adds a 1-pixel outline around every opaque pixel."""
        src = self.img.copy().load()
        for x in range(self.w):
            for y in range(self.h):
                if src[x, y][3] == 0 and any(0 <= x + dx < self.w and 0 <= y + dy < self.h and src[x + dx, y + dy][3] > 0
                                             for dx, dy in ((1, 0), (-1, 0), (0, 1), (0, -1))):
                    self.set(x, y, c)

    def save(self, rel):
        path = OUT / rel
        path.parent.mkdir(parents=True, exist_ok=True)
        self.img.save(path)


def mix(a, b, t):
    return tuple(int(round(a[i] + (b[i] - a[i]) * t)) for i in range(3))


# Items --------------------------------------------------------------------------------------------------------------

def rift_shard():
    c = Canvas()
    pts = [(9, 1), (13, 6), (10, 14), (5, 13), (3, 7)]
    poly = Image.new('L', (16, 16), 0)
    ImageDraw.Draw(poly).polygon(pts, fill=255)
    m = poly.load()
    for x in range(16):
        for y in range(16):
            if m[x, y]:
                t = (x + y) / 30
                c.set(x, y, mix(MAG_L, MAG_D, t))
    c.line(9, 2, 6, 12, MAG_L)
    c.line(10, 3, 12, 6, (255, 255, 255))
    c.set(8, 4, (255, 255, 255))
    c.line(5, 12, 10, 13, MAG_D)
    c.outline((60, 6, 52))
    return c


def flux_cell():
    c = Canvas()
    c.rect(5, 2, 10, 13, GUN)
    c.rect(5, 2, 10, 3, STEEL_L)
    c.rect(6, 1, 9, 1, STEEL)
    c.rect(5, 12, 10, 13, STEEL)
    c.rect(6, 5, 9, 10, CYAN_D)
    for y in range(5, 11):
        c.set(7, y, CYAN)
        c.set(8, y, CYAN_L if y % 2 else CYAN)
    c.set(5, 4, GUN_L)
    c.set(10, 4, GUN_D)
    c.rect(7, 14, 8, 14, AMBER)
    c.outline()
    return c


def jetpack():
    c = Canvas()
    for x0 in (2, 9):
        c.rect(x0, 2, x0 + 4, 11, STEEL)
        c.rect(x0, 2, x0 + 4, 2, STEEL_L)
        c.rect(x0 + 1, 3, x0 + 1, 10, STEEL_L)
        c.rect(x0 + 4, 3, x0 + 4, 11, STEEL_D)
        c.rect(x0 + 1, 12, x0 + 3, 13, GUN_D)
        c.rect(x0 + 1, 14, x0 + 3, 14, AMBER)
        c.set(x0 + 2, 15, AMBER_L)
    c.rect(7, 4, 8, 10, DARK)
    c.rect(7, 5, 8, 8, CYAN)
    c.set(7, 6, CYAN_L)
    c.outline()
    return c


def plasma_cutter():
    c = Canvas()
    c.rect(2, 9, 4, 13, GUN_D)
    c.rect(3, 10, 3, 12, GUN_L)
    for i in range(7):
        c.rect(3 + i, 8 - i, 5 + i, 9 - i, STEEL if i % 2 else STEEL_L)
    c.rect(4, 7, 6, 8, GUN)
    c.rect(9, 3, 11, 5, GUN_D)
    c.rect(11, 1, 13, 3, CYAN_D)
    c.set(12, 2, CYAN_L)
    c.set(13, 1, CYAN)
    c.set(14, 0, (255, 255, 255))
    c.set(5, 10, AMBER)
    c.outline()
    return c


def pulse_rifle():
    c = Canvas()
    c.line(1, 14, 12, 3, GUN)
    c.line(2, 14, 13, 3, GUN_D)
    c.line(1, 13, 12, 2, STEEL_L)
    c.rect(3, 11, 5, 13, DARK)
    c.rect(6, 9, 8, 11, GUN)
    c.rect(7, 11, 8, 13, GUN_D)
    c.line(9, 6, 13, 2, CYAN)
    c.set(14, 1, CYAN_L)
    c.set(15, 0, (255, 255, 255))
    c.set(8, 7, CYAN_D)
    c.outline()
    return c


def rift_tuner():
    c = Canvas()
    c.rect(4, 4, 11, 14, GUN)
    c.rect(4, 4, 11, 4, GUN_L)
    c.rect(5, 5, 10, 9, MAG_D)
    for x in range(5, 11):
        c.set(x, 7 + int(round(math.sin(x) * 1.2)), MAG_L)
    c.rect(6, 11, 7, 12, STEEL_L)
    c.rect(9, 11, 10, 12, CYAN)
    c.line(10, 3, 12, 0, STEEL)
    c.set(12, 0, MAG)
    c.outline()
    return c


def drone():
    c = Canvas()
    for cx, cy in ((3, 3), (12, 3), (3, 12), (12, 12)):
        c.line(cx - 2, cy, cx + 2, cy, DARK)
        c.line(cx, cy - 2, cx, cy + 2, DARK)
        c.set(cx, cy, CYAN)
    c.line(4, 4, 11, 11, STEEL)
    c.line(11, 4, 4, 11, STEEL)
    c.rect(5, 5, 10, 10, WHITE)
    c.rect(5, 5, 10, 5, (250, 252, 255))
    c.rect(5, 10, 10, 10, STEEL)
    c.rect(6, 7, 9, 8, DARK)
    c.rect(7, 7, 8, 8, CYAN)
    c.outline()
    return c


def skiff():
    c = Canvas()
    c.rect(2, 8, 13, 10, WHITE)
    c.rect(1, 9, 1, 9, STEEL)
    c.rect(3, 6, 8, 7, WHITE)
    c.rect(4, 5, 7, 5, (110, 200, 240))
    c.rect(5, 4, 6, 4, (110, 200, 240))
    c.rect(2, 10, 13, 10, STEEL_D)
    c.rect(2, 9, 13, 9, CYAN)
    c.rect(10, 6, 14, 8, STEEL)
    c.rect(15, 6, 15, 8, AMBER)
    c.rect(11, 3, 12, 5, STEEL_L)
    c.rect(3, 11, 12, 11, DARK)
    c.rect(5, 12, 10, 12, CYAN_D)
    c.outline()
    return c


def chip(color, light, symbol):
    c = Canvas()
    c.rect(3, 3, 12, 12, (24, 40, 34))
    c.rect(3, 3, 12, 3, (44, 70, 58))
    for i in range(4, 12, 2):
        for x, y in ((i, 1), (i, 2), (i, 13), (i, 14), (1, i), (2, i), (13, i), (14, i)):
            c.set(x, y, AMBER if (x + y) % 4 else AMBER_D)
    c.rect(5, 5, 10, 10, DARK)
    for x, y in symbol:
        c.set(x, y, color)
    c.set(6, 6, light)
    c.outline()
    return c


def spawn_egg():
    c = Canvas()
    poly = Image.new('L', (16, 16), 0)
    ImageDraw.Draw(poly).ellipse((3, 1, 12, 15), fill=255)
    m = poly.load()
    rng = random.Random(3)
    for x in range(16):
        for y in range(16):
            if m[x, y]:
                c.set(x, y, mix((60, 52, 62), (30, 26, 34), (x + y) / 30))
    for _ in range(7):
        x, y = rng.randint(4, 11), rng.randint(3, 13)
        if m[x, y]:
            c.set(x, y, RED)
    c.rect(5, 6, 10, 6, RED)
    c.set(5, 3, (110, 100, 112))
    c.outline((10, 8, 12))
    return c


# Blocks -------------------------------------------------------------------------------------------------------------

def panel(base, dark, light, seed, seams=True):
    c = Canvas()
    rng = random.Random(seed)
    for x in range(16):
        for y in range(16):
            n = rng.random()
            c.set(x, y, mix(base, light if n > 0.5 else dark, abs(n - 0.5) * 0.3))
    if seams:
        for i in range(16):
            c.set(i, 0, light)
            c.set(0, i, mix(base, light, 0.5))
            c.set(i, 15, dark)
            c.set(15, i, dark)
        for i in range(1, 15):
            c.set(i, 7, dark)
            c.set(7, i, mix(base, dark, 0.6))
            c.set(i, 8, mix(base, light, 0.35))
        for x, y in ((2, 2), (13, 2), (2, 13), (13, 13), (5, 10), (10, 5)):
            c.set(x, y, light)
    return c


def storage(metal):
    tints = {'titanium': ((196, 204, 214), (140, 150, 164), (236, 240, 246)),
             'cobalt': ((60, 98, 196), (34, 60, 130), (120, 160, 240)),
             'orichalcum': ((214, 130, 52), (150, 80, 24), (255, 196, 110))}
    base, dark, light = tints[metal]
    c = panel(base, dark, light, metal)
    for x in range(3, 13):
        c.set(x, 3, light)
        c.set(x, 12, dark)
    for y in range(3, 13):
        c.set(3, y, light)
        c.set(12, y, dark)
    return c


def rift_glass_frames():
    frames = []
    for f in range(4):
        c = Canvas()
        rng = random.Random(11)
        for x in range(16):
            for y in range(16):
                n = rng.random()
                c.set(x, y, mix((22, 14, 30), (10, 6, 16), n * 0.6))
        rng = random.Random(5)
        for k in range(5):
            x, y = rng.randrange(16), rng.randrange(16)
            for step in range(9):
                glow = (step + f * 2 + k) % 8
                col = MAG_L if glow < 2 else MAG if glow < 5 else MAG_D
                c.set(x, y, col)
                x = (x + rng.choice((-1, 0, 1))) % 16
                y = (y + rng.choice((-1, 0, 1))) % 16
        for i in range(16):
            c.set(i, 0, (60, 34, 76))
            c.set(0, i, (60, 34, 76))
        frames.append(c)
    return frames


def strip(frames):
    img = Image.new('RGBA', (16, 16 * len(frames)))
    for i, f in enumerate(frames):
        img.paste(f.img, (0, 16 * i))
    return img


def fabricator_faces():
    side = panel(GUN, GUN_D, GUN_L, 'fab_side')
    for x in range(2, 14):
        side.set(x, 13, CYAN_D)
        side.set(x, 2, STEEL)
    front = panel(GUN, GUN_D, GUN_L, 'fab_front', seams=False)
    front.rect(2, 2, 13, 9, DARK)
    for x in range(3, 13):
        for y in range(3, 9):
            if (x * 7 + y * 3) % 5 == 0:
                front.set(x, y, CYAN)
            elif (x + y) % 3 == 0:
                front.set(x, y, CYAN_D)
    front.rect(3, 11, 6, 13, STEEL)
    front.rect(9, 11, 12, 12, CYAN)
    front.set(11, 13, AMBER)
    top = panel(STEEL_D, GUN_D, STEEL, 'fab_top', seams=False)
    for x in range(16):
        for y in range(16):
            d = math.hypot(x - 7.5, y - 7.5)
            if 4.5 <= d < 6:
                top.set(x, y, CYAN if (x + y) % 2 else CYAN_D)
            elif d < 2.5:
                top.set(x, y, CYAN_L if d < 1.2 else CYAN)
            elif d < 4.5:
                top.set(x, y, DARK)
    bottom = panel(GUN_D, DARK, GUN, 'fab_bottom')
    return {'fabricator_side': side, 'fabricator_front': front, 'fabricator_top': top, 'fabricator_bottom': bottom}


def charger_faces(lit):
    side = panel(GUN, GUN_D, GUN_L, 'chg_side')
    side.rect(5, 3, 10, 12, DARK)
    for y in range(4, 12):
        for x in range(6, 10):
            fill = lit or y > 8
            side.set(x, y, (CYAN if (y % 3) else CYAN_L) if fill else CYAN_D if y > 6 else (22, 40, 48))
    front = panel(GUN, GUN_D, GUN_L, 'chg_front', seams=False)
    front.rect(3, 3, 12, 12, DARK)
    for x in range(4, 12):
        for y in range(4, 12):
            d = math.hypot(x - 7.5, y - 7.5)
            if d < 3.5:
                front.set(x, y, (CYAN_L if d < 1.5 else CYAN) if lit else (CYAN_D if d < 1.5 else (30, 60, 70)))
    front.rect(2, 13, 13, 13, AMBER if lit else AMBER_D)
    top = panel(STEEL_D, GUN_D, STEEL, 'chg_top')
    for x in range(4, 12):
        for y in range(4, 12):
            if (x + y) % 2 == 0:
                top.set(x, y, AMBER if lit else (120, 80, 40))
    suffix = '_on' if lit else ''
    return {f'flux_charger_side{suffix}': side, f'flux_charger_front{suffix}': front, f'flux_charger_top{suffix}': top}


def gate_top_frames():
    frames = []
    for f in range(8):
        c = Canvas()
        for x in range(16):
            for y in range(16):
                d = math.hypot(x - 7.5, y - 7.5)
                a = math.atan2(y - 7.5, x - 7.5)
                swirl = (math.sin(a * 3 + d * 0.9 - f * math.pi / 4) + 1) / 2
                if d > 7:
                    c.set(x, y, GUN_D)
                elif d > 6:
                    c.set(x, y, STEEL)
                else:
                    c.set(x, y, mix(MAG_D, MAG_L, swirl * (1 - d / 7) + 0.1))
        c.set(7, 7, (255, 255, 255))
        c.set(8, 8, (255, 255, 255))
        frames.append(c)
    return frames


def gate_side():
    c = panel(GUN_D, DARK, GUN, 'gate_side', seams=False)
    for x in range(16):
        c.set(x, 12, MAG if x % 3 else MAG_L)
        c.set(x, 15, DARK)
    return c


def cache_faces():
    side = panel((30, 22, 40), (14, 10, 20), (64, 46, 84), 'cache', seams=False)
    for i in range(16):
        side.set(i, 1, STEEL)
        side.set(i, 14, STEEL_D)
        side.set(1, i, STEEL)
        side.set(14, i, STEEL_D)
    for x, y in ((4, 4), (5, 5), (6, 6), (7, 7), (8, 8), (9, 9), (10, 10), (11, 11), (11, 4), (10, 5), (9, 6), (6, 9), (5, 10), (4, 11)):
        side.set(x, y, MAG)
    side.rect(7, 7, 8, 8, MAG_L)
    top = panel((30, 22, 40), (14, 10, 20), (64, 46, 84), 'cache_top', seams=False)
    for i in range(16):
        top.set(i, 1, STEEL)
        top.set(1, i, STEEL)
        top.set(i, 14, STEEL_D)
        top.set(14, i, STEEL_D)
    top.rect(4, 4, 11, 11, MAG_D)
    top.rect(6, 6, 9, 9, MAG)
    top.rect(7, 7, 8, 8, MAG_L)
    return {'rift_cache_side': side, 'rift_cache_top': top}


def beacon_faces():
    side = panel(GUN, GUN_D, GUN_L, 'beacon_side', seams=False)
    side.rect(5, 0, 10, 15, DARK)
    for y in range(16):
        side.set(7, y, MAG if y % 4 else MAG_L)
        side.set(8, y, MAG_D)
    side.rect(0, 0, 15, 1, STEEL)
    side.rect(0, 14, 15, 15, STEEL_D)
    top = panel(STEEL_D, GUN_D, STEEL, 'beacon_top', seams=False)
    for x in range(16):
        for y in range(16):
            d = math.hypot(x - 7.5, y - 7.5)
            if d < 3:
                top.set(x, y, MAG_L if d < 1.5 else MAG)
            elif d < 4:
                top.set(x, y, DARK)
            elif d < 5 and (x + y) % 2:
                top.set(x, y, MAG_D)
    return {'rift_beacon_side': side, 'rift_beacon_top': top}


# GUI ----------------------------------------------------------------------------------------------------------------

NAVY = (13, 18, 32)
NAVY_L = (24, 32, 52)
EDGE = (27, 140, 154)


def gui_panel(w, h):
    img = Image.new('RGBA', (256, 256), (0, 0, 0, 0))
    d = ImageDraw.Draw(img)
    d.rectangle((0, 0, w - 1, h - 1), fill=NAVY + (255,))
    for y in range(h):
        t = y / h
        d.line((2, y, w - 3, y), fill=mix(NAVY_L, NAVY, t) + (255,))
    d.rectangle((0, 0, w - 1, h - 1), outline=EDGE + (255,))
    d.rectangle((1, 1, w - 2, h - 2), outline=(8, 12, 22, 255))
    for i in range(0, w, 4):
        img.putpixel((i, 2), CYAN_D + (255,))
    d.line((3, h - 3, 40, h - 3), fill=MAG + (255,))
    d.line((w - 41, h - 3, w - 4, h - 3), fill=MAG + (255,))
    return img, d


def slot(d, x, y, big=False):
    s = 26 if big else 18
    o = 4 if big else 0
    d.rectangle((x - 1 - o, y - 1 - o, x - 2 - o + s, y - 2 - o + s), fill=(8, 11, 20, 255), outline=(40, 52, 76, 255))
    d.line((x - 1 - o, y - 2 - o + s, x - 2 - o + s, y - 2 - o + s), fill=(60, 76, 104, 255))
    if big:
        d.rectangle((x - 1 - o, y - 1 - o, x - 2 - o + s, y - 2 - o + s), outline=CYAN + (255,))


def fabricator_gui():
    img, d = gui_panel(256, 222)
    d.rectangle((7, 17, 112, 102), fill=(9, 13, 24, 255), outline=(40, 52, 76, 255))
    d.rectangle((118, 17, 249, 128), fill=(9, 13, 24, 255), outline=(40, 52, 76, 255))
    slot(d, 216, 98, big=True)
    for row in range(3):
        for col in range(9):
            slot(d, 48 + col * 18, 140 + row * 18)
    for col in range(9):
        slot(d, 48 + col * 18, 198)
    return img


def charger_gui():
    img, d = gui_panel(176, 166)
    slot(d, 44, 35, big=True)
    slot(d, 116, 35, big=True)
    d.rectangle((70, 17, 105, 68), fill=(6, 10, 18, 255), outline=CYAN_D + (255,))
    for y in range(20, 68, 6):
        d.line((70, y, 73, y), fill=CYAN_D + (255,))
    d.rectangle((45, 57, 58, 70), fill=(6, 10, 18, 255), outline=(60, 50, 40, 255))
    for row in range(3):
        for col in range(9):
            slot(d, 8 + col * 18, 84 + row * 18)
    for col in range(9):
        slot(d, 8 + col * 18, 142)
    return img


# Particles ----------------------------------------------------------------------------------------------------------

def glow_dot(core, edge, size=8):
    img = Image.new('RGBA', (size, size), (0, 0, 0, 0))
    px = img.load()
    c = (size - 1) / 2
    for x in range(size):
        for y in range(size):
            d = math.hypot(x - c, y - c) / (size / 2)
            if d < 1:
                a = int(255 * (1 - d) ** 1.2)
                px[x, y] = mix(core, edge, d) + (a,)
    return img


def main():
    if OUT.exists():
        shutil.rmtree(OUT)
    for old, new in LEGACY_MAP.items():
        target = OUT / new
        target.parent.mkdir(parents=True, exist_ok=True)
        shutil.copy(LEGACY / old, target)
    items = {
        'rift_shard': rift_shard(), 'flux_cell': flux_cell(), 'jetpack': jetpack(), 'plasma_cutter': plasma_cutter(),
        'pulse_rifle': pulse_rifle(), 'rift_tuner': rift_tuner(), 'drone': drone(), 'skiff': skiff(), 'sentinel_spawn_egg': spawn_egg(),
        'collector_chip': chip(CYAN, CYAN_L, [(6, 8), (7, 9), (8, 8), (9, 7), (7, 7), (7, 6), (8, 6)]),
        'guard_chip': chip(RED, (255, 186, 192), [(7, 6), (8, 6), (6, 7), (9, 7), (6, 8), (9, 8), (7, 9), (8, 9)]),
        'lamp_chip': chip(AMBER, AMBER_L, [(7, 6), (8, 6), (7, 7), (8, 7), (6, 8), (9, 8), (7, 9), (8, 9)]),
    }
    for name, canvas in items.items():
        canvas.save(f'item/{name}.png')
    for metal in ('titanium', 'cobalt', 'orichalcum'):
        storage(metal).save(f'block/{metal}_block.png')
    glass = strip(rift_glass_frames())
    (OUT / 'block').mkdir(parents=True, exist_ok=True)
    glass.save(OUT / 'block' / 'rift_glass.png')
    (OUT / 'block' / 'rift_glass.png.mcmeta').write_text('{"animation": {"frametime": 6, "interpolate": true}}\n')
    gate = strip(gate_top_frames())
    gate.save(OUT / 'block' / 'rift_gate_top.png')
    (OUT / 'block' / 'rift_gate_top.png.mcmeta').write_text('{"animation": {"frametime": 3}}\n')
    gate_side().save('block/rift_gate_side.png')
    faces = {}
    faces.update(fabricator_faces())
    faces.update(charger_faces(False))
    faces.update(charger_faces(True))
    faces.update(cache_faces())
    faces.update(beacon_faces())
    for name, canvas in faces.items():
        canvas.save(f'block/{name}.png')
    (OUT / 'gui').mkdir(parents=True, exist_ok=True)
    fabricator_gui().save(OUT / 'gui' / 'fabricator.png')
    charger_gui().save(OUT / 'gui' / 'flux_charger.png')
    (OUT / 'particle').mkdir(parents=True, exist_ok=True)
    glow_dot((255, 255, 255), CYAN).save(OUT / 'particle' / 'flux.png')
    glow_dot((255, 230, 250), MAG).save(OUT / 'particle' / 'rift.png')
    glow_dot((255, 230, 230), RED).save(OUT / 'particle' / 'laser.png')
    glow_dot((255, 255, 255), (255, 255, 255)).save(OUT / 'particle' / 'thruster.png')
    # The jetpack is drawn as a model on the player's back; its armour layer is empty.
    (OUT / 'models' / 'armor').mkdir(parents=True, exist_ok=True)
    Image.new('RGBA', (64, 32), (0, 0, 0, 0)).save(OUT / 'models' / 'armor' / 'jetpack_layer_1.png')
    print('textures written to', OUT)


if __name__ == '__main__':
    main()
