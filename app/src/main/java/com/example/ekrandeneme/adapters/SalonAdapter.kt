package com.example.ekrandeneme.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ekrandeneme.R

class SalonAdapter : RecyclerView.Adapter<SalonAdapter.SalonViewHolder>(), Filterable {
    private var salons: List<Map<String, String>> = listOf()
    private var filteredSalons: List<Map<String, String>> = listOf()
    private var onItemClickListener: ((Map<String, String>) -> Unit)? = null

    fun setData(newSalons: List<Map<String, String>>) {
        salons = newSalons
        filteredSalons = newSalons
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (Map<String, String>) -> Unit) {
        onItemClickListener = listener
    }

    fun sortByRating(ascending: Boolean) {
        filteredSalons = if (ascending) {
            filteredSalons.sortedBy { it["rating"]?.toFloatOrNull() ?: 0f }
        } else {
            filteredSalons.sortedByDescending { it["rating"]?.toFloatOrNull() ?: 0f }
        }
        notifyDataSetChanged()
    }

    fun sortByDate() {
        // Eğer created_at alanı varsa buna göre sıralama yapılabilir
        filteredSalons = filteredSalons.sortedByDescending { it["id"]?.toIntOrNull() ?: 0 }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_salon, parent, false)
        return SalonViewHolder(view)
    }

    override fun onBindViewHolder(holder: SalonViewHolder, position: Int) {
        val salon = filteredSalons[position]
        holder.bind(salon)
        holder.itemView.setOnClickListener { onItemClickListener?.invoke(salon) }
    }

    override fun getItemCount(): Int = filteredSalons.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.lowercase() ?: ""
                val filteredList = if (query.isEmpty()) {
                    salons
                } else {
                    salons.filter { salon ->
                        salon["name"]?.lowercase()?.contains(query) == true ||
                        salon["address"]?.lowercase()?.contains(query) == true ||
                        salon["phone"]?.contains(query) == true
                    }
                }
                return FilterResults().apply { values = filteredList }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredSalons = results?.values as? List<Map<String, String>> ?: listOf()
                notifyDataSetChanged()
            }
        }
    }

    class SalonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewSalonName: TextView = itemView.findViewById(R.id.textViewSalonName)
        private val textViewAddress: TextView = itemView.findViewById(R.id.textViewAddress)
        private val textViewPhone: TextView = itemView.findViewById(R.id.textViewPhone)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        private val textViewRating: TextView = itemView.findViewById(R.id.textViewRating)

        fun bind(salon: Map<String, String>) {
            textViewSalonName.text = salon["name"]
            textViewAddress.text = salon["address"]
            textViewPhone.text = salon["phone"]
            
            val rating = salon["rating"]?.toFloatOrNull() ?: 0f
            ratingBar.rating = rating
            textViewRating.text = String.format("%.1f", rating)
        }
    }
} 