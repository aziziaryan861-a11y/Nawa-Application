# NAWA — Heavenly Recitation, Living Ambience

**Created by ABDUL AHAD ARYAN**

NAWA is a beautiful Android app for listening to the Holy Quran. You choose one of
three legendary Egyptian Qaris, pick one of the five most-beloved Surahs, and listen
while a living ambience (rain, fire, river, birds, wind or night) plays around the
recitation — each ambience with its own matching animated background scene and its
own separate volume slider.

---

## Features

- **Three Qari characters** — Abdul Basit Abdul Samad, Mahmoud Khalil Al-Hussary and
  Mohamed Siddiq Al-Minshawi, each with a hand-drawn character avatar.
- **Top five Surahs** — Ar-Rahman (55), Yasin (36), Al-Mulk (67), Al-Waqi'ah (56), Qaf (50).
- **Six living ambiences** — Rain, Fire, River, Birds, Wind and Night. The ambience
  loops seamlessly and **never falls silent** while the Surah is reciting, and stops
  automatically when the recitation stops.
- **Matching background scenes** — choosing Rain shows dark rain clouds, Fire shows
  burning logs with rising embers, River shows flowing water, and so on. Each scene
  is a smooth 8-second looping video with a soft crossfade between scenes.
- **Two independent volume sliders** — one for the Qari's voice (ExoPlayer volume)
  and one for the ambience effects (SoundPool stream volume).
- **Elegant UI** — deep emerald + gold theme, Poppins for Latin text and the classical
  Amiri face for Arabic names, glass-style cards, portrait orientation.

---

## Requirements

- **Android Studio** Ladybug (2024.2) or newer — https://developer.android.com/studio
- Any computer that runs Android Studio (Windows, macOS or Linux). The correct
  JDK is bundled inside Android Studio.
- An Android phone with **Android 7.0 (API 24) or newer**, or the built-in emulator.
- Internet connection on first build (Gradle downloads dependencies) and on the phone
  while listening (recitations are streamed; the ambiences and background videos are
  bundled offline inside the app).

---

## How to build on your computer

1. **Unzip** this project anywhere you like.
2. Open **Android Studio** → `File → Open…` → select the unzipped `NAWA` folder
   (the folder that contains `settings.gradle.kts`).
3. Wait for **Gradle sync** to finish. The first sync downloads the Gradle build
   system and libraries — it can take 5–20 minutes depending on your internet.
   If Studio asks about the Gradle JDK, accept the default (Embedded JDK).
4. Press the green **Run ▶** button with your phone connected (or an emulator started).

### Put it on your phone

- **Easiest:** enable *Developer options → USB debugging* on the phone, plug it in,
  and press **Run ▶** in Android Studio. The app installs and opens automatically.
- **APK file:** `Build → Build App Bundle(s) / APK(s) → Build APK(s)`. Then copy
  `app/build/outputs/apk/debug/app-debug.apk` to your phone and tap it
  (allow *Install from unknown sources* if asked).

---

## Project structure

```
NAWA/
├── app/src/main/
│   ├── java/com/nawa/app/
│   │   ├── MainActivity.kt              # entry point
│   │   ├── data/NawaData.kt             # Qaris, Surahs, effects + recitation URLs
│   │   ├── playback/EffectEngine.kt     # seamless SoundPool ambience loops
│   │   └── ui/
│   │       ├── AppNav.kt                # navigation between the four screens
│   │       ├── theme/                   # colors, typography (Poppins + Amiri)
│   │       └── screens/                 # Splash, Qari, Surah, Player screens
│   └── res/
│       ├── drawable/                    # Qari avatars + golden emblem
│       ├── font/                        # Poppins + Amiri fonts
│       ├── mipmap-*/                    # launcher icons
│       └── raw/
│           ├── snd_*.ogg                # six seamless ambience loops
│           └── vid_*.mp4                # six looping background scenes
└── (Gradle files)
```

## How to customize

| What | Where |
| --- | --- |
| Add / change a Qari or recitation server | `data/NawaData.kt` → `qaris` (files are `001.mp3`…`114.mp3` per surah on mp3quran.net servers) |
| Add more Surahs | `data/NawaData.kt` → `surahs` (use the surah number, the URL is built automatically) |
| Replace an ambience sound | overwrite `res/raw/snd_<name>.ogg` (keep the file name) |
| Replace a background video | overwrite `res/raw/vid_<name>.mp4` (keep the file name) |
| Replace character art | overwrite `res/drawable/qari_*.png` or `emblem.png` |
| Change colors / fonts | `ui/theme/Color.kt`, `ui/theme/Type.kt` |

## Notes

- The Qari avatars are respectful artistic character illustrations, not photographs
  of the reciters — you can replace them with real photos of the same size.
- Recitations stream from the public mp3quran.net servers (URLs verified working).
  If a server ever changes, update `baseUrl` in `NawaData.kt`.
- Ambience sounds and background videos work fully offline; they are bundled in the APK.
- The screen uses edge-to-edge dark styling; everything is portrait-locked by design.
