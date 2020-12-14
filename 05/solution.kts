#!/usr/bin/env kscript

import kotlin.text.Regex

import java.io.File

fun binarySpacePartitioning(specifier: String, lowerHalfIdentifier: Char, upperHalfIdentifier: Char): Int {
    var lower = 0
    var upper = (1 shl specifier.length) - 1
    for (c in specifier) {
        when {
            c == lowerHalfIdentifier -> upper -= (upper + 1 - lower) / 2
            c == upperHalfIdentifier -> lower += (upper + 1 - lower) / 2
            else -> require(false)
        }
    }
    return lower
}

val String.row: Int get() = binarySpacePartitioning(substring(0, 7), 'F', 'B')
val String.col: Int get() = binarySpacePartitioning(substring(7, 10), 'L', 'R')
val String.id: Int get() = row * 8 + col

fun findMySeat(ids: List<Int>): Int {
    val existingSeats = BooleanArray(1 shl 10) { false }
    val availableSeats = BooleanArray(1 shl 10) { true }
    ids.forEach {
        existingSeats[it+1] = true
        existingSeats[it-1] = true
        availableSeats[it] = false
    }
    val mySeatCandidates = (existingSeats zip availableSeats)
        .map { (e, a) -> e && a }
        .withIndex()
        .filter { it.value }
        .map { it.index }
        .sorted()
    return mySeatCandidates[mySeatCandidates.size / 2]
}

val filename = args.getOrNull(0) ?: "boarding-passes"
val lines = File(filename).readLines()
val ids = lines.map { it.id }
println(ids.max())
println(findMySeat(ids))

// uncomment for logging
// val rows = lines.map { it.row }
// val cols = lines.map { it.col }
// val seats = rows zip cols
// (seats zip ids).forEach { (rc, i) -> println("${rc.first} ${rc.second} $i") }
