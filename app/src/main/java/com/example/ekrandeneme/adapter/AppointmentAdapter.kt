package com.example.ekrandeneme.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ekrandeneme.R

class AppointmentAdapter(
    private var appointments: List<Map<String, Any>>,
    private val onConfirmClick: (Long) -> Unit,
    private val onCompleteClick: (Long) -> Unit,
    private val onCancelClick: (Long) -> Unit
) : RecyclerView.Adapter<AppointmentAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val customerName: TextView = view.findViewById(R.id.textViewCustomerName)
        val status: TextView = view.findViewById(R.id.textViewStatus)
        val service: TextView = view.findViewById(R.id.textViewService)
        val date: TextView = view.findViewById(R.id.textViewDate)
        val time: TextView = view.findViewById(R.id.textViewTime)
        val buttonConfirm: Button = view.findViewById(R.id.buttonConfirm)
        val buttonComplete: Button = view.findViewById(R.id.buttonComplete)
        val buttonCancel: Button = view.findViewById(R.id.buttonCancel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_appointment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appointment = appointments[position]
        
        holder.customerName.text = appointment["customer_name"] as String
        holder.service.text = appointment["service_name"] as String
        holder.date.text = appointment["date"] as String
        holder.time.text = appointment["time"] as String

        val status = appointment["status"] as String
        holder.status.text = when (status) {
            "PENDING" -> "Bekleyen"
            "CONFIRMED" -> "Onaylandı"
            "COMPLETED" -> "Tamamlandı"
            "CANCELLED" -> "İptal Edildi"
            else -> status
        }

        // Set status background color
        val colorRes = when (status) {
            "PENDING" -> android.R.color.holo_orange_dark
            "CONFIRMED" -> android.R.color.holo_green_dark
            "COMPLETED" -> android.R.color.holo_blue_dark
            "CANCELLED" -> android.R.color.holo_red_dark
            else -> android.R.color.darker_gray
        }
        holder.status.setBackgroundColor(ContextCompat.getColor(holder.status.context, colorRes))

        // Show/hide buttons based on status
        val appointmentId = appointment["appointment_id"] as Long
        when (status) {
            "PENDING" -> {
                holder.buttonConfirm.visibility = View.VISIBLE
                holder.buttonComplete.visibility = View.GONE
                holder.buttonCancel.visibility = View.VISIBLE
            }
            "CONFIRMED" -> {
                holder.buttonConfirm.visibility = View.GONE
                holder.buttonComplete.visibility = View.VISIBLE
                holder.buttonCancel.visibility = View.VISIBLE
            }
            else -> {
                holder.buttonConfirm.visibility = View.GONE
                holder.buttonComplete.visibility = View.GONE
                holder.buttonCancel.visibility = View.GONE
            }
        }

        // Set button click listeners
        holder.buttonConfirm.setOnClickListener { onConfirmClick(appointmentId) }
        holder.buttonComplete.setOnClickListener { onCompleteClick(appointmentId) }
        holder.buttonCancel.setOnClickListener { onCancelClick(appointmentId) }
    }

    override fun getItemCount() = appointments.size

    fun updateAppointments(newAppointments: List<Map<String, Any>>) {
        appointments = newAppointments
        notifyDataSetChanged()
    }
} 