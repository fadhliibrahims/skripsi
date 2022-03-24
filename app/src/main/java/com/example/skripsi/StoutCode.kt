package com.example.skripsi

class StoutCode {
    fun pow(value: Long, exp: Int): Long {
        return when {
            exp > 1 -> value * pow(value,exp-1)
            exp == 1 -> value
            else -> 1
        }
    }

    fun generateStoutCodeList(amountOfCode: Int, l: Int): ArrayList<String> {
        var codeList = ArrayList<String>()
        if (amountOfCode>0 && l>=2) {
            for(n in 1..amountOfCode) {
                if (n>=pow(2, l)) {
                    //L = n.toBinaryString().length
                    val L = n.toString(2).length
                    //str = n.toBinaryString(L)
                    val str = n.toString(2).padStart(L, '0')
                    //m = L-1-l
                    var m = L-1-l

                    while(m>=pow(2, l)) {
                        //L = m.toBinaryString().length
                        val L = m.toString(2).length
                        //str = m.toBinaryString(L)
                        val str = m.toString(2).padStart(L, '0')
                        //m = L
                        m = L
                    }

                    codeList.add(m.toString(2).padStart(l, '0') + str + '0')
                } else {
                    //codelist[n] = n.toBinaryString(l)
                    codeList.add(n.toString(2).padStart(l, '0') + '0')
                }
            }
        } else {
            print("Selesai")
        }
        return codeList
    }
}