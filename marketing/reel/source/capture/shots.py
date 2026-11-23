#!/usr/bin/env python3
"""Records the Chronofall reel footage on a dev client whose clock (and the server's) runs 10x slow.

  python3 shots.py <shot> [<shot>...]

Every wait below is in game seconds and is multiplied by SLOW. The screen is grabbed at 15 fps, which catches
every frame the software renderer draws (about 58 per game second). Stage: the flattened 41x41 plains pad around
(32, 63, -64) on the showcase seed; the open coast lies south, wooded hills north.
"""
import json
import math
import os
import subprocess
import sys
import time

sys.path.insert(0, '/home/user/theyear3001/tools/qa')
from rcon import Rcon  # noqa: E402

SLOW = 10.0
P = 'Builder'
NS = 'chronofall'
STATE = '/tmp/claude-0/cf/rigstate.json'
OX, OZ, GY = 32, -64, 63   # pad centre and grass level


def item(path, flux=None):
    return f'{NS}:{path}' + (f'[minecraft:custom_data={{Flux:{flux}}}]' if flux is not None else '')


class Rig:
    def __init__(self):
        self.r = Rcon(port=25575, password='qa', timeout=600)
        self.env = dict(os.environ, DISPLAY=':2')
        self.st = json.load(open(STATE)) if os.path.exists(STATE) else {'view': 0, 'hud_hidden': True}
        self.capproc = None

    def save(self):
        json.dump(self.st, open(STATE, 'w'))

    def cmd(self, *cs):
        out = ''
        for c in cs:
            out = self.r.command(c)
        return out

    def tp(self, x, y, z, yaw=0.0, pitch=0.0, facing=None, eye=1.62):
        if facing:
            dx, dy, dz = facing[0] - x, facing[1] - (y + eye), facing[2] - z
            yaw = math.degrees(math.atan2(-dx, dz))
            pitch = -math.degrees(math.atan2(dy, math.hypot(dx, dz)))
        self.cmd(f'tp {P} {x} {y} {z} {yaw:.2f} {pitch:.2f}')

    @staticmethod
    def g(seconds):
        time.sleep(seconds * SLOW)

    def x(self, *args):
        subprocess.run(['xdotool', *args], env=self.env, check=False)

    def focus(self):
        out = subprocess.run(['xdotool', 'search', '--name', 'Minecraft'], env=self.env, capture_output=True, text=True).stdout.split()
        if out:
            self.x('windowfocus', '--sync', out[-1])

    def key(self, k):
        self.x('key', k)

    def view(self, mode):
        """0 first person, 1 behind, 2 in front (F5 cycles)."""
        while self.st['view'] != mode:
            self.key('F5')
            self.st['view'] = (self.st['view'] + 1) % 3
            time.sleep(1.0)
        self.save()

    def hud(self, visible):
        if visible == self.st['hud_hidden']:
            self.key('F1')
            self.st['hud_hidden'] = not visible
            time.sleep(1.0)
        self.save()

    def cap(self, name, game_seconds):
        self.capproc = subprocess.Popen(['/tmp/claude-0/cf/cap.sh', name, str(game_seconds * SLOW)])
        time.sleep(1.0)  # ffmpeg up and grabbing

    def wait_cap(self):
        if self.capproc:
            self.capproc.wait()
            self.capproc = None

    def pad(self):
        subprocess.run(['/tmp/claude-0/cf/pad.sh'], check=True)

    def reset(self, tod, mode='spectator'):
        self.cmd(f'kill @e[type=!minecraft:player]', f'clear {P}', f'effect clear {P}', f'time set {tod}',
                 f'gamemode {mode} {P}')
        self.view(0)
        self.hud(False)
        self.focus()


# ------------------------------------------------------------------ shots
def hook_sky(s):
    """Twilight sky: the rift opens at the top of the frame and the pod falls the full height of it."""
    s.pad()
    s.reset(12700)
    s.tp(OX + 0.5, GY + 0.6, OZ + 9.5, 180, -36)
    s.g(2.5)
    s.cap('hook_sky', 6.5)
    s.g(0.3)
    s.cmd(f'chronofall rift_at {OX} {GY} {OZ - 5}')
    s.wait_cap()


def hook_ground(s):
    """Low angle beside the landing spot: the pod drops into the top of the frame and slams down."""
    s.pad()
    s.reset(12700)
    lx, lz = OX, OZ - 5
    s.tp(lx + 5.5, GY + 0.3, lz + 5.5, facing=(lx + 0.5, GY + 2.6, lz + 0.5))
    s.g(2.5)
    s.cap('hook_ground', 8.5)
    s.g(0.3)
    s.cmd(f'chronofall rift_at {lx} {GY} {lz}')
    s.wait_cap()


