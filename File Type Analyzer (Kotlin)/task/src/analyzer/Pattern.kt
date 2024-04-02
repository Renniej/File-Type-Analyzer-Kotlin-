package analyzer


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

class Pattern (val name : String, val  pattern : String, val priority : Int) {

    val lps : IntArray

    init {
        lps = computeLPSArray(pattern)
    }

}