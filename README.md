# Sample-Android

A Sample login flow built with **Jetpack Compose**, **MVVM** with both unit and UI tests.

## Project Structure
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

Running Tests

# Unit tests
./gradlew test

# UI tests
./gradlew connectedAndroidTest

# All tests
./gradlew test connectedAndroidTest