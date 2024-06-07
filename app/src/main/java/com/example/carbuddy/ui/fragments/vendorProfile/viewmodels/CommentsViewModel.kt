package com.example.carbuddy.ui.fragments.vendorProfile.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.carbuddy.data.models.comment.Comment
import com.example.carbuddy.utils.DataState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val db: FirebaseFirestore
) : ViewModel() {

    private val _comments = MutableLiveData<DataState<List<Comment>>>()
    val comments: LiveData<DataState<List<Comment>>> = _comments
    private var commentsListener: ListenerRegistration? = null

    fun saveComment(comment: Comment, providerUid: String) {
        val commentsRef = db.collection("comments").document(providerUid).collection("comments")

        commentsRef.add(comment)
            .addOnSuccessListener {
                loadComments(providerUid)
            }
            .addOnFailureListener { e ->
                _comments.value = DataState.Error(e.localizedMessage ?: "Unknown error")
            }
    }

    fun loadComments(providerUid: String) {
        _comments.value = DataState.Loading

        val commentsRef = db.collection("comments").document(providerUid).collection("comments")
            .orderBy("timestamp", Query.Direction.DESCENDING)

        commentsListener?.remove()

        commentsListener = commentsRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                _comments.value = DataState.Error(error.localizedMessage ?: "Unknown error")
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                val commentsList = snapshot.documents.map { document ->
                    val comment = document.toObject(Comment::class.java)!!
                    comment.docId = document.id // Set the document ID
                    comment
                }
                _comments.value = DataState.Success(commentsList)
            } else {
                _comments.value = DataState.Success(emptyList())
            }
        }
    }

    fun deleteComment(providerUid: String, commentId: String) {
        val commentRef = db.collection("comments").document(providerUid).collection("comments")
            .document(commentId)

        commentRef.delete()
            .addOnSuccessListener {
                // Optionally, you can reload comments or handle UI updates
            }
            .addOnFailureListener { e ->
                _comments.value = DataState.Error(e.localizedMessage ?: "Unknown error")
            }
    }


    override fun onCleared() {
        super.onCleared()
        commentsListener?.remove()
    }
}
