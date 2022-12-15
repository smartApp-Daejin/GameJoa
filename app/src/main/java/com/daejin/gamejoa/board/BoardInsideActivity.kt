package com.daejin.gamejoa.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.daejin.gamejoa.R
import com.daejin.gamejoa.comment.CommentListViewAdapter
import com.daejin.gamejoa.comment.CommentModel
import com.daejin.gamejoa.databinding.ActivityBoardInsideBinding
import com.daejin.gamejoa.utils.FBAuth
import com.daejin.gamejoa.utils.FBRef
import java.lang.Exception

class BoardInsideActivity : AppCompatActivity() {
    private val TAG = BoardInsideActivity::class.java.simpleName
    private lateinit var binding: ActivityBoardInsideBinding
    private lateinit var key: String
    private val commentDataList = mutableListOf<CommentModel>()
    private lateinit var commentAdapter : CommentListViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)

        binding.boardSettingBtn.setOnClickListener {
            showDialog()
        }

        key = intent.getStringExtra("key").toString()

        commentAdapter = CommentListViewAdapter(commentDataList)
        binding.commentListView.adapter = commentAdapter

        getBoardData(key)
        getImageData(key)
        getCommentData(key)

        binding.commentBtn.setOnClickListener {
            insertComment(key)
        }
    }



    private fun insertComment(key: String) {
        FBRef.commentRef
            .child(key)
            .push()
            .setValue(CommentModel(binding.commentArea.text.toString(), FBAuth.getTime()))
        Toast.makeText(this, "댓글 작성 완료", Toast.LENGTH_LONG).show()
        binding.commentArea.setText("")
    }

    private fun showDialog() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("게시글 수정/삭제")

        val alertDialog = mBuilder.show()
        alertDialog.findViewById<Button>(R.id.editBtn)?.setOnClickListener {
            val intent = Intent(this, BoardEditActivity::class.java)
            intent.putExtra("key", key)
            startActivity(intent)
            alertDialog.dismiss()
        }

        alertDialog.findViewById<Button>(R.id.removeBtn)?.setOnClickListener {
            deleteContent()
            Toast.makeText(this, "삭제 완료", Toast.LENGTH_LONG).show()
            finish()
            alertDialog.dismiss()
        }
    }

    private fun deleteContent() {
        FBRef.boardRef.child(key).removeValue()
        val storage = Firebase.storage
        val storageRef = storage.reference
        val desertRef = storageRef.child(key + ".png")
        desertRef.delete()
    }

    private fun getImageData(key: String) {
        val storageReference = Firebase.storage.reference.child(key + ".png")

        val imageViewFromFB = binding.imageArea

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)
            } else {
                binding.imageArea.isVisible = false
            }
        })
    }

    private fun getBoardData(key: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                    binding.titleArea.text = dataModel?.title
                    binding.contentArea.text = dataModel?.content
                    binding.timeArea.text = dataModel?.time

                    val myUid = FBAuth.getUid()
                    val writerUid = dataModel!!.uid

                    if(myUid.equals(writerUid)) {
                        binding.boardSettingBtn.isVisible = true
                    }

                } catch (e: Exception) {
                    Log.w(TAG, "삭제")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }

    private fun getCommentData(key : String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                commentDataList.clear()
                for(dataModel in dataSnapshot.children){
                    val item = dataModel.getValue(CommentModel::class.java)
                    commentDataList.add(item!!)
                }
                commentAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.commentRef.child(key).addValueEventListener(postListener)
    }
}