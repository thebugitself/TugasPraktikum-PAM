# ProfileApp

## Screenshot
<img width="300" height="700" alt="image" src="https://github.com/user-attachments/assets/df9d2df9-74be-41eb-be21-cb6c017285dd" />
<img width="300" height="700" alt="image" src="https://github.com/user-attachments/assets/120feb3c-e6b9-4019-b533-acea08e561b0" />
<img width="300" height="700" alt="image" src="https://github.com/user-attachments/assets/782a6b75-76bf-4c38-ae63-b699af5df6f4" />


**ProfileApp** adalah aplikasi manajemen profil interaktif berbasis **Kotlin Multiplatform (KMP)** & **Compose Multiplatform** yang mendukung platform **Android** dan **Desktop (JVM)**.

Aplikasi ini dikembangkan untuk Tugas Praktikum Minggu 4 dengan menerapkan pola arsitektur **MVVM (Model-View-ViewModel)**, **State Hoisting**, dan transisi **Smooth Dark Mode Theme**.

---

## 🌟 Fitur Utama

1. **Arsitektur MVVM (`ProfileViewModel` + `ProfileUiState`)**:
   * Pengelolaan UI State secara reaktif menggunakan `StateFlow<ProfileUiState>`.
   * Pemisahan logika bisnis (ViewModel) dan antarmuka pengguna (Composables).

2. **Fitur Edit Profile (State Hoisting)**:
   * Form pengeditan profil yang interaktif untuk memperbarui Nama, Role/Profesi, Bio/About Me, Email, Telepon, dan Lokasi.
   * Menerapkan **State Hoisting** dengan tombol *Save* dan *Cancel*.

3. **Smooth Dark Mode Theme Toggle**:
   * Mode Terang & Gelap dapat diubah sewaktu-waktu melalui tombol di Top Bar.
   * Menggunakan interpolasi warna `animateColorAsState` dengan spesifikasi animasi `tween(400ms)` untuk transisi tema yang sangat mulus (*buttery-smooth*).

---

## 📁 Struktur Proyek

* **`shared/src/commonMain`**: Kode utama yang dibagikan ke seluruh platform.
  * [`ProfileUiState.kt`](./shared/src/commonMain/kotlin/com/akhdan/myprofileapp/ProfileUiState.kt): Model data UI State.
  * [`ProfileViewModel.kt`](./shared/src/commonMain/kotlin/com/akhdan/myprofileapp/ProfileViewModel.kt): ViewModel pengelola state dan logika aksi.
  * [`App.kt`](./shared/src/commonMain/kotlin/com/akhdan/myprofileapp/App.kt): Komponen UI Compose Multiplatform (`MyProfileScreen`, `SmoothDarkTheme`, `EditProfileForm`, dll).
* **`androidApp`**: Modul aplikasi khusus Android (`MainActivity.kt`).
* **`desktopApp`**: Modul aplikasi khusus Desktop (`main.kt`).

---

## 🚀 Cara Menjalankan Aplikasi

### 1. Android App
```bash
# Build APK Debug
./gradlew :androidApp:assembleDebug

# Install ke Perangkat/Emulator
adb install androidApp/build/intermediates/apk/debug/androidApp-debug.apk
```

### 2. Desktop App (JVM)
```bash
# Menjalankan aplikasi Desktop
./gradlew :desktopApp:run

# Menjalankan dengan Hot Reload
./gradlew :desktopApp:hotRun --auto
```

---

## 🧪 Menguji Aplikasi

```bash
# Uji coba Android
./gradlew :shared:testAndroidHostTest

# Uji coba Desktop
./gradlew :shared:jvmTest
```
