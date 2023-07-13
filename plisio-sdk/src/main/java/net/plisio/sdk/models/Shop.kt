package net.plisio.sdk.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlisioShop(
    @SerialName("name") val name: String,
    @SerialName("link") val url: String? = null,
    @SerialName("logo") val logoURL: String? = null
)