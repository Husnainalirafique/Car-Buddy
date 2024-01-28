package com.example.carbuddy.utils

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
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

    fun permissionAlertDialog(context: Context,message:String,callback:() -> Unit) {
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
    fun showDatePickerDialog(context: Context,onDateSelected:(date:Date) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                val date = selectedDate.time
                onDateSelected.invoke(date)
            }, year, month, day
        )
        datePickerDialog.show()
    }

}