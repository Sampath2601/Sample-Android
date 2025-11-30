# Sample-Android

A Sample login flow built with **Jetpack Compose**, **MVVM** with both unit and UI tests.

---
## Project Structure
```
app/src/
├── main/                 
│   └── java/com/example/sample/
│       ├── ui/login/      → LoginScreen, LoginViewModel
│       ├── Mock/          →  NetworkMonitor
│       |── Creds.kt (shared test data)
|       ├── MainActivity
│       ├── TestTags (Single File for referencing in UITests)
├── test/                  → Unit tests (JVM)
│   └── LoginViewModelTest.kt
│
└── androidTest/           → Instrumented UI tests
└── java/com/example/sample/
├── screens/       → BaseScreen + LoginScreen (POM)
├── tests/         → BaseTest + LoginFlowTest
└── utils/         → NetworkMonitorProvider, etc.
```
---
## ScreenRecording
This app uses Credentials Username: test, Password: password



https://github.com/user-attachments/assets/ebc88bf9-ecf9-460d-9e2a-db38a57bbede


---
# How To Run Tests

# Unit tests
./gradlew test

# UI tests
./gradlew connectedAndroidTest

# All tests
./gradlew test connectedAndroidTest
