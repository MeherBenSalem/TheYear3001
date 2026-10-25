#!/usr/bin/env python3
"""Entity models for Chronofall: one spec per model, two outputs.

* ``client/model/ModelMeshes.java`` in every workspace: the layer definitions (MeshDefinition, PartDefinition,
  CubeListBuilder and PartPose are the same API on every supported Minecraft version). The animated model classes
  look parts up by the names used here.
* ``textures/entity/<model>.png`` and ``<model>_glow.png``: each box's faces painted by material (gunmetal panels with
  seams and rivets, trim, glass, rift glass with cracks, and glowing parts). The glow texture holds only the glowing
  pixels; renderers draw it full-bright on top.

Units are model pixels (16 per block), y down, the entity's front facing -z (north), ground at y = 24.

  python3 tools/gen_models.py            # writes into every workspace
"""
from __future__ import annotations

import math
import random
from pathlib import Path

from PIL import Image

ROOT = Path(__file__).resolve().parents[1]
WORKSPACES = ['1.20.1', '1.21.1', '26.1.2', '26.2', '26.3']

# Palettes: (base, dark seam, light edge). Glow materials have (core, centre).
PALETTE = {
    'hull': ((58, 66, 80), (34, 39, 49), (92, 102, 118)),
    'trim': ((123, 135, 152), (70, 79, 94), (178, 189, 204)),
    'dark': ((27, 31, 39), (14, 16, 21), (48, 54, 66)),
    'red_hull': ((44, 40, 46), (24, 20, 26), (84, 70, 80)),
    'red_trim': ((120, 38, 48), (70, 18, 26), (176, 70, 82)),
    'white': ((214, 220, 228), (150, 158, 170), (240, 244, 248)),
    'glass': ((24, 72, 104), (12, 40, 62), (110, 200, 240)),
    'rift': ((22, 14, 30), (10, 6, 16), (60, 34, 76)),
}
GLOW = {
    'glow_cyan': ((56, 243, 255), (200, 252, 255)),
    'glow_red': ((255, 40, 60), (255, 110, 120)),
    'glow_amber': ((255, 181, 71), (255, 240, 196)),
    'glow_magenta': ((255, 63, 210), (255, 196, 240)),
}


class Box:
    def __init__(self, x, y, z, w, h, d, mat='hull', faces=None):
        self.pos = (x, y, z)
        self.size = (w, h, d)
        self.mat = mat
        self.faces = faces or {}  # face name -> material override (north is the front)
        self.uv = (0, 0)


class Part:
    def __init__(self, name, pivot=(0, 0, 0), rot=(0, 0, 0), boxes=(), children=()):
        self.name = name
        self.pivot = pivot
        self.rot = rot
        self.boxes = list(boxes)
        self.children = list(children)

    def walk(self):
        yield self
        for child in self.children:
            yield from child.walk()


def deg(a):
    return math.radians(a)


# Models ------------------------------------------------------------------------------------------------------------

def drone():
    arms = []
    for i in range(4):
        arms.append(Part(f'arm{i}', (0, 0, 0), (0, deg(45 + 90 * i), 0), [Box(-0.75, -0.5, -9, 1.5, 1, 6, 'trim')], [
            Part(f'rotor{i}', (0, -1, -8.5), (0, 0, 0), [Box(-4, 0, -0.5, 8, 1, 1, 'dark'), Box(-0.5, 0, -4, 1, 1, 8, 'dark'),
                                                         Box(-1, -1, -1, 2, 1, 2, 'glow_cyan')]),
        ]))
    body = Part('body', (0, 18, 0), (0, 0, 0), [
        Box(-4, -2.5, -4, 8, 5, 8, 'white', {'north': 'white'}),
        Box(-3, -3.5, -3, 6, 1, 6, 'trim'),
        Box(-2, -1.5, -4.6, 4, 2, 1, 'glow_cyan'),
        Box(-1.5, 2.5, -1.5, 3, 1, 3, 'dark'),
        Box(-2.5, 2.6, -2.5, 5, 1, 5, 'hull'),
        Box(0.5, -7.5, 1, 1, 4, 1, 'dark'),
        Box(0.25, -8.5, 0.75, 1.5, 1, 1.5, 'glow_cyan'),
    ], arms)
    return 'drone', 64, 64, [body]