def up_rift(s):
    """Straight up under the rift as it tears open, the pod coming down at the camera."""
    s.pad()
    s.reset(13100)
    s.tp(OX + 0.5, GY + 0.2, OZ + 1.5, 180, -74)
    s.g(2.5)
    s.cap('up_rift', 6.0)
    s.g(0.3)
    s.cmd(f'chronofall rift_at {OX} {GY} {OZ - 5}')
    s.wait_cap()


def crash2(s):
    """Three-quarter view from above: impact, crater, the Rift Cache and its Sentinels."""
    s.pad()
    s.reset(13300)
    lx, lz = OX - 4, OZ - 2
    s.tp(lx + 8.5, GY + 6.5, lz + 9.5, facing=(lx + 0.5, GY + 0.5, lz + 0.5))
    s.g(2.5)
    s.cap('crash2', 8.0)
    s.g(0.3)
    s.cmd(f'chronofall rift_at {lx} {GY} {lz}')
    s.wait_cap()


def sentinel(s):
    """Dusk: Sentinels face the camera player and fire laser bolts straight at the lens."""
    s.pad()
    s.reset(13050, 'survival')
    s.cmd(f'effect give {P} minecraft:resistance infinite 255 true', f'effect give {P} minecraft:regeneration infinite 255 true',
          f'attribute {P} minecraft:generic.knockback_resistance base set 1')
    x, y, z = OX, GY + 1, OZ + 8
    for dx, dz in ((-1.5, -7), (2, -8.5)):
        s.cmd(f'summon {NS}:sentinel {x + dx} {y} {z + dz} {{PersistenceRequired:1b,Rotation:[0f,0f]}}')
    s.tp(x + 0.5, y, z + 0.5, facing=(x + 0.5, y + 1.8, z - 7.5))
    s.g(1.0)
    s.cap('sentinel', 7.0)
    s.wait_cap()
    s.cmd(f'kill @e[type={NS}:sentinel]', f'attribute {P} minecraft:generic.knockback_resistance base set 0')


def cache(s):
    """Cracking a Rift Cache open: salvage bursts out."""
    s.pad()
    s.reset(13600, 'survival')
    s.cmd(f'setblock {OX} {GY + 1} {OZ} {NS}:rift_cache')
    s.tp(OX + 0.5, GY + 1, OZ + 2.6, facing=(OX + 0.5, GY + 1.4, OZ + 0.5))
    s.g(1.5)
    s.cap('cache', 3.5)
    s.g(0.6)
    s.x('click', '3')
    s.wait_cap()


def fabricator(s):
    """The Fabricator printing a Jetpack, its hologram spinning, beside a Flux Charger."""
    s.pad()
    s.reset(13300)
    x, y, z = OX, GY + 1, OZ
    s.cmd(f'setblock {x} {y} {z} {NS}:fabricator[facing=south]', f'setblock {x + 2} {y} {z} {NS}:flux_charger[facing=south]',
          f'item replace block {x + 2} {y} {z} container.0 with minecraft:coal_block 16',
          f'setblock {x - 2} {y} {z} {NS}:rift_beacon')
    s.cmd(f'data merge block {x} {y} {z} {{Selected:3,Printing:3,Progress:40}}')
    s.tp(x + 0.5, y - 0.4, z + 2.9, facing=(x + 0.5, y + 1.1, z + 0.5))
    s.g(1.5)
    s.cap('fabricator', 4.5)
    s.wait_cap()


def jetpack(s):
    """Jetpack take-off at sunset, from behind: thruster flames, then forward flight."""
    s.pad()
    s.reset(12300, 'survival')
    s.cmd(f'item replace entity {P} armor.chest with {item("jetpack", 12000)}')
    s.tp(OX + 0.5, GY + 1, OZ + 8.5, 90, 6)
    s.view(1)
    s.g(1.5)
    s.cap('jetpack', 6.5)
    s.g(0.5)
    s.x('keydown', 'space')
    s.g(1.3)
    s.x('keydown', 'w')
    s.g(3.0)
    s.x('keyup', 'w')
    s.x('keyup', 'space')
    s.wait_cap()
    s.view(0)


def skiff(s):
    """Hover Skiff over the coast at sunset, from behind: climb, then a banked turn."""
    s.pad()
    s.reset(12300, 'creative')
    x, y, z = OX, GY + 1, OZ + 14
    s.cmd(f'summon {NS}:skiff {x}.5 {y} {z}.5 {{Flux:30000,Rotation:[0f,0f]}}')
    s.tp(x + 0.5, y, z - 2.5, facing=(x + 0.5, y + 0.4, z + 0.5))
    s.g(0.8)
    s.x('click', '3')
    s.g(1.0)
    s.view(1)
    s.g(0.5)
    s.cap('skiff', 7.5)
    s.g(0.3)
    s.x('keydown', 'w')
    s.g(1.0)
    s.x('keydown', 'space')
    s.g(1.6)
    s.x('keyup', 'space')
    s.x('keydown', 'a')
    s.g(1.8)
    s.x('keyup', 'a')
    s.x('keydown', 'd')
    s.g(1.4)
    s.x('keyup', 'd')
    s.x('keyup', 'w')
    s.wait_cap()
    s.view(0)
    s.x('keydown', 'shift')
    s.g(0.2)
    s.x('keyup', 'shift')


