package com.example.carbuddy.ui.fragments.vendorProfile.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.carbuddy.data.models.comment.Comment
import com.example.carbuddy.databinding.FragmentCommentsBinding
import com.example.carbuddy.preferences.PreferenceManager
import com.example.carbuddy.ui.fragments.vendorProfile.adapter.AdapterComments
import com.example.carbuddy.ui.fragments.vendorProfile.viewmodels.CommentsViewModel
import com.example.carbuddy.utils.Constants
import com.example.carbuddy.utils.DataState
import com.example.carbuddy.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CommentsFragment : Fragment() {
    private var _binding: FragmentCommentsBinding? = null
    private val binding get() = _binding!!
    private val vm: CommentsViewModel by viewModels()
    private lateinit var vendorUid: String

    @Inject
    lateinit var preferenceManager: PreferenceManager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommentsBinding.inflate(inflater, container, false)
        messageBoxHeightToTopOfKeyboard()
        inIt()
        return binding.root
    }

    private fun messageBoxHeightToTopOfKeyboard() {
        /* The code ensures that when the keyboard (IME) appears, the view's
          bottom padding is adjusted to the height of the keyboard. */
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            // This returns the space taken up by the keyboard in pixels
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            v.setPadding(
                /* By using v.paddingLeft, v.paddingTop, and v.paddingRight, you ensure that the
                existing padding values on the left, top, and right sides of the view are preserved. */
                v.paddingLeft,
                v.paddingTop,
                v.paddingRight,
                imeInsets.bottom
            )
            WindowInsetsCompat.CONSUMED
        }

    }

    private fun inIt() {
        getVendorUid()
        setOnClickListener()
        fetchComments()
    }

    private fun getVendorUid() {
        arguments?.getString(Constants.KEY_PROVIDER_UID)?.let { vendorUid = it }
    }

    private fun fetchComments() {
        vm.loadComments(vendorUid)
        vm.comments.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Loading -> {}

                is DataState.Success -> {
                    it.data?.let { commentsList ->
                        val state = binding.rvComments.layoutManager?.onSaveInstanceState()
                        binding.rvComments.adapter = AdapterComments(commentsList) { comment ->
                            vm.deleteComment(vendorUid, comment.docId)
                        }
                        binding.rvComments.layoutManager?.onRestoreInstanceState(state)
                    }
                }

                is DataState.Error -> {
                    toast(it.errorMessage)
                }
            }
        }

    }

    private fun setOnClickListener() {
        binding.backArrow.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnSendComment.setOnClickListener {
            sendComment()
        }
    }

    private fun sendComment() {
        val etComment = binding.etComment.text
        if (etComment.toString().isNotEmpty()) {
            preferenceManager.getUserData()?.let {
                val comment = Comment(it.fullName, etComment.toString())
                vm.saveComment(comment, vendorUid)
                etComment?.clear()
            }
        } else return
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}