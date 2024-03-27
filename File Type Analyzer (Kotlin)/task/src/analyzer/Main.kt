package analyzer


import java.io.File


fun naiveSearch(pattern: String, content: String) : Boolean {
    if (pattern.length > content.length) return false;

    var patterFound = false

   bothLoops@for(cIndex in content.indices) {

        for (pIndex in pattern.indices) {
            if (content[cIndex + pIndex] == pattern[cIndex]) {
                if (pIndex == content.lastIndex) {
                    patterFound = true;
                    break@bothLoops;
                }
            } else {
                break;
            }
        }

    }

    return patterFound

}

fun naivePrefixFunction(string: String) : List<Int> {

}

fun isSuffix(pattern : String, content: String) : Boolean {

    var patternFound = false;

    fullLoop@for (sIndex in content.indices.reversed()) {
        for (pIndex in pattern.indices.reversed()) {
            if (content[sIndex - pIndex] == pattern[pIndex]) {
                if (pIndex == pattern.lastIndex) {
                    patternFound = true;
                    break@fullLoop
                }

            }
            else {
                break;
            }
        }
    }


    return  patternFound;
}

fun kmpPrefixFunction(pattern: String) : List<Int> {

    val lps = mutableListOf<Int>();
    val prefixes = mutableListOf<String>();
    var lastStr = "";

    for (ch in pattern) {
        lastStr += ch;
        prefixes.add(lastStr);
    }

    for (prefix in prefixes) {

        var length : Int = when {
            !isSuffix(prefix, pattern) -> 0;
            prefix.length == 1 -> 0;
            prefix.length == pattern.length -> 0;

            else -> {

                //find longest border

            }
        }



    }




    return lps;


}

fun kmpSearch(pattern: String, content : String) : Boolean {
    val patterFound = false







    return patterFound

}


fun main(args  : Array<String>) {

    if (args.size < 4) {
        println("Needs 3 arguments.  FileName, pattern, desired file type")
        return;
    }

    val file = File(args[1])
    val pattern = args[2]
    val desiredType = args[3]

    val searchAlgo = when(args[3]) {
        "--KMP" -> ::kmpSearch
        "--naive" -> ::naiveSearch
        else -> null
    }

    when {
        searchAlgo == null -> println ("${args[3]} is a invalid algorithm. Try again.")
        !file.exists() -> println("File ${args[0]} does not exist ):")

        else -> {
            val fileContents = file.readText()
            val patternFound = searchAlgo(pattern, fileContents)
            println(if (patternFound) desiredType else "Unknown file type")
        }

    }







}