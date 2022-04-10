package com.example.stuplan.sqlite

object TaskContract {

    object TaskEntry{
        const val TABLE_NAME = "task"
        const val COLUMN_NAME = "task_title"
        const val COLUMN_DUE_DATE = "due_date"
        const val COLUMN_IS_COMPLETED = "completed"
        const val COLUMN_COURSE_ID = "course_id_task"
        const val COLUMN_NOTE = "note"

    }
}