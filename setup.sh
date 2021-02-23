#!/usr/bin/env bash

rm settings.gradle
echo "include ':core', ':android', ':annotations', ':processor', ':android-test" >> settings.gradle
echo "include ':extras:android-fragment', ':extras:android-activityresult'" >> settings.gradle

cat settings.gradle