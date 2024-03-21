package com.example.carbuddy.ui.fragments.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.carbuddy.R
import com.example.carbuddy.databinding.FragmentProfileBinding
import com.example.carbuddy.preferences.PreferenceManager
import com.example.carbuddy.ui.activities.AuthActivity
import com.example.carbuddy.utils.Dialogs
import com.example.carbuddy.utils.Glide
import com.example.carbuddy.utils.startActivity
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var auth: FirebaseAuth
    @Inject
    lateinit var gso: GoogleSignInClient
    @Inject
    lateinit var preferenceManager: PreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        inIt()
        return binding.root
    }

    private fun inIt() {
        setUserDataToViews()
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.btnLogout.setOnClickListener {
            logOut()
        }

        binding.btnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

        binding.btnMyCars.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_myVehicleFragment)
        }
    }

    private fun logOut() {
        Dialogs.logoutDialog(requireContext(), layoutInflater) {
            gso.signOut().addOnSuccessListener {
                auth.signOut()
                startActivity(AuthActivity::class.java)
                requireActivity().finish()

            }
        }
    }

    private fun setUserDataToViews() {
        val user = preferenceManager.getUserData()
        if (user != null) {
            binding.tvUserName.text = user.fullName
            binding.tvUserEmail.text = user.email
            Glide.loadImage(requireContext(), user.profileImageUri, binding.imgProfilePic)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}