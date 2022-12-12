package com.daejin.gamejoa.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.daejin.gamejoa.R
import com.daejin.gamejoa.adapter.CommentLVAdapter
import com.daejin.gamejoa.comment.CommentModel
import com.daejin.gamejoa.databinding.ActivityBoardInsideBinding
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

class BoardInsideActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBoardInsideBinding
    private val TAG = BoardInsideActivity::class.java.simpleName
    private lateinit var key : String
    private val commentDataList = mutableListOf<CommentModel>()  // 댓글 데이터를 담을 리스트 선언
    private lateinit var commentLVAdapter: CommentLVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)

        binding.boardSettingIcon.setOnClickListener {
            showDialog()

        }


        // 방법 2-4 보내준 데이터를 받음
        key = intent.getStringExtra("key").toString()
//        Toast.makeText(this, key, Toast.LENGTH_SHORT).show()
        getBoardData(key)
        getImageData(key)

        // 댓글 입력 버튼 클릭 이벤트
        binding.commentBtn.setOnClickListener {
            insertComment(key)
        }

        // 댓글 어댑터와 연결
        commentLVAdapter = CommentLVAdapter(commentDataList)
        binding.commentLV.adapter = commentLVAdapter

        getCommentData(key)

    }

    // 방법 2-5 받은 데이터를 토대로 데이터를 가져옴
    private fun getBoardData(key: String) {
        // 데이터베이스에서 게시글 데이터를 가져오는 코드
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                try {
                    // 방법 2-7 BoardModel 형태로 데이터를 가져옴
                    // 데이터 하나만 가져오면 되기 때문에 for 문 실행 X
                    val dataModel = dataSnapshot.getValue(BoardModel::class.java) // BoardModel 형태로 데이터를 받는 다는 의미

                    // 방법 2-8 데이터를 해당 view 에 할당
                    binding.titleArea.text = dataModel!!.title
                    binding.contentArea.text = dataModel.content
                    binding.timeArea.text = dataModel.time

                    val myUid = FBAuth.getUid() // 로그인한 사용자 uid
                    val writerUid = dataModel.uid // 게시글 작성자 uid

                    // 게시글 작성자 uid 와 로그인한 사용자 uid 가 같으면 
                    if(myUid == writerUid) {
                        binding.boardSettingIcon.isVisible = true // settingIcon 을 보여줌
                    }

                }catch (e: Exception) {
                    Log.d(TAG, "삭제 완료")

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        // 방법 2-6 board filed 에 key 값을 가리키게 만듬
        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }

    // 이미지를 다운로드 메서드
    private fun getImageData(key: String) {
        
        // 스토리지에 "$key.png" 값으로 이미지를 가져오겠다는 의미
        val storageReference = Firebase.storage.reference.child("$key.png")

        val imageViewFromFB = binding.getImageArea

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener{ task ->
            if(task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)
            } else {
                binding.getImageArea.isVisible = false

            }
        })
    }

    // 수정, 삭제를 할 수 있는 다이얼로그를 띄워주는 메서드
    private fun showDialog() {
        // 레이아웃을 연결
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)

        // 다이얼로그 띄우기
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("게시글 수정 / 삭제")

        val alertDialog = mBuilder.show()
        
        // 수정 버튼 클릭 시 동작하는 코드
        alertDialog.findViewById<Button>(R.id.editBtn)?.setOnClickListener {
            var intent = Intent(this, BoardEditActivity::class.java)
            intent.putExtra("key",key) // 수정할 게시글의 내용을 보여줄 수 있게 intent 로 key 값을 넘겨줌
            startActivity(intent)
        }

        // 삭제 버튼 클릭 시 동작하는 코드
        alertDialog.findViewById<Button>(R.id.removeBtn)?.setOnClickListener {
            FBRef.boardRef
                .child(key)
                .removeValue()
            Toast.makeText(this, "게시글이 삭제되었습니다.",Toast.LENGTH_SHORT).show()
            finish() // 현재 게시글 보기 activity 를 종료하는 기능 -> 게시글이 삭제되면 그 게시글 페이지는 사라져야 하기 때문
        }
    }

    // 댓글 입력 메서드
    // 댓글 파이어베이스 field 구조
    // comment                   FBRef.commentRef
    //  - BoardKey               .child(key)
    //      - commentKey(자동생성) .push()
    //          - commentData    .setValue()
    private fun insertComment(key: String) {
        FBRef.commentRef
            .child(key)
            .push()
            .setValue(CommentModel(binding.commentArea.text.toString(), FBAuth.getTime()))

        Toast.makeText(this, "댓글이 등록되었습니다.", Toast.LENGTH_SHORT).show()
        binding.commentArea.setText("")
    }

    // 댓글 데이터 가져오기
    private fun getCommentData(key: String) {
        // 데이터베이스에서 댓글 데이터를 가져오는 코드
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                commentDataList.clear()
                for (dataModel in dataSnapshot.children) {
                    val item = dataModel.getValue(CommentModel::class.java) // 데이터를 boardModel 형태로 출력
                    commentDataList.add(item!!)

                }
                commentDataList.reverse()
                commentLVAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        FBRef.commentRef.child(key).addValueEventListener(postListener)

    }
}