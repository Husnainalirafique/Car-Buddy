package com.example.carbuddy.ui.fragments.vendorProfile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.carbuddy.R
import com.example.carbuddy.data.models.comment.Comment
import com.example.carbuddy.databinding.ItemCommentBinding
import java.util.concurrent.TimeUnit

class AdapterComments(
    private val items: List<Comment>,
    private val onDeleteClick: (Comment) -> Unit
) :
    RecyclerView.Adapter<AdapterComments.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = items[position]
        holder.bind(data)
    }

    inner class ViewHolder(val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Comment) {
            binding.tvUserName.text = data.userName
            binding.tvTime.text = getRelativeTime(data.timestamp)
            binding.tvComment.text = data.comment

            itemView.setOnLongClickListener {
                showPopupMenu(it, data)
                true
            }
        }
    }

    private fun showPopupMenu(view: View, comment: Comment) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.menuInflater.inflate(R.menu.comment_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.btnCommentDelete -> {
                    onDeleteClick(comment)
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }

    private fun getRelativeTime(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp

        return when {
            diff < TimeUnit.MINUTES.toMillis(1) -> "Just now"
            diff < TimeUnit.HOURS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toMinutes(diff)} min ago"
            diff < TimeUnit.DAYS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toHours(diff)} hours ago"
            diff < TimeUnit.DAYS.toMillis(30) -> "${TimeUnit.MILLISECONDS.toDays(diff)} days ago"
            diff < TimeUnit.DAYS.toMillis(365) -> "${TimeUnit.MILLISECONDS.toDays(diff) / 30} months ago"
            else -> "${TimeUnit.MILLISECONDS.toDays(diff) / 365} years ago"
        }
    }

}