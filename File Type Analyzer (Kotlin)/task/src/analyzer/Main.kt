package analyzer


import java.io.File
import java.security.MessageDigest
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


fun kmpSearch(p: Pattern, text: String): Boolean {

    val lps = p.lps
    val pattern = p.str

 
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

fun hash(str : String): String {  //sha263
    val bytes = str.toByteArray()
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    return digest.fold(StringBuilder()) { sb, it -> sb.append("%02x".format(it)) }.toString()
}


fun naiveMatch(pattern : String, text : String) : Boolean {
    if (pattern.length != text.length) return false;

    var match = true;

    for (i in pattern.indices) {

        if (pattern[i] != text[i]) {
            match = false;
            break;
        }

    }

    return  match;
}

fun rabinKarp(pattern : Pattern, text  : String ) : Boolean{

    val patternHash = hash(pattern.str)
    val pLength = pattern.str.length;
    var matchFound = false;

    for ( i in text.indices) {
        try {
            val sub = text.substring(i, i + pLength)
            val subHash = hash(sub)

            if (subHash == patternHash) {

                if (naiveMatch(pattern.str, sub)) {
                    matchFound = true;
                    break;
                }
            }
        } catch (e : Exception) {
            break; //Assume we are at the end of the text and t
        }
    }

    return  matchFound;
}

fun findMatchingPattern(file : File, patternDB : List<Pattern>, executor: ExecutorService ) {

    val searchTasks = mutableListOf<Callable<Any>>();
    val matchingPatterns = Collections.synchronizedList(mutableListOf<Pattern>())
    var foundPattern : Pattern? = null;


    //add any patterns that are found into the file into the matchingPatterns list
    patternDB.forEach { pattern ->
        searchTasks.add(
                Callable {



                    if (rabinKarp(pattern, file.readText())) {
                
                        matchingPatterns.add(pattern)
                    }
                }
        )
    }

    executor.invokeAll(searchTasks);

    foundPattern = matchingPatterns.maxByOrNull { it.priority }

    if (foundPattern != null) {
        println("${file.name}: ${foundPattern.name}")
    } else {
        println("${file.name}: Unknown file type")
    }


}

fun createPatternDB(dbFile : File, executor: ExecutorService) : List<Pattern> {

    val strDB = dbFile.readLines();
    val initTasks = mutableListOf<Callable<Pattern>>()
    val patternDB = mutableListOf<Pattern>()

    strDB.forEach {entry : String ->


        initTasks.add(
                Callable {
                    val (priority, patternStr, name) = entry.split(";").map { it.trim('"') }
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

