package analyzer


import java.io.File
import java.util.concurrent.Callable
import java.util.concurrent.Executor
import java.util.concurrent.Executors
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



fun createPatternObjList(db : File, threadManager : Executor) : List<Pattern> {

    val list = emptyList<Pattern>()
    val callables = emptyList<Callable<Pattern>>()
    val entries = db.readLines();


    entries.forEach {entry ->

        val priority : Int;
        val name : String
        val patternStr : String

        (priority, name, patternStr) = entr

    }

}

fun main(args  : Array<String>) {

    if (args.size < 2) {
        println("Needs 3 arguments.  FileName, pattern, desired file type")
        return;
    }

    val threadManager = Executors.newFixedThreadPool(100);
    val filesToCheck = File(args[0]);
    val patternDB = File(args[1])


    val patternList : List<Pattern> = createPatternObjList(patternDB, threadManager);







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