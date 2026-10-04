# Kart - E-Commerce Android Application

Kart is a minimal, beautifully designed e-commerce Android application that allows users to browse products, view details, and manage an offline-first shopping cart. This project was developed as a technical assessment focusing on clean architecture, modern Android development practices, and robustness.

## Setup & Build Instructions

1. **Clone the repository:**
   ```bash
   git clone https://github.com/akashanand301/Kart.git
   ```
2. **Open the project** in Android Studio (Jellyfish or newer recommended).
3. **Sync Gradle** to download all necessary dependencies.
4. **Build and Run:**
   - Select either an emulator or a physical device.
   - Click the **Run** button or use the terminal:
     ```bash
     ./gradlew installDebug
     ```
5. **No API keys are required.** The app connects to the public `DummyJSON` API.

## Architecture Used

This application strictly follows **Clean Architecture** combined with the **MVVM (Model-View-ViewModel)** pattern.

The codebase is modularized by features and layered cleanly:
* **Domain Layer:** Contains use cases, models (`Product`, `CartItem`), and repository interfaces. This layer is completely independent of the Android framework.
* **Data Layer:** Implements repository interfaces. Handles fetching from the remote API (`Retrofit`) and caching to local storage (`Room`). It maps DTOs to Domain Models.
* **Presentation Layer:** Uses `ViewModels` to manage state and expose `StateFlow` to the UI. The UI is completely built using **Jetpack Compose**.
* **Manual Dependency Injection:** Dependencies are provided manually via a centralized `AppContainer` injected through a custom `ViewModelProvider.Factory`.

## Libraries Used

* **Jetpack Compose:** For a modern, declarative UI.
* **Material Design 3:** For components, theming, and responsive layouts.
* **Navigation Compose:** For seamless screen transitions (`AppNavigation`).
* **Kotlin Coroutines & Flow:** For asynchronous operations and reactive state management.
* **Retrofit2 & OkHttp:** For handling REST API network requests securely.
* **Kotlinx Serialization:** For fast and safe JSON parsing.
* **Room Database:** For offline cart data persistence using SQLite.
* **Coil:** For efficient, asynchronous image loading and caching.
* **Core SplashScreen:** For a smooth app launch experience.

## Local Storage Approach

The app uses **Room Database** to provide a fully offline-first shopping cart experience.
* **CartDao:** Exposes standard synchronous functions executing on `Dispatchers.IO` and reactive `Flow` streams for UI observation.
* **Real-time Sync:** The `ProductDetailsScreen` and `CartScreen` observe the local Room database via `StateFlow` so changes to cart quantities are instantly reflected across all screens without network dependency.
* **Offline Functionality:** Since cart modifications (add, remove, quantity update) occur purely locally, the user can manage their cart seamlessly without an internet connection.

## Important Design Decisions

* **Manual Dependency Injection:** Instead of relying on heavy frameworks like Hilt or Koin, a manual DI container (`AppContainer`) was implemented. This demonstrates a deep understanding of dependency management and keeps the app lightweight.
* **State Management:** MVI-like state management is handled using `UiState` sealed classes (`Loading`, `Success`, `Error`). This ensures the UI is predictable and handles edge cases properly.
* **Pagination & Debouncing:** The home screen search features a 500ms debounce to prevent API spam. Standard product loading supports pagination (limit and skip) using a "Show More" interaction to save data and memory.
* **UI/UX Aesthetics:** A customized turquoise-based color palette is used along with micro-animations (`animateContentSize`, `AnimatedVisibility`) to give the app a premium feel.

## Known Limitations

* **No Checkout Flow:** As per the requirements, the checkout button on the cart screen acts as a placeholder and does not initiate a payment flow.
* **Local Data Only:** Cart items are strictly tied to local device storage. If the app data is cleared or uninstalled, the cart is lost (no cloud syncing is implemented).
* **Pagination Model:** The "Show More" functionality on the main screen is implemented as a simple manual trigger rather than infinite scrolling (e.g., Paging3) to keep the architecture straightforward and lightweight for this assessment.
