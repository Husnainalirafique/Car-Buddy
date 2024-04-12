package com.example.carbuddy.ui.fragments.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carbuddy.data.models.service.ModelService
import com.example.carbuddy.databinding.ItemServiceBinding

class AdapterServices(private val items: List<ModelService>) :
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

    inner class ViewHolder(val binding: ItemServiceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ModelService) {
            binding.tvProviderName.text = data.nameProvider
            binding.tvDistance.text = data.distanceToProvider
            binding.tvSpeciality.text = data.specialityOfProvider
            binding.imgServiceProvider.setImageResource(data.imgProvider)
        }
    }

}