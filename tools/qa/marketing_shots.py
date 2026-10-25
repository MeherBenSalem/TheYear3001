#!/usr/bin/env python3
"""Captures the listing screenshots from a real dev client (see marketing/README.md).

Run against a 26.3 Fabric dev server on the showcase world (level-name=showcase, level-type minecraft:normal,
level-seed SEED below, view-distance=12), with the QA client online and opped:

  python3 tools/qa/marketing_shots.py [shot...]

Each scene is set up with commands near a plains biome (the ground is flattened where machines stand); everything
the mod does in the pictures (the rift and the falling pod, the crater, laser bolts, charge beams, the hologram, the
thruster flames, the HUD, the cutter beam) is the mod running. Each shot is one unedited frame: a screen capture, or
for moments too quick for that (the pod in the sky, a laser bolt in flight) one frame of a lossless screen recording,
picked by a fixed rule (the most rift-magenta or laser-red pixels).
"""
from __future__ import annotations

import argparse
import re
import sys
import tempfile
import time
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
from client import Session, PLAYER  # noqa: E402

SEED = '7214950366214'
NS = 'chronofall'
OUT = Path(__file__).resolve().parents[2] / 'marketing' / 'images' / 'screenshots'


def item(path, flux=None):
    return f'{NS}:{path}' + (f'[minecraft:custom_data={{Flux:{flux}}}]' if flux is not None else '')


