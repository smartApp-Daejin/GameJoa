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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.daejin.gamejoa.R
import com.daejin.gamejoa.contensList.BookmarkRVAdapter
import com.daejin.gamejoa.contensList.ContentModel
import com.daejin.gamejoa.databinding.FragmentBookmarkBinding
import com.daejin.gamejoa.utils.FBAuth
import com.daejin.gamejoa.utils.FBRef

class BookmarkFragment : Fragment() {

    private lateinit var binding : FragmentBookmarkBinding

    private val TAG = BookmarkFragment::class.java.simpleName

    lateinit var rvAdapter: BookmarkRVAdapter

    val bookmarkIdList = mutableListOf<String>()
    val items = ArrayList<ContentModel>()
    val itemKeyList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bookmark, container, false)


        // 2. 사용자가 북마크한 정보들 가져옴
        getBookmarkData()

        // 3. 1의 전체 컨텐츠 데이터들 중 사용자가 북마크한 정보들만 가져
        rvAdapter = BookmarkRVAdapter(requireContext(), items, itemKeyList, bookmarkIdList)

        val rv : RecyclerView = binding.bookmarkRV
        rv.adapter = rvAdapter
        rv.layoutManager = GridLayoutManager(requireContext(), 2)

        binding.homeTab.setOnClickListener {
            it.findNavController().navigate(R.id.action_bookmarkFragment_to_homeFragment)
        }

        binding.tipTab.setOnClickListener {
            it.findNavController().navigate(R.id.action_bookmarkFragment_to_tipFragment)
        }

        binding.talkTab.setOnClickListener {
            it.findNavController().navigate(R.id.action_bookmarkFragment_to_talkFragment)
        }

        binding.storeTab.setOnClickListener {
            it.findNavController().navigate(R.id.action_bookmarkFragment_to_storeFragment)
        }

        return binding.root
    }

    private fun getCategoryData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(dataModel in dataSnapshot.children){
                    Log.d(TAG, dataModel.toString())

                    val item = dataModel.getValue(ContentModel::class.java)
                    // 전체 컨텐츠들 중 사용자가 북마크한 항목만 보여줌
                    if(bookmarkIdList.contains(dataModel.key.toString())) {
                        items.add(item!!)
                        itemKeyList.add(dataModel.key.toString())
                    }
                }
                rvAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentsListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.category1.addValueEventListener(postListener)
        FBRef.category2.addValueEventListener(postListener)
    }

    private fun getBookmarkData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(dataModel in dataSnapshot.children){
                    Log.e(TAG, dataModel.toString())
                    bookmarkIdList.add(dataModel.key.toString())
                }
                // 1. 전체 카테고리에 있는 컨텐츠 데이터들을 모두 가져옴
                getCategoryData()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentsListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.bookmarkRef.child(FBAuth.getUid()).addValueEventListener(postListener)
    }
}