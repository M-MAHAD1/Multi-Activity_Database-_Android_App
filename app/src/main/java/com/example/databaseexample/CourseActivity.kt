package com.example.databaseexamples.courseDatabase

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.databaseexamples.R
import com.example.databaseexamples.courseDatabase.CourseDBHelper

class CourseActivity : AppCompatActivity() {

    lateinit var dbHelper: CourseDBHelper
    lateinit var listViewCourses: ListView
    lateinit var arrayAdapter: ArrayAdapter<String>
    var courseList = arrayListOf<String>()
    var courseIds = arrayListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course)

        dbHelper = CourseDBHelper(this)
        listViewCourses = findViewById(R.id.listViewCourses)
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val etCourseName = findViewById<EditText>(R.id.etCourseName)
        val etCourseCode = findViewById<EditText>(R.id.etCourseCode)
        val etCreditHours = findViewById<EditText>(R.id.etCreditHours)

        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, courseList)
        listViewCourses.adapter = arrayAdapter

        loadCourses()

        btnAdd.setOnClickListener {
            val courseName = etCourseName.text.toString()
            val courseCode = etCourseCode.text.toString()
            val creditHours = etCreditHours.text.toString().toIntOrNull()

            if (courseName.isNotEmpty() && courseCode.isNotEmpty() && creditHours != null) {
                val inserted = dbHelper.insertCourse(courseName, courseCode, creditHours)
                if (inserted) {
                    Toast.makeText(this, "Course added", Toast.LENGTH_SHORT).show()
                    etCourseName.text.clear()
                    etCourseCode.text.clear()
                    etCreditHours.text.clear()
                    loadCourses()
                } else {
                    Toast.makeText(this, "Failed to add course", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        listViewCourses.setOnItemClickListener { _, _, position, _ ->
            val courseId = courseIds[position]
            showEditDeleteOptions(courseId)
        }
    }

    private fun loadCourses() {
        courseList.clear()
        courseIds.clear()
        val cursor = dbHelper.getAllCourses()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val courseName = cursor.getString(1)
                val courseCode = cursor.getString(2)
                val credits = cursor.getInt(3)
                courseList.add("$courseName ($courseCode) - $credits Credit Hours")
                courseIds.add(id)
            } while (cursor.moveToNext())
        }
        arrayAdapter.notifyDataSetChanged()
    }

    private fun showEditDeleteOptions(courseId: Int) {
        val options = arrayOf("Edit", "Delete")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Option")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> showEditCourseDialog(courseId)
                1 -> {
                    dbHelper.deleteCourse(courseId)
                    loadCourses()
                    Toast.makeText(this, "Course deleted", Toast.LENGTH_SHORT).show()
                }
            }
        }
        builder.show()
    }

    private fun showEditCourseDialog(courseId: Int) {
        val dialogView = layoutInflater.inflate(R.layout.dialogue_edit_course, null)
        val etCourseName = dialogView.findViewById<EditText>(R.id.etEditCourseName)
        val etCourseCode = dialogView.findViewById<EditText>(R.id.etEditCourseCode)
        val etCredits = dialogView.findViewById<EditText>(R.id.etEditCredits)

        val cursor = dbHelper.getCourseById(courseId)
        if (cursor.moveToFirst()) {
            etCourseName.setText(cursor.getString(1))
            etCourseCode.setText(cursor.getString(2))
            etCredits.setText(cursor.getInt(3).toString())
        }

        AlertDialog.Builder(this)
            .setTitle("Edit Course")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val name = etCourseName.text.toString()
                val code = etCourseCode.text.toString()
                val credits = etCredits.text.toString().toIntOrNull()

                if (name.isNotEmpty() && code.isNotEmpty() && credits != null) {
                    dbHelper.updateCourse(courseId, name, code, credits)
                    loadCourses()
                    Toast.makeText(this, "Course updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
