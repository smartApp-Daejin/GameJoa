package com.daejin.gamejoa.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.daejin.gamejoa.R
import com.daejin.gamejoa.databinding.ActivityBoardEditBinding
import com.daejin.gamejoa.model.BoardModel
import com.daejin.gamejoa.util.FBAuth
import com.daejin.gamejoa.util.FBRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.lang.Exception

class BoardEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardEditBinding
    private lateinit var key : String
    private val TAG = BoardEditActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_board_edit)

        key = intent.getStringExtra("key").toString()

        getBoardData(key)
        getImageData(key)
        binding.editBtn.setOnClickListener {
            editBoardData(key)
        }
    }

    // 수정할 게시글의 데이터(title, content, time)을 받아오는 메서드
    private fun getBoardData(key: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                binding.titleArea.setText(dataModel?.title)
                binding.contentArea.setText(dataModel?.title)
                binding.timeArea.text = dataModel?.time

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }

        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }

    // 수정할 게시글의 이미지를 받아오는 메서드
    private fun getImageData(key: String) {

        // 스토리지에 "$key.png" 값으로 이미지를 가져오겠다는 의미
        val storageReference = Firebase.storage.reference.child("$key.png")

        val imageViewFromFB = binding.imageArea

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener{ task ->
            if(task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)
            } else {

            }
        })
    }

    private fun editBoardData(key: String) {

        FBRef.boardRef
            .child(key)
            .setValue(
                BoardModel(binding.titleArea.text.toString(),
                    binding.contentArea.text.toString(),
                    FBAuth.getUid(),
                    FBAuth.getTime()))

        Toast.makeText(this@BoardEditActivity, "수정 완료", Toast.LENGTH_SHORT).show()
        finish()
    }
}