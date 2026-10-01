# News Feed Simulator

Proyek ini adalah implementasi dari **News Feed Simulator** menggunakan Kotlin (Kotlin Multiplatform / Android), mengadopsi fitur *Flow*, *StateFlow*, dan *Coroutines* untuk mensimulasikan aliran data (berita) secara *asynchronous* dan *reactive*. 

## 📂 Struktur Direktori Proyek

Proyek ini menggunakan struktur standar Kotlin Multiplatform (meskipun antarmuka direalisasikan dalam native Android menggunakan Jetpack Compose), yang terdiri dari:

- `androidApp/`
  - Berisi kode *entry-point* Android, khususnya `MainActivity.kt`.
  - Mengonfigurasi antarmuka pengguna UI menggunakan **Jetpack Compose**.
  - Mengelola *lifecycle* dari aplikasi Android dan menghubungkannya dengan `NewsManager`.
- `shared/`
  - Berisi kode *business logic* yang dapat digunakan secara lintas platform (Cross-platform).
  - `NewsSimulator.kt`: Mendefinisikan *data class* `News` dan `NewsRepository` sebagai sumber *Cold Flow* dan pemanggilan simulasi jaringan yang lambat (async).
  - `NewsManager.kt`: Bertanggung jawab untuk mengkonsumsi *Cold Flow* dari *repository*, melakukan operasi filter, map, *catch* *error*, dan mengekspos `StateFlow` (`readCount` & `newsList`) agar bisa di-observasi oleh UI.
  - `shared/src/commonTest/`: Berisi file implementasi *Unit Test* (`NewsManagerTest.kt`) yang menguji perilaku dari *Flow* & *StateFlow* secara sinkron.
- `gradle/`
  - Konfigurasi dependensi menggunakan *Version Catalog* (`libs.versions.toml`).

## 🔄 Konsep Cold Flow & StateFlow

### 1. Cold Flow (`NewsRepository.getNewsStream`)
`Flow` di Kotlin pada dasarnya bersifat **Cold**. Artinya, aliran data (emisi berita baru setiap 2 detik) **tidak akan berjalan** sebelum ada kolektor (fungsi *terminal operator* seperti `.collect()`) yang mulai memantaunya. Dalam aplikasi ini:
- `NewsRepository` membuat *Flow* menggunakan *builder* `flow { ... }`.
- Setiap kali `NewsManager.startSimulation()` memanggil `.collect()`, blok kode `flow` dieksekusi dari awal secara independen, memancarkan (*emit*) berita setiap 2 detik hingga *coroutine* dibatalkan.
- Proses penyaringan data `.filter` dan perubahan struktur `.map` juga dilakukan pada aliran *Cold Flow* ini, membuatnya lebih memori-efisien.

### 2. StateFlow (`NewsManager.readCount` & `NewsManager.newsList`)
`StateFlow` adalah *Flow* yang bersifat **Hot**. Aliran ini dirancang khusus untuk memegang/menyimpan satu *state* terakhir secara persisten, lalu membagikannya ke beberapa kolektor yang aktif. Pada aplikasi ini:
- `_readCount` mengelola jumlah iterasi dari berapa banyak berita yang berhasil ditangkap.
- `_newsList` bertugas menampung secara akumulatif *List* berita apa saja yang siap tayang di UI.
- Saat antarmuka UI di-*recompose* oleh Jetpack Compose, UI me-*listen* *state* ini menggunakan ekstensi `.collectAsStateWithLifecycle()`. `StateFlow` memastikan UI secara instan (dan aman dalam *lifecycle*) akan menampilkan data berita terakhir.

## 📱 Skenario UI & Error Handling (`.catch`)

Saat menjalankan aplikasi ini di perangkat Android, Anda akan melihat interaksi UI sebagai berikut:
1. **Inisialisasi**: Saat aplikasi baru dibuka, "Total Berita Dibaca" akan menunjukkan nilai `0` dan daftar (*list*) masih kosong.
2. **Streaming Berita**: Setelah jeda sekitar 2-3 detik (akibat operator *delay* yang disimulasikan sebagai network call secara *asynchronous*), kartu berita baru akan ditambahkan satu demi satu dari atas ke bawah.
3. **Filtering**: Berita yang masuk hanyalah dari kategori yang relevan (`Tech` dan `Cybersecurity`), menandakan operator `.filter` berfungsi mencegah munculnya kategori lain (seperti `Sports`).
4. **Error Handling (`.catch`) & Selesainya Aliran**:
   - Di *layer* repository (`NewsRepository`), terdapat **peluang kegagalan jaringan sebesar 10%** setiap 2 detik (dibuat menggunakan pelemparan `Exception` acak).
   - Ketika `Exception` terjadi, *Flow* secara alamiah akan terputus dan berhenti memproduksi data.
   - Tanpa *error handler*, hal ini akan menyebabkan aplikasi *Crash (Force Close)*. 
   - Namun, operator `.catch` yang disematkan akan mencegat *crash* tersebut dan menggantinya dengan memancarkan (*emit*) data darurat/fallback. Akibatnya, Anda akan melihat berita terakhir berbunyi **"🚨 BREAKING System - Sistem Dalam Perbaikan"**, dan proses streaming berita akan otomatis berhenti. **Hal ini disengaja** untuk mendemonstrasikan bagaimana fitur `catch` menyelesaikan *Flow* secara reaktif dan aman tanpa mengorbankan stabilitas UI aplikasi.

## 🚀 Instruksi Build dan Menjalankan Aplikasi

Pastikan sistem Anda sudah meng-install **Android Studio** dan **JDK 17** (atau versi yang lebih baru).

### Cara Build Aplikasi via Terminal
1. Buka Terminal pada root direktori proyek (`NewsFeedSimulator/`).
2. Ketik perintah berikut:
   ```bash
   ./gradlew :androidApp:assembleDebug
   ```
3. Tunggu hingga proses kompilasi selesai. APK akan tersimpan di dalam direktori `androidApp/build/outputs/apk/debug/androidApp-debug.apk`.

### Cara Menjalankan Aplikasi (Run)
1. Buka proyek ini menggunakan Android Studio.
2. Sambungkan *device* Android (baik *physical device* via kabel USB/Wireless ADB atau *Android Emulator*).
3. Di panel atas Android Studio, pastikan target modul yang berjalan adalah **`androidApp`** dan perangkat yang dituju sudah benar.
4. Klik tombol ikon ▷ **Run 'androidApp'** (atau tekan tombol kombinasi `Shift + F10`).
5. Aplikasi akan melakukan tahap *build* lalu ter-*install* dan diluncurkan pada perangkat.

### Menjalankan Unit Test
Aplikasi ini sudah dilengkapi dengan Unit Test dengan *library* `kotlinx-coroutines-test` di mana semua coroutine dieksekusi secara instan (tanpa perlu menahan *thread* akibat penundaan aslinya).
Untuk menjalankannya:
```bash
./gradlew :shared:testDebugUnitTest
```
---

*Proyek ini merupakan demonstrasi untuk pemahaman implementasi Structured Concurrency, Flow, StateFlow, Coroutines Test, dan integrasinya dengan Jetpack Compose di Android.*
