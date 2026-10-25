// Renders the listing artwork in marketing/images/ from real in-game captures (marketing/images/screenshots/) and the
// bundled OFL fonts. Usage (from the repository root): node marketing/source/render.cjs [name...]
// Requires the `playwright` package with a Chromium build available (NODE_PATH="$(npm root -g)" if installed globally).
const path = require('path');
const fs = require('fs');
const { chromium } = require('playwright');

// The release and the supported Minecraft versions. Adding a version is one line here
// (plus one row in the download table of curseforge/description.md).
const RELEASE = { version: '2.0', name: 'Impact' };
const VERSIONS = [
  { minecraft: '1.20.1', loaders: ['Fabric', 'Forge'] },
  { minecraft: '1.21.1', loaders: ['Fabric', 'NeoForge'] },
  { minecraft: '26.1.2', loaders: ['Fabric', 'NeoForge'] },
  { minecraft: '26.2', loaders: ['Fabric', 'NeoForge'] },
  { minecraft: '26.3', loaders: ['Fabric', 'NeoForge'] },
];
const LOADERS = ['Fabric', 'Forge', 'NeoForge'].filter((loader) => VERSIONS.some((v) => v.loaders.includes(loader)));

// The palette: night navy, flux cyan (anything powered), rift magenta (anything from the rift), thruster amber (heat).
const C = { night: '#070b1a', deep: '#0f1733', steel: '#1c2747', ink: '#e8f1ff', flux: '#38f3ff', rift: '#ff3fd2', amber: '#ffb547', red: '#ff5a6e' };

const root = path.resolve(__dirname, '..', '..');
const out = path.join(root, 'marketing', 'images');
const shots = path.join(out, 'screenshots');
const fonts = path.join(__dirname, 'fonts');
const url = (file) => 'file://' + file;
const shot = (name) => url(path.join(shots, name));

