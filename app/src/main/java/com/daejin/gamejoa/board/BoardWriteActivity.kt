package com.daejin.gamejoa.board

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.daejin.gamejoa.R
import com.daejin.gamejoa.databinding.ActivityBoardWriteBinding
import com.daejin.gamejoa.model.BoardModel
import com.daejin.gamejoa.util.FBAuth
import com.daejin.gamejoa.util.FBRef
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class BoardWriteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBoardWriteBinding
    private val TAG = BoardWriteActivity::class.java.simpleName

    private var isImageUpload = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_write)


        onClick(binding.writeBtn)
        onClick(binding.imageArea)

    }

    private fun onClick(v: View) {
        when(v.id) {
            // 게시글 작성 완료 버튼 클릭 시 실행되는 코드
            R.id.writeBtn -> binding.writeBtn.setOnClickListener {
                val title = binding.titleArea.text.toString() // 입력한 글 제목
                val content = binding.contentArea.text.toString() // 입력한 글 내용
                val uid = FBAuth.getUid() // 글을 작성한 사용자 uid
                val time = FBAuth.getTime() // 글을 작성한 시간

                /*
                파이어베이스 스토리지에 이미지를 저장해야 하는데
                만약에 내가 게시글을 클릭했을 때, 게시글에 대한 정보를 받아와야 하는데
                이미지 이름에 대한 정보를 모르기 때문에
                이미지 이름을 문서의 key 값으로 해줘서 이미지에 대한 정보를 찾기 쉽게 해놓음
                * */
                // 이미지를 스토리지에 저장 시 해당 게시글에 key 값으로 저장하기 위해사 데이터베이스를 생성하기 전에 미리 key 값을 받아옴
                val key = FBRef.boardRef.push().key.toString()

                /* board => FBRef.boardRef
                    - key(자동으로 추가되는 고유한 값) => push()
                        - boardModel(title, content, uid, time)
                * */
                // 데이터베이스에 데이터 추가 코드
                FBRef.boardRef
                    .child(key)
                    .setValue(BoardModel(title, content, uid, time)) // 제목, 내용, 작성자 uid, 작성 시간

                Toast.makeText(this, "게시글이 작성되었습니다.", Toast.LENGTH_SHORT).show()

                if(isImageUpload) {
                    // 키값을 인수로 넣겨줌
                    imageUpload(key)
                }

                finish()
            }

            // 이미지 업로드 버튼 클릭 시 실행되는 코드
            R.id.imageArea -> binding.imageArea.setOnClickListener {
                // 갤러리 이동 1-2 버튼 클릭 시 갤러리(사진첩)으로 이동하는 코드
                val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(gallery, 100)
                isImageUpload = true
            }
        }
    }

    // 갤러리 이동 1-1
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 갤러리 이동 1-3
        if(resultCode == RESULT_OK && requestCode == 100) {

            // 선택한 사진을 imageArea 에 저장하는 코드
            binding.imageArea.setImageURI(data?.data)
        }
    }

    // 이미지 업로드 메서드
    private fun imageUpload(key: String) {
        val storage = Firebase.storage

        // 경로 설정
        val storageRef = storage.reference
        // 문서의 키값으로 사진을 저장함
        val imageRef = storageRef.child("$key.png")
        
        // 이미지 업로드
        val imageView = binding.imageArea
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = imageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }


}