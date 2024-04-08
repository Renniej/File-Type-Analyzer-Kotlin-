package analyzer


import java.io.File
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread


fun kmpSearch(p: Pattern, text: String): Boolean {

    val lps = p.lps
    val pattern = p.pattern

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


fun findMatchingPattern(file : File, patternDB : List<Pattern>, executor: ExecutorService ) {

    val searchTasks = mutableListOf<Callable<Any>>();
    val matchingPatterns = Collections.synchronizedList(mutableListOf<Pattern>())
    var foundPattern : Pattern? = null;


    //add any patterns that are found into the file into the matchingPatterns list
    patternDB.forEach { pattern ->
        searchTasks.add(
                Callable {
                    if (kmpSearch(pattern, file.readText())) {

                        matchingPatterns.add(pattern)
                    }
                }
        )
    }

    executor.invokeAll(searchTasks);

    foundPattern = matchingPatterns.maxByOrNull { it.priority }

    if (foundPattern != null) {
        println("${file.name}: ${foundPattern.name}")
    }


}

fun createPatternDB(dbFile : File, executor: ExecutorService) : List<Pattern> {

    val strDB = dbFile.readLines();
    val initTasks = mutableListOf<Callable<Pattern>>()
    val patternDB = mutableListOf<Pattern>()

    strDB.forEach {entry : String ->


        initTasks.add(
                Callable {
                    val (priority, patternStr, name) = entry.split(";")
                    val pattern = Pattern(name, patternStr, priority.toInt())
                    pattern;
                }
        )
    }

    val results = executor.invokeAll(initTasks)

    for ( pattern in results){
        patternDB.add(pattern.get());
    }


    return patternDB;
}


fun main(args  : Array<String>) {

    if (args.size < 2) {
        println("Needs 2 arguments : test files, patterns database")
        return;
    }


    val testFolder = File(args[0]);
    val dbFile = File(args[1])
    val threadManager = Executors.newFixedThreadPool(100);
    val patternDB = createPatternDB(dbFile,threadManager);


    testFolder.listFiles()?.forEach { file ->
        threadManager.submit {
            findMatchingPattern(file,patternDB,threadManager)
        }
    }



    threadManager.awaitTermination(10, TimeUnit.SECONDS);

}

