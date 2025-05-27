package com.unibuc.medtrack.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unibuc.medtrack.R
import com.unibuc.medtrack.adapters.CalendarAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PatientCalendarFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_patient_calendar, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupData()
    }

    private fun setupData() {
        addCalendar()
    }

    private fun addCalendar() {
        val recyclerView: RecyclerView = requireActivity().findViewById(R.id.calendarRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireActivity(), 7)

        val calendar = Calendar.getInstance()
        val days = generateCalendarDays(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH))

        val monthFormat = SimpleDateFormat("MMMM", Locale.ENGLISH)
        val monthName = monthFormat.format(calendar.time)
        val monthTextView = requireView().findViewById<TextView>(R.id.current_month)
        monthTextView.text = "${monthName.uppercase()}, ${calendar.get(Calendar.YEAR)}"

        val adapter = CalendarAdapter(days, calendar.time) { day: Int ->
            Toast.makeText(requireActivity(), "test test $day", Toast.LENGTH_SHORT).show()
        }

        recyclerView.adapter = adapter
    }
    private fun generateCalendarDays(year: Int, month: Int): List<Int> {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, 1)

        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        val offset = if (firstDayOfWeek == Calendar.SUNDAY) 6 else firstDayOfWeek - 2

        val days = mutableListOf<Int>()

        for (i in 0 .. 34) {
            val dayNumber = i - offset + 1
            if (i < offset || dayNumber > daysInMonth)
                days.add(-1)
            else
                days.add(dayNumber)
        }

        return days
    }
}