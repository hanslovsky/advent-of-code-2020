#!/usr/bin/env kscript

import kotlin.text.Regex

import java.nio.file.Files
import java.nio.file.Paths

fun findUniqueAnswers(groupAnswers: List<List<String>>): List<Set<Char>> {
    return groupAnswers.map { group ->
        group.fold(mutableSetOf<Char>()) { s, answers -> answers.forEach { s += it }; s  }
    }
}

fun findAnswersYesByAll(groupAnswers: List<List<String>>): List<Map<Char, Int>> {
    return groupAnswers
        .map { group -> group.map { person -> mutableSetOf<Char>().also { s -> person.forEach { s += it } } }.toList() }
        .map { group ->
            group.fold(mutableMapOf<Char, Int>()) { m, answers -> answers.forEach { m[it] = m.getOrPut(it) { group.size } - 1  }; m }
        }
}

fun List<Set<Char>>.sum() = map { it.size }.sum()
fun List<Map<Char, Int>>.countZeros() = map { m -> m.count { (_, v) -> v <= 0 } }.sum()


val filename = args.getOrNull(0) ?: "questions"
val groupAnswers = String(Files.readAllBytes(Paths.get(filename))).split("\n\n").map { it.split("\n").filterNot { it.isEmpty() } }
val answerSum = findUniqueAnswers(groupAnswers).sum()
val answerYesByAllSum = findAnswersYesByAll(groupAnswers).countZeros()
println(answerSum)
println(answerYesByAllSum)

