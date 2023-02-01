package it.futurecraft.atom.io

import java.nio.file.Path

data class PathBuilder(
    var path: Path? = null,
    var name: String = "",
    var format: Format = Format.TOML
)

fun PathBuilder.build(): Path {
    return path?.resolve("$name.${format.ext}") ?: Path.of("$name.${format.ext}")
}

infix fun Path.resolve(other: PathBuilder): Path = resolve(other.build())