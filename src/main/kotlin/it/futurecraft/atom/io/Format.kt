package it.futurecraft.atom.io

data class Format(val name: String, val ext: String) {
    companion object
}

val Format.Companion.TOML
    get() = Format("TOML", "toml")

val Format.Companion.JSON
    get() = Format("Json", "json")