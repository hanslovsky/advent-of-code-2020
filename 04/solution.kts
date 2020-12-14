#!/usr/bin/env kscript

import kotlin.text.Regex

import java.io.File

val filename = args.getOrNull(0) ?: "passports"

val lines = File(filename).readLines()

val requiredFields = hashSetOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")

fun String.within(min: Int, max: Int) = toIntOrNull()?.let { it >= min && it <= max }
fun String?.within(min: Int, max: Int) = this?.within(min, max) ?: false
fun String.checkHeight() = when {
    endsWith("cm") -> substring(0, 3).within(150, 193)
    endsWith("in") -> substring(0, 2).within(59, 76)
    else -> false
}
fun String?.checkHeight() = (this?.checkHeight() ?: false)
val hairColorRegex = "#[0-9a-f]{6,6}".toRegex()
fun String?.checkHairColor() = this?.let { hairColorRegex.matches(it) } ?: false
val eyeColors = hashSetOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
fun String?.checkEyeColor() = this?.let { it in eyeColors } ?: false
val passportIdRegex = "[0-9]{9,9}".toRegex()
fun String?.checkPassportId() = this?.let { passportIdRegex.matches(it) } ?: false

val validators = mapOf<String, (String?) -> Boolean> (
    "byr" to { v: String? -> v.within(1920, 2002) },
    "iyr" to { v: String? -> v.within(2010, 2020) },
    "eyr" to { v: String? -> v.within(2020, 2030) },
    "hgt" to { v: String? -> v.checkHeight() },
    "hcl" to { v: String? -> v.checkHairColor() },
    "ecl" to { v: String? -> v.checkEyeColor() },
    "pid" to { v: String? -> v.checkPassportId() }
)

fun Set<String>.hasAllRequiredFields(requiredFields: Set<String>) = requiredFields.all { it in this }
fun Map<String, String>.validate(validators: Map<String, (String?) -> Boolean>) = validators.entries.all { (k, v) -> v(this[k]) }

fun parseLine(line: String, passport: MutableMap<String, String>) {
    line.split(" ").forEach { entry ->
        val (key, value) = entry.split(":")
        passport[key] = value
    }
}

fun scanLines(lines: List<String>): List<Map<String, String>> {
    val splitIndices: List<Int> = lines.withIndex().filter { it.value.isEmpty() }.map { it.index }.let { listOf(-1) + it + lines.size }
    return (splitIndices.subList(0, splitIndices.size - 1) zip splitIndices.subList(1, splitIndices.size)).map { (start, stop) ->
        mutableMapOf<String, String>().also { passport ->
            lines.subList(start + 1, stop).forEach { parseLine(it, passport) }
        }
    }
}

fun List<Map<String, String>>.countValid(predicate: (Map<String, String>) -> Boolean) = this.filter(predicate).size

val passports = scanLines(lines)
println(passports.countValid { it.keys.hasAllRequiredFields(requiredFields) })
println(passports.countValid { it.validate(validators) })

