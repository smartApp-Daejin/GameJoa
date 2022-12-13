package com.daejin.gamejoa.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.daejin.gamejoa.MainActivity
import com.daejin.gamejoa.R
import com.daejin.gamejoa.databinding.ActivityJoinBinding

class JoinActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityJoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_join)
        auth = Firebase.auth

        binding.joinBtn.setOnClickListener {

            var flag = true //flag
            val email = binding.emailArea.text.toString()
            val password1 = binding.passwordArea1.text.toString()
            val password2 = binding.passwordArea2.text.toString()

            //이메일 입력 체크
            if (email.isEmpty()) {
                Toast.makeText(this, "email을 입력해주세요", Toast.LENGTH_LONG).show()
                flag = false
            } else {
                //비밀번호 입력 체크
                if (password1.isEmpty()) {
                    Toast.makeText(this, "password를 입력해주세요", Toast.LENGTH_LONG).show()
                    flag = false
                }
                else {
                    //비밀번호 확인 입력 체크
                    if (password2.isEmpty()) {
                        Toast.makeText(this, "password check를 입력해주세요", Toast.LENGTH_LONG).show()
                        flag = false
                    }
                    else {
                        //비밀번호 일치 체크
                        if (!password1.equals(password2)) {
                            Toast.makeText(this, "password가 일치하지 않습니다", Toast.LENGTH_LONG).show()
                            flag = false
                        }
                        else {
                            //비밀번호 길이 체크
                            if (password1.length < 6) {
                                Toast.makeText(this, "password를 6자리 이상으로 입력해주세요", Toast.LENGTH_LONG).show()
                                flag = false
                            }
                        }
                    }
                }
            }

            if(flag) {
                auth.createUserWithEmailAndPassword(email, password1)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()
                            //main 액티비티로 이동
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //기존 액티비티 모두 종
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}