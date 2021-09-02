package com.zxq.purerss.test

interface Visitor {
    fun visit(teacher: Teacher?)
    fun visit(student: Student?)
}