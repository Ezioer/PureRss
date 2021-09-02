package com.zxq.purerss.test

class GradleSelectionVisitor : Visitor {
    override fun visit(teacher: Teacher?) {
        if (teacher?.score ?: 0 >= 85) {
            println("${teacher?.name}得分为${teacher?.score}入选成绩优秀奖")
        }
    }

    override fun visit(student: Student?) {
        if (student?.gradle ?: 0 >= 80) {
            println("${student?.name}得分为${student?.gradle}入选成绩优秀奖")
        }
    }
}