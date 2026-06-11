package com.orukunnn.shapesnapapp.app

import androidx.compose.ui.graphics.Color

/**
 * アプリ全体で使用するカラー定数。
 * ハードコードを避け、ここで一元管理する。
 */
object ShapeSnapColors {
    /** ブランドカラー（水色） */
    val Brand = Color(0xFF62BCE7)

    /** アクセントカラー（ピンク / いいね） */
    val Accent = Color(0xFFEA6399)

    /** アイコンボタン用ダークティール */
    val IconTint = Color(0xFF005D53)

    /** セクション見出し・サブテキスト用ダークグレー */
    val TextSecondary = Color.DarkGray

    /** 補助テキスト・非活性要素用グレー */
    val TextTertiary = Color.Gray

    /** 背景・コンテナ用ホワイト */
    val Surface = Color.White

    /** プレースホルダー背景用ライトグレー */
    val Placeholder = Color.LightGray
}
