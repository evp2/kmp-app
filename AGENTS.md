# Kotlin Multiplatform (KMP) Best Practices for AI Agents

This guide provides steering instructions for LLMs and AI Agents when working on this Kotlin Multiplatform project.

## 1. Core Principles
- **Prefer Common Code**: Always attempt to implement logic in `commonMain`. Only use `expect`/`actual` when platform-specific APIs are absolutely necessary.
- **Dependency Injection**: Use interfaces in `commonMain` and provide platform-specific implementations via DI (e.g., Koin) or manual injection.
- **Library Selection**: Use multiplatform-ready libraries (e.g., Ktor for networking, SQLDelight for database, kotlinx-datetime for time, kotlinx-serialization for JSON).

## 2. Project Structure
- `shared/src/commonMain`: Shared business logic and data models.
- `shared/src/[androidMain|iosMain|jvmMain|jsMain]`: Platform-specific implementations.
- `composeApp/src/commonMain`: Shared UI using Compose Multiplatform.

## 3. Coding Guidelines
### Expect / Actual
- Keep `expect` declarations minimal. Prefer passing dependencies from the platform side to the common side.
- Avoid placing large amounts of logic in `actual` implementations.

### Concurrency
- Use Kotlin Coroutines (`kotlinx-coroutines-core`).
- Be mindful of the Main thread across different platforms (Dispatchers.Main).

### Data Layer
- Use `kotlinx-serialization` for all data transfer objects (DTOs).
- Keep data models in `commonMain`.

## 4. Testing
- Write unit tests in `commonTest` to ensure logic works across all targets.
- Use `kotlin.test` for assertions.
- Use `runTest` from `kotlinx-coroutines-test` for asynchronous code.

## 5. UI (Compose Multiplatform)
- Use Material3 components for consistent look and feel.
- Use `remember` and `mutableStateOf` for local state management.
- Keep UI components small and composable.

## 6. Resources
- Use `compose.components.resources` for shared images, strings, and fonts.
