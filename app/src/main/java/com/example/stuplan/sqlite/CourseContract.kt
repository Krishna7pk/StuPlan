package com.example.stuplan.sqlite

import android.provider.BaseColumns

object CourseContract {

    object CourseEntry : BaseColumns {
        const val TABLE_NAME = "course"
        const val COLUMN_NAME = "course_name"

    }
}