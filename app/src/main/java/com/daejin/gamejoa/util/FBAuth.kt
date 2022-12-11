package com.daejin.gamejoa.util

import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class FBAuth {

    companion object {
        private lateinit var auth : FirebaseAuth

        // 로그인한 사용자 uid 값을 얻는 메서드
        fun getUid(): String {
            auth = FirebaseAuth.getInstance()

            return auth.currentUser?.uid.toString()
        }

        // 현재 시간을 얻는 method
        fun getTime() : String {
            val currentDateTime = Calendar.getInstance().time // 현재 시간을 가져와서 currentDateTime 에 할당
            // 현재시간을 년.월.요일 시:분:초 형태로 바꿈
            val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN).format(currentDateTime)

            return dateFormat
        }
    }
}