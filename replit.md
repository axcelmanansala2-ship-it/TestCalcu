# TestCalcu

A simple Android calculator app that builds to an APK via GitHub Actions.

## Stack

- Kotlin + Android SDK 34
- ViewBinding
- Material Components
- Gradle 8.7 / AGP 8.3.2

## Features

- Basic arithmetic: +, −, ×, ÷
- Decimal support
- Backspace, +/-, percentage
- iOS-style dark UI

## GitHub Actions

The workflow (`TestCalcu`) triggers on every push to `main` and on manual dispatch:
1. Sets up JDK 17
2. Installs Gradle 8.7
3. Generates a temporary release keystore
4. Builds a signed release APK
5. Uploads APK as a workflow artifact

## Where things live

- `app/src/main/java/com/example/testcalcu/MainActivity.kt` — calculator logic
- `app/src/main/res/layout/activity_main.xml` — UI layout
- `app/src/main/res/values/` — colors, strings, themes
- `.github/workflows/build.yml` — CI/CD pipeline

## User preferences

- GitHub repo: axcelmanansala2-ship-it/TestCalcu
