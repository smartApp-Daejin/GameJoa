package com.daejin.gamejoa.board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.daejin.gamejoa.R
import com.daejin.gamejoa.databinding.ActivityBoardWriteBinding
import com.daejin.gamejoa.utils.FBAuth
import com.daejin.gamejoa.utils.FBRef
import java.io.ByteArrayOutputStream

class BoardWriteActivity : AppCompatActivity() {

    private val TAG = BoardWriteActivity::class.java.simpleName
    private var isImageUpload = false
    private lateinit var binding: ActivityBoardWriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_write)

        binding.writebtn.setOnClickListener {
            val title = binding.titleArea.text.toString()
            val content = binding.contentArea.text.toString()
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()

            Log.d(TAG, title)
            Log.d(TAG, content)

            if (!title.isEmpty()) {
                if (!content.isEmpty()) {

                    val key = FBRef.boardRef.push().key.toString()

                    FBRef.boardRef
                        .child(key)
                        .setValue(BoardModel(title, content, uid, time))
                    Toast.makeText(this, "게시글 업로드 완료", Toast.LENGTH_LONG).show()
                    if (isImageUpload) {
                        imageUpload(key)
                    }
                    finish()
                } else {
                    Toast.makeText(this, "내용을 입력해주세요", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "제목을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        binding.imageArea.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
            isImageUpload = true
        }
    }

    private fun imageUpload(key: String) {
        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child(key + ".png")

        val imageView = binding.imageArea
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {

        }.addOnSuccessListener { taskSnapshot ->

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            binding.imageArea.setImageURI(data?.data)
        }
    }
}