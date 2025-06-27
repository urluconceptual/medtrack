package com.unibuc.medtrack.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.unibuc.medtrack.R
import com.unibuc.medtrack.data.models.FullTreatmentWithNotifications
import com.unibuc.medtrack.data.models.TreatmentModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DoctorTreatmentAdapter(
    private val treatments: List<TreatmentModel>
) : RecyclerView.Adapter<DoctorTreatmentAdapter.TreatmentViewHolder>() {

    inner class TreatmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.name)
        val dose: TextView = view.findViewById(R.id.dose)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TreatmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_treatment_doctor, parent, false)
        return TreatmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: TreatmentViewHolder, position: Int) {
        val treatment = treatments[position]

        Log.i("TreatmentAdapter", "Treatment data: " + treatment);

        holder.name.text = treatment.medicineName
        holder.dose.text = "${treatment.dosage} ${treatment.dosageUnit} every ${treatment.dose_interval}h"
    }

    override fun getItemCount(): Int = treatments.size
}

