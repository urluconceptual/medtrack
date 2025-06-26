package com.unibuc.medtrack.ui.home

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unibuc.medtrack.R
import com.unibuc.medtrack.data.models.FullTreatmentWithNotifications
import com.unibuc.medtrack.data.models.TreatmentModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class PatientHomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_patient_home, container, false)

    private val viewModel: PatientHomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchTreatments()
        setupData()
        viewModel.scheduleAllTodayNotifications(requireContext())
    }

    private fun setupData() {
        setupGreeting()
        setupCurrentDate()
        setupDaysOfTheWeek()
    }

    private fun setupGreeting() {
        val greetingTextView = requireView().findViewById<TextView>(R.id.greeting_text)
        viewModel.loadUserName()
        viewModel.userName.observe(viewLifecycleOwner) { name ->
            greetingTextView.text = "Hello, $name!"
        }
    }

    private fun setupCurrentDate() {
        val dateTextView = requireView().findViewById<TextView>(R.id.date_text)
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)

        dateTextView.text = "Today is $formattedDate"
    }

    private fun setupDaysOfTheWeek() {
        val today = LocalDate.now()

        for (i in 0..6) {
            val daysToAdd = i - today.dayOfWeek.value + 1L
            val date = today.plusDays(daysToAdd)

            val dayNumberId = resources.getIdentifier(
                "day_number_${date.dayOfWeek.name.lowercase()}",
                "id",
                requireContext().packageName
            )

            val textView = requireView().findViewById<TextView>(dayNumberId)
            textView?.text = date.dayOfMonth.toString()

            if (daysToAdd == 0L) {
                setupStyleOfCurrentDay(date)
            }
        }
    }

    private fun setupStyleOfCurrentDay(date: LocalDate) {
        val dayContainerId = resources.getIdentifier(
            "day_container_${date.dayOfWeek.name.lowercase()}",
            "id",
            requireContext().packageName
        )

        val linearLayout = requireView().findViewById<LinearLayout>(dayContainerId)
        val tintColor = ContextCompat.getColor(requireContext(), R.color.primary_blue)

        linearLayout.backgroundTintList = ColorStateList.valueOf(tintColor)

        for (i in 0 until linearLayout.childCount) {
            val view = linearLayout.getChildAt(i)
            if (view is TextView) {
                view.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }
        }
    }

    private fun fetchTreatments() {
        Log.i("PatientHomeFragment", "Registering observers")

        viewModel.todaysNotifications.observe(viewLifecycleOwner) { fullTreatmentList ->
            Log.i("PatientHomeFragment", "Today's treatments with notifications: $fullTreatmentList")
            bindTreatmentsToUI(fullTreatmentList)
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        Log.i("PatientHomeFragment", "Calling fetchTodayNotifications()")
        viewModel.fetchTodayNotifications()
    }

    private fun bindTreatmentsToUI(treatmentsWithNotifications: List<FullTreatmentWithNotifications>) {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.treatments_recycler)
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        recyclerView?.adapter = TreatmentAdapter(treatmentsWithNotifications)
    }


}