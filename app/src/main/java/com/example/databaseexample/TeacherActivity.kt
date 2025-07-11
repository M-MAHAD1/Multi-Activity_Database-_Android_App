package com.example.databaseexamples.teacherDatabase

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.databaseexamples.R
import com.example.databaseexamples.teacherDatabase.TeacherDBHelper

class TeacherActivity : AppCompatActivity() {

    lateinit var dbHelper: TeacherDBHelper
    lateinit var listViewTeachers: ListView
    lateinit var arrayAdapter: ArrayAdapter<String>
    var teacherList = arrayListOf<String>()
    var teacherIds = arrayListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher)

        dbHelper = TeacherDBHelper(this)
        listViewTeachers = findViewById(R.id.listViewTeachers)
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val etName = findViewById<EditText>(R.id.etTeacherName)
        val etSubject = findViewById<EditText>(R.id.etSubject)
        val etPhone = findViewById<EditText>(R.id.etPhone)

        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, teacherList)
        listViewTeachers.adapter = arrayAdapter

        loadTeachers()

        btnAdd.setOnClickListener {
            val name = etName.text.toString()
            val subject = etSubject.text.toString()
            val phone = etPhone.text.toString()

            if (name.isNotEmpty() && subject.isNotEmpty() && phone.isNotEmpty()) {
                val inserted = dbHelper.insertTeacher(name, subject, phone)
                if (inserted) {
                    Toast.makeText(this, "Teacher added", Toast.LENGTH_SHORT).show()
                    etName.text.clear()
                    etSubject.text.clear()
                    etPhone.text.clear()
                    loadTeachers()
                } else {
                    Toast.makeText(this, "Insert failed", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        listViewTeachers.setOnItemClickListener { _, _, position, _ ->
            val teacherId = teacherIds[position]
            showEditDeleteOptions(teacherId)
        }
    }

    private fun loadTeachers() {
        teacherList.clear()
        teacherIds.clear()
        val cursor = dbHelper.getAllTeachers()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val name = cursor.getString(1)
                val subject = cursor.getString(2)
                val phone = cursor.getString(3)
                teacherList.add("$name - $subject - $phone")
                teacherIds.add(id)
            } while (cursor.moveToNext())
        }
        cursor.close()
        arrayAdapter.notifyDataSetChanged()
    }

    private fun showEditDeleteOptions(teacherId: Int) {
        val options = arrayOf("Edit", "Delete")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Option")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> showEditTeacherDialog(teacherId)
                1 -> {
                    dbHelper.deleteTeacher(teacherId)
                    loadTeachers()
                    Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
                }
            }
        }
        builder.show()
    }

    private fun showEditTeacherDialog(teacherId: Int) {
        val dialogView = layoutInflater.inflate(R.layout.dialogue_edit_teacher, null)
        val etEditName = dialogView.findViewById<EditText>(R.id.etEditName)
        val etEditSubject = dialogView.findViewById<EditText>(R.id.etEditSubject)
        val etEditPhone = dialogView.findViewById<EditText>(R.id.etEditPhone)

        val cursor = dbHelper.getAllTeachers()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                if (id == teacherId) {
                    etEditName.setText(cursor.getString(1))
                    etEditSubject.setText(cursor.getString(2))
                    etEditPhone.setText(cursor.getString(3))
                    break
                }
            } while (cursor.moveToNext())
        }
        cursor.close()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Edit Teacher")
        builder.setView(dialogView)
        builder.setPositiveButton("Update") { _, _ ->
            val newName = etEditName.text.toString()
            val newSubject = etEditSubject.text.toString()
            val newPhone = etEditPhone.text.toString()

            if (newName.isNotEmpty() && newSubject.isNotEmpty() && newPhone.isNotEmpty()) {
                dbHelper.updateTeacher(teacherId, newName, newSubject, newPhone)
                loadTeachers()
                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }
}
