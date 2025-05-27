package com.unibuc.medtrack.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.unibuc.medtrack.R
import java.util.Calendar
import java.util.Date

class CalendarAdapter(
    private val days: List<Int>,
    private val firstDay: Date,
    private val onDayClick: (Int) -> Unit
) : RecyclerView.Adapter<CalendarAdapter.DayViewHolder>() {

    private val todayDate: Date = Calendar.getInstance().time

    inner class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dayText: TextView = itemView.findViewById(R.id.day_text)

        fun bind(day: Int) {
            Calendar.getInstance().time

            if (day != -1) {
                dayText.text = day.toString()
                dayText.setOnClickListener {
                    onDayClick(day)
                }

                val thisDay = Calendar.getInstance().apply {
                    set(Calendar.YEAR, firstDay.year+1900)
                    set(Calendar.MONTH, firstDay.month)
                    set(Calendar.DAY_OF_MONTH, day)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                val today = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                when {
                    thisDay.before(today) -> {
                        dayText.setTextColor(Color.GRAY)
                        dayText.setBackgroundColor(Color.TRANSPARENT)
                    }
                    thisDay.equals(today) -> {
                        dayText.setTextColor(Color.WHITE)
                        dayText.setBackgroundResource(R.drawable.shape_rounded_blue)
                    }
                    else -> {
                        dayText.setTextColor(Color.BLACK)
                        dayText.setBackgroundColor(Color.TRANSPARENT)
                    }
                }
            } else {
                dayText.text = ""
                dayText.setOnClickListener(null)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calendar_day, parent, false)

        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind(days[position])
    }

    override fun getItemCount(): Int = days.size
}
