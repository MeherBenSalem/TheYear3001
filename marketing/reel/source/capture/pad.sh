#!/bin/bash
# pad.sh -- restores the flat 41x41 grass stage (craters from earlier takes, blocks, items, mobs)
R="python3 /tmp/claude-0/cf/rc.py"
$R "kill @e[type=!minecraft:player]" \
   "fill 12 58 -84 52 62 -44 minecraft:dirt" "fill 12 63 -84 52 63 -44 minecraft:grass_block" \
   "fill 12 64 -84 52 71 -44 minecraft:air" "fill 12 72 -84 52 79 -44 minecraft:air" "kill @e[type=minecraft:item]" > /dev/null
