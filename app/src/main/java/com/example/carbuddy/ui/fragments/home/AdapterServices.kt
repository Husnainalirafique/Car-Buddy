package com.example.carbuddy.ui.fragments.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.example.carbuddy.data.models.vendor.ModelVendorProfile
import com.example.carbuddy.databinding.ItemServiceBinding
import com.example.carbuddy.utils.Glide
import com.example.carbuddy.utils.MapUtils
import com.example.carbuddy.utils.SearchFilter
import com.example.carbuddy.utils.gone
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import java.util.Locale

class AdapterServices(
    private var items: List<ModelVendorProfile>,
    private val currentLatLng: LatLng? = null
) :
    RecyclerView.Adapter<AdapterServices.ViewHolder>() {

    init {
        setHasStableIds(true)
    }

    private var originalItems: List<ModelVendorProfile> = ArrayList(items)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    fun getServiceProviders(): List<ModelVendorProfile> {
        return items
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = items[position]
        holder.bind(data)
    }

    fun getFilter(): Filter {
        return SearchFilter(originalList = originalItems, adapter = this)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(newItems: List<ModelVendorProfile>) {
        items = newItems
        notifyDataSetChanged()
    }

    private var itemClickListener: ((ModelVendorProfile) -> Unit)? = null

    fun itemClickListener(listener: (ModelVendorProfile) -> Unit) {
        itemClickListener = listener
    }

    inner class ViewHolder(val binding: ItemServiceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ModelVendorProfile) {
            binding.tvProviderName.text = data.fullName
            binding.tvSpeciality.text = data.speciality
            Glide.loadImageWithListener(
                itemView.context,
                data.vendorImage,
                binding.imgServiceProvider
            ) {
                binding.progressCircular.gone()
            }

            itemView.setOnClickListener {
                itemClickListener?.invoke(data)
            }

            // Calculate distance from the current location to the service provider's location
            val (latitude, longitude) = MapUtils.extractLatLong(data.addressFromMap)!!
            if (currentLatLng != null) {
                val distance = calculateDistance(
                    currentLatLng.latitude,
                    currentLatLng.longitude,
                    latitude,
                    longitude
                )
                binding.tvDistance.text = String.format(Locale.getDefault(), "%.2f km", distance)
            } else {
                binding.tvDistance.text = "Distance: N/A"
            }
        }
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val distance = SphericalUtil.computeDistanceBetween(
            LatLng(lat1, lon1),
            LatLng(lat2, lon2)
        )
        return distance / 1000.toDouble() // Convert meters to kilometers
    }

}