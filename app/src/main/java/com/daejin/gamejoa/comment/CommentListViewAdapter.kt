package com.daejin.gamejoa.comment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.daejin.gamejoa.R

class CommentListViewAdapter (val commentList : MutableList<CommentModel>) : BaseAdapter() {
    override fun getCount(): Int {
        return commentList.size
    }

    override fun getItem(position: Int): Any {
        return commentList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if(view == null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.comment_list_item, parent, false)
        }

        val comment = view?.findViewById<TextView>(R.id.commentArea)
        comment!!.text = commentList[position].commentContent

        val time = view?.findViewById<TextView>(R.id.timeArea)
        time!!.text = commentList[position].commentCreatedTime

        return view!!
    }

}