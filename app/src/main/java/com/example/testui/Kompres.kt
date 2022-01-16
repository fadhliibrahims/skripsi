package com.example.testui

class Kompres {

    fun kompresText(text: String, algorithm: Int): String {
        val asciiStringBit = textToAscii(text)
        val charset = getCharsetFromText(text)
        val freqList = getFreqOfEachCharFromText(text, charset)
        val sortedCharset = insertionSort(charset, freqList)
        var compressedText = text

        if (algorithm == 0) {

        } else if (algorithm == 1) {
            val fibonacciCode = FibonacciCode()
            val fibonacciCodeList = fibonacciCode.generateFibonacciCodeList(charset.size)
            for(i in 0..sortedCharset.size-1) {
                compressedText = compressedText.replace(sortedCharset[i].toString(), fibonacciCodeList[i])
            }
        }

        return compressedText
    }

    private fun textToAscii(text: String): String {
        var asciiStringBit = ""
        for(char in text) {
            asciiStringBit += char.code.toString(2).padStart(8, '0')
        }
        return asciiStringBit
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