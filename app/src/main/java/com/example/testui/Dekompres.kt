package com.example.testui

class Dekompres {
    fun dekompresText(compressedText: String, algorithm: Int): String {
        //Get Charset from Compressed Text
        var decompressedText = ""
        val parts = compressedText.split("|*|")

        val compressedText2 = parts[0]
        val charset2 = parts[1]

        //Convert compressed text to compressed bit
        var compressedBit2 = textToAscii(compressedText2)

        //Remove padding bit and flag bit
        val flagBit2 = compressedBit2.substring(compressedBit2.length-8, compressedBit2.length)
        val padLength2 = Integer.parseInt(flagBit2, 2)

        compressedBit2 = compressedBit2.dropLast(padLength2+8)

        if (algorithm == 0) {

        } else if (algorithm == 1) {
//            val fibonacciCode = FibonacciCode()
//            val fibonacciCodeList = fibonacciCode.generateFibonacciCodeList(charset.size)
//            for(i in 0..sortedCharset.size-1) {
//                compressedBit = compressedBit.replace(sortedCharset[i].toString(), fibonacciCodeList[i])
//            }
            //Generate Table Encoding
            val amountOfCode2 = charset2.length
            val fibonacciCode = FibonacciCode()
            val fibonacciCodeList2 = fibonacciCode.generateFibonacciCodeList(amountOfCode2)
            val encodingTable2 = HashMap<String, Char>()
            for(i in 0..charset2.length-1) {
                encodingTable2[fibonacciCodeList2[i]] = charset2[i]
            }

            //Decompression
            var charCounter = 2
            var stringBit = compressedBit2.substring(0, charCounter)

            while(charCounter <= compressedBit2.length) {
//            println("String Bit " + stringBit)
                if(stringBit.substring(stringBit.length-2, stringBit.length) == "11") {
                    decompressedText = decompressedText + encodingTable2.getValue(stringBit).toString()
//                println(decompressedText)
                    if(charCounter < compressedBit2.length) {
                        stringBit = compressedBit2.substring(charCounter, charCounter+2)
                    }
                    charCounter = charCounter + 2
                } else {
                    stringBit = stringBit + compressedBit2.substring(charCounter, charCounter+1)
                    charCounter = charCounter + 1
                }
            }
        }



//        //Padding Bit and Flag Bit
//        val padLength = 8-(compressedBit.length%8)
//        val paddingBit = "0".repeat(padLength)
//        val flagBit = padLength.toString(2).padStart(8, '0')
//        compressedBit = compressedBit + paddingBit + flagBit
//        //Convert compressedBit to Text
//        var compressedText = asciiToText(compressedBit) + "|*|" + sortedCharset.joinToString("")

        return decompressedText
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