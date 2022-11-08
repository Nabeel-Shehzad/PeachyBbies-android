package com.peachy.bbies.model

data class Slime(
    val id: Int,
    val name: String,
    val texture:String) : Comparable<Slime> {
    override fun compareTo(other: Slime): Int {
        return if (this.name.compareTo(other.name) == 0) {
            this.texture.compareTo(other.texture)
        } else {
            this.name.compareTo(other.name)
        }
    }
    override fun toString(): String {
        return "${this.id}  ${this.name} ${this.texture}"
    }
}