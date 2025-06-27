package com.unibuc.medtrack.ui.home

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.unibuc.medtrack.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unibuc.medtrack.adapters.PatientAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.google.android.material.floatingactionbutton.FloatingActionButton

@AndroidEntryPoint
class DoctorHomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_doctor_home, container, false)

    private val userViewModel: DoctorHomeViewModel by viewModels()
    private lateinit var patientAdapter: PatientAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupData()
    }

    private fun setupData() {
        setupGreeting()
        setupCurrentDate()
        setupDaysOfTheWeek()
        setupPatientList()
        setupAddPatientButton()
    }

    private fun setupAddPatientButton() {
        requireView().findViewById<FloatingActionButton>(R.id.add_patient).setOnClickListener {
            val input = EditText(requireContext())
            input.hint = "Enter patient email"

            AlertDialog.Builder(requireContext())
                .setTitle("Add Patient")
                .setView(input)
                .setPositiveButton("Add") { _, _ ->
                    val email = input.text.toString().trim()
                    if (email.isNotEmpty()) {
                        userViewModel.viewModelScope.launch {
                            val patient = withContext(Dispatchers.IO) {
                                userViewModel.usersRepository.getByEmail(email)
                            }
                            if (patient != null) {
                                userViewModel.addPatient(patient.id)
                            } else {
                                Toast.makeText(requireContext(), "Patient not found", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun setupPatientList() {

        val recyclerView = requireView().findViewById<RecyclerView>(R.id.patients_recycler)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        userViewModel.loadPatients()
        userViewModel.patients.observe(viewLifecycleOwner) { patients ->
            patientAdapter = PatientAdapter(patients) { patient ->
                val bundle = Bundle().apply {
                    putString("patientId", patient.id)
                }
                findNavController().navigate(R.id.action_doctorHomeFragment_to_doctorPatientTreatmentFragment, bundle)
            }
            recyclerView.adapter = patientAdapter
        }
    }

    private fun setupGreeting() {
        val greetingTextView = requireView().findViewById<TextView>(R.id.greeting_text)
        userViewModel.loadUserName()
        userViewModel.userName.observe(viewLifecycleOwner) { name ->
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
}