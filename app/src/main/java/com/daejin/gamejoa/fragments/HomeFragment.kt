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
import com.daejin.gamejoa.auth.IntroActivity
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


    private val bookmarkIdList = mutableListOf<String>()
    private val itemKeyList = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        auth = Firebase.auth

        onClick(binding.talkTap)

        val rv: RecyclerView = binding.mainRV

        rv.layoutManager = GridLayoutManager(requireContext(),2)

        return binding.root
    }

    private fun onClick(v: View) {
        when(v.id) {
            R.id.talkTap -> binding.talkTap.setOnClickListener { it.findNavController().navigate(R.id.action_homeFragment_to_talkFragment) }

        }
    }

}