def sentinel():
    head = Part('head', (0, -3, 0), (0, 0, 0), [
        Box(-4, -8, -4, 8, 8, 8, 'red_hull'),
        Box(-3.5, -5.5, -4.6, 7, 2, 1, 'glow_red'),
        Box(-0.5, -10, -2, 1, 2, 6, 'red_trim'),
        Box(-4.5, -6, -2, 1, 3, 4, 'dark'), Box(3.5, -6, -2, 1, 3, 4, 'dark'),
    ])
    body = Part('body', (0, -3, 0), (0, 0, 0), [
        Box(-5, 0, -3, 10, 12, 6, 'red_hull'),
        Box(-1.5, 3, -3.6, 3, 3, 1, 'glow_red'),
        Box(-4, 1, 3, 8, 8, 2, 'dark'),
        Box(-3, 9, -2.5, 6, 3, 5, 'red_trim'),
    ])
    right_arm = Part('right_arm', (-6, -1, 0), (0, 0, 0), [
        Box(-4, -2, -3, 5, 4, 6, 'red_trim'),
        Box(-3, 2, -2, 3, 6, 4, 'red_hull'),
        Box(-3.5, 7, -2.5, 4, 8, 5, 'dark'),
        Box(-2.5, 14.5, -1.5, 2, 1, 2, 'glow_red'),
    ])
    left_arm = Part('left_arm', (6, -1, 0), (0, 0, 0), [
        Box(-1, -2, -3, 5, 4, 6, 'red_trim'),
        Box(0, 2, -2, 3, 11, 4, 'red_hull'),
        Box(-0.5, 12, -2.5, 4, 3, 5, 'dark'),
    ])
    legs = [Part(f'{side}_leg', (x, 9, 0), (0, 0, 0), [
        Box(-2, 0, -2, 4, 8, 4, 'red_hull'),
        Box(-2.5, 7, -2.5, 5, 2, 5, 'red_trim'),
        Box(-1.5, 9, -1.5, 3, 4, 3, 'dark'),
        Box(-2.5, 13, -4, 5, 2, 6, 'red_trim'),
    ]) for side, x in (('right', -2.5), ('left', 2.5))]
    return 'sentinel', 64, 64, [head, body, right_arm, left_arm, *legs]


def mech():
    legs = []
    for side, x in (('right', -7), ('left', 7)):
        legs.append(Part(f'{side}_leg', (x, 4, 0), (0, 0, 0), [
            Box(-3, 0, -3, 6, 9, 6, 'hull'),
            Box(-3.5, 8, -3.5, 7, 3, 7, 'trim', {'north': 'trim'}),
            Box(-2.5, 11, -2.5, 5, 7, 5, 'hull'),
            Box(-2, 11.5, -3, 4, 1, 1, 'glow_cyan'),
            Box(-4, 18, -6, 8, 2, 10, 'dark'),
        ]))
    body = Part('body', (0, 4, 0), (0, 0, 0), [
        Box(-8, -4, -5, 16, 5, 10, 'dark'),
        Box(-10, -10, -8, 20, 7, 16, 'hull'),
        Box(-9, -12, -7, 18, 2, 14, 'trim'),
        Box(-6, -9, -8.6, 12, 2, 1, 'glow_cyan'),
        Box(-5, -14, 7, 10, 10, 4, 'trim'),
        Box(-3, -12, 10.6, 6, 6, 1, 'glow_cyan'),
        Box(-10, -17, -9, 2, 5, 2, 'trim'), Box(8, -17, -9, 2, 5, 2, 'trim'),
        Box(-8, -17, -9, 16, 1, 2, 'trim'),
        Box(-8, -16, -8.5, 16, 4, 1, 'glass'),
    ])
    arms = []
    for side, x, sign in (('right', -11, -1), ('left', 11, 1)):
        ox = -6 if sign < 0 else 0
        arms.append(Part(f'{side}_arm', (x, -8, 0), (0, 0, 0), [
            Box(ox, -3, -4, 6, 6, 8, 'trim'),
            Box(ox + 0.5, -4, -2, 5, 1, 4, 'glow_cyan'),
            Box(ox + 1, 3, -3, 4, 9, 6, 'hull'),
            Box(ox + 0.5, 11, -4, 5, 6, 8, 'dark'),
            Box(ox + 1, 16.5, -3, 4, 1, 1, 'glow_cyan'),
        ]))
    return 'mech', 128, 128, [*legs, body, *arms]


