#!/usr/bin/env bash

rm settings.gradle
echo "include ':core', ':android', ':annotations', ':processor'" >> settings.gradle
echo ':samples:app', ':samples:approuter', 'samples:cart', 'samples:product' >> settings.gradle

cat settings.gradle