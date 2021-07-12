This project is a demo project to explore various frameworks with MVI pattern and to achieve maximum code sharing between different platforms. 
At present we share code between iOS and Android platforms. This project uses clean architecture and shares code upto the `ViewModel` layer between iOS and Android
 - Jetpack Compose for Android UI
 - Kotlin Multiplatform to share code b/w iOS and Android
 - Kotlin Coroutines, Flow: Android + shared
 - Hilt: DI for Android
 - Ktor: networking for shared
 - Koin: DI for shared
 - Swinject: DI for iOS

### MVI
MVI layer with coroutines is inspired by Spotify's [mobius](https://github.com/spotify/mobius) framework and implemented using coroutines. 

### Disclaimer
This is currently a WIP project, and might contain a lot of exploratory code, so take inspirations at your own risk.

