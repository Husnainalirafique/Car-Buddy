package com.example.carbuddy.ui.fragments.vendorProfile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.carbuddy.R
import com.example.carbuddy.databinding.FragmentVendorProfileBinding
import com.example.carbuddy.utils.ClipBoardUtils.copyTextToClipboard
import com.example.carbuddy.utils.Dialogs

class VendorProfileFragment : Fragment() {
    private var _binding: FragmentVendorProfileBinding? = null
    private val binding get() = _binding!!
    private var isLiked = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVendorProfileBinding.inflate(inflater, container, false)
        inIt()
        return binding.root
    }

    private fun inIt() {
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.btnLike.setOnClickListener {
            isLiked = !isLiked
            updateLikeButton()
        }
        binding.btnContact.setOnClickListener {
            Dialogs.dialogContactViaPhone(
                context = requireContext(),
                inflater = layoutInflater,
                copy = { copy("+923463752125") },
                callUs = { makePhoneCall() },
                openWhatsapp = { openWhatsAppChat() }
            )
        }
    }

    private fun updateLikeButton() {
        if (isLiked) {
            binding.btnLike.setImageResource(R.drawable.icon_heart_filled_red)
        } else {
            binding.btnLike.setImageResource(R.drawable.icon_heart_thin)
        }
    }

    private fun copy(text: String) {
        copyTextToClipboard(text)
    }

    private fun makePhoneCall() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:${+923463752125}")
        startActivity(intent)
    }

    private fun openWhatsAppChat() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://wa.me/${+923463752125}")
        startActivity(intent)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}