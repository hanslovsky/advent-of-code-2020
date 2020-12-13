#!/usr/bin/env kscript

import java.io.File

val filename = args.getOrNull(0) ?: "passwords"

data class Password(val min: Int, val max: Int, val c: Char, val password: String)
fun Password.isValid1(): Boolean {
    var count = 0
    for (p in password) {
        if (p == c) {
            ++count
            if (count > max)
                return false
        }
    }
    return count >= min
}
fun Password.isValid2(): Boolean {
    var count = 0
    val v1 = if (password[min-1] == c) 1 else 0
    val v2 = if (password[max-1] == c) 1 else 0
    return v1 + v2 == 1
}

val passwords = File(filename).readLines().map { line ->
    val (rule, value) = line.split(": ")
    val (stringRange, c) = rule.split(" ")
    val (min, max) = stringRange.split("-").map { it.toInt() }.toIntArray()
    Password(min, max, c[0], value)
}


println(passwords.filter { it.isValid1() }.size)
println(passwords.filter { it.isValid2() }.size)

