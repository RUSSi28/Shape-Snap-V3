package com.orukunnn.shapesnapapp.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PresetShareLinkTest {
    @Test
    fun `有効なプリセットIDからHTTPS共有リンクを作成できる`() {
        assertEquals(
            "https://shape-snap-app.web.app/preset/preset-123",
            PresetShareLink.create("preset-123"),
        )
    }

    @Test
    fun `有効なHTTPS共有リンクからプリセットIDを取得できる`() {
        assertEquals(
            "preset-123",
            PresetShareLink.parse("https://shape-snap-app.web.app/preset/preset-123"),
        )
    }

    @Test
    fun `想定外のホストとパスを拒否する`() {
        assertNull(PresetShareLink.parse("https://example.com/preset/preset-123"))
        assertNull(PresetShareLink.parse("https://shape-snap-app.web.app/preset/"))
        assertNull(PresetShareLink.parse("https://shape-snap-app.web.app/preset/preset-123/extra"))
    }
}
