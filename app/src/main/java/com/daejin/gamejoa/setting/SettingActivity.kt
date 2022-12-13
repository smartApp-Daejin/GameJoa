package com.daejin.gamejoa.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.daejin.gamejoa.R
import com.daejin.gamejoa.auth.IntroActivity
import com.daejin.gamejoa.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySettingBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting)

        auth = Firebase.auth

        binding.logoutBtn.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "로그아웃되었습니다", Toast.LENGTH_LONG).show()
            val intent = Intent(this, IntroActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}