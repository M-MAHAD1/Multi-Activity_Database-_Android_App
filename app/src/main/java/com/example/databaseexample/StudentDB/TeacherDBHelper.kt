package com.example.databaseexamples.teacherDatabase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TeacherDBHelper(context: Context) : SQLiteOpenHelper(context, "TeacherDB.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE Teacher (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                subject TEXT,
                phone TEXT
            )
        """
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Teacher")
        onCreate(db)
    }

    // Insert a new teacher
    fun insertTeacher(name: String, subject: String, phone: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("name", name)
            put("subject", subject)
            put("phone", phone)
        }
        val result = db.insert("Teacher", null, contentValues)
        return result != -1L
    }

    // Get all teachers
    fun getAllTeachers(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM Teacher", null)
    }

    // Update a teacher
    fun updateTeacher(id: Int, name: String, subject: String, phone: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("name", name)
            put("subject", subject)
            put("phone", phone)
        }
        val result = db.update("Teacher", contentValues, "id=?", arrayOf(id.toString()))
        return result > 0
    }

    // Delete a teacher
    fun deleteTeacher(id: Int): Int {
        val db = this.writableDatabase
        return db.delete("Teacher", "id=?", arrayOf(id.toString()))
    }
}
