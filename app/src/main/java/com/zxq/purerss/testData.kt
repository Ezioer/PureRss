package com.zxq.purerss

class testData {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val s = "straBassaaSaabsfbgssFFs"
            val len = s.length
            var isLast = false
            var i = 0
            while (i < len - 2) {
                if (s[i] == s[i + 2] && s[i] != s[i + 1]) {
                    //如果最后三个是驼峰的话则不输出，否则需要输出最后两个字符
                    if (i == len - 3) {
                        isLast = true
                    }
                    //遇到驼峰则跳过驼峰长度
                    i += 3
                } else {
                    //不是驼峰依次输出
                    print(s[i])
                    i++
                }
            }
            if (!isLast) {
                print(s[len - 2])
                print(s[len - 1])
            }
        }
    }
}