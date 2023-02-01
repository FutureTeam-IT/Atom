package it.futurecraft.atom.utils

import net.kyori.adventure.text.format.NamedTextColor
import org.intellij.lang.annotations.Pattern

object ColorConverter {
    @JvmStatic
    private val RGB_PATTERN = Regex("#([a-fA-F0-9]{6})")
    private const val ANSI_RGB = "\u001B[38;2;%d;%d;%dm"

    fun ansiColor(color: NamedTextColor, fallback: String): String {
        return ansiColor(color.value(), fallback)
    }

    fun ansiColor(@Pattern("#([a-fA-F0-9]{6})") color: String, fallback: String): String {
        if (!RGB_PATTERN.matches(color))
            return fallback

        return RGB_PATTERN.replace(color) {
            val hex = it.groupValues[1]
            ansiColor(hex.toInt(16), "")
        }
    }

    fun ansiColor(color: Int, fallback: String): String = ANSI_RGB.format(
        (color shr 16) and 0xFF,
        (color shr 8) and 0xFF,
        color and 0xFF
    )

    fun format(line: String): String = RGB_PATTERN.replace(line) {
        val hex = it.groupValues[1]
        ansiColor(hex.toInt(16), "")
    }
}

enum class AnsiColors(val code: String) {
    BLACK(ColorConverter.ansiColor(NamedTextColor.BLACK, "\u001B[0;30m")),
    DARK_BLUE(ColorConverter.ansiColor(NamedTextColor.DARK_BLUE, "\u001B[0;34m")),
    DARK_GREEN(ColorConverter.ansiColor(NamedTextColor.DARK_GREEN, "\u001B[0;32m")),
    DARK_AQUA(ColorConverter.ansiColor(NamedTextColor.DARK_AQUA, "\u001B[0;36m")),
    DARK_RED(ColorConverter.ansiColor(NamedTextColor.DARK_RED, "\u001B[0;31m")),
    DARK_PURPLE(ColorConverter.ansiColor(NamedTextColor.DARK_PURPLE, "\u001B[0;35m")),
    GOLD(ColorConverter.ansiColor(NamedTextColor.GOLD, "\u001B[0;33m")),
    GRAY(ColorConverter.ansiColor(NamedTextColor.GRAY, "\u001B[0;37m")),
    DARK_GRAY(ColorConverter.ansiColor(NamedTextColor.DARK_GRAY, "\u001B[0;30;1m")),
    BLUE(ColorConverter.ansiColor(NamedTextColor.BLUE, "\u001B[0;34;1m")),
    GREEN(ColorConverter.ansiColor(NamedTextColor.GREEN, "\u001B[0;32;1m")),
    AQUA(ColorConverter.ansiColor(NamedTextColor.AQUA, "\u001B[0;36;1m")),
    RED(ColorConverter.ansiColor(NamedTextColor.RED, "\u001B[0;31;1m")),
    LIGHT_PURPLE(ColorConverter.ansiColor(NamedTextColor.LIGHT_PURPLE, "\u001B[0;35;1m")),
    YELLOW(ColorConverter.ansiColor(NamedTextColor.YELLOW, "\u001B[0;33;1m")),
    WHITE(ColorConverter.ansiColor(NamedTextColor.WHITE, "\u001B[0;37;1m")),
    OBFUSCATED("\u001B[5m"),
    BOLD("\u001B[1m"),
    STRIKETHROUGH("\u001B[9m"),
    UNDERLINE("\u001B[4m"),
    ITALIC("\u001B[3m"),
    RESET("\u001B[m");

    override fun toString(): String = code
}