def drone(s):
    """Night: a Drone with guard and lamp chips shooting zombies beside its owner."""
    s.pad()
    s.reset(13800, 'survival')
    s.cmd(f'effect give {P} minecraft:resistance infinite 255 true')
    x, y, z = OX, GY + 1, OZ + 6
    s.tp(x + 0.5, y, z + 0.5, 180, 40)
    s.cmd(f'item replace entity {P} weapon.mainhand with {NS}:drone')
    s.g(0.6)
    s.x('click', '3')
    s.g(0.8)
    s.cmd(f'execute as @e[type={NS}:drone,limit=1] run data merge entity @s {{Flux:6000,lamp_chip:1b,guard_chip:1b}}')
    s.tp(x + 0.5, y, z + 0.5, 180, 4)
    s.view(1)
    s.g(1.5)
    s.cap('drone', 6.5)
    s.g(0.4)
    for dx, dz in ((-2, -7), (2, -8), (0, -9)):
        s.cmd(f'summon minecraft:zombie {x + dx}.5 {y} {z + dz}.5 {{NoAI:1b,PersistenceRequired:1b,Rotation:[0f,0f]}}')
    s.wait_cap()
    s.view(0)


def mech(s):
    """Riding the Mech at dusk towards Sentinels, from the front: walk, leap, ground slam among them."""
    s.pad()
    s.reset(12900, 'creative')
    x, y, z = OX, GY + 1, OZ + 8
    s.cmd(f'summon {NS}:mech {x}.5 {y} {z}.5 {{Flux:30000,Rotation:[180f,0f]}}')
    for dx, dz in ((-2, -6), (2, -7), (0, -9)):
        s.cmd(f'summon {NS}:sentinel {x + dx}.5 {y} {z + dz}.5 {{NoAI:1b,PersistenceRequired:1b,Rotation:[0f,0f]}}')
    s.tp(x + 0.5, y, z + 3.5, facing=(x + 0.5, y + 1.0, z + 0.5))
    s.g(0.8)
    s.x('click', '3')
    s.g(1.0)
    s.view(2)
    s.g(0.5)
    s.cap('mech', 7.0)
    s.g(0.3)
    s.x('keydown', 'w')
    s.g(1.2)
    s.x('keydown', 'space')
    s.g(0.15)
    s.x('keyup', 'space')
    s.g(2.4)
    s.x('keyup', 'w')
    s.g(0.4)
    s.x('keydown', 'space')
    s.g(0.15)
    s.x('keyup', 'space')
    s.wait_cap()
    s.view(0)
    s.x('keydown', 'shift')
    s.g(0.2)
    s.x('keyup', 'shift')


def cutter(s):
    """Night: the Plasma Cutter beam sweeping across a wall of stone and titanium ore."""
    s.pad()
    s.reset(13800, 'survival')
    x, y, z = OX - 2, GY + 1, OZ - 8
    s.cmd(f'fill {x - 4} {y} {z} {x + 4} {y + 5} {z} minecraft:stone',
          f'fill {x - 2} {y + 1} {z} {x + 2} {y + 3} {z} {NS}:titanium_ore')
    s.cmd(f'item replace entity {P} weapon.mainhand with {item("plasma_cutter", 20000)}')
    s.tp(x + 0.5, y, z + 6.5, facing=(x - 1.5, y + 2.5, z + 0.5))
    s.hud(True)
    s.g(1.5)
    s.cap('cutter', 4.0)
    s.g(0.4)
    s.x('mousedown', '3')
    for _ in range(60):  # a slow sweep left to right, a few pixels per real 0.5 s
        s.x('mousemove_relative', '--', '3', '0')
        time.sleep(0.5)
    s.x('mouseup', '3')
    s.wait_cap()
    s.hud(False)


