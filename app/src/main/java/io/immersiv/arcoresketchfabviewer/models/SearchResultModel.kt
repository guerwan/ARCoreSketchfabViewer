package io.immersiv.arcoresketchfabviewer.models

data class SearchResultModel(
    val next: String,
    val previous: String,
    val results: ArrayList<SketchfabModel>
)