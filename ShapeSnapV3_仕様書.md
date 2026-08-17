# ShapeSnapV3 現行仕様書

## 1. プロジェクト概要

- 本アプリは **Kotlin Multiplatform (KMP)** + **Compose Multiplatform (CMP)** で実装された、Android/iOS向けのクロスプラットフォームアプリです。
- 共有UIは `composeApp` モジュールの `commonMain` に集約され、iOSは `iosApp` の SwiftUI エントリから Compose 画面を表示します。
- 主機能は「Googleログイン」「プリセット閲覧」「いいね」「保存（ストレージ管理）」「自分の投稿閲覧」です。
- バックエンドは Firebase（Auth / Firestore）を使用し、KMP側では `dev.gitlive:firebase-*` 経由でアクセスします。

---

## 2. モジュール構成

### 2.1 ルート構成

- `composeApp`
  - KMP/CMP の本体（共通UI・共通ロジック・Android/iOS実装差分）
- `iosApp`
  - iOSアプリのネイティブエントリ（SwiftUI + Google Sign-In ブリッジ + Firebase iOS SDK初期化）
- `gradle/libs.versions.toml`
  - 依存関係とバージョン定義

### 2.2 sourceSet 構成

- `commonMain`
  - 画面UI、ViewModel、Repository、Datasourceインターフェース/実装（Firebase含む）、DI定義
- `androidMain`
  - Androidエントリ、Android用DI、CredentialManager実装、Android Manifest
- `iosMain`
  - iOSエントリ（`MainViewController`）、iOS用DI、Swiftブリッジ利用のCredentialProvider実装

---

## 3. 技術スタックと利用ライブラリ

## 3.1 言語・ビルド

- Kotlin `2.3.20`
- AGP `8.11.2`
- Compose Multiplatform `1.10.3`
- Android SDK
  - `compileSdk=36`, `targetSdk=36`, `minSdk=28`
- JVM target: `17`

### 3.2 UI・状態管理・DI

- `org.jetbrains.compose.*`（CMP UI）
- `org.jetbrains.androidx.lifecycle:*`（ViewModel/Compose連携）
- `org.jetbrains.androidx.navigation:navigation-compose`（型付きナビゲーション）
- `io.insert-koin:*`（DI。`viewModelOf` でViewModel注入）

### 3.3 バックエンド/データ

- `dev.gitlive:firebase-app/auth/firestore`（KMPからFirebase利用）
- `com.russhwolf:multiplatform-settings-*`（Key-Valueストレージ抽象）
- `org.jetbrains.kotlinx:kotlinx-serialization-json`（シリアライズ）
- `org.jetbrains.kotlinx:kotlinx-datetime`（日付変換）
- `org.jetbrains.kotlinx:kotlinx-coroutines-core`（非同期処理）
- `org.jetbrains.kotlinx:kotlinx-collections-immutable`（ImmutableList）

### 3.4 画像・通信・ログ

- `io.coil-kt.coil3:coil-compose`（画像表示）
- `io.coil-kt.coil3:coil-network-ktor3`（Coilネットワーク層）
- `io.ktor:ktor-client-core`
- Android: `ktor-client-okhttp`
- iOS: `ktor-client-darwin`
- `co.touchlab:kermit`（マルチプラットフォームログ）

### 3.5 Android Googleログイン関連

- `androidx.credentials:credentials`
- `androidx.credentials:credentials-play-services-auth`
- `com.google.android.libraries.identity.googleid:googleid`
- `com.google.gms.google-services` plugin

### 3.6 iOS 側ネイティブSDK（CocoaPods）

`iosApp/Podfile` で以下を導入:

- `FirebaseCore`
- `FirebaseAuth`
- `FirebaseFirestore`
- `GoogleSignIn (~> 8.0)`

---

## 4. アーキテクチャ

### 4.1 レイヤ構成

- `UI (Compose) -> ViewModel -> Repository -> Datasource -> Firebase/Settings`
- 依存注入は Koin:
  - 共通: `appModule()`
  - Android差分: `platformAndroidModule()`
  - iOS差分: `platformIosModule()`

### 4.2 依存の責務

- `AuthRepository`
  - 認証状態のFlow提供
  - Google認証クレデンシャルでサインイン
  - サインアウト
- `PresetRepository`
  - プリセット一覧購読（現行はスナップショット購読主体）
- `UserRepository`
  - ユーザードキュメント監視
  - いいね/保存更新
- `UserPostsRepository`
  - 自分の投稿のページ取得

### 4.3 ログインゲート

- `App()` で `authRepository.currentUser` を監視
  - `null`: `LoginScreen`
  - 非`null`: `MainScreen`

---

## 5. 画面仕様（現行）

### 5.1 ログイン画面 (`LoginScreen`)

