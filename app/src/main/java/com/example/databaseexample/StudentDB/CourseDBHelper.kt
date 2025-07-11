package com.example.databaseexamples.courseDatabase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class CourseDBHelper(context: Context) : SQLiteOpenHelper(context, "CourseDB.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE Course (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                courseName TEXT,
                courseCode TEXT,
                creditHours INTEGER
            )
        """
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Course")
        onCreate(db)
    }

    fun insertCourse(courseName: String, courseCode: String, creditHours: Int): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("courseName", courseName)
            put("courseCode", courseCode)
            put("creditHours", creditHours)
        }
        val result = db.insert("Course", null, contentValues)
        return result != -1L
    }

    fun getAllCourses(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM Course", null)
    }

    fun getCourseById(id: Int): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM Course WHERE id=?", arrayOf(id.toString()))
    }

    fun updateCourse(id: Int, courseName: String, courseCode: String, creditHours: Int): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("courseName", courseName)
            put("courseCode", courseCode)
            put("creditHours", creditHours)
        }
        val result = db.update("Course", contentValues, "id=?", arrayOf(id.toString()))
        return result > 0
    }

    fun deleteCourse(id: Int): Int {
        val db = this.writableDatabase
        return db.delete("Course", "id=?", arrayOf(id.toString()))
    }
}
