package com.unibuc.medtrack.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.unibuc.medtrack.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.unibuc.medtrack.adapters.DoctorTreatmentAdapter
import dagger.hilt.android.AndroidEntryPoint

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

        viewModel.treatments.observe(viewLifecycleOwner) { treatments ->
            if (treatments != null) {
                val adapter = DoctorTreatmentAdapter(treatments)
                recyclerView.adapter = adapter
            }
        }
        viewModel.loadTreatmentsForPatient(patientId)
    }

    private fun setupAddTreatmentButton() {
        val patientId = arguments?.getString("patientId") ?: return

        requireView().findViewById<FloatingActionButton>(R.id.add_treatment_button)
            .setOnClickListener {
                val bundle = Bundle().apply {
                    putString("patientId", patientId)
                }
                findNavController().navigate(
                    R.id.action_doctorPatientTreatmentFragment_to_treatmentFormFragment,
                    bundle
                )
            }
    }
}