import json, sys
sys.path.insert(0, '/home/user/theyear3001/tools/qa')
from client import Session
from marketing_shots import Showcase
s = Showcase(Session(display=':2'))
s.prepare()
json.dump({'ox': s.ox, 'oz': s.oz, 'gy': s.gy}, open('/tmp/claude-0/cf/stage.json', 'w'))
print(s.ox, s.oz, s.gy)
