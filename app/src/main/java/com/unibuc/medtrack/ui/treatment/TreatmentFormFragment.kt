package com.unibuc.medtrack.ui.treatment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.unibuc.medtrack.R
import com.unibuc.medtrack.data.models.MeasurementUnit
import com.unibuc.medtrack.data.models.TreatmentModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Locale
import java.util.UUID

@AndroidEntryPoint
class TreatmentFormFragment : Fragment() {
    private val viewModel: TreatmentFormViewModel by viewModels()
    private lateinit var medicineNameEdit: TextInputEditText
    private lateinit var descriptionEdit: TextInputEditText
    private lateinit var dosageEdit: TextInputEditText
    private lateinit var dosageIntervalEdit: TextInputEditText
    private lateinit var unitSpinner: Spinner
    private lateinit var startDateEdit: TextInputEditText
    private lateinit var endDateEdit: TextInputEditText
    private lateinit var startDateLayout: TextInputLayout
    private lateinit var endDateLayout: TextInputLayout


    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_treatment_form, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        medicineNameEdit = view.findViewById(R.id.input_treatment_name)
        descriptionEdit = view.findViewById(R.id.input_description)
        dosageEdit = view.findViewById(R.id.input_treatment_dosage)
        dosageIntervalEdit = view.findViewById(R.id.input_interval)
        unitSpinner = view.findViewById(R.id.dosage_unit_spinner)
        startDateEdit = view.findViewById(R.id.input_start_date)
        endDateEdit = view.findViewById(R.id.input_end_date)
        startDateLayout = view.findViewById(R.id.start_date)
        endDateLayout = view.findViewById(R.id.end_date)

        val units = MeasurementUnit.entries.map { it.name }
        unitSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, units)

        startDateEdit.setOnClickListener { showDatePicker(startDateEdit) }
        endDateEdit.setOnClickListener { showDatePicker(endDateEdit) }

        val patientId = arguments?.getString("patientId") ?: return

        view.findViewById<Button>(R.id.btn_save_treatment).setOnClickListener {
            if (validateForm()) {
                val treatment = TreatmentModel(
                    id = UUID.randomUUID().toString(),
                    doctorId = "",
                    patientId = patientId,
                    startDate = startDateEdit.text.toString(),
                    endDate = endDateEdit.text.toString(),
                    createdAt = LocalDateTime.now(),
                    medicineName = medicineNameEdit.text.toString(),
                    description = descriptionEdit.text.toString(),
                    dose_interval = dosageIntervalEdit.text.toString().toIntOrNull() ?: 0,
                    dosage = dosageEdit.text.toString().toDoubleOrNull() ?: 0.0,
                    dosageUnit = MeasurementUnit.valueOf(unitSpinner.selectedItem.toString())
                )
                viewModel.saveTreatment(treatment)
                val bundle = Bundle().apply {
                    putString("patientId", patientId)
                }
                findNavController().navigate(R.id.action_treatmentFormFragment_to_doctorPatientTreatmentFragment, bundle)
            }
        }
    }

    private fun showDatePicker(target: TextInputEditText) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val dateStr = "%04d-%02d-%02d".format(year, month + 1, dayOfMonth)
                target.setText(dateStr)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun validateForm(): Boolean {
        var valid = true

        startDateLayout.error = null
        endDateLayout.error = null
        medicineNameEdit.error = null
        descriptionEdit.error = null
        dosageEdit.error = null
        dosageIntervalEdit.error = null

        val startDateStr = startDateEdit.text.toString()
        val endDateStr = endDateEdit.text.toString()
        val medicineNameStr = medicineNameEdit.text.toString()
        val descriptionStr = descriptionEdit.text.toString()
        val dosageStr = dosageEdit.text.toString()
        val dosageIntervalStr = dosageIntervalEdit.text.toString()

        if (medicineNameStr.isBlank()) {
            medicineNameEdit.error = "Medicine name required"
            valid = false
        }
        if (descriptionStr.isBlank()) {
            descriptionEdit.error = "Description required"
            valid = false
        }
        if (dosageStr.isBlank()) {
            dosageEdit.error = "Dosage required"
            valid = false
        }
        if (dosageIntervalStr.isBlank()) {
            dosageIntervalEdit.error = "Interval required"
            valid = false
        }
        if (startDateStr.isBlank()) {
            startDateLayout.error = "Start date required"
            valid = false
        }
        if (endDateStr.isBlank()) {
            endDateLayout.error = "End date required"
            valid = false
        }

        if (valid) {
            try {
                val start = dateFormat.parse(startDateStr)
                val end = dateFormat.parse(endDateStr)
                if (!start.before(end)) {
                    endDateLayout.error = "End date must be after start date"
                    valid = false
                }
            } catch (e: Exception) {
                startDateLayout.error = "Invalid date"
                endDateLayout.error = "Invalid date"
                valid = false
            }
        }
        return valid
    }
}