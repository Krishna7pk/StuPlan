package com.example.stuplan.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.stuplan.model.CourseModel
import com.example.stuplan.model.TaskModel


class DatabaseHelper(context: Context)
    : SQLiteOpenHelper(context, DB_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTableCourse =
            "CREATE TABLE ${CourseContract.CourseEntry.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${CourseContract.CourseEntry.COLUMN_NAME} TEXT )"

        val createTableTask =
            "CREATE TABLE ${TaskContract.TaskEntry.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY ," +
                    "${TaskContract.TaskEntry.COLUMN_NAME} TEXT," +
                    "${TaskContract.TaskEntry.COLUMN_DUE_DATE} TEXT,"+
                    "${TaskContract.TaskEntry.COLUMN_COURSE_ID} INTEGER,"+
                    "${TaskContract.TaskEntry.COLUMN_IS_COMPLETED} INTEGER,"+
                    "${TaskContract.TaskEntry.COLUMN_NOTE} TEXT," +
                    "FOREIGN KEY (${TaskContract.TaskEntry.COLUMN_COURSE_ID})" +
                    "REFERENCES ${CourseContract.CourseEntry.TABLE_NAME}(${BaseColumns._ID}) ) "


        db.execSQL(createTableCourse)//SQL_CREATE_ENTRIES)
        db.execSQL(createTableTask)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        val dropCourseTable = "DROP TABLE IF EXISTS ${CourseContract.CourseEntry.TABLE_NAME}"
        val dropTaskTable = "DROP TABLE IF EXISTS ${TaskContract.TaskEntry.TABLE_NAME}"
        db.execSQL(dropCourseTable) //SQL_DELETE_ENTRIES)
        db.execSQL(dropTaskTable)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun insertCourse(course : CourseModel) : Long {
        val db = this.writableDatabase


        val values = ContentValues().apply {
            put(CourseContract.CourseEntry.COLUMN_NAME, course.courseName)
        }

        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(CourseContract.CourseEntry.TABLE_NAME, null, values)
        db.close()
        return newRowId
    }

    fun insertTask(task : TaskModel) : Long {
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(TaskContract.TaskEntry.COLUMN_NAME, task.taskName)
            put(TaskContract.TaskEntry.COLUMN_NOTE, task.taskNote)
            put(TaskContract.TaskEntry.COLUMN_COURSE_ID, task.courseId)
            put(TaskContract.TaskEntry.COLUMN_DUE_DATE, task.duedate)
            put(TaskContract.TaskEntry.COLUMN_IS_COMPLETED, task.completed)
        }

        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(TaskContract.TaskEntry.TABLE_NAME, null, values)
        db.close()
        return newRowId
    }

    fun getAllCourse(): ArrayList<CourseModel>{
            val courseList = ArrayList<CourseModel>()
            val selectAll = "SELECT * FROM ${CourseContract.CourseEntry.TABLE_NAME}"

            val db = this.readableDatabase

            // Define a projection that specifies which columns from the database you will actually use after this query.
            val projection =
                arrayOf(BaseColumns._ID,
                    CourseContract.CourseEntry.COLUMN_NAME)

            //use this for search feature
            // Filter results WHERE "title" = 'My Title'
            /*val selection = "${NotesContract.NotesEntry.COLUMN_NAME_TITLE} = ?"
            val selectionArgs = arrayOf("My Title")*/

            // How you want the results sorted in the resulting Cursor
            //val sortOrder = "${NotesContract.NotesEntry.COLUMN_NAME_TITLE} DESC"

            val cursor = db.query(
                CourseContract.CourseEntry.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
            )

            with(cursor) {
                while (moveToNext()) {
                    val id = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                    val courseName = getString(this.getColumnIndexOrThrow(CourseContract.CourseEntry.COLUMN_NAME))
                    courseList.add(CourseModel(id,courseName))
                }
            }
            cursor.close()
            db.close()

            return courseList

        }

    fun getAllTask(): ArrayList<TaskModel>{
        val taskList = ArrayList<TaskModel>()
        val selectAll = "SELECT * FROM ${TaskContract.TaskEntry.TABLE_NAME}"

        val db = this.readableDatabase

        val cursor = db.query(
            TaskContract.TaskEntry.TABLE_NAME,   // The table to query
            null,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )

        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(BaseColumns._ID ))
                val taskName = getString(this.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME))
                val taskNote = getString(this.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NOTE))
                val duedate = getString(this.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_DUE_DATE))
                val completed = getInt(this.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_IS_COMPLETED))
                val courseId = getLong(this.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_COURSE_ID))
                taskList.add(TaskModel(id,taskName,taskNote,duedate,completed,courseId))
            }
        }
        cursor.close()
        db.close()

        return taskList

    }

    fun setTaskCompleted(task: TaskModel) : Int{
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(TaskContract.TaskEntry.COLUMN_IS_COMPLETED, task.completed)
        }

        val selection = "${BaseColumns._ID} = ${task.id}"
        val selectionArgs = arrayOf(task.id)
        val count = db.update(
            TaskContract.TaskEntry.TABLE_NAME,
            values,
            selection,
            null)

        db.close()
        return count

    }

    fun getAllCompletedTask() : ArrayList<TaskModel>{
        val taskList = ArrayList<TaskModel>()
        val selectCompletedTask = "SELECT * FROM ${TaskContract.TaskEntry.TABLE_NAME} " +
                "WHERE ${TaskContract.TaskEntry.COLUMN_IS_COMPLETED}==1"

        val db = this.readableDatabase

        val selection = "${TaskContract.TaskEntry.COLUMN_IS_COMPLETED} = 1"
        val selectionArgs = arrayOf(1)

        val cursor = db.query(
            TaskContract.TaskEntry.TABLE_NAME,   // The table to query
            null,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )

        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(BaseColumns._ID ))
                val taskName = getString(this.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME))
                val taskNote = getString(this.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NOTE))
                val duedate = getString(this.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_DUE_DATE))
                val completed = getInt(this.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_IS_COMPLETED))
                val courseId = getLong(this.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_COURSE_ID))
                taskList.add(TaskModel(id,taskName,taskNote,duedate,completed,courseId))
            }
        }
        cursor.close()
        db.close()

        return taskList



    }


    fun deleteCourse(course: CourseModel){
        val db = this.writableDatabase

        // Define 'where' part of query.
        val selection = "${BaseColumns._ID} = ${course.id}"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(course.id)
        // Issue SQL statement.
        val deletedRows = db.delete(CourseContract.CourseEntry.TABLE_NAME, selection, null)
        db.close()
    }

/*
    fun updateNote(note: NoteModel): Int{
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(NotesContract.NotesEntry.COLUMN_NAME_TITLE, note.noteTitle)
            put(NotesContract.NotesEntry.COLUMN_NAME_DESCRIPTION, note.noteDescription)
        }

        //val query = db.update(NotesContract.NotesEntry.TABLE_NAME, values, "id="+note.id,null)

        // Which row to update, based on the id
        val selection = "${BaseColumns._ID} = ${note.id}"
        val selectionArgs = arrayOf(note.id)
        val count = db.update(
            NotesContract.NotesEntry.TABLE_NAME,
            values,
            selection,
            null)

        return count

    }
*/

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DB_NAME = "AndroidStuplan.db"
    }
}