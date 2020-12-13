#!/usr/bin/env kscript

import java.io.File

val filename = args.getOrNull(0) ?: "expense-report"
val numbers = File(filename).readLines().map { it.toInt() }.toIntArray()

fun findProduct2(numbers: IntArray): Int? {
    for (i in 0 until numbers.size) {
        val n1 = numbers[i]
        for (k in (i+1) until numbers.size) {
            val n2 = numbers[k]
            if (n1 + n2 == 2020)
                return n1 * n2
        }
    }
    return null
}

fun findProduct3(numbers: IntArray): Int? {
    for (i in 0 until numbers.size) {
        val n1 = numbers[i]
        for (k in (i+1) until numbers.size) {
            val n2 = numbers[k]
            for (n in (k+1) until numbers.size) {
                val n3 = numbers[n]
                if (n1 + n2 + n3 == 2020)
                    return n1 * n2 * n3
            }
        }
    }
    return null
}

println(findProduct2(numbers))
println(findProduct3(numbers))
