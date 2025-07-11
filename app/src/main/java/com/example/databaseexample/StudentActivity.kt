package com.example.databaseexamples.studentDatabase

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.databaseexamples.R

class StudentActivity : AppCompatActivity() {

    lateinit var dbHelper: StudentDBHelper
    lateinit var listViewStudents: ListView
    lateinit var arrayAdapter: ArrayAdapter<String>
    var studentList = arrayListOf<String>()
    var studentIds = arrayListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        dbHelper = StudentDBHelper(this)
        listViewStudents = findViewById(R.id.listViewStudents)
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val etName = findViewById<EditText>(R.id.etStudentName)
        val etRollNumber = findViewById<EditText>(R.id.etRollNumber)
        val etDepartment = findViewById<EditText>(R.id.etStudentClass)

        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, studentList)
        listViewStudents.adapter = arrayAdapter

        loadStudents()

        btnAdd.setOnClickListener {
            val name = etName.text.toString()
            val rollNumber = etRollNumber.text.toString()
            val department = etDepartment.text.toString()

            if (name.isNotEmpty() && rollNumber.isNotEmpty() && department.isNotEmpty()) {
                val inserted = dbHelper.insertStudent(name, rollNumber, department)
                if (inserted) {
                    Toast.makeText(this, "Student added", Toast.LENGTH_SHORT).show()
                    loadStudents()
                    etName.text.clear()
                    etRollNumber.text.clear()
                    etDepartment.text.clear()
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        listViewStudents.setOnItemClickListener { _, _, position, _ ->
            val studentId = studentIds[position]
            showEditDeleteOptions(studentId)
        }
    }

    private fun loadStudents() {
        studentList.clear()
        studentIds.clear()
        val cursor = dbHelper.getAllStudents()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val name = cursor.getString(1)
                val rollNumber = cursor.getString(2)
                val department = cursor.getString(3)
                studentList.add("$name - $rollNumber - $department")
                studentIds.add(id)
            } while (cursor.moveToNext())
        }
        arrayAdapter.notifyDataSetChanged()
    }

    private fun showEditDeleteOptions(studentId: Int) {
        val options = arrayOf("Edit", "Delete")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Option")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> showEditStudentDialog(studentId)
                1 -> deleteStudent(studentId)
            }
        }
        builder.show()
    }

    private fun showEditStudentDialog(studentId: Int) {
        val dialogView = layoutInflater.inflate(R.layout.dialogue_edit_student, null)
        val etName = dialogView.findViewById<EditText>(R.id.etEditName)
        val etRollNumber = dialogView.findViewById<EditText>(R.id.etEditRollNumber)
        val etDepartment = dialogView.findViewById<EditText>(R.id.etEditDepartment)

        val cursor = dbHelper.getAllStudents()
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getInt(0) == studentId) {
                    etName.setText(cursor.getString(1))
                    etRollNumber.setText(cursor.getString(2))
                    etDepartment.setText(cursor.getString(3))
                    break
                }
            } while (cursor.moveToNext())
        }

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val name = etName.text.toString()
                val rollNumber = etRollNumber.text.toString()
                val department = etDepartment.text.toString()
                if (name.isNotEmpty() && rollNumber.isNotEmpty() && department.isNotEmpty()) {
                    dbHelper.updateStudent(studentId, name, rollNumber, department)
                    loadStudents()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteStudent(studentId: Int) {
        dbHelper.deleteStudent(studentId)
        loadStudents()
    }
}
