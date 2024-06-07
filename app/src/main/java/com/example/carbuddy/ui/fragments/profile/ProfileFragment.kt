package com.example.carbuddy.ui.fragments.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.carbuddy.R
import com.example.carbuddy.databinding.FragmentProfileBinding
import com.example.carbuddy.preferences.PreferenceManager
import com.example.carbuddy.ui.activities.AuthActivity
import com.example.carbuddy.utils.DataState
import com.example.carbuddy.utils.Dialogs
import com.example.carbuddy.utils.Glide
import com.example.carbuddy.utils.ProgressDialogUtil.dismissProgressDialog
import com.example.carbuddy.utils.ProgressDialogUtil.showProgressDialog
import com.example.carbuddy.utils.gone
import com.example.carbuddy.utils.startActivity
import com.example.carbuddy.utils.toast
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
    lateinit var preferenceManager: PreferenceManager
    private val vmProfile: VmProfile by viewModels()

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
        setUpObserver()
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

        binding.btnPickPhoto.setOnClickListener {
            openPhotoPicker()
        }
        binding.btnContactUs.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_contactUsFragment)
        }
    }

    private fun logOut() {
        Dialogs.logoutDialog(requireContext(), layoutInflater) {
            auth.signOut()
            startActivity(AuthActivity::class.java)
            requireActivity().finish()
        }
    }

    private fun setUserDataToViews() {
        val user = preferenceManager.getUserData()
        if (user != null) {
            binding.tvUserName.text = user.fullName
            binding.tvUserEmail.text = user.email
            Glide.loadImageWithListener(
                requireContext(),
                user.profileImageUri,
                binding.imgProfilePic
            ) {
                binding.pgProfileImage.gone()
            }
        }
    }

    private fun setUpObserver() {
        vmProfile.userPicUpdate.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Success -> {
                    dismissProgressDialog()
                }

                is DataState.Error -> {
                    toast(it.errorMessage)
                    dismissProgressDialog()
                }

                is DataState.Loading -> {
                    showProgressDialog()
                }
            }
        }
    }

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                binding.imgProfilePic.setImageURI(uri)
                vmProfile.updatePic(uri)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    private fun openPhotoPicker() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}