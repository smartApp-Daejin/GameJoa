package com.daejin.gamejoa.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.daejin.gamejoa.R
import com.daejin.gamejoa.databinding.ActivityJoinBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class JoinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJoinBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_join)

        auth = Firebase.auth

        onClick(binding.joinBtn)
        onClick(binding.backBtn)

    }

    private fun onClick(v: View) {
        when(v.id) {
            R.id.joinBtn -> binding.joinBtn.setOnClickListener {
                var isGoToJoin = true

                val email = binding.emailArea.text.toString()
                val password1 = binding.passwordArea1.text.toString()
                val password2 = binding.passwordArea2.text.toString()

                // 이메일을 입력하지 않았을 경우 실행
                if(email.isEmpty()) {
                    Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    isGoToJoin = false
                } else if(password1.isEmpty() || password1.length < 6){ // 비밀번호를 입력하지 않거나 비밀번호 길이가 6자리 미만일 경우 실행
                    Toast.makeText(this, "비밀번호는 6자리 이상 입력해주세요.", Toast.LENGTH_SHORT).show()
                    isGoToJoin = false
                } else if(password2.isEmpty()){ // 비밀번호 재확인을 입력하지 않았을 경우 실행
                    Toast.makeText(this, "비밀번호 재확인을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    isGoToJoin = false
                } else if(password1 != password2) { // 비밀번호가 동일하지 않았을 경우 실행
                    Toast.makeText(this, "비밀번호를 동일하게 입력해주세요.", Toast.LENGTH_SHORT).show()
                    isGoToJoin = false
                }

                // 이메일, 비밀번호를 조건에 맞게 입력했을 경우 실행되는 코드
                if(isGoToJoin) {
                    auth.createUserWithEmailAndPassword(email, password1)
                        .addOnCompleteListener(this) { task ->
                            // 이메일이 데이터베이스에 존재하지 않고 비밀번호와 비밀번호 재확인이 일치하는 경우 실행
                            if (task.isSuccessful) {
                                Toast.makeText(this, "회원가입에 성공하셨습니다.\n로그인 후 이용해주세요.", Toast.LENGTH_SHORT).show()

                                val intent = Intent(this, LoginActivity::class.java)
                                // 회원가입이 끝난 후 뒤로 가기 버튼 클릭 시 회원가입 화면으로 돌아가지 않게 하는 코드
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)

                            } else { // 이메일이 이미 존재하거나 기타 형식에 맞게 작성되지 않았을 경우 실행
                                Toast.makeText(this, "이메일을 확인해주세요.", Toast.LENGTH_SHORT).show()

                            }

                        }
                }

            }
            R.id.backBtn -> binding.backBtn.setOnClickListener {
                startActivity(Intent(this, IntroActivity::class.java))
            }
        }
    }
}
