package com.example.databaseexamples

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.databaseexamples.studentDatabase.StudentActivity
import com.example.databaseexamples.courseDatabase.CourseActivity
import com.example.databaseexamples.teacherDatabase.TeacherActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find buttons by their IDs
        val studentBtn = findViewById<Button>(R.id.btnStudent)
        val courseBtn = findViewById<Button>(R.id.btnCourse)
        val teacherBtn = findViewById<Button>(R.id.btnTeacher)

        // Set up button click listeners
        studentBtn.setOnClickListener {
            startActivity(Intent(this, StudentActivity::class.java))
        }

        courseBtn.setOnClickListener {
            startActivity(Intent(this, CourseActivity::class.java))
        }

        teacherBtn.setOnClickListener {
            startActivity(Intent(this, TeacherActivity::class.java))
        }
    }
}
