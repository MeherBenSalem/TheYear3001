import sys
sys.path.insert(0, '/home/user/theyear3001/tools/qa')
from rcon import Rcon
r = Rcon(port=25575, password='qa')
for c in sys.argv[1:]:
    out = r.command(c)
    if out.strip():
        print(out)
