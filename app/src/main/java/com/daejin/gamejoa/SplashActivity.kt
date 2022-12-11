package com.daejin.gamejoa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.daejin.gamejoa.auth.IntroActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        auth = Firebase.auth


        // 현재 로그인한 유저의 "uid" 값을 가져옴
        // 만약 가져온 현재 uid 가 null 일 경우 로그인이 되지 않았기 때문에 인트로 화면으로 이동
        if(auth.currentUser?.uid == null) {
            Log.d("SplashActivity", "null")
            // 3초 후에 IntroActivity 로 이동

            Handler().postDelayed({
                startActivity(Intent(this, IntroActivity::class.java))
                finish()
            }, 3000)

        } else { // uid 가 null 이 아닐 경우는 로그인이 된 사용자이기 때문에 메인화면으로 이동
            Log.d("SplashActivity", "not null")
            // 3초 후에 IntroActivity 로 이동
            Handler().postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, 3000)

        }




    }
}