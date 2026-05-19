# AGENTS.md

## 基本方針

- 回答は日本語で行う。
- 回答には判断材料を必ず提示する。
- わからないことは推測で断定せず、「わからない」と明示する。
- 既存の設計、命名、ディレクトリ構成、実装パターンを優先する。
- ユーザーが明示しない限り、不要な大規模リファクタリングは行わない。
- ユーザーが明示しない限り、既存ファイルの削除や破壊的な Git 操作は行わない。
- 変更前に関連ファイルを読み、影響範囲を確認してから作業する。

## プロジェクト概要

このリポジトリは Kotlin Multiplatform / Compose Multiplatform プロジェクト。

- 共有コード: `composeApp/src/commonMain`
- Android 固有コード: `composeApp/src/androidMain`
- iOS 固有コード: `composeApp/src/iosMain`
- iOS アプリ側: `iosApp`
- 仕様書: `ShapeSnapV3_仕様書.md`

仕様や意図の確認が必要な場合は、まず `ShapeSnapV3_仕様書.md` と `README.md` を参照する。

## 開発環境

- IDE / エディタ候補: Cursor
- ビルドシステム: Gradle
- 主要言語: Kotlin
- UI: Compose Multiplatform
- Android ビルドには `./gradlew` を使用する。

## よく使うコマンド

Android Debug ビルド:

```sh
./gradlew :composeApp:assembleDebug
```

テスト:

```sh
./gradlew test
```

変更後は、影響範囲に応じて関連する Gradle タスクを実行する。実行できない場合は、その理由を回答に明記する。

## 作業時の注意

- `local.properties` や秘密情報を含むファイルは編集・公開しない。
- Firebase、Google、AdMob、認証関連の設定値は不用意に出力しない。
- `iosApp/Pods` 配下は依存関係の生成物として扱い、必要がない限り直接編集しない。
- `build`、`.gradle`、IDE 生成物などのビルド成果物は編集対象にしない。
- Android / iOS の片方だけに影響する変更か、共通コードに影響する変更かを確認してから実装する。
- UI 変更では、既存の Compose コンポーネント、テーマ、余白、色、命名に合わせる。

## Cursor での利用方針

Cursor に依頼する場合は、依頼文に以下を含めると精度が上がる。

- 目的: 何を実現したいか
- 対象: 触ってよいファイル、画面、機能
- 制約: 変えてはいけないこと、既存仕様、UI 方針
- 確認: 実行してほしいビルド・テストコマンド

依頼例:

```text
ログイン画面のエラーメッセージ表示を改善してください。

対象:
- composeApp/src/commonMain 配下

制約:
- 既存の ViewModel 構成は大きく変えない
- 文言は日本語
- 変更理由と判断材料を説明してください

確認:
- 可能なら ./gradlew :composeApp:assembleDebug を実行してください
```

## 回答に含めるべき内容

作業完了時の回答には、原則として以下を含める。

- 変更した内容
- 判断材料
- 影響範囲
- 実行した確認コマンド
- 確認できなかったことがあればその理由
