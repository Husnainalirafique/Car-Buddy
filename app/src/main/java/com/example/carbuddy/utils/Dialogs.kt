package com.example.carbuddy.utils

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.LocalDate
import java.util.Calendar
import java.util.Date

object Dialogs {

//    fun showProfileDoneDialog(
//        context: Context,
//        inflater: LayoutInflater,
//        setProfileImgCallback:() -> Unit
//    ) {
//        val dialog = Dialog(context)
//        val binding = LayoutCustomDialogProfileDoneBinding.inflate(inflater, null, false)
//        dialog.apply {
//            setContentView(binding.root)
//            setCancelable(true)
//            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            binding.profileDoneDialogImageView.setOnClickListener {
//                setProfileImgCallback.invoke()
//            }
//            show()
//        }
//    }

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

    fun showMaterialDatePickerDialog(
        fragmentManager: FragmentManager,
        onDateSelected: (String) -> Unit
    ) {

        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .build()

        picker.addOnPositiveButtonClickListener {
            onDateSelected(picker.headerText)
        }

        picker.show(fragmentManager, "DatePicker")
    }


}