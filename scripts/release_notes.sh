#!/bin/bash

auto-changelog --output changelogs/CHANGELOG.md

if [[ $(git describe --tags --exact-match 2>/dev/null) ]]; then
  VERSION=$(git describe --tags)
  cp changelogs/CHANGELOG.md changelogs/archived/CHANGELOG-$VERSION.md
  rm changelogs/CHANGELOG.md
fi
