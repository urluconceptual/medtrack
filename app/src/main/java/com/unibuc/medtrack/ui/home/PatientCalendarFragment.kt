package com.unibuc.medtrack.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unibuc.medtrack.R
import com.unibuc.medtrack.adapters.CalendarAdapter
import com.unibuc.medtrack.adapters.TreatmentAdapter
import com.unibuc.medtrack.data.models.FullTreatmentWithNotifications
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class PatientCalendarFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_patient_calendar, container, false)

    private val viewModel: PatientCalendarViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupData()
    }

    private fun setupData() {
        addCalendar()

        fetchTreatments(LocalDateTime.now())
        viewModel.updateSelectedDate(LocalDateTime.now())
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
            val thisDate = calendar.apply({ set(Calendar.DAY_OF_MONTH, day)})
            val localDate = LocalDateTime.of(
                thisDate.get(Calendar.YEAR),
                thisDate.get(Calendar.MONTH) + 1,
                thisDate.get(Calendar.DAY_OF_MONTH),
                0,
                0
            )
            fetchTreatments(localDate)
            viewModel.updateSelectedDate(localDate)
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

        for (i in 0 .. 40) {
            val dayNumber = i - offset + 1
            if (i < offset || dayNumber > daysInMonth)
                days.add(-1)
            else
                days.add(dayNumber)
        }

        return days
    }

    private fun fetchTreatments(date: LocalDateTime) {

        val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH)
        val formattedDate = date.format(formatter)

        view?.findViewById<TextView>(R.id.calendar_selected_day_header)?.setText(formattedDate)


        Log.i("PatientCalendarFragment", "registering observers")

        viewModel.todaysNotifications.observe(viewLifecycleOwner) { fullTreatmentList ->
            Log.i("PatientCalendarFragment", "full treatment+notif list: $fullTreatmentList")
            bindTreatmentsToUI(fullTreatmentList)
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        Log.i("PatientCalendarFragment", "calling fetchFullTreatmentsForPatient()")

        viewModel.fetchFullTreatmentDetails(date)
    }

    private fun bindTreatmentsToUI(treatments: List<FullTreatmentWithNotifications>) {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.treatments_recycler_calendar)
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())

        recyclerView?.adapter = TreatmentAdapter(treatments) { notificationId ->
            Log.i("PatientCalendarFragment", "trigger mark notifications");
            viewModel.markNotificationAsTaken(notificationId)
        }
    }
}