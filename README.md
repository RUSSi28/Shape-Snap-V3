# Shape-Snap-V3
CMP, KMPを使用したAndroid, iOS両対応のVRChatの改変支援アプリケーションです。

※このアプリはUnityエディタ拡張「[ShapeSnap](https://github.com/RUSSi28/ShapeSnapV2)」と使用する想定で作られています。

| ホーム画面 | 投稿画面 | 保存画面 | 設定画面 |
| --- | --- | --- | --- |
| <img width="1084" height="2412" alt="image" src="https://github.com/user-attachments/assets/300064e1-c1a4-4fa2-ab79-a9a2bae1b6a0" /> | <img width="1084" height="2412" alt="image" src="https://github.com/user-attachments/assets/ec197cc4-7c7c-47cb-b9b8-738c6e84571c" /> | <img width="1084" height="2412" alt="image" src="https://github.com/user-attachments/assets/c53d5909-3fc1-43da-999e-cb8e9dcb0ed3" /> | <img width="1084" height="2412" alt="image" src="https://github.com/user-attachments/assets/07f7ef83-198b-4f6f-bed0-4e1429824143" /> |
| 他の人が投稿している作品を閲覧・保存・共有 | 自身が投稿したプリセットをアバター名ごとに閲覧・投稿取り消し | 他の人の投稿しているプリセットのうち保存しているものを閲覧、保存の削除 | ログイン情報・ログアウト・アプリ規約・お問い合わせ先への遷移等 |

## 前提情報
VRChatは3Dモデリング技術を使用して作られた操作可能なアバターを利用してオンラインでコミュニケーションを行います。
アバターはBlendShapeという技術を利用して、シェイプキーという0から100までの値を調整して3Dモデルの形を簡単に変形することができます。

## アプリの目的
メッシュに対して用意されているシェイプキーの値とUnity上のシーンを画像化したもの等の情報をプリセットと定義します。
- **プリセットを簡単に他人に共有できる** (現在Androidのみリンクから直接詳細画面に遷移可能)
- 一度作ったプリセットを保存して次の改変時に役立てる
- 他の人が作ったプリセットを気軽にアバターに適用できる
- いいねや保存などのリアクションから集計した人気の造形がわかる

## 備考
本アプリはサーバー運用のために広告機能を搭載する前提で作成しています。それ以外の用途で広告収入を利用しません。

アプリのプレビューの画像に載っているアバターは ＠ぽんでろ 様の[オリジナル3Dアバター「しなの」](https://booth.pm/ja/items/6106863)です。
