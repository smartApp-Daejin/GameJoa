package com.daejin.gamejoa.util

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBRef {

    companion object {
        private val database = Firebase.database

        // 카테고리 데이터베이스 필드
        val category1 = database.getReference("contents")
        val category2 = database.getReference("contents2")
        val category3 = database.getReference("contents3")
        val category4 = database.getReference("contents4")
        val category5 = database.getReference("contents5")
        val category6 = database.getReference("contents6")
        val category7 = database.getReference("contents7")
        val category8 = database.getReference("contents8")

        // 북마크 데이터베이스 필드
        val bookmarkRef = database.getReference("bookmark_list")

        // 게시판 데이터베이스 필드
        val boardRef = database.getReference("board")

        // 댓글 데이터베이스 필드
        val commentRef = database.getReference("comment")



    }
}