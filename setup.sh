#!/usr/bin/env bash

rm settings.gradle
echo "include ':core', ':android', ':annotations', ':processor', ':android-test" >> settings.gradle

cat settings.gradle