- Googleログインボタンを表示
- `LoginViewModel.signInWithGoogle()` 実行
- `LoadState`（Idle/Loading/Success/Error）で状態管理

### 5.2 メイン画面 (`MainScreen`)

- `AppNavHost` + カスタムBottomBarを表示
- BottomSheetメニュー:
  - Home / Posts / Storage への遷移
  - ログアウト
  - 保存件数表示
- ログアウトは確認ダイアログを経由

### 5.3 Home (`HomeScreen`)

- Firestore `presets` コレクションのスナップショット購読で一覧表示
- Pull-to-refresh UI あり（現行実装は300ms待機で終了）
- 各カードで:
  - 画像表示（Coil）
  - いいねトグル
  - 保存トグル（保存は実質追加のみ。削除はStorage側）
- 無料保存上限:
  - `FREE_LIMIT = 5`
  - 上限超過時ダイアログ表示

### 5.4 Posts (`PostsScreen`)

- ログインユーザーの `users/{uid}/posts` をページング取得
- `Load more` ボタンで追記読み込み

### 5.5 Storage (`StorageScreen`)

- ユーザーの `storage` 配列（保存済みプリセットID）を表示
- 各IDを削除可能（`removePreset`）

### 5.6 Search / Profile

- 現在はプレースホルダー表示（「準備中」）

---

## 6. データ仕様

### 6.1 Firestore Database

- Database ID: `shape-snap`
- コレクション:
  - `presets`
  - `users`
  - `users/{uid}/posts`

### 6.2 `presets` ドキュメント（`PresetEntity`）

主なフィールド:

- `displayName` / `name`
- `description`
- `imageUrl` / `previewImageUrl`
- `characterTagId`
- `likedUserIds: List<String>`
- `savedUserIds: List<String>`
- `blendShapeWeights: Map<String, Double>`
- `createdAtEpochSeconds: Long?`

### 6.3 `users` ドキュメント（`UserEntity`）

- `displayName`
- `photoUrl`
- `storage: List<String>`（保存済みプリセットID）

### 6.4 `users/{uid}/posts`（`UserPost`）

- `id`
- `title`
- `body`
- `createdAt`

### 6.5 型変換仕様

- `PresetEntity.toPreset()` で:
  - `displayName` が空なら `name` を採用
  - `imageUrl` が無ければ `previewImageUrl`
  - `createdAtEpochSeconds` を `Instant` に変換
- `UserProfile.mergeWithFirestore()` で:
  - Auth側基本情報にFirestore拡張情報（storage等）をマージ

---

## 7. Android固有実装ロジック

### 7.1 エントリとDI

- `MainActivity` が `App()` をセット
- `MainApplication` で Koin を起動し、`appModule + platformAndroidModule` を登録

### 7.2 Googleログイン（Credential Manager）

- `AndroidCredentialProvider` が `CredentialManager.getCredentialAsync` を使用
- 取得するのは `GoogleIdTokenCredential`
- 返却トークン:
  - `idToken`: 取得
  - `accessToken`: `null`（Android CredentialManagerフローでは非提供）
- `GOOGLE_WEB_CLIENT_ID` を `BuildConfig` 経由で注入
  - 未設定時は `IllegalStateException`

### 7.3 Android Manifest

- `INTERNET` permission
- Application class: `MainApplication`
- Launcher Activity: `MainActivity`

### 7.4 Androidでの通信エンジン

- Coil/Ktor のため `ktor-client-okhttp` を `androidMain` に追加

---

## 8. iOS固有実装ロジック

### 8.1 起動・Compose連携

- `iOSApp.swift`
  - `FirebaseApp.configure()`
  - `GoogleSignInBridge.register()`
  - `.onOpenURL` で `GIDSignIn.sharedInstance.handle(url)` を実行
- `ContentView.swift` で `MainViewControllerKt.MainViewController()` を埋め込み

### 8.2 Koin初期化

- `MainViewController()` 内で `ensureIosKoinStarted()`
- 二重起動防止のため `koinStarted` フラグで制御

### 8.3 Googleログイン（Swiftブリッジ）

- Kotlin側: `IosCredentialProvider`
  - `IosCredentialBridge.tokenProvider` を呼び出してトークン取得
- Swift側: `GoogleSignInBridge`
  - `GIDSignIn` 実行
  - `idToken` と `accessToken` を Kotlinへ返却
- iOS Firebase Auth は実装上 `accessToken` を利用する前提で設計されている

### 8.4 CocoaPods依存

- `pod install` 必須
- `.xcworkspace` で起動する運用前提

### 8.5 iOSでの通信エンジン

- `iosMain` に `ktor-client-darwin` を追加

---

## 9. Firebase / 認証フロー仕様

### 9.1 認証フロー

