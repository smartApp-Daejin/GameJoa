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
import com.daejin.gamejoa.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        auth = Firebase.auth

        onClick(binding.loginBtn)
        onClick(binding.backBtn)

    }

    private fun onClick(v: View) {
        when(v.id) {
            R.id.loginBtn -> binding.loginBtn.setOnClickListener {

                var isGoToLogin = true
                val email = binding.emailArea.text.toString()
                val password = binding.passwordArea.text.toString()

                // 이메일을 입력하지 않을 경우 실행
                if(email.isEmpty()) {
                    Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    isGoToLogin = false

                } else if(password.isEmpty()){ // 비밀번호를 입력하지 않을 경우 실행
                    Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    isGoToLogin = false
                }

                // 이메일, 비밀번호를 둘 다 입력하고 호출되는 코드
                if(isGoToLogin) {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            // 이메일, 비밀번호가 데이터베이스에 존재하면 실행
                            if (task.isSuccessful) {
                                Toast.makeText(this, "${auth.currentUser?.email}님 환영합니다.", Toast.LENGTH_SHORT).show()

                                val intent = Intent(this, MainActivity::class.java)
                                // 로그인이 성공한 후 뒤로 가기 버튼 클릭 시 로그인 화면으로 돌아가지 않게 하는 코드
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)

                            } else { // 존재하지 않을 경우 실행
                                Toast.makeText(this, "이메일 또는 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()

                            }
                        }
                }
            }
            R.id.backBtn -> binding.backBtn.setOnClickListener {
                startActivity(Intent(this@LoginActivity, IntroActivity::class.java))
            }
        }
    }
}