class Showcase:
    def __init__(self, s: Session):
        self.s = s
        self.ox = self.oz = self.gy = None
        self.third = 0

    def cmd(self, *c):
        return self.s.cmd(*c)

    def save(self, name, wait=1.5):
        time.sleep(wait)
        png = OUT / (name + '.png')
        self.s.shot(png)
        self.jpg(png, name)

    @staticmethod
    def jpg(png, name):
        from PIL import Image
        Image.open(png).convert('RGB').save(OUT / (name + '.jpg'), quality=90)
        if png.parent == OUT:
            png.unlink()
        print('captured', name + '.jpg', flush=True)

    def best_frame(self, name, seconds, score, fps=10, before=None):
        """Records `seconds` of the screen and keeps the frame with the highest score(image)."""
        from PIL import Image
        folder = Path(tempfile.mkdtemp(prefix='cf-burst-'))
        rec = self.s.burst(folder, seconds, fps)
        if before:
            before()
        rec.wait()
        frames = sorted(folder.glob('f*.png'))
        best = max(frames, key=lambda f: score(Image.open(f).convert('RGB')))
        self.jpg(best, name)

    def surface(self, x, z, top=140, bottom=40):
        for y in range(top, bottom, -1):
            for ground in ('minecraft:grass_block', 'minecraft:dirt', 'minecraft:sand', 'minecraft:stone'):
                if 'passed' in self.cmd(f'execute if block {x} {y} {z} {ground}'):
                    return y
        return 64

    def view(self, mode):
        """0 first person, 1 third person behind, 2 third person in front (F5 cycles)."""
        while self.third != mode:
            self.s.key('F5')
            self.third = (self.third + 1) % 3

    def prepare(self):
        for rule in ('advance_time false', 'doDaylightCycle false', 'advance_weather false', 'doWeatherCycle false',
                     'spawn_mobs false', 'doMobSpawning false', 'send_command_feedback false', 'sendCommandFeedback false'):
            self.cmd(f'gamerule {rule}')
        self.cmd('time set 1000', 'weather clear', f'gamemode creative {PLAYER}', 'difficulty normal')
        out = self.cmd('locate biome minecraft:plains')
        m = re.search(r'\[(-?\d+), (?:~|-?\d+), (-?\d+)\]', out)
        self.ox, self.oz = (int(m.group(1)), int(m.group(2))) if m else (0, 0)
        self.cmd(f'forceload add {self.ox - 48} {self.oz - 48} {self.ox + 48} {self.oz + 48}')
        self.s.tp(self.ox + 0.5, 120, self.oz + 0.5)
        time.sleep(10)
        self.gy = self.surface(self.ox, self.oz)
        # A flat 40x40 pad of grass at the surface height, cleared above, for the machines and robots.
        x0, z0, y = self.ox - 20, self.oz - 20, self.gy
        for dy in range(1, 16, 5):
            self.cmd(f'fill {x0} {y + dy} {z0} {x0 + 40} {y + dy + 4} {z0 + 40} minecraft:air')
        self.cmd(f'fill {x0} {y} {z0} {x0 + 40} {y} {z0 + 40} minecraft:grass_block',
                 f'fill {x0} {y - 3} {z0} {x0 + 40} {y - 1} {z0 + 40} minecraft:dirt')
        self.cmd('kill @e[type=!minecraft:player]')

    def reset(self, time_of_day=1000):
        self.cmd(f'kill @e[type=!minecraft:player]', f'clear {PLAYER}', f'time set {time_of_day}', f'gamemode creative {PLAYER}')
        self.view(0)
        self.s.hud(False)

    # Scenes -------------------------------------------------------------------------------------------------------

    def fabricator(self):
        """The Fabricator printing a Jetpack, its hologram above it, beside a Flux Charger."""
        self.reset(13000)
        x, y, z = self.ox, self.gy + 1, self.oz
        self.cmd(f'setblock {x} {y} {z} {NS}:fabricator[facing=south]', f'setblock {x + 2} {y} {z} {NS}:flux_charger[facing=south]',
                 f'item replace block {x + 2} {y} {z} container.0 with minecraft:coal_block 16',
                 f'setblock {x - 2} {y} {z} {NS}:rift_beacon')
        self.cmd(f'data merge block {x} {y} {z} {{Selected:3,Printing:3,Progress:120}}')
        self.s.tp(x + 0.5, y - 0.2, z + 3.2, facing=(x + 0.5, y + 0.9, z + 0.5))
        self.save('fabricator', 3)

    def fabricator_gui(self):
        """The Fabricator's screen with a recipe selected and enough ingredients for it."""
        self.reset(6000)
        x, y, z = self.ox, self.gy + 1, self.oz
        self.cmd(f'setblock {x} {y} {z} {NS}:fabricator[facing=south]', f'data merge block {x} {y} {z} {{Selected:3}}',
                 f'give {PLAYER} minecraft:blaze_rod 4')
        for stack in ('titanium_ingot 16', 'printed_circuit_board 4', 'flux_cell 6', 'central_processing_unit 2', 'rift_shard 8',
                      'electrical_conductors 12', 'scrap 32'):
            self.cmd(f'give {PLAYER} {NS}:{stack}')
        self.cmd(f'gamemode survival {PLAYER}')
        self.s.tp(x + 0.5, y, z + 2.5, facing=(x + 0.5, y + 0.5, z + 0.5))
        time.sleep(1)
        self.s.hud(True)
        self.s.click(3)
        time.sleep(1.5)
        self.save('fabricator_gui', 1)
        self.s.key('Escape')
        self.s.hud(False)

    def charger(self):
        """A Flux Charger at night beaming charge into a Drone and a Mech."""
        self.reset(14000)
        x, y, z = self.ox + 8, self.gy + 1, self.oz
        self.cmd(f'setblock {x} {y} {z} {NS}:flux_charger[facing=south]',
                 f'item replace block {x} {y} {z} container.0 with minecraft:coal_block 64',
                 f'summon {NS}:drone {x - 4}.5 {y + 1} {z + 2}.5 {{Flux:0,NoAI:1b,Rotation:[60f,0f]}}',
                 f'summon {NS}:mech {x + 4}.5 {y} {z + 1}.5 {{Flux:0,NoAI:1b,Rotation:[-120f,0f]}}')
        self.s.tp(x + 0.5, y + 1.5, z + 9.5, facing=(x + 0.5, y + 1.0, z + 0.5))
        self.save('charger', 4)

    def rift(self):
        """A Chrono Rift open in the night sky with the Chrono Pod falling out of it."""
        self.reset(16000)
        x, z = self.ox, self.oz - 30
        self.s.tp(self.ox + 0.5, self.gy + 1, self.oz + 12.5, facing=(x + 0.5, self.gy + 22, z + 0.5))
        time.sleep(2)

        def magenta(img):
            small = img.resize((240, 135))
            return sum(1 for r, g, b in small.getdata() if r > 170 and b > 150 and g < 120)

        self.best_frame('rift', 7, magenta, before=lambda: self.cmd(f'chronofall rift_at {x} {self.gy + 1} {z}'))

    def crater(self):
        """The crash site in the morning: crater, Rift Cache and its Sentinels."""
        self.cmd(f'gamemode spectator {PLAYER}', 'time set 23500')
        self.view(0)
        out = self.cmd('chronofall last_landing')
        m = re.search(r'at (-?\d+) (-?\d+) (-?\d+)', out)
        if not m:
            print('no landing yet: run the rift scene first', flush=True)
            return
        x, y, z = (int(v) for v in m.groups())
        self.cmd(f'execute as @e[type={NS}:sentinel] run data merge entity @s {{NoAI:1b}}')
        self.s.tp(x + 0.5, y + 6, z + 10.5, facing=(x + 0.5, y + 0.5, z + 0.5))
        self.save('crater', 3)
        self.cmd(f'gamemode creative {PLAYER}')

    def sentinel(self):
        """A Sentinel at night firing a laser bolt, its visor glowing."""
        self.reset(18000)
        x, y, z = self.ox - 8, self.gy + 1, self.oz
        self.cmd(f'summon {NS}:sentinel {x}.5 {y} {z}.5 {{PersistenceRequired:1b,Rotation:[90f,0f]}}',
                 f'summon minecraft:iron_golem {x - 9}.5 {y} {z}.5 {{NoAI:1b,PersistenceRequired:1b}}')
        self.cmd(f'gamemode spectator {PLAYER}')
        self.s.tp(x + 1.5, y + 1.2, z + 5.5, facing=(x - 3, y + 1.6, z + 0.5))
        time.sleep(3)

        def red(img):
            small = img.resize((240, 135))
            return sum(1 for r, g, b in small.getdata() if r > 200 and g < 110 and b < 120)

        self.best_frame('sentinel', 6, red)
        self.cmd(f'gamemode creative {PLAYER}')

    def jetpack(self):
        """Flying with the Jetpack, seen from behind: thruster flames and the flux gauge."""
        self.reset(1000)
        self.cmd(f'item replace entity {PLAYER} armor.chest with {item("jetpack", 12000)}', f'gamemode survival {PLAYER}')
        self.s.tp(self.ox + 0.5, self.gy + 1, self.oz + 0.5, yaw=200, pitch=10)
        time.sleep(1.5)
        self.s.hud(True)
        self.view(1)
        self.s.focus()
        self.s.xdo('keydown', 'space')
        time.sleep(1.6)
        self.save('jetpack', 0)
        self.s.xdo('keyup', 'space')
        time.sleep(3)
        self.view(0)
        self.cmd(f'gamemode creative {PLAYER}')

    def skiff(self):
        """Flying the Hover Skiff over the plains, seen from behind, with its HUD."""
        self.reset(1000)
        x, y, z = self.ox, self.gy + 1, self.oz + 10
        self.cmd(f'summon {NS}:skiff {x}.5 {y} {z}.5 {{Flux:30000,Rotation:[180f,0f]}}')
        self.s.tp(x + 0.5, y, z + 3.5, facing=(x + 0.5, y + 0.4, z + 0.5))
        time.sleep(1)
        self.s.click(3)
        time.sleep(1.5)
        self.s.hud(True)
        self.view(1)
        self.s.focus()
        self.s.xdo('keydown', 'w')
        self.s.xdo('keydown', 'space')
        time.sleep(1.8)
        self.s.xdo('keyup', 'space')
        self.s.xdo('keydown', 'a')
        time.sleep(0.6)
        self.save('skiff', 0)
        self.s.xdo('keyup', 'a')
        self.s.xdo('keyup', 'w')
        time.sleep(2)
        self.view(0)
        self.s.key('shift', delay=0.6)

    def mech(self):
        """Riding the Mech towards Sentinels, seen from the front."""
        self.reset(12500)
        x, y, z = self.ox, self.gy + 1, self.oz - 6
        self.cmd(f'summon {NS}:mech {x}.5 {y} {z}.5 {{Flux:30000,Rotation:[0f,0f]}}')
        for dx in (-3, 3):
            self.cmd(f'summon {NS}:sentinel {x + dx}.5 {y} {z + 12}.5 {{NoAI:1b,PersistenceRequired:1b,Rotation:[180f,0f]}}')
        self.s.tp(x + 0.5, y, z - 2.5, facing=(x + 0.5, y + 1.0, z + 0.5))
        time.sleep(1)
        self.s.click(3)
        time.sleep(1.5)
        self.s.hud(True)
        self.view(2)
        self.s.focus()
        self.s.xdo('keydown', 'w')
        time.sleep(1.4)
        self.save('mech', 0)
        self.s.xdo('keyup', 'w')
        time.sleep(0.5)
        self.view(0)
        self.s.key('shift', delay=0.6)

    def drone(self):
        """A Drone with its Lamp chip lighting the way at night, beside its owner."""
        self.reset(18000)
        self.cmd(f'gamemode survival {PLAYER}')
        x, y, z = self.ox, self.gy + 1, self.oz
        self.s.tp(x + 0.5, y, z + 0.5, yaw=180, pitch=0)
        self.cmd(f'item replace entity {PLAYER} weapon.mainhand with {NS}:drone')
        time.sleep(1)
        self.s.tp(x + 0.5, y, z + 0.5, yaw=180, pitch=40)
        time.sleep(1)
        self.s.click(3)
        time.sleep(1)
        self.cmd(f'execute as @e[type={NS}:drone,limit=1] run data merge entity @s {{Flux:6000,lamp_chip:1b,guard_chip:1b}}')
        self.s.tp(x + 0.5, y, z + 0.5, yaw=180, pitch=0)
        time.sleep(4)
        self.view(2)
        self.save('drone', 2)
        self.view(0)
        self.cmd(f'gamemode creative {PLAYER}')

    def gate(self):
        """Two linked Rift Gates with their particles, the player stepping onto one."""
        self.reset(15000)
        x, y, z = self.ox + 6, self.gy + 1, self.oz + 8
        self.cmd(f'setblock {x} {y} {z} {NS}:rift_gate', f'setblock {x + 6} {y} {z} {NS}:rift_gate')
        self.cmd(f'item replace entity {PLAYER} weapon.mainhand with {NS}:rift_tuner')
        self.s.tp(x + 0.5, y, z + 2.5, facing=(x + 0.5, y + 0.1, z + 0.5))
        time.sleep(1)
        self.s.click(3)
        time.sleep(0.8)
        self.s.tp(x + 6.5, y, z + 2.5, facing=(x + 6.5, y + 0.1, z + 0.5))
        time.sleep(1)
        self.s.click(3)
        time.sleep(0.8)
        self.cmd(f'clear {PLAYER}', f'gamemode spectator {PLAYER}')
        self.s.tp(x + 3.5, y + 2.2, z + 6.5, facing=(x + 3.5, y + 0.4, z + 0.5))
        self.save('gate', 3)
        self.cmd(f'gamemode creative {PLAYER}')

    def cutter(self):
        """The Plasma Cutter's beam cutting into a stone wall."""
        self.reset(13500)
        x, y, z = self.ox - 6, self.gy + 1, self.oz - 12
        self.cmd(f'fill {x - 3} {y} {z} {x + 3} {y + 4} {z} minecraft:stone',
                 f'fill {x - 1} {y + 1} {z} {x + 1} {y + 2} {z} {NS}:titanium_ore')
        self.cmd(f'item replace entity {PLAYER} weapon.mainhand with {item("plasma_cutter", 20000)}', f'gamemode survival {PLAYER}')
        self.s.tp(x + 0.5, y, z + 7.5, facing=(x + 0.5, y + 1.5, z + 0.5))
        time.sleep(1.5)
        self.s.hud(True)
        self.s.focus()
        self.s.xdo('mousedown', '3')
        time.sleep(0.7)
        self.save('cutter', 0)
        self.s.xdo('mouseup', '3')
        self.cmd(f'gamemode creative {PLAYER}')


SCENES = ['fabricator', 'fabricator_gui', 'charger', 'rift', 'crater', 'sentinel', 'jetpack', 'skiff', 'mech', 'drone', 'gate', 'cutter']


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument('shots', nargs='*', default=SCENES)
    args = ap.parse_args()
    OUT.mkdir(parents=True, exist_ok=True)
    show = Showcase(Session())
    show.prepare()
    for name in args.shots:
        getattr(show, name)()
    show.s.hud(True)


if __name__ == '__main__':
    main()
