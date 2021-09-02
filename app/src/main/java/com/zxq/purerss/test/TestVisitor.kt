package com.zxq.purerss.test

class TestVisitor {
    fun test() {
        var student: TopClass = Student("zxq", 99, 7)
        var visitor: Visitor = GradleSelectionVisitor()
        student.accpet(visitor)
    }
}