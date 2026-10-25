#!/usr/bin/env bash
# Runs chronofall_checks.py (server and client checks) on a fresh world for each build:
#   tools/qa/run_matrix.sh [ws:loader ...]     (default: all ten)
# Builds with a world saved by Robotica 1.0.1 (tools/qa/fixtures/legacy-<ws>.zip, see the case below) load it and run the
# fixture checks.
# Results go to $QA_LOGS/results/<ws>-<loader>.txt; a summary line per build is printed.
set -u
ROOT=$(cd "$(dirname "$0")/../.." && pwd)
LOGS=${QA_LOGS:-/tmp/cf-qa}
mkdir -p "$LOGS/results"
BUILDS=${*:-"1.20.1:fabric 1.20.1:forge 1.21.1:fabric 1.21.1:neoforge 26.1.2:fabric 26.1.2:neoforge 26.2:fabric 26.2:neoforge 26.3:fabric 26.3:neoforge"}
QA="$ROOT/tools/qa"

wait_for() {  # file, pattern, seconds
  local deadline=$((SECONDS + $3))
  while [ $SECONDS -lt $deadline ]; do
    grep -qE "$2" "$1" 2>/dev/null && return 0
    sleep 5
  done
  return 1
}

for build in $BUILDS; do
  ws=${build%%:*}; loader=${build##*:}
  out="$LOGS/results/$ws-$loader.txt"
  "$QA/devrun.sh" stop-client; "$QA/devrun.sh" stop-server; sleep 10
  fixture="$QA/fixtures/legacy-$ws.zip"
  # 26.1.2 has no Robotica release: it upgrades the 1.21.1 world, the way a player moving to 26.1.2 would. 26.3 likewise
  # upgrades the 26.2 world.
  case $ws in 26.1.2) fixture="$QA/fixtures/legacy-1.21.1.zip";; 26.3) fixture="$QA/fixtures/legacy-26.2.zip";; esac
  extra=""
  if [ -f "$fixture" ]; then
    QA_FIXTURE="$fixture" "$QA/devrun.sh" prepare-server "$ws" "$loader"
    extra="--fixture"
  else
    "$QA/devrun.sh" prepare-server "$ws" "$loader"
  fi
  rm -f "$LOGS/$loader-server.log" "$LOGS/$loader-client.log"
  "$QA/devrun.sh" start-server "$ws" "$loader"
  if ! wait_for "$LOGS/$loader-server.log" 'Done \(|BUILD FAILED|Exception in thread|Failed to start the minecraft server' 1800 \
      || ! grep -q 'Done (' "$LOGS/$loader-server.log"; then
    echo "$ws $loader: SERVER DID NOT START" | tee "$out"; continue
  fi
  "$QA/devrun.sh" start-client "$ws" "$loader"
  if ! wait_for "$LOGS/$loader-server.log" 'Builder joined the game' 2400; then
    echo "$ws $loader: CLIENT DID NOT JOIN" | tee "$out"
    python3 "$QA/chronofall_checks.py" --log "$LOGS/$loader-server.log" --ws "$ws" --loader "$loader" $extra >> "$out" 2>&1
  else
    sleep 15
    python3 "$QA/chronofall_checks.py" --log "$LOGS/$loader-server.log" --ws "$ws" --loader "$loader" --client $extra \
      --shots "$LOGS/shots/$ws-$loader" > "$out" 2>&1
  fi
  cp "$LOGS/$loader-server.log" "$LOGS/results/$ws-$loader-server.log"
  cp "$LOGS/$loader-client.log" "$LOGS/results/$ws-$loader-client.log" 2>/dev/null
  echo "$ws $loader: $(tail -n 1 "$out")"
done
"$QA/devrun.sh" stop-client; "$QA/devrun.sh" stop-server
