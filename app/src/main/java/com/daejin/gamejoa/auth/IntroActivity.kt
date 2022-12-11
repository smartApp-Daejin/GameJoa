package com.daejin.gamejoa.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.daejin.gamejoa.MainActivity
import com.daejin.gamejoa.R
import com.daejin.gamejoa.databinding.ActivityIntroBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class IntroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro)

        auth = Firebase.auth

        onClick(binding.loginBtn)
        onClick(binding.joinBtn)
        onClick(binding.noAccountBtn)
    }

    private fun onClick(v: View) {
        when(v.id) {
            // 로그인 화면으로 이동하는 코드 
            R.id.loginBtn -> binding.loginBtn.setOnClickListener {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            // 회원가입 화면으로 이동하는 코드 
            R.id.joinBtn -> binding.joinBtn.setOnClickListener {
                startActivity(Intent(this, JoinActivity::class.java))
            }
            // 비회원 로그인 시 실행되는 코드 
            R.id.noAccountBtn -> binding.noAccountBtn.setOnClickListener {
                auth.signInAnonymously()
                    .addOnCompleteListener(this) { task ->
                        // 비회원 로그인 성공 
                        if (task.isSuccessful) {
                            Toast.makeText(this, "비회원으로 로그인되었습니다.", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this, MainActivity::class.java)
                            // 로그인이 성공한 후 뒤로 가기 버튼 클릭 시 로그인 화면으로 돌아가지 않게 하는 코드
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)

                            // 비회원 로그인 실패
                        } else {
                            Toast.makeText(this, "비회원으로 로그인 실패하였습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}