package com.example.testui

class Kompres {

    fun kompresText(text: String, algorithm: Int): String {
        val asciiStringBit = textToAscii(text)
        val charset = getCharsetFromText(text)
        val freqList = getFreqOfEachCharFromText(text, charset)
        val sortedCharset = insertionSort(charset, freqList)
        var compressedBit = text

        if (algorithm == 0) {

        } else if (algorithm == 1) {
            val fibonacciCode = FibonacciCode()
            val fibonacciCodeList = fibonacciCode.generateFibonacciCodeList(charset.size)
            for(i in 0..sortedCharset.size-1) {
                compressedBit = compressedBit.replace(sortedCharset[i].toString(), fibonacciCodeList[i])
            }
        }

        //Padding Bit and Flag Bit
        val padLength = 8-(compressedBit.length%8)
        val paddingBit = "0".repeat(padLength)
        val flagBit = padLength.toString(2).padStart(8, '0')
        compressedBit = compressedBit + paddingBit + flagBit
        //Convert compressedBit to Text
        var compressedText = asciiToText(compressedBit) + "|*|" + sortedCharset.joinToString("")

        return compressedText
    }

    private fun textToAscii(text: String): String {
        var asciiStringBit = ""
        for(char in text) {
            asciiStringBit += char.code.toString(2).padStart(8, '0')
        }
        return asciiStringBit
    }

    private fun asciiToText(asciiStringBit: String): String {
        var text = ""
        var asciiCode = ""
        for(char in asciiStringBit) {
            asciiCode = asciiCode + char
            if(asciiCode.length == 8) {
                text = text + Integer.parseInt(asciiCode, 2).toChar().toString()
                asciiCode = ""
            }
        }
        return text
    }

    private fun getCharsetFromText(text: String): ArrayList<Char> {
        var charset = ArrayList<Char>()
        for(char in text) {
            if(!charset.contains(char)) {
                charset.add(char)
            }
        }
        return charset
    }

    private fun getFreqOfEachCharFromText(text: String, charset: ArrayList<Char>): ArrayList<Int> {
        var freqList = ArrayList<Int>()
        for(char in charset) {
            val charFreq = text.split(char).size - 1
            freqList.add(charFreq)
        }
        return freqList
    }

    private fun insertionSort(charset: ArrayList<Char>, freqList:ArrayList<Int>): ArrayList<Char>{
        if (freqList.isEmpty() || freqList.size<2){
            return charset
        }
        for (count in 1..freqList.count() - 1){
            // println(items)
            val freq = freqList[count]
            val char = charset[count]
            var i = count
            while (i>0 && freq > freqList[i - 1]){
                freqList[i] = freqList[i - 1]
                charset[i] = charset[i - 1]
                i -= 1
            }
            freqList[i] = freq
            charset[i] = char
        }
        return charset
    }
}