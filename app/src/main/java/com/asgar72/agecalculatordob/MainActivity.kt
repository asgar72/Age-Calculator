package com.asgar72.agecalculatordob

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var etDOB: EditText
    private lateinit var etCurrentDate: EditText
    private lateinit var btnCalculate: Button
    private lateinit var tvResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etDOB = findViewById(R.id.DOB)
        etCurrentDate = findViewById(R.id.CurrentDate)
        btnCalculate = findViewById(R.id.btnCal)
        tvResult = findViewById(R.id.tvResult)

        // Set the current date to the "CurrentDate" EditText when the activity starts
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val currentDateStr = dateFormat.format(currentDate)
        etCurrentDate.setText(currentDateStr)

        // Show DatePickerDialog when the Date of Birth EditText is clicked
        etDOB.setOnClickListener { showDatePickerDialog(etDOB) }

        // Show DatePickerDialog when the Age at the Date of EditText is clicked
        etCurrentDate.setOnClickListener { showDatePickerDialog(etCurrentDate) }

        btnCalculate.setOnClickListener { calculateAge() }
    }

    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Update the EditText with the selected date
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                editText.setText(formattedDate)
            },
            year,
            month,
            day
        )

        // Show DatePickerDialog
        datePickerDialog.show()
    }

    private fun calculateAge() {
        val dobStr = etDOB.text.toString()
        val currentDateStr = etCurrentDate.text.toString()

        if (dobStr.isNotEmpty() && currentDateStr.isNotEmpty()) {
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

            try {
                val dobDate = dateFormat.parse(dobStr)
                val currentDate = dateFormat.parse(currentDateStr)

                if (dobDate != null && currentDate != null) {
                    val ageInMillis = currentDate.time - dobDate.time

                    val years = ageInMillis / (1000L * 60 * 60 * 24 * 365)
                    val months =
                        ((ageInMillis % (1000L * 60 * 60 * 24 * 365)) / (1000L * 60 * 60 * 24 * 30)).toInt()
                    val days =
                        ((ageInMillis % (1000L * 60 * 60 * 24 * 30)) / (1000L * 60 * 60 * 24)).toInt()
                    val weeks = (ageInMillis / (1000L * 60 * 60 * 24 * 7)).toInt()
                    val hours = (ageInMillis / (1000L * 60 * 60)).toInt()
                    val minutes = (ageInMillis / (1000L * 60)).toInt()

                    val resultString = "Age: \n$years years $months months $days days" +
                            "\n$weeks weeks $days days" +
                            "\n$days days" +
                            "\n$hours hours" +
                            "\n$minutes minutes"

                    tvResult.text = resultString
                    // Update the visibility of tvResult to visible
                    tvResult.visibility = View.VISIBLE

                } else {
                    tvResult.text = "Invalid date format"
                }
            } catch (e: Exception) {
                tvResult.text = "Error parsing date"
            }
        } else {
            tvResult.text = "Please enter DOB dates"
        }
    }
}
