package com.daejin.gamejoa.utils

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBRef {
    companion object {
        private val database = Firebase.database

        val boardRef = database.getReference("board")
        val commentRef = database.getReference("comment")
    }
}