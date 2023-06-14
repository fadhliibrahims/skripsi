package com.example.skripsi

class FibonacciCode {

    private val N = 30
    private val fib = IntArray(N)

    private fun largestFiboLessOrEqual(n: Int): Int{
        fib[0] = 1
        fib[1] = 2
        var i = 2
        while (fib[i-1] <= n) {
            fib[i] = fib[i-2] + fib[i-1]
            i += 1
        }
        println("Fibonacci terbesar = " + (i-2).toString())
        return (i-2)
    }

    private fun fibonacciEncoding(n: Int): String{
        val index = largestFiboLessOrEqual(n)
        val codeword = CharArray(index+2)
        var i = index
        var x = n
        while (x > 0) {

            codeword[i] = '1'
            x -= fib[i]
            i -= 1

            while (i >= 0 && fib[i] > x)
            {
                codeword[i] = '0'
                i -= 1
            }
        }
        codeword[index + 1] = '1'
        println("Deret = " + fib.map { it.toString() }.toTypedArray().contentToString())
        println("Fibonacci Codeword = " + String(codeword))
        return String(codeword)
    }

    fun generateFibonacciCodeList(n: Int): ArrayList<String> {
        val codeList = ArrayList<String>()
        for(i in 1..n) {
            codeList.add(fibonacciEncoding(i))
        }
        return codeList
    }
}