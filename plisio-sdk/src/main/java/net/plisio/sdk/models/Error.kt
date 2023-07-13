package net.plisio.sdk.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlisioError(
    @SerialName("name") val name: String? = null,
    @SerialName("message") override val message: String? = null,
    @SerialName("code") val code: Int = 0
) : Throwable(message)

data class PlisioErrorWithResponseText(
    override val cause: Throwable,
    val responseText: String
) : Throwable(cause)

data class PlisioNotFoundError(
    val url: String
) : Throwable("PlisioNotFoundError: Request '$url' returned status 404 Not Found")