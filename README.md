# 📱 MyToDo — Modern Android To-Do Application

![Kotlin](https://img.shields.io/badge/Kotlin-1.9-blue?logo=kotlin)
![Android](https://img.shields.io/badge/Android-14+-green?logo=android)
![MVVM](https://img.shields.io/badge/Architecture-MVVM-orange)
![Hilt](https://img.shields.io/badge/DI-Hilt-brightgreen)

A clean, modern, and scalable **To-Do App** built using **Kotlin + Jetpack Components**, following **MVVM architecture**, **Clean Architecture principles**, and **Modern Android Development (MAD)** practices.

---

## 🚀 Features

### 📝 Task Management
- ➕ Create new tasks
- ✏️ Edit and update tasks
- ❌ Delete tasks
- ♻️ Restore from Recycle Bin
- 📋 View All, Completed, Pending, and Overdue tasks
- 🔍 Filter tasks by status

### ⚙️ Architecture & Tech
- MVVM + Clean Architecture
- Kotlin Coroutines + Flow
- Room Database (Offline support)
- Retrofit Networking
- Paging 3 (Efficient infinite lists)
- Hilt Dependency Injection
- Jetpack Navigation Component
- CameraX Integration
- Media3 ExoPlayer Support
- WorkManager for background tasks

---

## 🧠 Tech Stack

| Layer | Technology |
|------|------------|
| **Architecture** | MVVM + Clean Architecture |
| **Dependency Injection** | Hilt (Dagger) |
| **Database** | Room + Room-Paging |
| **Networking** | Retrofit + Gson |
| **Async & Reactive** | Coroutines, Flow, ViewModel KTX |
| **UI Layer** | RecyclerView + ListAdapter + ViewBinding |
| **Pagination** | Paging 3 |
| **Media** | CameraX, Media3 ExoPlayer |
| **Background Work** | WorkManager |
| **Navigation** | Navigation Component + Safe Args |

---

## 📂 Project Structure

```
mytodo/
│
├── data/
│ ├── local/
│ ├── remote/
│ ├── mapper/
│ └── repository/
│
├── domain/
│ ├── model/
│ ├── repository/
│ └── usecase/
│
├── ui/
│ ├── view/
│ ├── adapter/
│ └── viewModel/
│
├── util/
└── di/

```

---

## 📦 Dependencies (Major Highlights)

```gradle
// Hilt
implementation("com.google.dagger:hilt-android:2.57.2")
ksp("com.google.dagger:hilt-compiler:2.57.2")

// Room
implementation("androidx.room:room-runtime:2.8.3")
implementation("androidx.room:room-paging:2.8.3")
ksp("androidx.room:room-compiler:2.8.3")

// Paging 3
implementation("androidx.paging:paging-runtime:3.3.6")

// Retrofit
implementation("com.squareup.retrofit2:retrofit:3.0.0")
implementation("com.squareup.retrofit2:converter-gson:3.0.0")

// Kotlin Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

// Navigation
implementation("androidx.navigation:navigation-fragment-ktx:2.9.6")
implementation("androidx.navigation:navigation-ui-ktx:2.9.6")
```

## ⚙️ Features

- MVVM Architecture with Clean Code Separation  
- Reactive UI updates with Kotlin Flow  
- Dependency Injection using Hilt  
- Offline-first approach using Room  
- Network calls with Retrofit  
- Coroutine-based asynchronous processing  
- Modern UI using RecyclerView + ListAdapter  

---

## 🚀 Getting Started

### Prerequisites
- Android Studio (Ladybug or newer)
- Kotlin 1.9+
- Gradle 8+
- Minimum SDK: 24 (Android 7.0)

### Steps to Run
1. Clone the repository  
   ```bash
   git clone https://github.com/JeelBhensadadiya/MyToDoApp.git
2. Open the project in Android Studio
3. Sync Gradle files
4. Run the app on an emulator or physical device

## 🧪 Learning Outcomes

Through building this project, I (**Jeel Bhensadadiya**) have learned:

- MVVM + Clean Architecture in-depth
- How to implement Hilt dependency injection
- Building offline-first apps using Room
- Networking with Retrofit
- Flow + Coroutines for reactive streams
- Paging3 for efficient list loading
- Using CameraX and Media3 ExoPlayer
- Clean UI architecture with ViewBinding
- How to manage UI State with sealed classes

