#!/usr/bin/env kscript

@file:MavenRepository("jitpack", "https://jitpack.io")
@file:DependsOnMaven("com.github.saalfeldlab:imklib2:022d06220c")

import java.io.File

import net.imglib2.RandomAccessibleInterval as RAI
import net.imglib2.imklib.*
import net.imglib2.type.numeric.IntegerType
import net.imglib2.view.Views

fun linesAsImage(lines: List<String>) = lines
    .joinToString("")
    .let { fl -> imklib.bytes(lines[0].length.toLong(), lines.size.toLong()) { if(fl[it] == '#') 1 else 0 } }

fun <T: IntegerType<T>> findNumTrees(img: RAI<T>, offsetX: Int, offsetY: Int): Int {
    val map = Views.extendPeriodic(img)
    return (0 until img.dimension(1).toInt() step offsetY).mapIndexed { idx, y -> map[offsetX * idx, y].integer }.sum()
}

fun <T: IntegerType<T>> findNumTreesMultipleSlopes(img: RAI<T>, vararg offsets: Pair<Int, Int>) = IntArray(offsets.size) {
    findNumTrees(img, offsets[it].first, offsets[it].second)
}

val filename = args.getOrNull(0) ?: "trees"

val lines = File(filename).readLines()

val img = linesAsImage(lines)
val map = Views.extendPeriodic(img)
val numTrees = findNumTrees(img, 3, 1)
val prodNumTreesMultipleSlopes = findNumTreesMultipleSlopes(img, 1 to 1, 3 to 1, 5 to 1, 7 to 1, 1 to 2).map { it.toLong() }.reduce { s1, s2 -> s1 * s2 }

println(numTrees)
println(prodNumTreesMultipleSlopes)

