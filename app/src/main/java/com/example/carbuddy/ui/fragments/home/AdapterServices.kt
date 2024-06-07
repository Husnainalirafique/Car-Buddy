package com.example.carbuddy.ui.fragments.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carbuddy.data.models.ModelVendorProfile
import com.example.carbuddy.databinding.ItemServiceBinding
import com.example.carbuddy.utils.Glide

class AdapterServices(private val items: List<ModelVendorProfile>) :
    RecyclerView.Adapter<AdapterServices.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = items[position]
        holder.bind(data)
    }

    private var itemClickListener: ((ModelVendorProfile) -> Unit)? = null

    fun itemClickListener(listener: (ModelVendorProfile) -> Unit) {
        itemClickListener = listener
    }

    inner class ViewHolder(val binding: ItemServiceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ModelVendorProfile) {
            binding.tvProviderName.text = data.fullName
            binding.tvDistance.text = "5km away"
            binding.tvSpeciality.text = data.speciality
            Glide.loadImage(itemView.context, data.vendorImage, binding.imgServiceProvider)

            itemView.setOnClickListener {
                itemClickListener?.invoke(data)
            }
        }
    }

}