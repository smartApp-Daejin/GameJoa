package com.daejin.gamejoa.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.daejin.gamejoa.R
import com.daejin.gamejoa.adapter.BoardListLVAdapter
import com.daejin.gamejoa.board.BoardInsideActivity
import com.daejin.gamejoa.board.BoardWriteActivity
import com.daejin.gamejoa.databinding.FragmentTalkBinding
import com.daejin.gamejoa.model.BoardModel
import com.daejin.gamejoa.util.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class TalkFragment : Fragment() {
    private lateinit var binding: FragmentTalkBinding
    private val TAG = TalkFragment::class.java.simpleName
    private val boardDataList = mutableListOf<BoardModel>() //getFBBoardData method 에 dataModel 을 담을 리스트

    private lateinit var boardRVAdapter : BoardListLVAdapter

    private val boardKeyList = mutableListOf<String>() // 방법 2-1 board 에 키 값을 담을 리스트 변수 생성

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_talk, container, false)

        onClick(binding.homeTap)
        onClick(binding.writeBtn)

        boardRVAdapter = BoardListLVAdapter(boardDataList) // 어댑터 정의
        binding.boardListView.adapter = boardRVAdapter // 어댑터를 boardListView 와 연결

        // 게시글 클릭 시 BoardModel(title, content, time)을 다른 액티비티로 보내는 방법
        binding.boardListView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(requireContext(), BoardInsideActivity::class.java)
            // 방법 2. 파이어베이스에 있는 board(field)에 대한 데이터의 id를 기반으로 다시 데이터를 받아오는 방법
            intent.putExtra("key",boardKeyList[position]) // 방법 2-3 intent 로 boardKeyList 를 보내줌
            startActivity(intent)
        }


        // 방법 2. 파이어베이스에 있는 board 에 대한 데이터의 id 를 기반으로 다시 데이터를 받아오는 방법

        getFBBoardData()

        return binding.root
    }

    private fun onClick(v: View) {
        when(v.id) {
            R.id.homeTap -> binding.homeTap.setOnClickListener { it.findNavController().navigate(R.id.action_talkFragment_to_homeFragment) }
            R.id.writeBtn -> binding.writeBtn.setOnClickListener { startActivity(Intent(requireContext(), BoardWriteActivity::class.java)) }
        }
    }

    private fun getFBBoardData() {

        // 데이터베이스에서 게시글 데이터를 가져오는 코드
         val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                boardDataList.clear()
                for (dataModel in dataSnapshot.children) {
                    val item = dataModel.getValue(BoardModel::class.java) // 데이터를 boardModel 형태로 출력
                    boardDataList.add(item!!) // boardDataList 에 추가
                    boardKeyList.add(dataModel.key.toString()) // 방법 2-2. 키 값을 boardKeyList 에 추가

                }
                boardDataList.reverse()
                boardKeyList.reverse()
                boardRVAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        FBRef.boardRef.addValueEventListener(postListener)

    }

}