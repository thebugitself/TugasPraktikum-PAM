# TugasPraktikum-PAM

Kumpulan tugas praktikum mata kuliah **Pengembangan Aplikasi Mobile (PAM)**, semua proyek berbasis **Kotlin Multiplatform** + Compose Multiplatform, dikumpulkan dalam satu repositori.

## Daftar Proyek

| Folder | Proyek | Target |
|---|---|---|
| [`KMP1HelloWorld/`](./KMP1HelloWorld) | Hello World KMP | Android, iOS |
| [`MyProfileApp/`](./MyProfileApp) | Aplikasi Profil (Compose Multiplatform) | Android, Desktop (JVM) |
| [`NewsFeedSimulator/`](./NewsFeedSimulator) | Simulasi News Feed (Flow / StateFlow / Coroutines) | Android, iOS |

## Struktur

```
TugasPraktikum-PAM/
├── KMP1HelloWorld/
│   ├── androidApp/
│   ├── iosApp/
│   └── shared/
├── MyProfileApp/
│   ├── androidApp/
│   ├── desktopApp/
│   └── shared/
└── NewsFeedSimulator/
    ├── androidApp/
    ├── iosApp/
    └── shared/
```

Setiap folder adalah proyek Gradle yang berdiri sendiri (masing-masing punya `settings.gradle.kts` dan `gradlew` sendiri), jadi dibuka satu per satu di Android Studio — bukan sebagai single root project.

## Menjalankan

```bash
cd <nama-proyek>
./gradlew :androidApp:assembleDebug     # build APK Android
./gradlew :desktopApp:run               # MyProfileApp saja (target Desktop)
```

Untuk target iOS, buka folder `iosApp/` di Xcode.
