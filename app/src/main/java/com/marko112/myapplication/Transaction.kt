package com.marko112.myapplication

import java.sql.Timestamp
import java.time.LocalDate
import java.util.Date

data class Transaction(
    var id: String? = "",
    var date: com.google.firebase.Timestamp? = null,
    //var date: com.google.firebase.Timestamp? = null,
    var description: String? = null,
    var amount: Double? = null,
    var payerEmail: String? = null,
    var recipientEmail: String? = null
)
