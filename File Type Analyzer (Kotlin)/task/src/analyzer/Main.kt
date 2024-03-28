package analyzer


import java.io.File
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime


fun naiveSearch(pattern: String, content: String) : Boolean {
    if (pattern.length > content.length) return false;


    var patterFound = false

    var n = content.length
    var m = pattern.length
    var range = 0..(n-m+1)

    for (i in range) {
        var j = 0

        while (j < m){
            if (content[i + j] != pattern[j]) {
                break
            }

            j += 1
        }

        if (j == m) {
            patterFound = true;
            break;
        }

    }






    return patterFound

}


fun kmpSearch(pattern: String, text: String): Boolean {

    val lps = computeLPSArray(pattern)
    var i = 0
    var j = 0
    while (i < text.length) {
        if (pattern[j] == text[i]) {
            j++
            i++
        }
        if (j == pattern.length) {
            return true;
        } else if (i < text.length && pattern[j] != text[i]) {
            if (j != 0) {
                j = lps[j - 1]
            } else {
                i++
            }
        }
    }

    return false
}

fun computeLPSArray(pattern: String): IntArray {
    val lps = IntArray(pattern.length)
    var len = 0
    var i = 1
    lps[0] = 0
    while (i < pattern.length) {
        if (pattern[i] == pattern[len]) {
            len++
            lps[i] = len
            i++
        } else {
            if (len != 0) {
                len = lps[len - 1]
            } else {
                lps[i] = len
                i++
            }
        }
    }
    return lps
}


@OptIn(ExperimentalTime::class)
fun main(args  : Array<String>) {

    if (args.size < 4) {
        println("Needs 3 arguments.  FileName, pattern, desired file type")
        return;
    }

    val file = File(args[1])
    val pattern = args[2]
    val desiredType = args[3]

    val searchAlgo = when(args[0]) {
        "--KMP" -> ::kmpSearch
        "--naive" -> ::naiveSearch
        else -> null
    }

    when {
        searchAlgo == null -> println ("${args[3]} is a invalid algorithm. Try again.")
        !file.exists() -> println("File ${file.path} does not exist ):")

        else -> {
            val fileContents = file.readText()
            val patternFound : Boolean;

            val elapsed : Duration =  measureTime {
                patternFound = searchAlgo(pattern, fileContents)
            }



            println(if (patternFound) desiredType else "Unknown file type")
            println("It took ${elapsed.inWholeSeconds} seconds")
        }

    }







}