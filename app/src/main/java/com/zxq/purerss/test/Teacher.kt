package com.zxq.purerss.test

class Teacher(var name: String, var score: Int, var paperCount: Int) : TopClass {
    override fun accpet(visitor: Visitor) {
        visitor.visit(this)
    }
}