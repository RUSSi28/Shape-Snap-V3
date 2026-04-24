import SwiftUI
import FirebaseCore
import GoogleSignIn

@main
struct iOSApp: App {
    init() {
        FirebaseApp.configure()
        // Kotlin 共通モジュールから呼び出される Google サインインの実装を登録する。
        GoogleSignInBridge.register()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
                .onOpenURL { url in
                    // GoogleSignIn の OAuth リダイレクト URL を受け取って処理する。
                    GIDSignIn.sharedInstance.handle(url)
                }
        }
    }
}
