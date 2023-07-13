package net.plisio.sdk.models

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class PlisioAmount(val value: Double) : Comparable<PlisioAmount> {
    override fun compareTo(other: PlisioAmount): Int = value.compareTo(other.value)
    override fun toString(): String = value.toString()

    companion object {
        val Zero = PlisioAmount(0.0)
    }
}