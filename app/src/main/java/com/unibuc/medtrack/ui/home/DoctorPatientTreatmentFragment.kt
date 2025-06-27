package com.unibuc.medtrack.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.unibuc.medtrack.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unibuc.medtrack.adapters.DoctorTreatmentAdapter
import dagger.hilt.android.AndroidEntryPoint
import com.unibuc.medtrack.adapters.TreatmentAdapter

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
        val patientId = arguments?.getString("patientId") ?: return

        viewModel.username.observe(viewLifecycleOwner) { name ->
            view.findViewById<TextView>(R.id.patient_name).text = name ?: "Patient"
        }
        viewModel.loadUsernameForPatient(patientId)

        val recyclerView = view.findViewById<RecyclerView>(R.id.treatments_doctor_recycler)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = DoctorTreatmentAdapter(emptyList())
        recyclerView.adapter = adapter

        viewModel.loadTreatmentsForPatient(patientId)
    }
}