const css = `
@font-face { font-family: Display; font-weight: 400 900; src: url('${url(path.join(fonts, 'orbitron.woff2'))}'); }
@font-face { font-family: Body; font-weight: 100 900; src: url('${url(path.join(fonts, 'exo2.woff2'))}'); }
* { box-sizing: border-box; }
html, body { margin: 0; background: ${C.night}; color: ${C.ink}; font-family: Body, sans-serif; font-weight: 600; }
.stage { position: relative; overflow: hidden;
         background: radial-gradient(ellipse at 88% -10%, rgba(255,63,210,.22) 0%, rgba(255,63,210,0) 45%),
                     radial-gradient(ellipse at 0% 110%, rgba(56,243,255,.16) 0%, rgba(56,243,255,0) 50%),
                     linear-gradient(160deg, ${C.deep} 0%, ${C.night} 70%); }
.display { font-family: Display, sans-serif; font-weight: 900; letter-spacing: .02em; text-transform: uppercase; }
.title { background: linear-gradient(180deg, #ffffff 0%, #bdfaff 40%, ${C.flux} 70%, #1a9fd0 100%);
         -webkit-background-clip: text; background-clip: text; color: transparent; }
.shadow { filter: drop-shadow(0 4px 0 rgba(3,6,16,.9)) drop-shadow(0 0 26px rgba(56,243,255,.35)); }
.kicker { font-family: Display, sans-serif; font-weight: 700; color: ${C.rift}; letter-spacing: .22em; font-size: 20px; text-transform: uppercase; }
.grid { position: absolute; inset: 0; opacity: .08;
        background-image: linear-gradient(rgba(56,243,255,.9) 1px, transparent 1px), linear-gradient(90deg, rgba(56,243,255,.9) 1px, transparent 1px);
        background-size: 48px 48px; -webkit-mask-image: linear-gradient(90deg, #000 0%, transparent 70%); mask-image: linear-gradient(90deg, #000 0%, transparent 70%); }
.rift { position: absolute; width: 640px; height: 640px; right: -210px; top: -300px; border-radius: 50%;
        background: radial-gradient(circle, rgba(255,63,210,.45) 0%, rgba(255,63,210,.08) 50%, rgba(255,63,210,0) 68%); }
.backdrop { position: absolute; inset: -40px; background-size: cover; background-position: center;
            filter: blur(14px) brightness(.3) saturate(1.25); }
.frame { position: relative; overflow: hidden; background: #000; border-radius: 14px; border: 3px solid ${C.flux};
         box-shadow: 0 0 0 2px rgba(3,6,16,.9), 0 0 32px rgba(56,243,255,.35), 0 26px 60px rgba(0,0,0,.65); }
.frame::after { content: ''; position: absolute; inset: 0; pointer-events: none;
                background: linear-gradient(135deg, rgba(255,255,255,.08) 0%, rgba(255,255,255,0) 30%); }
.frame > img, .frame > div > img { display: block; }
.label { position: absolute; display: inline-flex; align-items: center; gap: 10px; padding: 7px 16px 6px; border-radius: 6px;
         font-family: Display, sans-serif; font-weight: 700; font-size: 15px; letter-spacing: .12em; text-transform: uppercase;
         color: ${C.night}; background: ${C.ink}; box-shadow: 0 6px 18px rgba(0,0,0,.45); }
.dot { display: inline-block; width: 10px; height: 10px; transform: rotate(45deg); }
.bar { height: 4px; border-radius: 2px; background: linear-gradient(90deg, var(--c, ${C.flux}) 0%, rgba(56,243,255,0) 100%); }
ul.points { list-style: none; padding: 0; margin: 0; }
ul.points li { position: relative; padding-left: 34px; margin: 0 0 15px; font-size: 26px; line-height: 1.3; color: #d6e2f7; }
ul.points li::before { content: ''; position: absolute; left: 4px; top: 11px; width: 12px; height: 12px; transform: rotate(45deg);
                       background: var(--accent, ${C.flux}); box-shadow: 0 0 12px var(--accent, ${C.flux}); }
b.hi { color: #fff; font-weight: 800; }
.stats { display: flex; margin-top: 22px; border-radius: 12px; overflow: hidden; background: rgba(7,11,26,.9);
         border: 2px solid rgba(56,243,255,.35); box-shadow: 0 18px 50px rgba(0,0,0,.5); }
.stats > div { flex: 1; padding: 12px 20px 11px; border-left: 2px solid rgba(56,243,255,.15); }
.stats > div:first-child { border-left: 0; }
.stats .t { font-family: Display, sans-serif; font-weight: 700; font-size: 13px; letter-spacing: .18em; color: #8ea6cf; text-transform: uppercase; }
.stats .v { font-family: Display, sans-serif; font-weight: 900; font-size: 28px; color: ${C.ink}; margin-top: 4px; white-space: nowrap; }
.pill { display: inline-block; border-radius: 6px; padding: 8px 18px 7px; margin-right: 10px; font-family: Display, sans-serif;
        font-weight: 700; font-size: 18px; letter-spacing: .1em; text-transform: uppercase; color: ${C.night}; background: ${C.flux}; }
.step { display: flex; align-items: center; gap: 16px; padding: 14px 18px; border-radius: 12px; background: rgba(7,11,26,.92);
        border: 2px solid var(--c); box-shadow: 0 0 18px color-mix(in srgb, var(--c) 35%, transparent), 0 14px 34px rgba(0,0,0,.45); }
.step .n { font-family: Display, sans-serif; font-weight: 900; font-size: 34px; color: var(--c); line-height: 1; }
.step .s { font-size: 20px; line-height: 1.2; color: ${C.ink}; }
`;

const page = (width, height, body, transparent = false) => `<!doctype html><html><head><meta charset="utf-8"><style>${css}
body { width: ${width}px; height: ${height}px; } .stage { width: ${width}px; height: ${height}px; }
${transparent ? 'html, body, .stage { background: transparent; }' : ''}</style></head>
<body><div class="stage">${body}</div></body></html>`;

// An exact region (x, y, w, h in 1920x1080 pixels) of a capture, scaled to W pixels wide.
const region = (file, x, y, w, h, W) => `<div style="position:relative;width:${W}px;height:${Math.round(W * h / w)}px;overflow:hidden">
    <img src="${shot(file)}" style="position:absolute;max-width:none;width:${W * 1920 / w}px;left:${-x * W / w}px;top:${-y * W / w}px"></div>`;
const framed = (inner, color = C.flux, style = '') =>
  `<div class="frame" style="border-color:${color};box-shadow:0 0 0 2px rgba(3,6,16,.9),0 0 32px ${color}55,0 26px 60px rgba(0,0,0,.65);${style}">${inner}</div>`;
