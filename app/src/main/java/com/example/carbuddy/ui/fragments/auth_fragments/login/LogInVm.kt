package com.example.carbuddy.ui.fragments.auth_fragments.login

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.carbuddy.data.valueclasses.Email
import com.example.carbuddy.data.valueclasses.EmailError
import com.example.carbuddy.data.valueclasses.Password
import com.example.carbuddy.data.valueclasses.PasswordError


class LogInVm : ViewModel() {
    val validationResult = MutableLiveData<Pair<EmailError, PasswordError>>()

    fun validateInput(email: Email, password: Password) {
        validationResult.value = Pair(
            EmailError(validateEmail(email.value)),
            PasswordError(validatePassword(password.value))
        )
    }

    private fun validateEmail(email: String): String? {
        return when {
            email.isEmpty() -> "Email cannot be empty"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Invalid email"
            else -> null
        }
    }

    private fun validatePassword(password: String): String? {
        return when {
            password.isEmpty() -> "Password cannot be empty"
            password.length < 7 -> "Password must be at least 7 characters long"
            else -> null
        }
    }
}