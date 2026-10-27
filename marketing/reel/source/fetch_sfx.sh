#!/bin/bash
# Downloads the reel's sound effects from Mixkit (Mixkit Sound Effects Free License: free for commercial use in
# videos, no attribution required) into $1 (default ./sfxlib), as WAV. sfx_pro.py reads them from there.
# A few files are only served as MP3 previews; those are converted to WAV.
set -e
D=${1:-sfxlib}; mkdir -p "$D"
for id in 3024 1703 788 1022 1492 790 2908 2687 2297 1714 1687 1686 1681 3110 2952 3108 2352 3184 2530 3163 2738 2733 1670 2743 3165 1417 3057 904 2598 1678 3113 2614 2600 1144 2901 2950 1481; do
  f="$D/$id.wav"; [ -s "$f" ] && continue
  curl -sf --max-time 60 -o "$f" "https://assets.mixkit.co/active_storage/sfx/$id/$id.wav" || rm -f "$f"
  if ! head -c 4 "$f" 2>/dev/null | grep -q RIFF; then
    curl -sf --max-time 60 -o "$D/$id.mp3" "https://assets.mixkit.co/active_storage/sfx/$id/$id-preview.mp3"
    ffmpeg -loglevel error -y -i "$D/$id.mp3" -c:a pcm_s16le "$f" && rm "$D/$id.mp3"
  fi
done
ls "$D" | wc -l
