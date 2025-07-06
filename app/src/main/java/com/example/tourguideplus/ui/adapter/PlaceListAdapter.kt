package com.example.tourguideplus.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.tourguideplus.R
import com.example.tourguideplus.data.model.Place

class PlaceListAdapter(
    private val onClick: (Place) -> Unit
) : ListAdapter<Place, PlaceListAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_place, parent, false)
        return ViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        itemView: View,
        val onClick: (Place) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val ivPreview: ImageView = itemView.findViewById(R.id.iv_preview)
        private val tvName: TextView    = itemView.findViewById(R.id.tv_name)
        private val tvCategory: TextView= itemView.findViewById(R.id.tv_category)

        fun bind(place: Place) {
            tvName.text = place.name
            tvCategory.text = place.category
            if (place.imageUri != null) {
                ivPreview.load(place.imageUri)
            } else {
                ivPreview.setImageResource(android.R.drawable.ic_menu_report_image)
            }
            itemView.setOnClickListener { onClick(place) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Place>() {
            override fun areItemsTheSame(old: Place, new: Place) = old.id == new.id
            override fun areContentsTheSame(old: Place, new: Place) = old == new
        }
    }
}
