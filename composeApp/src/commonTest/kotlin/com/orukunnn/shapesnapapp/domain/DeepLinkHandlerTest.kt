package com.orukunnn.shapesnapapp.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DeepLinkHandlerTest {
    @Test
    fun `受信した共有リンクのプリセットIDを消費するまで保持する`() {
        DeepLinkHandler.receive("https://shape-snap-app.web.app/preset/preset-123")

        assertEquals("preset-123", DeepLinkHandler.pendingPresetId.value)

        DeepLinkHandler.consume("other-preset")
        assertEquals("preset-123", DeepLinkHandler.pendingPresetId.value)

        DeepLinkHandler.consume("preset-123")
        assertNull(DeepLinkHandler.pendingPresetId.value)
    }

    @Test
    fun `不正なリンクを受信した場合は遷移対象を保持しない`() {
        DeepLinkHandler.receive("https://example.com/preset/preset-123")

        assertNull(DeepLinkHandler.pendingPresetId.value)
    }
}
