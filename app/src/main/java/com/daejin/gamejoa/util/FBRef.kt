package com.daejin.gamejoa.util

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBRef {

    companion object {
        private val database = Firebase.database

        // 게시판 데이터베이스 필드
        val boardRef = database.getReference("board")

        // 댓글 데이터베이스 필드
        val commentRef = database.getReference("comment")

    }
}