def skiff():
    windshield = Part('windshield', (0, -3, -8), (deg(-35), 0, 0), [Box(-6, -6, 0, 12, 6, 1, 'glass')])
    fin = Part('fin', (0, -3, 9), (deg(-15), 0, 0), [Box(-0.5, -8, 0, 1, 8, 6, 'trim'), Box(-0.5, -8.6, 3, 1, 1, 3, 'glow_cyan')])
    hull = Part('hull', (0, 17, 0), (0, 0, 0), [
        Box(-8, -3, -16, 16, 6, 30, 'white'),
        Box(-6, -2, -24, 12, 4, 8, 'white'),
        Box(-3, -1, -28, 6, 2, 4, 'trim'),
        Box(-7, 3, -12, 14, 1, 22, 'dark'),
        Box(-1, 3.4, -10, 2, 1, 18, 'glow_cyan'),
        Box(-4, -5, -3, 8, 2, 8, 'dark'),
        Box(-4, -11, 4, 8, 6, 2, 'dark'),
        Box(-14, -0.5, -2, 28, 1, 10, 'hull'),
        Box(-8.5, -2, -16, 1, 2, 30, 'glow_cyan'),
        Box(7.5, -2, -16, 1, 2, 30, 'glow_cyan'),
    ], [windshield, fin])
    engines = [Part(f'{side}_engine', (x, 17, 4), (0, 0, 0), [
        Box(-3, -3, -10, 6, 6, 20, 'trim'),
        Box(-2, -2, 9.6, 4, 4, 1, 'glow_amber'),
        Box(-2, -2, -10.6, 4, 4, 1, 'dark'),
        Box(-3.5, -3.5, 6, 7, 7, 3, 'hull'),
    ]) for side, x in (('left', 14), ('right', -14))]
    return 'skiff', 128, 128, [hull, *engines]


def chrono_pod():
    fins = [Part(f'fin{i}', (0, 0, 0), (0, deg(90 * i), 0), [Box(-1, -11, -12, 2, 12, 4, 'dark'), Box(-0.5, -11.5, -12.5, 1, 4, 1, 'glow_magenta')])
            for i in range(4)]
    body = Part('body', (0, 12, 0), (0, 0, 0), [
        Box(-8, -10, -8, 16, 18, 16, 'rift'),
        Box(-6, 8, -6, 12, 4, 12, 'trim'),
        Box(-3, 12, -3, 6, 2, 6, 'glow_amber'),
        Box(-6, -13, -6, 12, 3, 12, 'dark'),
        Box(-2, -15, -2, 4, 2, 4, 'glow_magenta'),
    ], fins)
    return 'chrono_pod', 64, 128, [body]


def jetpack():
    # Drawn on the player's body part: y 0 is the shoulders, +z is the back.
    pack = Part('pack', (0, 0, 0), (0, 0, 0), [
        Box(-4, 1, 2, 8, 9, 2, 'dark'),
        Box(-4.5, 0, 4, 4, 10, 4, 'trim'),
        Box(0.5, 0, 4, 4, 10, 4, 'trim'),
        Box(-1, 2, 4.2, 2, 6, 1, 'glow_cyan'),
        Box(-4, 10, 4.5, 3, 2, 3, 'dark'),
        Box(1, 10, 4.5, 3, 2, 3, 'dark'),
        Box(-3.5, 11.6, 5, 2, 1, 2, 'glow_amber'),
        Box(1.5, 11.6, 5, 2, 1, 2, 'glow_amber'),
    ])
    return 'jetpack', 64, 32, [pack]


MODELS = [drone, sentinel, mech, skiff, chrono_pod, jetpack]


# Layout and painting ------------------------------------------------------------------------------------------------

