package com.unibuc.medtrack.ui.home

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.unibuc.medtrack.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

class DoctorHomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_doctor_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupData()
    }

    private fun setupData() {
        setupGreeting()
        setupCurrentDate()
    }

    private fun setupGreeting() {
        val greetingTextView = requireView().findViewById<TextView>(R.id.greeting_text)
        val userName = "John Doe"
        greetingTextView.text = "Hello, Dr. $userName!"
    }

    private fun setupCurrentDate() {
        val dateTextView = requireView().findViewById<TextView>(R.id.date_text)
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)

        dateTextView.text = "Today is $formattedDate"
    }
}