1. UIから Googleサインイン要求
2. Platform別 `CredentialProvider` でトークン取得
3. `AuthRepository.signInWithGoogleCredentials()`
4. `AuthDatasourceImpl` が `GoogleAuthProvider.credential(idToken, accessToken)` を作成
5. Firebase Auth にサインイン
6. `authStateChanged` で `currentUser` が更新され画面遷移

### 9.2 ユーザードキュメント保証

- `HomeScreenViewModel` 初期化時、認証ユーザー検知で `ensureUserDocument`
- `users/{uid}` が無ければ作成（`merge=true`）

---

## 10. 例外処理・リトライ方針

- Firestoreアクセスは `FirestoreDatasourceImpl` で集中管理
- `UNAVAILABLE` 相当エラーを判定し指数バックオフで再試行
- `snapshots` にも `retryWhen` を適用
- `runCatching` の suspend文脈問題を回避するため `suspendRunCatching` を独自実装
- デシリアライズ失敗時はログを出し、該当ドキュメントをスキップ

---

## 11. 現行制約・未実装/注意点

- `HomeScreen` の `loadMore()` は現状未使用（スナップショット購読前提）
- `SearchScreen` / `ProfileScreen` はプレースホルダー
- `AdBannerView` は Android/iOS ともに高さ50dpの `Spacer` 実装（広告SDK未統合）
- `StorageScreen` はプリセット詳細ではなくID一覧表示
- Firebase設定ファイル（`google-services.json`, `GoogleService-Info.plist`）とOAuth設定整合性が前提

---

## 12. KMP/CMP初学者がつまづきやすいポイント

### 12.1 expect/actual と DI の役割分担

- `CredentialProvider` を `expect/actual` ではなくインターフェース + platform DIで差し替えている。
- どこを `commonMain` に置き、どこを `androidMain/iosMain` に逃がすかを誤ると依存解決に失敗しやすい。

### 12.2 iOSログインは「Swiftブリッジ」が必要

- KMP側だけでGoogle Sign-In完結ではなく、Swiftで `GIDSignIn` と URLハンドリングを実装する必要がある。
- `IosCredentialBridge.tokenProvider` 未登録時は必ず失敗する。

### 12.3 FirebaseをKMPで使う際のiOS依存

- `dev.gitlive:firebase-*` を使っていても、iOS側の公式SDK（Pods）導入は必要。
- `pod install` 後、`xcworkspace` を開かないとビルド失敗しやすい。

### 12.4 Android Credential Manager のトークン仕様差

- Android側は `accessToken` が得られず `null`。
- iOS側は `accessToken` を返す実装になっており、プラットフォーム差を設計で吸収している。

### 12.5 Coroutine + Flow + ライフサイクル

- `collectAsStateWithLifecycle` と `stateIn(WhileSubscribed)` の組み合わせを理解しないと、購読停止/再購読で挙動が読みにくい。

### 12.6 Firestoreエラーの包み込み

- Androidでは例外がラップされるケースがあり、単純な型判定だけでは `UNAVAILABLE` 判定漏れが起こる。
- 本実装は cause チェーンを辿って判定している。

### 12.7 Navigationの型付きルート

- `@Serializable data object` をルートに使う方式のため、従来の文字列ルート設計と混在させると混乱しやすい。

### 12.8 Coil + Ktor エンジン依存

- 共有コードで画像表示していても、各プラットフォームに対応エンジン（OkHttp/Darwin）を入れないとネット画像が読めない。

---

## 13. 開発・実行時の前提設定

- Android:
  - `GOOGLE_WEB_CLIENT_ID` を `local.secrets.properties`（gitignore 済み）で設定
  - Firebase設定 (`composeApp/google-services.json`) 配置
- iOS:
  - `iosApp/GoogleService-Info.plist` 配置
  - `cd iosApp && pod install`
  - Xcodeで `iosApp.xcworkspace` を開く
- 共通:
  - Firestore の `shape-snap` DBとコレクション構造が前提

---

## 14. 参考: 主要クラス一覧

- エントリ: `App`, `MainActivity`, `MainApplication`, `MainViewController`, `iOSApp`
- 認証: `CredentialProvider`, `AndroidCredentialProvider`, `IosCredentialProvider`, `GoogleSignInBridge`
- DI: `appModule`, `platformAndroidModule`, `platformIosModule`
- データ: `AuthDatasourceImpl`, `FirestoreDatasourceImpl`, `KeyValueDatasourceImpl`
- 画面VM: `LoginViewModel`, `HomeScreenViewModel`, `MainScreenViewModel`, `PostsScreenViewModel`, `StorageScreenViewModel`

---

本仕様書は、現在のコード実装を基準にした「現行仕様」です。将来の機能追加（検索・プロフィール実装、広告SDK連携、Homeページング本実装など）に合わせて更新してください。
