package com.daejin.gamejoa.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.daejin.gamejoa.R
import com.daejin.gamejoa.databinding.ActivityBoardEditBinding
import com.daejin.gamejoa.databinding.ActivityBoardInsideBinding
import com.daejin.gamejoa.utils.FBAuth
import com.daejin.gamejoa.utils.FBRef
import java.lang.Exception

class BoardEditActivity : AppCompatActivity() {

    private lateinit var key: String
    private lateinit var binding: ActivityBoardEditBinding
    private val TAG = BoardEditActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_edit)
        key = intent.getStringExtra("key").toString()

        getImageData(key)
        getBoardData(key)

        binding.editBtn.setOnClickListener {
            editBoardData(key)
        }
    }

    private fun editBoardData(key: String) {
        FBRef.boardRef
            .child(key)
            .setValue(BoardModel(
                binding.titleArea.text.toString(),
                binding.contentArea.text.toString(),
                FBAuth.getUid(),
                FBAuth.getTime()))
        Toast.makeText(this, "수정 완료", Toast.LENGTH_LONG).show()
        finish()
    }

    private fun getImageData(key: String) {
        val storageReference = Firebase.storage.reference.child(key + ".png")

        val imageViewFromFB = binding.imageArea

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)
            }
        })
    }

    private fun getBoardData(key: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                binding.titleArea.setText(dataModel?.title)
                binding.contentArea.setText(dataModel?.content)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }
}