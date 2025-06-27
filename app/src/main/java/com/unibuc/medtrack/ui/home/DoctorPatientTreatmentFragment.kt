package com.unibuc.medtrack.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.unibuc.medtrack.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.unibuc.medtrack.adapters.DoctorTreatmentAdapter
import dagger.hilt.android.AndroidEntryPoint
import com.unibuc.medtrack.adapters.TreatmentAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class DoctorPatientTreatmentFragment : Fragment() {
    private val viewModel: DoctorPatientTreatmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_doctor_patient_treatment, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupData();
        setupAddTreatmentButton();
    }

    private fun setupData() {
        val patientId = arguments?.getString("patientId") ?: return

        viewModel.username.observe(viewLifecycleOwner) { name ->
            requireView().findViewById<TextView>(R.id.patient_name).text = name ?: "Patient"
        }
        viewModel.loadUsernameForPatient(patientId)

        val recyclerView = requireView().findViewById<RecyclerView>(R.id.treatments_doctor_recycler)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = DoctorTreatmentAdapter(emptyList())
        recyclerView.adapter = adapter

        viewModel.loadTreatmentsForPatient(patientId)
    }

    private fun setupAddTreatmentButton() {
        requireView().findViewById<FloatingActionButton>(R.id.add_treatment_button).setOnClickListener {

        }
    }
}