package com.daejin.gamejoa.contentsList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daejin.gamejoa.R
import com.daejin.gamejoa.adapter.ContentRVAdapter
import com.daejin.gamejoa.util.FBAuth
import com.daejin.gamejoa.util.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ContentListActivity : AppCompatActivity() {

    private lateinit var myRef : DatabaseReference  // 데이터 참조 변수 생성
    
    private val bookmarkIdList = mutableListOf<String>() // 북마크한 아이템에 아이디를 넣을 리스트 생성

    private lateinit var rvAdapter : ContentRVAdapter // 어댑터 생성

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_list)


        val items = ArrayList<ContentModel>() // 데이터를 저장할 리스트 변수

        val itemKeyList = ArrayList<String>() // 데이터의 키 값을 저장할 리스트 변수

        rvAdapter = ContentRVAdapter(items, baseContext, itemKeyList, bookmarkIdList) // 어댑터로 해당 데이터 보냄

        val database = Firebase.database // 데이터베이스 정의

         // category 에 따라 myRef Reference 참조 변경
        // val getCategory = intent.getStringExtra("category") // tipFragment 에서 받아온 category 를 저장할 변수 생성
        when(intent.getStringExtra("category")) {
            "category1" -> myRef = FBRef.category1
            "category2" -> myRef = FBRef.category2
            "category3" -> myRef = FBRef.category3
            "category4" -> myRef = FBRef.category4
            "category5" -> myRef = FBRef.category5
            "category6" -> myRef = FBRef.category6
            "category7" -> myRef = FBRef.category7
            "category8" -> myRef = FBRef.category8
        }

        // 데이터베이스에서 데이터 읽어오는 코드 
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataModel in dataSnapshot.children) {
                    // Log.d("ContentListActivity", dataModel.toString())
                    // Log.d("ContentListActivity", dataModel.key.toString()) // 데이터베이스의 항목마다 키 값을 가져옴
                    val item = dataModel.getValue(ContentModel::class.java) // dataModel 에 있는 데이터를 ContentModel 형식으로 받는 다는 의미
                    items.add(item!!) // 데이터베이스에 있는 데이터를 items(데이터를 저장할 리스트 변수)에 추가

                    itemKeyList.add(dataModel.key.toString()) // 데이터 항목마다 키 값을 리스트에 추가


                }
                rvAdapter.notifyDataSetChanged() // 리사이클러 뷰 전체 업데이트
                 // Log.d("ContentListActivity", items.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        myRef.addValueEventListener(postListener)


        // recyclerView 변수 선언
        val rv : RecyclerView = findViewById(R.id.rv)

        // RecyclerView 에 어댑터 연결
        rv.adapter = rvAdapter

        // layout 을 어떤식으로 보여즐지 결정?
        // 한줄에 2개 씩 보여줌
        // LinearLayoutManager => 기본은 vertical
        rv.layoutManager = GridLayoutManager(this, 2)

        // 리사이클러 뷰 아이템 클릭 방법 1
        /*
        // 리사이클러뷰의 아이템 중 클릭되면 발생하는 코드
        rvAdapter.itemClick = object : ContentRVAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                Toast.makeText(baseContext, items[position].title, Toast.LENGTH_LONG).show()
                val intent = Intent(this@ContentListActivity, ContentShowActivity::class.java)
                // 클릭된 아이템의 webUrl 을 ContentShowActivity 로 데이터를 보냄
                intent.putExtra("url",items[position].webUrl)
                startActivity(intent)
            }

        }
        */

        getBookmarkData()

    }

    // bookmark ref 데이터 가져오는 코드
    private fun getBookmarkData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // bookmarkIdList 에 데이터가 이중으로 추가되는 문제 해결
                // bookmarkIdList 를 불러오기 전에 한번 클리어 시킨다. -> 다시 한번 불러오면 다시 클리어 시키고 데이터 불러옴
                bookmarkIdList.clear()

                for (dataModel in dataSnapshot.children) {
                    // Log.d("getBookmarkData", dataModel.toString())
                    // Log.d("getBookmarkData", dataModel.key.toString())
                    bookmarkIdList.add(dataModel.key.toString())

                }
                rvAdapter.notifyDataSetChanged()
                // Log.d("getBookmarkData", bookmarkIdList.toString()) // 북마크리스트에 아이템의 키 값이 추가됬는지 확인 하는 로그캣

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        // bookmarkRef 안에 FBAuth.getUid 안에 값을 key 와 value 형태로 가져오기 위한 코드
        // key 는 bookmark 를 체크한 item value 는 bookmarkModel 에 boolean 값
        FBRef.bookmarkRef.child(FBAuth.getUid()).addValueEventListener(postListener)

    }
}

// 샘플 데이터
/* items.add(ContentModel("밥솥 리코타치즈 황금레시피", "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FblYPPY%2Fbtq66v0S4wu%2FRmuhpkXUO4FOcrlOmVG4G1%2Fimg.png", "https://philosopher-chan.tistory.com/1235?category=941578"))
   items.add(ContentModel("황금노른자장 황금레시피", "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FznKK4%2Fbtq665AUWem%2FRUawPn5Wwb4cQ8BetEwN40%2Fimg.png","https://philosopher-chan.tistory.com/1236?category=941578"))
   items.add(ContentModel("사골곰탕 파스타 황금레시피", "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fbtig9C%2Fbtq65UGxyWI%2FPRBIGUKJ4rjMkI7KTGrxtK%2Fimg.png","https://philosopher-chan.tistory.com/1237?category=941578"))
   items.add(ContentModel("아웃백 투움바 파스타 황금레시피", "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FcOYyBM%2Fbtq67Or43WW%2F17lZ3tKajnNwGPSCLtfnE1%2Fimg.png","https://philosopher-chan.tistory.com/1238?category=941578"))
*/

// 데이터베이스에 데이터 추가
/*
    myRef.push().setValue(
        ContentModel("밥솥 리코타치즈 황금레시피", "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FblYPPY%2Fbtq66v0S4wu%2FRmuhpkXUO4FOcrlOmVG4G1%2Fimg.png", "https://philosopher-chan.tistory.com/1235?category=941578")
    )
*/