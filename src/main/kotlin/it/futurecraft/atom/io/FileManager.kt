package it.futurecraft.atom.io

import com.akuleshov7.ktoml.Toml
import com.akuleshov7.ktoml.TomlIndentation
import com.akuleshov7.ktoml.TomlInputConfig
import com.akuleshov7.ktoml.TomlOutputConfig
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.ConcurrentHashMap

object FileManager {
    @JvmStatic
    val toml = Toml(
        inputConfig = TomlInputConfig(
            ignoreUnknownNames = true,
            allowEmptyValues = false,
            allowNullValues = true,
            allowEscapedQuotesInLiteralStrings = true,
            allowEmptyToml = true,
        ),
        outputConfig = TomlOutputConfig(
            indentation = TomlIndentation.FOUR_SPACES,
        ),
    )

    @JvmStatic
    val json = Json {
        encodeDefaults = true
        prettyPrint = true
    }

    @PublishedApi
    internal val cache = ConcurrentHashMap<Int, Any>()

    /**
     * Reads the content of the file at the given path.
     *
     * @param path The path to the file.
     *
     * @return The file content if file exits, null otherwise.
     */
    fun read(path: Path): String? = when (Files.exists(path)) {
        true -> String(Files.readAllBytes(path))
        false -> null
    }

    @PublishedApi
    internal fun key(path: Path): Int = path.toString().hashCode()

    /**
     * Reads the content of the file at the given path.
     * Then the content is parsed using the provided deserializer.
     *
     * @param path The path to the file.
     * @param deserializer The deserializer to use to parse the file.
     *
     * @return The file content if file exits, null otherwise.
     */
    inline fun <reified T : Any> read(path: Path, deserializer: (String) -> T): T? {
        val key = key(path)

        if (cache.containsKey(key)) {
            return cache[key] as T
        }

        val content = read(path) ?: return null
        val data = deserializer(content)

        cache[key] = data

        return data
    }

    /**
     * Reads and parses a TOML file.
     * If the file does not exist, default will be serialized and write into new file at the provided path.
     *
     * @param default The default value of the file.
     * @param builder The path build.
     *
     * @return The new instance of the parsed file.
     */
    @OptIn(InternalSerializationApi::class)
    inline fun <reified T : Any> toml(default: T, builder: PathBuilder.() -> Unit): T {
        val pathBuilder = PathBuilder()
        pathBuilder.builder()

        val path = pathBuilder.build()
        return read(path) { toml.decodeFromString(T::class.serializer(), it) } ?: write(
            path,
            default
        ) { toml.encodeToString(T::class.serializer(), it) }
    }

    /**
     * Reads and parses a TOML file.
     * If the file does not exist, default will be serialized and write into new file at the provided path.
     *
     * @param default The default value of the file.
     * @param builder The path build.
     *
     * @return The new instance of the parsed file.
     */
    @OptIn(InternalSerializationApi::class)
    inline fun <reified T : Any> json(default: T, builder: PathBuilder.() -> Unit): T {
        val pathBuilder = PathBuilder()
        pathBuilder.builder()

        val path = pathBuilder.build()
        return read(path) { json.decodeFromString(T::class.serializer(), it) } ?: write(
            path,
            default
        ) { json.encodeToString(T::class.serializer(), it) }
    }

    /**
     * Serialize the data object with the given serializer and writes to the file at the given path.
     *
     * @param path The path of the file.
     * @param data The data to serialize.
     * @param serializer The serializer used to parse the data.
     *
     * @return The data object.
     */
    inline fun <reified T : Any> write(path: Path, data: T, serializer: (T) -> String): T {
        val content = serializer(data)
        val parent = path.parent

        if (!Files.exists(parent)) {
            Files.createDirectories(parent)
        }

        Files.writeString(path, content)

        val key = key(path)
        cache[key] = data

        return data
    }
}