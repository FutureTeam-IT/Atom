package it.futurecraft.atom.utils

import kotlin.test.Test
import kotlin.test.assertEquals


class ColorConverterTest {

    @Test
    fun rgbToAnsi() {
        val ansi = ColorConverter.ansiColor("#ff0000", "")

        assertEquals("\u001B[38;2;255;0;0m", ansi)
    }

    @Test
    fun rgbLineToAnsi() {
        val ansi = ColorConverter.format("#ff0000Red #00ff00Green #0000ffBlue")
        println("Fromatted string is = $ansi")

        assertEquals("\u001B[38;2;255;0;0mRed \u001B[38;2;0;255;0mGreen \u001B[38;2;0;0;255mBlue", ansi)
    }

    @Test
    fun rgbIntToAnsi() {
        val ansi = ColorConverter.ansiColor(0xaa23c2, "")
        println("Fromatted string is =$ansi ANSI")

        assertEquals("\u001B[38;2;170;35;194m", ansi)

    }
}