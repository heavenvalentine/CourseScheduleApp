package com.dicoding.courseschedule.ui.add

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.ui.list.ListViewModelFactory
import com.dicoding.courseschedule.util.TimePickerFragment
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Calendar

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    private lateinit var addViewModel: AddCourseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)

        val factory = ListViewModelFactory.createFactory(this)
        addViewModel = ViewModelProvider(this, factory)[AddCourseViewModel::class.java]

        addViewModel.saved.observe(this) { event ->
            event.getContentIfNotHandled()?.let { isSaved ->
                if (isSaved) {
                    Toast.makeText(this, "Successfully saved.", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Something needs to be inputted.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_insert -> {
                val courseNameEditText: EditText = findViewById(R.id.ed_course_name)
                val spinnerDay: Spinner = findViewById(R.id.spinner_day)
                val startTimeTextView: TextView = findViewById(R.id.tv_start_time)
                val endTimeTextView: TextView = findViewById(R.id.tv_end_time)
                val lecturerEditText: EditText = findViewById(R.id.ed_lecturer)
                val noteEditText: EditText = findViewById(R.id.ed_note)

                val courseName = courseNameEditText.text.toString()
                val day = spinnerDay.selectedItemPosition
                val startTime = startTimeTextView.text.toString()
                val endTime = endTimeTextView.text.toString()
                val lecturer = lecturerEditText.text.toString()
                val note = noteEditText.text.toString()

                addViewModel.insertCourse(courseName, day, startTime, endTime, lecturer, note)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun buttonTimePickerStartTime(view: View) {
        showTimePicker("startTimePicker")
    }

    fun buttonTimePickerEndTime(view: View) {
        showTimePicker("endTimePicker")
    }

    private fun showTimePicker(tag: String) {
        val dialogFragment = TimePickerFragment()
        dialogFragment.show(supportFragmentManager, tag)
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        when (tag) {
            "startTimePicker" -> findViewById<TextView>(R.id.tv_start_time).text = timeFormat.format(calendar.time)
            "endTimePicker" -> findViewById<TextView>(R.id.tv_end_time).text = timeFormat.format(calendar.time)
        }
    }
}