def rifle(s):
    """Night: Pulse Rifle bolts fired at Sentinels."""
    s.pad()
    s.reset(13800, 'survival')
    x, y, z = OX, GY + 1, OZ + 8
    for dx, dz in ((-2, -11), (2, -12)):
        s.cmd(f'summon {NS}:sentinel {x + dx}.5 {y} {z + dz}.5 {{NoAI:1b,PersistenceRequired:1b,Rotation:[0f,0f]}}')
    s.cmd(f'item replace entity {P} weapon.mainhand with {item("pulse_rifle", 20000)}')
    s.tp(x + 0.5, y, z + 0.5, facing=(x - 2 + 0.5, y + 1.3, z - 11 + 0.5))
    s.hud(True)
    s.g(1.5)
    s.cap('rifle', 4.0)
    s.g(0.4)
    s.x('mousedown', '3')
    for _ in range(50):
        s.x('mousemove_relative', '--', '2', '0')
        time.sleep(0.5)
    s.x('mouseup', '3')
    s.wait_cap()
    s.hud(False)


def gate(s):
    """Two linked Rift Gates: the player steps on one, the rift spirals up and they arrive at the other."""
    s.pad()
    s.reset(13300, 'survival')
    a = (OX - 6, GY + 1, OZ + 4)
    b = (OX + 14, GY + 1, OZ - 14)
    s.cmd(f'setblock {a[0]} {a[1]} {a[2]} {NS}:rift_gate', f'setblock {b[0]} {b[1]} {b[2]} {NS}:rift_gate')
    s.cmd(f'item replace entity {P} weapon.mainhand with {NS}:rift_tuner')
    for gx, gy, gz in (a, b):
        s.tp(gx + 0.5, gy, gz + 2.5, facing=(gx + 0.5, gy + 0.1, gz + 0.5))
        s.g(0.8)
        s.x('click', '3')
        s.g(0.8)
    s.cmd(f'clear {P}')
    s.tp(a[0] + 0.5, a[1] + 0.1, a[2] + 3.5, 180, 8)
    s.view(1)
    s.g(1.5)
    s.cap('gate', 5.5)
    s.g(0.3)
    s.x('keydown', 'w')
    s.g(0.55)
    s.x('keyup', 'w')
    s.wait_cap()
    s.view(0)


def charger(s):
    """Dusk: a Flux Charger beaming charge into a Drone, a Mech and a Skiff, no cables."""
    s.pad()
    s.reset(13150)
    x, y, z = OX, GY + 1, OZ
    s.cmd(f'setblock {x} {y} {z} {NS}:flux_charger[facing=south]',
          f'item replace block {x} {y} {z} container.0 with minecraft:coal_block 64',
          f'summon {NS}:drone {x - 2}.5 {y + 1.3} {z + 1}.5 {{Flux:0,NoAI:1b,Rotation:[30f,0f]}}',
          f'summon {NS}:mech {x + 3}.5 {y} {z - 1}.5 {{Flux:0,NoAI:1b,Rotation:[-150f,0f]}}',
          f'summon {NS}:skiff {x - 3}.5 {y} {z - 3}.5 {{Flux:0,Rotation:[135f,0f]}}')
    s.tp(x + 0.5, y + 0.3, z + 4.6, facing=(x + 0.3, y + 1.0, z - 0.5))
    s.g(1.5)
    s.cap('charger', 4.5)
    s.wait_cap()


def mech2(s):
    """The Mech from behind at dusk: walk up to the Sentinels, leap, slam down among them."""
    s.pad()
    s.reset(12900, 'creative')
    x, y, z = OX, GY + 1, OZ + 8
    s.cmd(f'summon {NS}:mech {x}.5 {y} {z}.5 {{Flux:30000,Rotation:[180f,0f]}}')
    for dx, dz in ((-1.5, -5), (1.5, -5.5), (0, -7)):
        s.cmd(f'summon {NS}:sentinel {x + dx} {y} {z + dz} {{NoAI:1b,PersistenceRequired:1b,Rotation:[0f,0f]}}')
    s.tp(x + 0.5, y, z + 3.5, facing=(x + 0.5, y + 1.0, z + 0.5))
    s.g(0.8)
    s.x('click', '3')
    s.g(1.0)
    s.view(1)
    s.g(0.5)
    s.cap('mech2', 6.0)
    s.g(0.3)
    s.x('keydown', 'w')
    s.g(0.9)
    s.x('keydown', 'space')
    s.g(0.15)
    s.x('keyup', 'space')
    s.g(1.6)
    s.x('keyup', 'w')
    s.wait_cap()
    s.view(0)
    s.x('keydown', 'shift')
    s.g(0.2)
    s.x('keyup', 'shift')


SHOTS = {f.__name__: f for f in (hook_sky, hook_ground, up_rift, crash2, sentinel, cache, fabricator, jetpack, skiff,
                                 drone, mech, cutter, rifle, gate, charger, mech2)}

if __name__ == '__main__':
    rig = Rig()
    for name in sys.argv[1:]:
        t0 = time.time()
        SHOTS[name](rig)
        print(f'{name} done in {time.time() - t0:.0f}s', flush=True)
