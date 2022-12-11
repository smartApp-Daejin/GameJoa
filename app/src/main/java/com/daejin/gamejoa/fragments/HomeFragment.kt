package com.daejin.gamejoa.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daejin.gamejoa.R
import com.daejin.gamejoa.adapter.BookmarkRVAdapter
import com.daejin.gamejoa.auth.IntroActivity
import com.daejin.gamejoa.contentsList.ContentModel
import com.daejin.gamejoa.databinding.FragmentHomeBinding
import com.daejin.gamejoa.util.FBRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val TAG = HomeFragment::class.java.simpleName

    private lateinit var auth : FirebaseAuth

    private lateinit var rvAdapter : BookmarkRVAdapter

    private val bookmarkIdList = mutableListOf<String>()
    private val items = ArrayList<ContentModel>()
    private val itemKeyList = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        auth = Firebase.auth


        onClick(binding.tipTap)
        onClick(binding.talkTap)
        onClick(binding.bookmarkTap)
        onClick(binding.storeTap)

        getCategoryData()
        rvAdapter = BookmarkRVAdapter(items, requireContext(),  itemKeyList, bookmarkIdList)
        val rv: RecyclerView = binding.mainRV

        rv.adapter = rvAdapter

        rv.layoutManager = GridLayoutManager(requireContext(),2)


        return binding.root
    }

    private fun onClick(v: View) {
        when(v.id) {
            R.id.tipTap-> binding.tipTap.setOnClickListener{ it.findNavController().navigate(R.id.action_homeFragment_to_tipFragment) }
            R.id.talkTap -> binding.talkTap.setOnClickListener { it.findNavController().navigate(R.id.action_homeFragment_to_talkFragment) }
            R.id.bookmarkTap -> binding.bookmarkTap.setOnClickListener { it.findNavController().navigate(R.id.action_homeFragment_to_bookmarkFragment) }
            R.id.storeTap -> binding.storeTap.setOnClickListener { it.findNavController().navigate(R.id.action_homeFragment_to_storeFragment) }

        }
    }

    //  전체 카테고리에 있는 컨텐츠 데이터를 다 가져옴
    private fun getCategoryData() {

        // 데이터베이스에서 전체 카테고리에 해당하는 데이터를 가져오는 코드
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataModel in dataSnapshot.children) {
                    val item = dataModel.getValue(ContentModel::class.java)
                    items.add(item!!)
                    itemKeyList.add(dataModel.key.toString())

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
}