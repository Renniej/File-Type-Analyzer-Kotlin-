package analyzer


import java.io.File





fun naiveSearch(pattern: String, content: String) : Boolean {
    val found = false


    return found

}

fun kmpSearch(pattern: String, content : String) : Boolean {
    val found = false


    return found

}


fun main(args  : Array<String>) {

    if (args.size < 4) {
        println("Needs 3 arguments.  FileName, pattern, desired file type")
        return;
    }


    val algorithm = args[0]
    val file = File(args[1])
    val pattern = args[2]
    val desiredType = args[3]

    val searchAlgo = when(args[3]) {
        "--KMP" -> ::kmpSearch
        "--naive" -> ::naiveSearch
        else -> null
    }

    when {
        searchAlgo == null ->println ("${args[3]} is a invalid algorithm. Try again.")
        !file.exists() -> println("File ${args[0]} does not exist ):")
        else -> {
            val fileContents = file.readText()
            val patternFound = searchAlgo(pattern, fileContents)
            println(if (patternFound) desiredType else "Unknown file type")
        }

    }







}