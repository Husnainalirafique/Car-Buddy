package com.example.carbuddy.utils

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import com.example.carbuddy.R

@SuppressLint("ClickableViewAccessibility")
fun setPasswordVisibilityToggle(editText: EditText, ) { editText.setOnTouchListener { view, motionEvent ->
    val drawableEnd = editText.compoundDrawablesRelative[2]
    if (drawableEnd != null && motionEvent.action == MotionEvent.ACTION_UP) {
        val bounds = drawableEnd.bounds
        val x = motionEvent.x.toInt()

        if (x >= view.width - view.paddingRight - bounds.width()) {
            val cursorPosition = editText.selectionStart
            val isPasswordVisible =
                editText.transformationMethod == PasswordTransformationMethod.getInstance()

            editText.transformationMethod =
                if (isPasswordVisible) HideReturnsTransformationMethod.getInstance()
                else PasswordTransformationMethod.getInstance()

            editText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                if (isPasswordVisible) R.drawable.icon_password_visibility_off else R.drawable.icon_password_visibility_on,
                0
            )

            editText.setSelection(cursorPosition)
            return@setOnTouchListener true
        }
    }
    false
} }

//@SuppressLint("UseCompatLoadingForDrawables")
//fun setFocusChangeBackground(context: Context, editText: EditText) {
//    editText.setOnFocusChangeListener { _, hasFocus ->
//        if (hasFocus) {
//            editText.background = context.getDrawable(R.drawable.bg_editext_with_stroke)
//        } else {
//            editText.background = context.getDrawable(R.drawable.bg_edittext)
//        }
//    }
//}

fun editTextWatcherForChatButton(editText: EditText, sendBtn: View, voiceBtn: View){
    editText.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            val text = s?.toString() ?: ""

            if (text.isNotBlank()){
                sendBtn.visible()
                voiceBtn.invisible()
            }
            else {
                sendBtn.invisible()
                voiceBtn.visible()
            }
        }
    })
}

