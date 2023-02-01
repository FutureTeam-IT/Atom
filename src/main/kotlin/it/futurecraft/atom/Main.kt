@file:JvmName("Main")

package it.futurecraft.atom

import org.fusesource.jansi.AnsiConsole

fun main() {
    AnsiConsole.systemInstall()

    Atom.init()
    Atom.launch()
}