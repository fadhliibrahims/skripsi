package com.example.skripsi

class StoutCode {
    fun pow(value: Long, exp: Int): Long {
        return when {
            exp > 1 -> value * pow(value,exp-1)
            exp == 1 -> value
            else -> 1
        }
    }

    fun generateStoutCodeList(amountOfCode: Int): ArrayList<String> {
        var codeList = ArrayList<String>()
        val l = 2
        if (amountOfCode>0) {
            for(n in 1..amountOfCode) {
                if (n>=pow(2, l)) {
                    val L = n.toString(2).length
                    println("L = $L")
                    val str = n.toString(2).padStart(L, '0')
                    println("str = $str")
                    var m = L-1-l
                    println("m = L-1-l = $m")
                    while(m>=pow(2, l)) {
                        val L = m.toString(2).length
                        println("L = $L")
                        val str = m.toString(2).padStart(L, '0')
                        println("str = $str")
                        m = L
                        println("m = L = $m")
                    }
                    println("Kode = " + m.toString(2).padStart(l, '0') + str + '0')
                    codeList.add(m.toString(2).padStart(l, '0') + str + '0')
                } else {
                    println("Kode = " + n.toString(2).padStart(l, '0') + '0')
                    codeList.add(n.toString(2).padStart(l, '0') + '0')
                }
            }
        } else {
            print("Selesai")
        }
        return codeList
    }
}