const label = (text, color, pos) => `<span class="label" style="${pos}"><span class="dot" style="background:${color}"></span>${text}</span>`;
const stats = (items) => `<div class="stats">${items.map(([t, v, flex]) =>
  `<div${flex ? ` style="flex:${flex}"` : ''}><div class="t">${t}</div><div class="v">${v}</div></div>`).join('')}</div>`;

// A feature card: copy on the left, artwork on the right.
const card = ({ kicker, title, points, art, width = 1600, height = 900, artWidth = 940, backdrop = 'rift.jpg', color = C.flux }) => page(width, height, `
  <div class="backdrop" style="background-image:url('${shot(backdrop)}')"></div><div class="grid"></div><div class="rift"></div>
  <div style="position:absolute;left:76px;top:0;bottom:0;width:${width - artWidth - 150}px;display:flex;flex-direction:column;justify-content:center;--accent:${color}">
    <div class="kicker" style="color:${color}">${kicker}</div>
    <div class="display title shadow" style="font-size:54px;line-height:1.06;margin:14px 0 22px">${title}</div>
    <div class="bar" style="width:240px;margin-bottom:30px;--c:${color}"></div>
    <ul class="points">${points.map((point) => `<li>${point.endsWith('>') ? point : point.replace(/ (\S+)$/, '&nbsp;$1')}</li>`).join('')}</ul>
  </div>
  <div style="position:absolute;right:64px;top:50%;transform:translateY(-50%);width:${artWidth}px">${art}</div>`);

const versionText = VERSIONS.map((v) => v.minecraft).join(' · ');

