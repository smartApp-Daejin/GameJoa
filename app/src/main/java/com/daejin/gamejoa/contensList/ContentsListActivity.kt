package com.daejin.gamejoa.contensList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.daejin.gamejoa.R
import com.daejin.gamejoa.utils.FBAuth
import com.daejin.gamejoa.utils.FBRef

class ContentsListActivity : AppCompatActivity() {

    private lateinit var myRef : DatabaseReference
    private lateinit var rvAdapter: ContentsRVAdapter

    val bookmarkIdList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contents_list)

        val items = ArrayList<ContentModel>()
        val itemKeyList = ArrayList<String>()

        rvAdapter = ContentsRVAdapter(baseContext, items, itemKeyList, bookmarkIdList)

        val database = Firebase.database

        // 어떤 카테고리를 선택했는지 받아옴
        val category = intent.getStringExtra("category")
        if(category == "category1") {
             myRef = database.getReference("contents")
        }
        else if(category == "category2") {
             myRef = database.getReference("contents2")
        }

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for(dataModel in dataSnapshot.children){
                    Log.d("ContentsListActivity", dataModel.toString())
                    Log.d("ContentsListActivity", dataModel.key.toString())

                    val item = dataModel.getValue(ContentModel::class.java)
                    items.add(item!!)
                    itemKeyList.add(dataModel.key.toString())
                }

                // 동기화
                rvAdapter.notifyDataSetChanged()
                Log.d("ContentsListActivity", items.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentsListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        myRef.addValueEventListener(postListener)

        val rv : RecyclerView = findViewById(R.id.recyclerView)

        rv.adapter = rvAdapter
        rv.layoutManager = GridLayoutManager(this, 2)

        getBookmarkData()
    }

    private fun getBookmarkData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                bookmarkIdList.clear()

                for(dataModel in dataSnapshot.children){
                    bookmarkIdList.add(dataModel.key.toString())
                }
                Log.d("Bookmark : ", bookmarkIdList.toString())
                rvAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentsListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.bookmarkRef.child(FBAuth.getUid()).addValueEventListener(postListener)
    }

}