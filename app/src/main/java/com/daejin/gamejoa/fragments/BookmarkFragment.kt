package com.daejin.gamejoa.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daejin.gamejoa.R
import com.daejin.gamejoa.adapter.BookmarkRVAdapter
import com.daejin.gamejoa.contentsList.ContentModel
import com.daejin.gamejoa.databinding.FragmentBookmarkBinding
import com.daejin.gamejoa.util.FBAuth
import com.daejin.gamejoa.util.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class BookmarkFragment : Fragment() {

    private lateinit var binding: FragmentBookmarkBinding

    private val TAG = BookmarkFragment::class.java.simpleName

    private lateinit var rvAdapter: BookmarkRVAdapter // bookmark Adapter 생성

    private val bookmarkIdList = mutableListOf<String>()
    private val items = ArrayList<ContentModel>()
    private val itemKeyList = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bookmark, container, false)

        // 북마크 탭에 북마크한 데이터만 보여주는 방법 순서

        // 1. 전체 카테고리에 있는 컨텐츠 데이터를 다 가져옴
        // getBookmarkData() method 안에서 실행

        // 2. 사용자가 북마크한 정보를 다 가져옴
        getBookmarkData()


        rvAdapter = BookmarkRVAdapter(items, requireContext(),  itemKeyList, bookmarkIdList)

        val rv : RecyclerView = binding.bookmarkRV

        rv.adapter = rvAdapter

        rv.layoutManager = GridLayoutManager(requireContext(), 2)




        onClick(binding.homeTap)
        onClick(binding.tipTap)
        onClick(binding.talkTap)
        onClick(binding.storeTap)


        return binding.root
    }

    private fun onClick(v: View) {
        when(v.id) {
            R.id.homeTap -> binding.homeTap.setOnClickListener { it.findNavController().navigate(R.id.action_bookmarkFragment_to_homeFragment) }
            R.id.tipTap-> binding.tipTap.setOnClickListener{ it.findNavController().navigate(R.id.action_bookmarkFragment_to_tipFragment) }
            R.id.talkTap -> binding.talkTap.setOnClickListener { it.findNavController().navigate(R.id.action_bookmarkFragment_to_talkFragment) }
            R.id.storeTap -> binding.storeTap.setOnClickListener { it.findNavController().navigate(R.id.action_bookmarkFragment_to_storeFragment) }

        }
    }



    // 1. 전체 카테고리에 있는 컨텐츠 데이터를 다 가져옴
    private fun getCategoryData() {

        // 데이터베이스에서 전체 카테고리에 해당하는 데이터를 가져오는 코드
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataModel in dataSnapshot.children) {
                    // Log.d(TAG, dataModel.toString())

                    val item = dataModel.getValue(ContentModel::class.java)

                    // 3. 전체 컨테츠 중에서, 사용자가 북마크한 정보만 보여줌
                    // 카테고리에 키값이 bookmarkIdList 에 포함되어 있다면 item 들을 추가
                    if(bookmarkIdList.contains(dataModel.key.toString())){
                        items.add(item!!) // 카테고리에 데이터(항목)들을 items 에 추가
                        itemKeyList.add(dataModel.key.toString()) // 각 카테고리에 해당하는 키 값을 itemKeyList 에 추가
                    }
                }
                rvAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        FBRef.category1.addValueEventListener(postListener)
        FBRef.category2.addValueEventListener(postListener)
        FBRef.category3.addValueEventListener(postListener)
        FBRef.category4.addValueEventListener(postListener)
        FBRef.category5.addValueEventListener(postListener)
        FBRef.category6.addValueEventListener(postListener)
        FBRef.category7.addValueEventListener(postListener)
        FBRef.category8.addValueEventListener(postListener)

    }

    private fun getBookmarkData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (dataModel in dataSnapshot.children) {
                    // Log.e(TAG, dataModel.toString())
                    bookmarkIdList.add(dataModel.key.toString()) // 북마크 데이터에 각 데이터마다 해당하는 키 값을 bookmarkIdList 에 추가

                }
                // 1. 전체 카테고리에 있는 컨텐츠 데이터를 다 가져옴
                getCategoryData()
            }


            override fun onCancelled(databaseError: DatabaseError) {

            }
        }

        FBRef.bookmarkRef.child(FBAuth.getUid()).addValueEventListener(postListener)

    }


}