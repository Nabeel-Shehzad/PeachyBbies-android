package com.peachy.bbies.model

data class Slime(
    val id: Int,
    val name: String,
    val texture: String,
    val multiplier: Int
) : Comparable<Slime> {
    override fun compareTo(other: Slime): Int {
        return if (this.name.lowercase().compareTo(other.name.lowercase()) == 0) {
            this.texture.lowercase().compareTo(other.texture.lowercase())
        } else {
            this.name.lowercase().compareTo(other.name.lowercase())
        }
    }

    override fun toString(): String {
        return "${this.id}  ${this.name} ${this.texture} ${this.multiplier}"
    }
}