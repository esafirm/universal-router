@file:JvmName("Util")

package nolambda.linkrouter

import okio.JvmName

/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Increments [startIndex] until this string is not ASCII whitespace. Stops at [endIndex].
 */
fun String.indexOfFirstNonAsciiWhitespace(startIndex: Int = 0, endIndex: Int = length): Int {
    for (i in startIndex until endIndex) {
        when (this[i]) {
            '\t', '\n', '\u000C', '\r', ' ' -> Unit
            else -> return i
        }
    }
    return endIndex
}

/**
 * Decrements [endIndex] until `input[endIndex - 1]` is not ASCII whitespace. Stops at [startIndex].
 */
fun String.indexOfLastNonAsciiWhitespace(startIndex: Int = 0, endIndex: Int = length): Int {
    for (i in endIndex - 1 downTo startIndex) {
        when (this[i]) {
            '\t', '\n', '\u000C', '\r', ' ' -> Unit
            else -> return i + 1
        }
    }
    return startIndex
}

/**
 * Returns the index of the first character in this string that contains a character in
 * [delimiters]. Returns endIndex if there is no such character.
 */
fun String.delimiterOffset(delimiters: String, startIndex: Int = 0, endIndex: Int = length): Int {
    for (i in startIndex until endIndex) {
        if (this[i] in delimiters) return i
    }
    return endIndex
}

/**
 * Returns the index of the first character in this string that is [delimiter]. Returns [endIndex]
 * if there is no such character.
 */
fun String.delimiterOffset(delimiter: Char, startIndex: Int = 0, endIndex: Int = length): Int {
    for (i in startIndex until endIndex) {
        if (this[i] == delimiter) return i
    }
    return endIndex
}

fun Char.parseHexDigit(): Int = when (this) {
    in '0'..'9' -> this - '0'
    in 'a'..'f' -> this - 'a' + 10
    in 'A'..'F' -> this - 'A' + 10
    else -> -1
}

infix fun Byte.and(mask: Int): Int = toInt() and mask
infix fun Short.and(mask: Int): Int = toInt() and mask
infix fun Int.and(mask: Long): Long = toLong() and mask
