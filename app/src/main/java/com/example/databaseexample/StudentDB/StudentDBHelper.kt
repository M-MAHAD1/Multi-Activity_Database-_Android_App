package com.example.databaseexamples.studentDatabase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class StudentDBHelper(context: Context) : SQLiteOpenHelper(context, "StudentDB.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE Student (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                rollNumber TEXT,
                course TEXT
            )
        """
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Student")
        onCreate(db)
    }

    fun insertStudent(name: String, rollNumber: String, course: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("name", name)
            put("rollNumber", rollNumber)
            put("course", course)
        }
        val result = db.insert("Student", null, contentValues)
        return result != -1L
    }

    fun getAllStudents(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM Student", null)
    }

    fun updateStudent(id: Int, name: String, rollNumber: String, course: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("name", name)
            put("rollNumber", rollNumber)
            put("course", course)
        }
        val result = db.update("Student", contentValues, "id=?", arrayOf(id.toString()))
        return result > 0
    }

    fun deleteStudent(id: Int): Int {
        val db = this.writableDatabase
        return db.delete("Student", "id=?", arrayOf(id.toString()))
    }
}
