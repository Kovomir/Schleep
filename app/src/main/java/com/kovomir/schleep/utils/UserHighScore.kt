package com.kovomir.schleep.utils

import com.google.firebase.firestore.DocumentId

data class UserHighScore(
    @DocumentId
    val userId: String = "",
    val userName: String = "",
    val score: Int = 0,
    val highestScore: Int = 0
)