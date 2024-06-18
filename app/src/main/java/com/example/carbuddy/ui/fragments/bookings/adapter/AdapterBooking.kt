package com.example.carbuddy.ui.fragments.bookings.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carbuddy.data.models.booking.ModelBookingData
import com.example.carbuddy.databinding.ItemBookingBinding
import com.example.carbuddy.utils.DateTimeUtils
import com.example.carbuddy.utils.Glide
import com.example.carbuddy.utils.gone

class AdapterBooking(private val items: List<ModelBookingData>) :
    RecyclerView.Adapter<AdapterBooking.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBookingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = items[position]
        holder.bind(data)
    }

    inner class ViewHolder(val binding: ItemBookingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ModelBookingData) {
            Glide.loadImageWithListener(itemView.context, data.userImageUri, binding.imgUser) {
                binding.progressCircular.gone()
            }
            binding.tvUserName.text = data.userName
            binding.tvTypeTag.text = data.bookingTypeTag
            binding.tvBookingStatus.text = data.bookingStatus
            val time = DateTimeUtils.forBookingDateAndTime(data.timestamp)
            binding.tvTimeStamp.text = time
        }
    }

}