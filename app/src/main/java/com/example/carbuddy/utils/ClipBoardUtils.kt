package com.example.carbuddy.utils
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

object ClipBoardUtils {
    fun Fragment.copyTextToClipboard(text: String) {
        val clipboardManager =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Copied Text", text)
        clipboardManager.setPrimaryClip(clipData)

        Toast.makeText(context, "Copied.", Toast.LENGTH_SHORT).show()
    }
}