// The first lines of the config file the mod writes on first start (copied from a generated file).
const configPanel = () => {
  const file = path.join(__dirname, 'config-excerpt.json');
  const esc = (t) => t.replace(/&/g, '&amp;').replace(/</g, '&lt;');
  const text = fs.readFileSync(file, 'utf8').split('\n').slice(0, 27).map((line) => {
    if (/^\s*\/\//.test(line)) return `<span style="color:#7d8db0">${esc(line)}</span>`;
    return esc(line).replace(/("[^"]+")(:)/g, `<span style="color:${C.flux}">$1</span>$2`)
      .replace(/: (true|false|-?[\d.]+|"[^"]*")/g, `: <span style="color:${C.amber}">$1</span>`);
  }).join('\n');
  return `<pre style="margin:0;padding:20px 22px;font-size:13.6px;line-height:1.52;color:#dbe6fb;white-space:pre;font-family:'DejaVu Sans Mono',monospace">${text}</pre>`;
};

// The loop: salvage, fabricate, power, go.
const loopSteps = () => `<div style="display:grid;grid-template-columns:1fr 1fr 1fr 1fr;gap:12px;margin-top:20px">
  ${[[C.rift, '1', 'Salvage<br>the crash'], [C.flux, '2', 'Fabricate<br>the tech'], [C.amber, '3', 'Charge<br>with Flux'], [C.ink, '4', 'Fly, ride,<br>fight']]
    .map(([c, n, s]) => `<div class="step" style="--c:${c}"><div class="n">${n}</div><div class="s">${s}</div></div>`).join('')}</div>`;

const header = (title, color = C.flux) => page(1400, 120, `
  <div style="position:absolute;inset:0;display:flex;align-items:center;justify-content:center;gap:22px">
    <div class="bar" style="width:220px;transform:scaleX(-1);--c:${color}"></div>
    <span class="dot" style="background:${color};width:14px;height:14px;box-shadow:0 0 14px ${color}"></span>
    <div class="display title shadow" style="font-size:48px;white-space:nowrap">${title}</div>
    <span class="dot" style="background:${color};width:14px;height:14px;box-shadow:0 0 14px ${color}"></span>
    <div class="bar" style="width:220px;--c:${color}"></div>
  </div>`, true);

const divider = () => page(1400, 44, `
  <div style="position:absolute;inset:0;display:flex;align-items:center;justify-content:center;gap:16px">
    <div class="bar" style="width:440px;transform:scaleX(-1);opacity:.8"></div>
    ${[C.rift, C.flux, C.amber, C.flux, C.rift].map((c) => `<span class="dot" style="background:${c};box-shadow:0 0 10px ${c}"></span>`).join('')}
    <div class="bar" style="width:440px;opacity:.8"></div>
  </div>`, true);

const assets = {
  banner: () => page(1800, 600, `
    <img src="${shot('rift.jpg')}" style="position:absolute;right:-60px;top:-160px;width:1380px;-webkit-mask-image:linear-gradient(90deg,transparent 0%,#000 32%);mask-image:linear-gradient(90deg,transparent 0%,#000 32%)">
    <div style="position:absolute;inset:0;background:linear-gradient(90deg,rgba(7,11,26,.98) 0%,rgba(7,11,26,.92) 33%,rgba(7,11,26,.3) 62%,rgba(7,11,26,0) 82%)"></div>
    <div style="position:absolute;inset:0;background:linear-gradient(0deg,rgba(7,11,26,.8) 0%,rgba(7,11,26,0) 30%)"></div>
    <div class="grid"></div>
    <div style="position:absolute;left:84px;top:70px;width:1100px">
      <div class="kicker">${RELEASE.version} · ${RELEASE.name}</div>
      <div class="display title shadow" style="display:inline-block;font-size:112px;line-height:1;margin:16px 0 22px;padding-right:12px;white-space:nowrap">Chronofall</div>
      <div class="bar" style="width:560px;margin-bottom:26px"></div>
      <div style="font-size:32px;color:${C.ink};line-height:1.3;font-weight:700">The year 3001 crashed into your world. Salvage rift wrecks<br>and build jetpacks, drones, mechs and hover skiffs.</div>
      <div style="margin-top:30px">${LOADERS.map((l) => `<span class="pill">${l}</span>`).join('')}
        <span style="font-family:Display,sans-serif;font-size:18px;color:#9fb3d9;margin-left:6px;font-weight:700;letter-spacing:.08em">${versionText}</span></div>
    </div>`),

  icon: () => page(512, 512, `
    <div style="position:absolute;inset:0;border-radius:96px;overflow:hidden;background:${C.night}">
      <img src="${shot('rift.jpg')}" style="position:absolute;width:1500px;left:-500px;top:-120px;filter:saturate(1.3) brightness(.75)">
      <div style="position:absolute;inset:0;background:radial-gradient(ellipse at 50% 50%,rgba(7,11,26,.1) 0%,rgba(7,11,26,.9) 78%)"></div>
      <div style="position:absolute;left:86px;top:86px;width:340px;height:340px;border-radius:50%;
                  border:16px solid ${C.rift};box-shadow:0 0 40px ${C.rift},inset 0 0 40px ${C.rift}"></div>
      <div style="position:absolute;left:116px;top:116px;width:280px;height:280px;border-radius:50%;
                  background:radial-gradient(circle at 40% 36%,#203063,${C.night} 70%)"></div>
      <div class="display" style="position:absolute;left:0;right:0;top:150px;text-align:center;font-size:200px;line-height:1;color:${C.flux};
                  text-shadow:0 0 34px rgba(56,243,255,.75),0 8px 0 #0b5a72">C</div>
      <div style="position:absolute;left:300px;top:58px;width:16px;height:150px;border-radius:8px;transform:rotate(32deg);
                  background:linear-gradient(180deg,rgba(255,181,71,0) 0%,${C.amber} 70%,#fff 100%);box-shadow:0 0 22px ${C.amber}"></div>
    </div>
    <div style="position:absolute;inset:0;border-radius:96px;border:8px solid ${C.flux};box-shadow:inset 0 0 0 4px ${C.night},0 0 0 0 transparent"></div>`, true),

  'card-loop': () => card({
    kicker: 'The headline', title: 'The future<br>is falling', color: C.rift, backdrop: 'crater.jpg',
    points: ['At night a <b class="hi">Chrono Rift</b> tears open and a pod of future tech crashes down',
      'Fight off its <b class="hi">Sentinels</b>, crack its <b class="hi">Rift Cache</b>', 'Fabricate jetpacks, drones, mechs and skiffs',
      'Power them all with <b class="hi">Flux</b>: no cables, no machines to chain'],
    art: `${framed(region('crater.jpg', 120, 40, 1680, 945, 940), C.rift)}${loopSteps()}` }),

  'card-rifts': () => card({
    kicker: 'World event', title: 'Chrono<br>Rifts', color: C.rift, backdrop: 'rift.jpg',
    points: ['A rift opens in the night sky 24 to 48 blocks away', 'A Chrono Pod streaks down and digs a scorched crater',
      'A <b class="hi">Rift Cache</b> of salvage, guarded by laser-firing Sentinels', 'A <b class="hi">Rift Beacon</b> calls one on demand'],
    art: `<div style="position:relative">${framed(region('rift.jpg', 160, 0, 1600, 900, 940), C.rift)}
      <div class="frame" style="position:absolute;right:-26px;bottom:-60px;width:400px;border-color:${C.rift}">${region('crater.jpg', 240, 135, 1440, 810, 400)}</div>
      ${label('The crash site', C.rift, 'right:-6px;bottom:194px')}</div>
      <div style="margin-top:74px;width:520px">${stats([['Chance', '4 %/min'], ['Guards', '2–3'], ['Shards', '2–4']])}</div>` }),

  'card-fabricator': () => card({
    kicker: 'Workbench', title: 'The<br>Fabricator', color: C.flux, backdrop: 'fabricator.jpg',
    points: ['20 recipes in one list, with have / need counts', 'One click takes the parts from your inventory',
      'A <b class="hi">hologram</b> of the item spins above it while it prints', 'Hoppers take the output'],
    art: `<div style="position:relative">${framed(region('fabricator.jpg', 160, 60, 1600, 900, 940))}
      <div class="frame" style="position:absolute;right:-26px;bottom:-70px;width:440px">${region('fabricator_gui.jpg', 500, 200, 920, 700, 440)}</div></div>
      <div style="margin-top:84px;width:470px">${stats([['Recipes', '20'], ['Cables', '0']])}</div>` }),

  'card-flux': () => card({
    kicker: 'Energy', title: 'Flux,<br>no cables', color: C.amber, backdrop: 'charger.jpg',
    points: ['The <b class="hi">Flux Charger</b> burns coal or a Rift Shard', 'Beams charge to drones, mechs and skiffs within 8 blocks',
      'Charges the gear of players within 4 blocks', 'Every device shows its charge as a bar'],
    art: `${framed(region('charger.jpg', 160, 60, 1600, 900, 940), C.amber)}
      ${stats([['Coal', '6,400'], ['Rift Shard', '40,000'], ['Range', '8 blocks']])}` }),

  'card-flight': () => card({
    kicker: 'Get airborne', title: 'Jetpack and<br>Hover Skiff', color: C.amber, backdrop: 'skiff.jpg',
    points: ['<b class="hi">Jetpack</b>: hold jump to fly, thruster flames, no fall damage', '<b class="hi">Hover Skiff</b>: throttle, bank, climb and dive',
      'Flux, speed and altitude on the HUD', 'Smooth on servers, no flight kicks'],
    art: `<div style="display:grid;grid-template-columns:1fr 1fr;gap:18px">
      <div style="position:relative">${framed(region('jetpack.jpg', 420, 120, 1080, 810, 461), C.amber)}${label('Jetpack', C.amber, 'left:14px;top:14px')}</div>
      <div style="position:relative">${framed(region('skiff.jpg', 420, 120, 1080, 810, 461), C.flux)}${label('Hover Skiff', C.flux, 'left:14px;top:14px')}</div></div>
      ${stats([['Jetpack', '≈75 s'], ['Skiff', '≈4 min']])}` }),

  'card-robots': () => card({
    kicker: 'Companions', title: 'Drone<br>and Mech', color: C.flux, backdrop: 'mech.jpg',
    points: ['A <b class="hi">Drone</b> that follows you: Collector, Guard and Lamp chips', 'A rideable <b class="hi">Mech</b> that leaps and slams the ground',
      'Upgrade cards with fixed, capped steps', 'Pick both up as items; they keep charge and upgrades'],
    art: `<div style="display:grid;grid-template-columns:1fr 1fr;gap:18px">
      <div style="position:relative">${framed(region('drone.jpg', 420, 120, 1080, 810, 461))}${label('Drone', C.flux, 'left:14px;top:14px')}</div>
      <div style="position:relative">${framed(region('mech.jpg', 420, 120, 1080, 810, 461), C.amber)}${label('Mech', C.amber, 'left:14px;top:14px')}</div></div>
      ${stats([['Chips', '3'], ['Slam', '8 dmg'], ['Cards', '5']])}` }),

  'card-arsenal': () => card({
    kicker: 'Tools of 3001', title: 'Cutter, rifle<br>and Sentinels', color: C.red, backdrop: 'sentinel.jpg',
    points: ['<b class="hi">Plasma Cutter</b>: mine the block you look at, 12 blocks away', '<b class="hi">Pulse Rifle</b>: laser bolts, 7 damage',
      '<b class="hi">Sentinels</b> keep their distance and shoot back', 'Four metal tiers of tools and armour'],
    art: `<div style="display:grid;grid-template-columns:1fr 1fr;gap:18px">
      <div style="position:relative">${framed(region('cutter.jpg', 420, 120, 1080, 810, 461))}${label('Plasma Cutter', C.flux, 'left:14px;top:14px')}</div>
      <div style="position:relative">${framed(region('sentinel.jpg', 420, 120, 1080, 810, 461), C.red)}${label('Sentinel', C.red, 'left:14px;top:14px')}</div></div>
      ${stats([['Cutter', '12 blocks'], ['Rifle', '7 dmg'], ['Tiers', '4']])}` }),

  'card-gates': () => card({
    kicker: 'Fast travel', title: 'Rift<br>Gates', color: C.rift, backdrop: 'gate.jpg',
    points: ['Link two gates with a <b class="hi">Rift Tuner</b>', 'Stand on one, arrive at the other', 'Any distance, across dimensions',
      'Rift particles spiral up while it charges'],
    art: framed(region('gate.jpg', 160, 60, 1600, 900, 940), C.rift) }),

  'card-config': () => card({
    kicker: 'For servers and modpacks', title: 'Old worlds<br>welcome', color: C.flux, backdrop: 'fabricator.jpg',
    points: ['Robotica and The Year 3001 worlds load: blocks, items and robots migrate',
      '<b class="hi">config/chronofall.json</b> with comments: rifts, gates, robots', 'Same features and ids on every version and loader',
      'No extra library to install'],
    art: `<div style="display:flex;flex-direction:column;gap:18px">
      ${framed(configPanel(), C.flux, 'background:#060a18')}
      ${stats([['Loaders', 'Fabric · NeoForge · Forge', 2], ['Versions', '5']])}</div>` }),
};

const HEADERS = {
  'header-whats-new': ['What’s New in 2.0', C.rift], 'header-loop': ['The Future Is Falling', C.rift],
  'header-rifts': ['Chrono Rifts', C.rift], 'header-fabricator': ['The Fabricator', C.flux],
  'header-flux': ['Flux', C.amber], 'header-flight': ['Flight', C.amber],
  'header-robots': ['Drone and Mech', C.flux], 'header-arsenal': ['Tools and Enemies', C.red],
  'header-gates': ['Rift Gates', C.rift], 'header-config': ['For Servers and Modpacks', C.flux],
  'header-downloads': ['Downloads', C.flux], 'header-faq': ['FAQ', C.flux], 'header-gallery': ['Gallery', C.flux],
};
for (const [name, [title, color]] of Object.entries(HEADERS)) {
  assets[name] = () => header(title, color);
}
assets.divider = divider;

(async () => {
  const wanted = process.argv.slice(2);
  const names = wanted.length ? wanted : Object.keys(assets);
  const browser = await chromium.launch();
  const context = await browser.newContext({ deviceScaleFactor: 1 });
  for (const name of names) {
    if (!assets[name]) {
      console.error('unknown asset', name);
      continue;
    }
    const html = assets[name]();
    const file = path.join(__dirname, `.render-${name}.html`);
    fs.writeFileSync(file, html);
    const p = await context.newPage();
    await p.goto(url(file));
    await p.evaluate(() => document.fonts.ready);
    await p.waitForTimeout(200);
    const { width, height } = await p.evaluate(() => ({ width: document.body.scrollWidth, height: document.body.scrollHeight }));
    await p.setViewportSize({ width, height });
    const transparent = name.startsWith('header') || name === 'divider' || name === 'icon';
    await p.screenshot({ path: path.join(out, `${name}.png`), omitBackground: transparent });
    await p.close();
    fs.unlinkSync(file);
    console.log('rendered', name);
  }
  await browser.close();
})();
