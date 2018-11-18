package io.immersiv.arcoresketchfabviewer.models

data class SketchfabModel(
    val name: String,
    val description: String,
    val uid: String,
    val vertexCount: Int,
    val thumbnails: SketchfabThumbnailModel
) {
    fun getBiggestThumbnailUrl(): String? {
        if (thumbnails.images.isNotEmpty()) {
            thumbnails.images.sortedByDescending { it.size }
            return thumbnails.images[0].url
        }
        return null
    }
}