package net.plisio.sdk.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class PlisioDate(val secondsUTC: Long) {
    fun toInstant() = Instant.fromEpochSeconds(secondsUTC)
}