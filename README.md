# 📝 MyToDo App

### Built with Modern Android Development Practices

**Developer:** Jeel Bhensadadiya  
**Language:** Kotlin  
**Architecture:** MVVM (Model–View–ViewModel)  
**Tech Stack:** Hilt • Room • Retrofit • Coroutines • Flow • ListAdapter • Jetpack Navigation

---

## 📱 Overview

**MyToDo** is a simple yet powerful ToDo application built entirely in **Kotlin**, demonstrating the use of **Modern Android Architecture Components** and **best development practices**.

It allows users to:
- ✅ Create new tasks  
- ✏️ Update existing tasks  
- ❌ Delete tasks  
- 📋 View all tasks (with live updates)  
- 🕒 Manage task completion status  

This project is created for learning and practice purposes to understand **MVVM**, **Dependency Injection**, **Asynchronous programming**, and **Reactive data streams** using **Kotlin Flow**.

---

## 🧠 Tech Stack & Concepts Used

| Layer | Technology | Description |
|-------|-------------|-------------|
| **Architecture** | **MVVM** | Clean separation between UI, ViewModel, Repository, and Data layers |
| **Dependency Injection** | **Hilt (Dagger)** | Simplifies dependency injection and lifecycle management |
| **Database** | **Room** | Local persistence with Entity, DAO, and Database |
| **Networking** | **Retrofit** | REST API integration for CRUD operations |
| **Asynchronous Work** | **Kotlin Coroutines** | Efficient background threading |
| **Reactive Streams** | **Kotlin Flow** | Observing and collecting live database updates |
| **UI Components** | **RecyclerView + ListAdapter** | Efficient UI rendering with DiffUtil |
| **Lifecycle Awareness** | **ViewModel + LiveData/Flow** | Prevents leaks and ensures smooth UI updates |

---

## 🧩 Project Structure
```
mytodo/
    mytodo/
        MyApplication.kt
        data/
            local/
                database/
                    UserDatabase.kt
                entity/
                    TaskEntity.kt
                    UserEntity.kt
                dao/
                    UserDao.kt
                    TaskDao.kt
            mapper/
                TaskDomainMapper.kt
            remote/
                api/
                    ApiService.kt
                dto/
                    TodoDto.kt
        domain/
            model/
                User.kt
                Task.kt
            repository/
                UserRepository.kt
                TaskRepository.kt
            usecase/
                task/
                    AddTaskUseCase.kt
                    DeleteTaskUseCase.kt
                    GetAllTaskUseCase.kt
                    GetCompletedTaskUseCase.kt
                    GetPendingTaskUseCase.kt
                user/
                    LoginUseCase.kt
                    RegisterUseCase.kt
        ui/
            view/
                MainActivity.kt
                TaskViewFragment.kt
                CreateTaskFragment.kt
                LoginFragment.kt
                RecycleBinFragment.kt
                SignUpFragment.kt
            viewmodel/
                LoginViewModel.kt
                SignUpViewModel.kt
                TaskViewModel.kt
        util/
            UIState.kt
            Validator.kt
        di/
            AppModule.kt
    app/
        build.gradle
    gradle/
    build.gradle
    settings.gradle
```



---

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

- 🧩 How to structure an app using **MVVM Architecture**  
- 💉 How to use **Hilt** for dependency injection  
- 💾 How to persist data locally using **Room**  
- 🌐 How to perform network operations with **Retrofit**  
- ⚡ How to use **Coroutines** and **Flow** for asynchronous operations  
- 🎨 How to design reactive, lifecycle-aware UIs with **ListAdapter**  

