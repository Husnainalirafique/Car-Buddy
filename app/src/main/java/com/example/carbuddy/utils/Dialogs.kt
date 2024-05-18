package com.example.carbuddy.utils

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import com.example.carbuddy.databinding.DialogContactEmailBinding
import com.example.carbuddy.databinding.DialogContactThroughNumberBinding
import com.example.carbuddy.databinding.DialogLogoutBinding
import java.util.Calendar
import java.util.Date

object Dialogs {

    fun logoutDialog(
        context: Context,
        inflater: LayoutInflater,
        logout: () -> Unit
    ) {
        val dialog = Dialog(context)
        val binding = DialogLogoutBinding.inflate(inflater, null, false)

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.gravity = Gravity.BOTTOM
        layoutParams.verticalMargin = 0.019f // 10% margin from bottom

        dialog.window?.attributes = layoutParams

        dialog.apply {
            setContentView(binding.root)
            setCancelable(true)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            binding.btnYes.setOnClickListener {
                logout.invoke()
                dismiss()
            }
            binding.btnNo.setOnClickListener {
                dismiss()
            }
            show()
        }
    }

    fun dialogContactViaPhone(
        context: Context,
        inflater: LayoutInflater,
        copy: () -> Unit,
        callUs: () -> Unit,
        openWhatsapp: () -> Unit,
    ) {
        val dialog = Dialog(context)
        val binding = DialogContactThroughNumberBinding.inflate(inflater, null, false)

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.gravity = Gravity.BOTTOM
        layoutParams.verticalMargin = 0.019f // 10% margin from bottom

        dialog.window?.attributes = layoutParams

        dialog.apply {
            setContentView(binding.root)
            setCancelable(true)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            binding.btnCopy.setOnClickListener {
                copy.invoke()
                dismiss()
            }
            binding.btnCallUs.setOnClickListener {
                callUs.invoke()
                dismiss()
            }
            binding.btnOpenWhatsapp.setOnClickListener {
                openWhatsapp.invoke()
                dismiss()
            }
            show()
        }
    }

    fun dialogContactViaEmail(
        context: Context,
        inflater: LayoutInflater,
        copy: () -> Unit,
        sendEmail: () -> Unit,
    ) {
        val dialog = Dialog(context)
        val binding = DialogContactEmailBinding.inflate(inflater, null, false)

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.gravity = Gravity.BOTTOM
        layoutParams.verticalMargin = 0.019f // 10% margin from bottom

        dialog.window?.attributes = layoutParams

        dialog.apply {
            setContentView(binding.root)
            setCancelable(true)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            binding.btnCopy.setOnClickListener {
                copy.invoke()
                dismiss()
            }
            binding.btnSendEmail.setOnClickListener {
                sendEmail.invoke()
                dismiss()
            }
            show()
        }
    }


    inline fun permissionAlertDialog(context: Context,message:String,crossinline callback:() -> Unit) {
        val builder = AlertDialog.Builder(context)
        builder.apply{
            setMessage(message)
            setPositiveButton("Yes") { dialog, _ ->
                callback.invoke()
                dialog.dismiss()
            }
            setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }
            setCancelable(false)
            create()
            show()
        }
    }

    inline fun showDatePickerDialog(
        context: Context,
        crossinline onDateSelected: (date: Date) -> Unit
    ) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context, { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                val date = selectedDate.time
                onDateSelected.invoke(date)
            }, year, month, day
        )
        datePickerDialog.show()
    }


}