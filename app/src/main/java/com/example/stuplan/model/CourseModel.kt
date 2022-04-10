package com.example.stuplan.model

data class CourseModel(var id: Long?,
                  var courseName: String,) {
    override fun toString(): String {
        return courseName
    }
}