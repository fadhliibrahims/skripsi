package com.example.testui

class FibonacciCode {

    private val N = 30
    private val fib = IntArray(N)

    private fun largestFiboLessOrEqual(n: Int): Int{
        fib[0] = 1
        fib[1] = 2
        var i = 2
        while (fib[i-1] <= n) {
            fib[i] = fib[i-2] + fib[i-1]
            i = i+1
        }
        return (i-2)
    }

    private fun fibonacciEncoding(n: Int): String{
        val index = largestFiboLessOrEqual(n)
        var codeword = CharArray(index+2)
        var i = index
        var x = n
        while (x > 0) {
            // Mark usage of Fibonacci f(1 bit)
            codeword[i] = '1'

            // Subtract f from n
            x = x-fib[i]

            // Move to Fibonacci just smaller than f
            i = i-1

            // Mark all Fibonacci > n as not used
            // (0 bit), progress backwards
            while (i >= 0 && fib[i] > x)
            {
                codeword[i] = '0'
                i = i - 1
            }
        }
        codeword[index + 1] = '1'
        return String(codeword)
    }

    fun generateFibonacciCodeList(n: Int): ArrayList<String> {
        var codeList = ArrayList<String>()
        for(i in 1..n) {
            codeList.add(fibonacciEncoding(i))
        }
        return codeList
    }
}