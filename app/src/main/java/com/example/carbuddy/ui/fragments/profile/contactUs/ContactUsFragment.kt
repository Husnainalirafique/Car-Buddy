package com.example.carbuddy.ui.fragments.profile.contactUs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.carbuddy.databinding.FragmentContactUsBinding
import com.example.carbuddy.utils.ClipBoardUtils.copyTextToClipboard
import com.example.carbuddy.utils.Dialogs

class ContactUsFragment : Fragment() {
    private var _binding: FragmentContactUsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactUsBinding.inflate(inflater, container, false)
        inIt()
        return binding.root
    }

    private fun inIt() {
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.backArrow.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnLinkedIn.setOnClickListener {
            openLinkedInProfile()
        }

        binding.btnWhatsapp.setOnClickListener {
            openWhatsAppChat()
        }

        binding.btnContactNumber.setOnClickListener {
            Dialogs.dialogContactViaPhone(
                context = requireContext(),
                inflater = layoutInflater,
                copy = { copy(binding.tvPhoneNum.text.toString()) },
                callUs = { makePhoneCall() },
                openWhatsapp = { openWhatsAppChat() }
            )
        }

        binding.btnContactEmail.setOnClickListener {
            Dialogs.dialogContactViaEmail(
                context = requireContext(),
                inflater = layoutInflater,
                copy = { copy(binding.tvEmailAddress.text.toString()) },
                sendEmail = { openEmailClient() },
            )
        }
    }

    private fun copy(text: String) {
        copyTextToClipboard(text)
    }

    private fun makePhoneCall() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:${binding.tvPhoneNum.text}")
        startActivity(intent)
    }

    private fun openWhatsAppChat() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://wa.me/${binding.tvPhoneNum.text}")
        startActivity(intent)
    }

    private fun openEmailClient() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:${binding.tvEmailAddress.text}")
        startActivity(intent)
    }

    private fun openLinkedInProfile() {
        val profileUrl = "https://www.linkedin.com/in/husnain-ali-rafique"
        val linkedInAppIntent =
            requireContext().packageManager.getLaunchIntentForPackage("com.linkedin.android")
        if (linkedInAppIntent != null) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(profileUrl))
            intent.setPackage("com.linkedin.android")
            startActivity(intent)
        } else {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(profileUrl))
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}