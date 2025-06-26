package com.unibuc.medtrack.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.unibuc.medtrack.R
import com.unibuc.medtrack.data.models.FullTreatmentWithNotifications
import com.unibuc.medtrack.data.models.TreatmentModel
import java.time.format.DateTimeFormatter

class TreatmentAdapter(
    private val treatments: List<FullTreatmentWithNotifications>
) : RecyclerView.Adapter<TreatmentAdapter.TreatmentViewHolder>() {

    inner class TreatmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.name)
        val dose: TextView = view.findViewById(R.id.dose)
        val hour: TextView = view.findViewById(R.id.hour)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TreatmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_treatment_patient, parent, false)
        return TreatmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: TreatmentViewHolder, position: Int) {
        val item = treatments[position]
        val treatment = item.treatment
        val notification = item.notification

        holder.name.text = treatment.medicineName
        holder.dose.text = "${treatment.dosage} ${treatment.dosageUnit} every ${treatment.dose_interval}h"

        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        holder.hour.text = notification.time.format(formatter)
    }

    override fun getItemCount(): Int = treatments.size
}

