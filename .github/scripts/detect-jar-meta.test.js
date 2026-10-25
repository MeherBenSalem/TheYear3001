#!/usr/bin/env node
'use strict';

const assert = require('assert');
const { detect, detectRequired } = require('./detect-jar-meta.js');

const cases = [
  // Current MultiLoader naming
  ['chronofall-fabric-1.20.1-2.0.0.jar', 'fabric', '1.20.1'],
  ['chronofall-forge-1.20.1-2.0.0.jar', 'forge', '1.20.1'],
  ['chronofall-fabric-1.21.1-2.0.0.jar', 'fabric', '1.21.1'],
  ['chronofall-neoforge-1.21.1-2.0.0.jar', 'neoforge', '1.21.1'],
  ['chronofall-fabric-26.2-2.0.0.jar', 'fabric', '26.2'],
  ['chronofall-neoforge-26.2-2.0.0.jar', 'neoforge', '26.2'],
  ['chronofall-fabric-26.3-2.0.0.jar', 'fabric', '26.3'],
  ['chronofall-neoforge-26.3-2.0.0.jar', 'neoforge', '26.3'],
  ['releases/chronofall-neoforge-26.3-2.0.0.jar', 'neoforge', '26.3'],
  // Calendar versions with a patch number
  ['chronofall-fabric-26.1.2-2.0.0.jar', 'fabric', '26.1.2'],
  ['chronofall-neoforge-26.1.2-2.0.0.jar', 'neoforge', '26.1.2'],
  // 0.5.0 names (same convention) and older names (mod version first)
  ['chronofall-neoforge-1.21.1-0.5.0.jar', 'neoforge', '1.21.1'],
  ['chronofall-0.4.0-forge-1.20.1.jar', 'forge', '1.20.1'],
  ['chronofall-0.4.0-neoforge-1.21.1.jar', 'neoforge', '1.21.1'],
];

let failed = 0;
for (const [name, loader, gv] of cases) {
  const got = detect(name);
  try {
    assert.strictEqual(got.loader, loader, `${name} loader`);
    assert.strictEqual(got.gv, gv, `${name} gv`);
    console.log('ok', name, '->', got);
  } catch (e) {
    failed += 1;
    console.error('FAIL', name, 'got', got, e.message);
  }
}

// Unparseable names must fail instead of falling back to a default Minecraft version.
try {
  detectRequired('chronofall-fabric-mystery.jar');
  failed += 1;
  console.error('FAIL expected throw for mystery jar');
} catch (e) {
  console.log('ok refuses default fallback:', e.message.split('\n')[0]);
}

if (failed) {
  console.error(`${failed} case(s) failed`);
  process.exit(1);
}
console.log('all detect-jar-meta cases passed');
