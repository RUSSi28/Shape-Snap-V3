package com.orukunnn.shapesnapapp.domain

object PresetShareLink {
    const val host: String = "shape-snap-app.web.app"
    private const val legacyFirebaseHostingHost = "shape-snap-app.firebaseapp.com"
    private const val scheme = "https"
    private const val pathPrefix = "/preset/"

    fun create(presetId: String): String? =
        presetId.takeIf(::isValidPresetId)?.let { "$scheme://$host$pathPrefix$it" }

    fun parse(url: String): String? {
        val prefix = supportedHosts
            .firstNotNullOfOrNull { linkHost ->
                "$scheme://$linkHost$pathPrefix".takeIf(url::startsWith)
            }
        val presetId = prefix?.let(url::removePrefix)
        return presetId?.takeIf {
            isValidPresetId(it) && '/' !in it && '?' !in it && '#' !in it
        }
    }

    private val supportedHosts = setOf(host, legacyFirebaseHostingHost)

    private fun isValidPresetId(presetId: String): Boolean =
        presetId.isNotBlank() && presetId.none {
            it.isWhitespace() || it == '/' || it == '?' || it == '#'
        }
}
