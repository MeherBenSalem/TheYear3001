#!/bin/bash
# client.sh <clock factor, 1 = normal> -- relaunches the Chronofall dev client in portrait, optionally on a slowed clock
F=${1:-1}
cd /home/user/theyear3001/1.21.1/fabric/runs/client
mapfile -t CMD < /tmp/claude-0/cf/client_cmd.txt
n=${#CMD[@]}; CMD[$n-3]=1080; CMD[$n-1]=1920
export DISPLAY=:2 LIBGL_ALWAYS_SOFTWARE=1 GALLIUM_DRIVER=llvmpipe MESA_GL_VERSION_OVERRIDE=4.5 MESA_GLSL_VERSION_OVERRIDE=450
if [ "$F" != 1 ]; then
  export FAKETIME="+0 x$F" FAKETIME_DONT_FAKE_MONOTONIC=0 LD_PRELOAD=/usr/lib/x86_64-linux-gnu/faketime/libfaketimeMT.so.1
fi
exec "${CMD[@]}"
