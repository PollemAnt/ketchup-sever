package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class ImageItem(val id: Int, val url: String, val title: String)
