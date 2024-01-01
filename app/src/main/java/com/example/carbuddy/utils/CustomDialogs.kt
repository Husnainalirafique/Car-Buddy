package com.example.carbuddy.utils

import android.app.AlertDialog
import android.content.Context

object CustomDialogs {

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

}