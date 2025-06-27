package com.unibuc.medtrack.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.unibuc.medtrack.R
import com.unibuc.medtrack.data.models.UserModel

class PatientAdapter(private val patients: List<UserModel>, private val onOpenTreatment: (UserModel) -> Unit) :
    RecyclerView.Adapter<PatientAdapter.PatientViewHolder>() {

    class PatientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.patient_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_patient, parent, false)
        return PatientViewHolder(view)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        val patient = patients[position]
        holder.name.text = patient.name
        holder.itemView.findViewById<ImageButton>(R.id.open_patient_treatment_button)
            .setOnClickListener {
                onOpenTreatment(patient)
            }
    }

    override fun getItemCount() = patients.size
}