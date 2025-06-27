package com.unibuc.medtrack.ui.treatment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputEditText
import com.unibuc.medtrack.R
import com.unibuc.medtrack.data.models.MeasurementUnit
import java.util.Calendar

class TreatmentFormFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_treatment_form, container, false)

    private val viewModel: TreatmentFormViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val unitSpinner = view.findViewById<Spinner>(R.id.dosage_unit_spinner)
        val units = MeasurementUnit.entries.map { it.name }
        unitSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, units)
        val startDateEdit = view.findViewById<TextInputEditText>(R.id.input_start_date)
        val endDateEdit = view.findViewById<TextInputEditText>(R.id.input_end_date)

        fun showDatePicker(target: TextInputEditText) {
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

        startDateEdit.setOnClickListener { showDatePicker(startDateEdit) }
        endDateEdit.setOnClickListener { showDatePicker(endDateEdit) }
    }
}