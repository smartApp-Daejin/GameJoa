package com.daejin.gamejoa.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.daejin.gamejoa.R
import com.daejin.gamejoa.contentsList.ContentListActivity
import com.daejin.gamejoa.databinding.FragmentTipBinding


class TipFragment : Fragment() {

    private lateinit var binding: FragmentTipBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_tip, container, false)

        onClick(binding.homeTap)
        onClick(binding.talkTap)
        onClick(binding.bookmarkTap)
        onClick(binding.storeTap)
        onClick(binding.category1)
        onClick(binding.category2)
        onClick(binding.category3)
        onClick(binding.category4)
        onClick(binding.category5)
        onClick(binding.category6)
        onClick(binding.category7)
        onClick(binding.category8)

        return binding.root
    }


    private fun onClick(v: View) {
        val intent = Intent(context, ContentListActivity::class.java)
        when(v.id) {
            R.id.homeTap -> binding.homeTap.setOnClickListener{ it.findNavController().navigate(R.id.action_tipFragment_to_homeFragment) }
            R.id.talkTap -> binding.talkTap.setOnClickListener { it.findNavController().navigate(R.id.action_tipFragment_to_talkFragment) }
            R.id.bookmarkTap -> binding.bookmarkTap.setOnClickListener { it.findNavController().navigate(R.id.action_tipFragment_to_bookmarkFragment) }
            R.id.storeTap -> binding.storeTap.setOnClickListener { it.findNavController().navigate(R.id.action_tipFragment_to_storeFragment) }
            R.id.category1 -> binding.category1.setOnClickListener {
                intent.putExtra("category", "category1")
                startActivity(intent)
            }
            R.id.category2 -> binding.category2.setOnClickListener {
                intent.putExtra("category", "category2")
                startActivity(intent)
            }
            R.id.category3 -> binding.category3.setOnClickListener {
                intent.putExtra("category", "category3")
                startActivity(intent)
            }
            R.id.category4 -> binding.category4.setOnClickListener {
                intent.putExtra("category", "category4")
                startActivity(intent)
            }
            R.id.category5 -> binding.category5.setOnClickListener {
                intent.putExtra("category", "category5")
                startActivity(intent)
            }
            R.id.category6 -> binding.category6.setOnClickListener {
                intent.putExtra("category", "category6")
                startActivity(intent)
            }
            R.id.category7 -> binding.category7.setOnClickListener {
                intent.putExtra("category", "category7")
                startActivity(intent)
            }
            R.id.category8 -> binding.category8.setOnClickListener {
                intent.putExtra("category", "category8")
                startActivity(intent)
            }
        }
    }
}