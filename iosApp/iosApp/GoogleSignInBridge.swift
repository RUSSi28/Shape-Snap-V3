import Foundation
import UIKit
import FirebaseCore
import GoogleSignIn
import ComposeApp

/// Kotlin 側の `IosCredentialBridge.tokenProvider` に Google サインイン処理を登録するための Swift ブリッジ。
/// `iOSApp.init()` 内で `GoogleSignInBridge.register()` を呼ぶことで、
/// 共通モジュールから Google サインイン (idToken 取得) を実行できるようになる。
enum GoogleSignInBridge {
    /// Kotlin 側の IosCredentialBridge に Swift 実装を登録する。
    static func register() {
        IosCredentialBridge.shared.tokenProvider = { onResult in
            DispatchQueue.main.async {
                performSignIn { idToken, accessToken, errorMessage in
                    onResult(idToken, accessToken, errorMessage)
                }
            }
        }
    }

    /// 実際のサインインフロー。idToken と accessToken の両方を返す。
    private static func performSignIn(
        completion: @escaping (_ idToken: String?, _ accessToken: String?, _ errorMessage: String?) -> Void
    ) {
        guard let clientID = FirebaseApp.app()?.options.clientID, !clientID.isEmpty else {
            completion(nil, nil, "Firebase clientID が見つかりません。GoogleService-Info.plist を確認してください。")
            return
        }

        let configuration = GIDConfiguration(clientID: clientID)
        GIDSignIn.sharedInstance.configuration = configuration

        guard let presentingVC = topMostViewController() else {
            completion(nil, nil, "Presenting view controller が取得できません。")
            return
        }

        GIDSignIn.sharedInstance.signIn(withPresenting: presentingVC) { result, error in
            if let error = error {
                completion(nil, nil, error.localizedDescription)
                return
            }
            guard let user = result?.user,
                  let idToken = user.idToken?.tokenString else {
                completion(nil, nil, "idToken が取得できませんでした。")
                return
            }
            // accessToken は GoogleSignIn では非 Optional。Firebase Auth iOS が必須とする。
            let accessToken = user.accessToken.tokenString
            completion(idToken, accessToken, nil)
        }
    }

    /// 現在表示中の最前面 UIViewController を取得する。
    private static func topMostViewController() -> UIViewController? {
        let scenes = UIApplication.shared.connectedScenes
        let windowScene = scenes.compactMap { $0 as? UIWindowScene }.first
        let keyWindow = windowScene?.windows.first(where: { $0.isKeyWindow })
            ?? windowScene?.windows.first

        var vc = keyWindow?.rootViewController
        while let presented = vc?.presentedViewController {
            vc = presented
        }
        return vc
    }
}
