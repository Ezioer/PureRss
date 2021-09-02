package com.zxq.purerss.test

class Student(var name: String, var gradle: Int, var paperCount: Int) : TopClass {

    override fun accpet(visitor: Visitor) {
        visitor.visit(this)
    }
}