package com.unibuc.medtrack.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.unibuc.medtrack.R
import com.unibuc.medtrack.data.models.FullTreatmentWithNotifications
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DoctorTreatmentAdapter(
    private val treatments: List<FullTreatmentWithNotifications>
) : RecyclerView.Adapter<DoctorTreatmentAdapter.TreatmentViewHolder>() {

    inner class TreatmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.name)
        val dose: TextView = view.findViewById(R.id.dose)
        val hour: TextView = view.findViewById(R.id.hour)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TreatmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_treatment_doctor, parent, false)
        return TreatmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: TreatmentViewHolder, position: Int) {
        val item = treatments[position]
        val treatment = item.treatment
        val notification = item.notification

        Log.i("TreatmentAdapter", "Treatment data: " + treatment);
        Log.i("TreatmentAdapter", "Notification data: " + notification);

        holder.name.text = treatment.medicineName
        holder.dose.text = "${treatment.dosage} ${treatment.dosageUnit} every ${treatment.dose_interval}h"

        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        holder.hour.text = notification.time.format(formatter)

        val container = holder.itemView.findViewById<View>(R.id.treatment_container)

        val bgRes = when {
            notification.takenAt != null -> R.drawable.shape_rounded_treatment_outer_green
            notification.time.plusMinutes(15).isBefore(LocalDateTime.now()) -> R.drawable.shape_rounded_treatment_outer_red
            else -> R.drawable.shape_rounded_treatment_outer
        }

        container.setBackgroundResource(bgRes)
    }

    override fun getItemCount(): Int = treatments.size
}

