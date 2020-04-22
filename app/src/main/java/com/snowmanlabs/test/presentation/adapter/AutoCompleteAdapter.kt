package com.snowmanlabs.test.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.snowmanlabs.test.R
import kotlinx.android.synthetic.main.adapter_search_result.view.*

class AutoCompleteAdapter(private val results: List<AutocompletePrediction>, private val onClick: (String) -> Unit) :
    RecyclerView.Adapter<AutoCompleteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_search_result, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = results.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(results[position], onClick)
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val name: TextView = item.locationName
        private val description: TextView = item.locationDescription
        private val location: LinearLayout = item.locationLayout

        fun bind(prediction: AutocompletePrediction, onClick: (String) -> Unit) {
            name.text = prediction.getPrimaryText(null).toString()
            description.text = prediction.getSecondaryText(null).toString()

            location.setOnClickListener { onClick(prediction.placeId) }
        }
    }
}