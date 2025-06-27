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

class TreatmentAdapter(
    private val treatments: List<FullTreatmentWithNotifications>,
    private val onCheckboxChecked: (notificationId: String) -> Unit
) : RecyclerView.Adapter<TreatmentAdapter.TreatmentViewHolder>() {

    inner class TreatmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.name)
        val dose: TextView = view.findViewById(R.id.dose)
        val hour: TextView = view.findViewById(R.id.hour)
        val checkBox: CheckBox = view.findViewById(R.id.checkBox)
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

        Log.i("TreatmentAdapter", "Treatment data: " + treatment);
        Log.i("TreatmentAdapter", "Notification data: " + notification);

        holder.name.text = treatment.medicineName
        holder.dose.text = "${treatment.dosage} ${treatment.dosageUnit} every ${treatment.dose_interval}h"

        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        holder.hour.text = notification.time.format(formatter)

        holder.checkBox.setOnCheckedChangeListener(null)

        holder.checkBox.isChecked = notification.takenAt != null
        holder.checkBox.isEnabled = notification.takenAt == null

        val now = LocalDateTime.now()

        val container = holder.itemView.findViewById<View>(R.id.treatment_container)

        val bgRes = when {
            notification.takenAt != null -> R.drawable.shape_rounded_treatment_outer_green
            notification.time.plusMinutes(15).isBefore(LocalDateTime.now()) -> R.drawable.shape_rounded_treatment_outer_red
            else -> R.drawable.shape_rounded_treatment_outer
        }

        container.setBackgroundResource(bgRes)


        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            Log.i("TreatmentAdapter", "checked box")
            if (isChecked && notification.takenAt == null) {
                if (now.isBefore(notification.time)) {
                    holder.checkBox.isChecked = false
                    Toast.makeText(holder.itemView.context, "You can't take this treatment before the scheduled time.", Toast.LENGTH_SHORT).show()
                } else {
                    holder.checkBox.isEnabled = false
                    Toast.makeText(holder.itemView.context, "Treatment taken successfully", Toast.LENGTH_SHORT).show()
                    onCheckboxChecked(notification.id)
                }
            }
        }
    }

    override fun getItemCount(): Int = treatments.size
}