def isize(v):
    return max(1, int(math.ceil(v - 1e-6)))


def layout(parts, width, height):
    boxes = [b for p in parts for part in p.walk() for b in part.boxes]
    order = sorted(boxes, key=lambda b: -(isize(b.size[2]) + isize(b.size[1])))
    x = y = row = 0
    for b in order:
        w, h, d = (isize(s) for s in b.size)
        bw, bh = 2 * (w + d), d + h
        if x + bw > width:
            x, y, row = 0, y + row, 0
        b.uv = (x, y)
        x += bw
        row = max(row, bh)
        if y + bh > height:
            return False
    return True


def faces(b):
    """(name, x0, y0, w, h) of each face in the texture."""
    u, v = b.uv
    w, h, d = (isize(s) for s in b.size)
    return [('down', u + d, v, w, d), ('up', u + d + w, v, w, d), ('west', u, v + d, d, h), ('north', u + d, v + d, w, h),
            ('east', u + d + w, v + d, d, h), ('south', u + 2 * d + w, v + d, w, h)]


def mix(a, b, t):
    return tuple(int(round(a[i] + (b[i] - a[i]) * t)) for i in range(3))


def paint_face(img, glow, rng, mat, name, x0, y0, w, h):
    px, gx = img.load(), glow.load()
    if mat in GLOW:
        core, centre = GLOW[mat]
        for i in range(w):
            for j in range(h):
                edge = i in (0, w - 1) or j in (0, h - 1)
                c = core if edge and w > 2 and h > 2 else centre if (w <= 2 or h <= 2) else mix(centre, core, rng.random() * 0.3)
                px[x0 + i, y0 + j] = c + (255,)
                gx[x0 + i, y0 + j] = c + (255,)
        return
    base, seam, light = PALETTE[mat]
    for i in range(w):
        for j in range(h):
            n = rng.random()
            c = mix(base, light if n > 0.5 else seam, abs(n - 0.5) * 0.25)
            if mat == 'glass':
                c = mix(base, light, max(0.0, 1 - abs((i - j * 0.8) - w * 0.3) / 2.0) * 0.6) if w > 3 else base
            px[x0 + i, y0 + j] = c + (255,)
    if mat == 'glass':
        return
    # Edge highlight on the top and left, seam on the bottom and right.
    if w >= 3 and h >= 3:
        for i in range(w):
            px[x0 + i, y0] = mix(base, light, 0.6) + (255,)
            px[x0 + i, y0 + h - 1] = seam + (255,)
        for j in range(h):
            px[x0, y0 + j] = mix(base, light, 0.4) + (255,)
            px[x0 + w - 1, y0 + j] = seam + (255,)
    # Panel lines on large faces.
    if w >= 8 and h >= 6:
        for cx in range(x0 + rng.randint(4, 6), x0 + w - 3, rng.randint(6, 9)):
            for j in range(1, h - 1):
                px[cx, y0 + j] = seam + (255,)
        for cy in range(y0 + rng.randint(3, 5), y0 + h - 2, rng.randint(5, 8)):
            for i in range(1, w - 1):
                px[x0 + i, cy] = seam + (255,)
    # Rivets in the corners of medium faces.
    if w >= 5 and h >= 5 and mat not in ('rift',):
        for cx, cy in ((1, 1), (w - 2, 1), (1, h - 2), (w - 2, h - 2)):
            px[x0 + cx, y0 + cy] = light + (255,)
    # Rift glass: magenta cracks that glow.
    if mat == 'rift':
        core, centre = GLOW['glow_magenta']
        for _ in range(max(1, (w * h) // 40)):
            cx, cy = rng.randrange(w), rng.randrange(h)
            for _ in range(rng.randint(3, 8)):
                c = centre if rng.random() < 0.3 else core
                px[x0 + cx, y0 + cy] = c + (255,)
                gx[x0 + cx, y0 + cy] = c + (255,)
                cx = max(0, min(w - 1, cx + rng.choice((-1, 0, 1))))
                cy = max(0, min(h - 1, cy + rng.choice((-1, 0, 1))))
    # Small status lights on the front of hull parts.
    if name == 'north' and mat in ('hull', 'white', 'trim') and w >= 6 and h >= 4 and rng.random() < 0.6:
        cx, cy = x0 + w - 3, y0 + 2
        px[cx, cy] = GLOW['glow_cyan'][0] + (255,)
        gx[cx, cy] = GLOW['glow_cyan'][0] + (255,)


def paint(name, width, height, parts):
    rng = random.Random(name)
    img = Image.new('RGBA', (width, height), (0, 0, 0, 0))
    glow = Image.new('RGBA', (width, height), (0, 0, 0, 0))
    for p in parts:
        for part in p.walk():
            for b in part.boxes:
                for face, x0, y0, w, h in faces(b):
                    paint_face(img, glow, rng, b.faces.get(face, b.mat), face, x0, y0, w, h)
    return img, glow


# Java ---------------------------------------------------------------------------------------------------------------

def f(v):
    s = f'{v:.4f}'.rstrip('0').rstrip('.')
    return (s if '.' in s else s + '.0') + 'F'


def java_part(out, part, parent, var):
    cubes = 'CubeListBuilder.create()' + ''.join(
        f'.texOffs({b.uv[0]}, {b.uv[1]}).addBox({f(b.pos[0])}, {f(b.pos[1])}, {f(b.pos[2])}, {f(b.size[0])}, {f(b.size[1])}, {f(b.size[2])})'
        for b in part.boxes)
    pose = (f'PartPose.offsetAndRotation({f(part.pivot[0])}, {f(part.pivot[1])}, {f(part.pivot[2])}, '
            f'{f(part.rot[0])}, {f(part.rot[1])}, {f(part.rot[2])})')
    if part.children:
        out.append(f'        PartDefinition {var} = {parent}.addOrReplaceChild("{part.name}", {cubes}, {pose});')
    else:
        out.append(f'        {parent}.addOrReplaceChild("{part.name}", {cubes}, {pose});')
    for child in part.children:
        java_part(out, child, var, child.name.replace('-', '_'))


def java(models):
    out = ['package tn.nightbeam.chronofall.client.model;', '',
           'import net.minecraft.client.model.geom.PartPose;',
           'import net.minecraft.client.model.geom.builders.CubeListBuilder;',
           'import net.minecraft.client.model.geom.builders.LayerDefinition;',
           'import net.minecraft.client.model.geom.builders.MeshDefinition;',
           'import net.minecraft.client.model.geom.builders.PartDefinition;', '',
           '/** Generated by tools/gen_models.py; edit the specs there, not this file. */',
           'public final class ModelMeshes {', '    private ModelMeshes() {', '    }']
    for name, width, height, parts in models:
        method = ''.join(w.capitalize() if i else w for i, w in enumerate(name.split('_')))
        out += ['', f'    public static LayerDefinition {method}() {{', '        MeshDefinition mesh = new MeshDefinition();',
                '        PartDefinition root = mesh.getRoot();']
        for part in parts:
            java_part(out, part, 'root', part.name)
        out += [f'        return LayerDefinition.create(mesh, {width}, {height});', '    }']
    out += ['}', '']
    return '\n'.join(out)


def main():
    models = []
    for spec in MODELS:
        name, width, height, parts = spec()
        while not layout(parts, width, height):
            if height <= width:
                height *= 2
            else:
                width *= 2
        models.append((name, width, height, parts))
    source = java(models)
    for ws in WORKSPACES:
        common = ROOT / ws / 'common' / 'src' / 'main'
        if not common.exists():
            continue
        target = common / 'java' / 'tn' / 'nightbeam' / 'chronofall' / 'client' / 'model' / 'ModelMeshes.java'
        target.parent.mkdir(parents=True, exist_ok=True)
        target.write_text(source)
        tex = common / 'resources' / 'assets' / 'chronofall' / 'textures' / 'entity'
        tex.mkdir(parents=True, exist_ok=True)
        for name, width, height, parts in models:
            img, glow = paint(name, width, height, parts)
            img.save(tex / f'{name}.png')
            glow.save(tex / f'{name}_glow.png')
    print('wrote', ', '.join(m[0] for m in models))


if __name__ == '__main__':
    main()
