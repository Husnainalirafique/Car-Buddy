package com.example.carbuddy.data

@JvmInline
value class Email(val value:String)

@JvmInline
value class Password(val value:String)

@JvmInline
value class EmailError(val value: String?)

@JvmInline
value class PasswordError(val value: String?)
