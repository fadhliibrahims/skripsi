package com.example.skripsi

class Dekompres {
    fun dekompresText(compressedText: String, algorithm: Int): String {
        //Get Charset from Compressed Text
//        var decompressedText = ""
        val decompressedText = StringBuilder()
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
            //Generate Table Encoding
            val l = 2
            val amountOfCode2 = charset2.length
            val stoutCode = StoutCode()
            val stoutCodeList2 = stoutCode.generateStoutCodeList(amountOfCode2)
            val encodingTable2 = HashMap<String, Char>()
            for(i in 0..charset2.length-1) {
                encodingTable2[stoutCodeList2[i]] = charset2[i]
            }
            println(encodingTable2)

            //Decompression
            var counter = 0
            var x: String
            var y: String
            var bigL: Int
            while(counter < compressedBit2.length) {
                x = compressedBit2.substring(counter, counter+l+1)
         print("x: " + x + "   ")
                y = compressedBit2.substring(counter, counter+l)
         println("y: " + y)
                counter = counter + l+1
                while(encodingTable2.containsKey(x)==false) {
                    bigL = binaryToDecimal(y.toLong()) + 1 + l
             println("bigL: " + bigL)
                    if (bigL == 7) {
                        bigL = 1
                    }
                    x = x + compressedBit2.substring(counter, counter+bigL)
             print("x: " + x + "   ")
//                    y = compressedBit2.substring(counter-1, counter+bigL-1)
                    y = compressedBit2.substring(counter-1, counter-1+binaryToDecimal(y.toLong()))
             println("y: " + y)
                    counter = counter + bigL
                }
                println(x + ": " + encodingTable2[x])
                decompressedText.append(encodingTable2[x])
//         println(decompressedText)
            }

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
                    decompressedText.append(encodingTable2.getValue(stringBit).toString())
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

        return decompressedText.toString()
    }

    private fun textToAscii(text: String): String {
//        var asciiStringBit = ""
        val asciiStringBit = StringBuilder()
        for(t in text) {
            asciiStringBit.append(t.code.toString(2).padStart(8, '0'))
        }
        return asciiStringBit.toString()
    }


    private fun binaryToDecimal(num: Long): Int {
        var num = num
        var decimal = 0
        var i = 0
        var remainder: Long

        while (num.toInt() != 0) {
            remainder = num % 10
            num /= 10
            decimal += (remainder * Math.pow(2.0, i.toDouble())).toInt()
            ++i
        }
        return decimal
    }

}