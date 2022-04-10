package com.example.stuplan.model

data class TaskModel(
    var id : Long?,
    var taskName : String,
    var taskNote : String,
    var duedate : String,
    var completed : Int = 0,
    var courseId : Long) {
}