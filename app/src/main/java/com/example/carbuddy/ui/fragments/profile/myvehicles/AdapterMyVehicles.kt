package com.example.carbuddy.ui.fragments.profile.myvehicles

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carbuddy.data.models.vehicles.ModelVehicle
import com.example.carbuddy.databinding.ItemVehicleBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdapterMyVehicles(private val items: List<ModelVehicle>) :
    RecyclerView.Adapter<AdapterMyVehicles.ViewHolder>() {
    val db = Firebase.firestore
    val auth = Firebase.auth
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemVehicleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = items[position]
        holder.bind(data)
    }

    inner class ViewHolder(val binding: ItemVehicleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ModelVehicle) {
            binding.tvVehicleName.text = data.model
            binding.tvMake.text = data.make
            binding.tvYear.text = data.year.toString()
            binding.tvPlateNumber.text = data.lpn
            deleteVehicle(data.docId)
        }

        private fun deleteVehicle(docId: String) {
            binding.btnDeleteVehicle.setOnClickListener {
                db.collection("users_vehicles")
                    .document(auth.uid!!)
                    .collection("vehicles")
                    .document(docId)
                    .delete()
            }
        }
    }

}