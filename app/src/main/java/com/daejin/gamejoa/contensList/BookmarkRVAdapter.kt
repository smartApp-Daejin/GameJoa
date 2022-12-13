package com.daejin.gamejoa.contensList

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.daejin.gamejoa.R
import com.daejin.gamejoa.utils.FBAuth
import com.daejin.gamejoa.utils.FBRef

class BookmarkRVAdapter (val context : Context,
                         val items : ArrayList<ContentModel>,
                         val itemKeyList : ArrayList<String>,
                         val bookmarkIdList : MutableList<String>)
    : RecyclerView.Adapter<BookmarkRVAdapter.Viewholder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookmarkRVAdapter.Viewholder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.contents_rv_item, parent, false)

        Log.d("BookmarkRVAdapter", itemKeyList.toString())
        Log.d("BookmarkRVAdapter", bookmarkIdList.toString())
        return Viewholder(v)
    }

    override fun onBindViewHolder(holder: BookmarkRVAdapter.Viewholder, position: Int) {


        holder.bindItems(items[position], itemKeyList[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(item: ContentModel, key: String) {

            itemView.setOnClickListener {
                val intent = Intent(context, ContentShowActivity::class.java)
                intent.putExtra("url", item.webUrl)
                itemView.context.startActivity(intent)
            }

            val contentTitle = itemView.findViewById<TextView>(R.id.textArea)
            contentTitle.text = item.title

            val imageViewArea = itemView.findViewById<ImageView>(R.id.imageArea)

            val bookmarkArea = itemView.findViewById<ImageView>(R.id.bookmarkArea)

            if (bookmarkIdList.contains(key)) {
                bookmarkArea.setImageResource(R.drawable.bookmark_color)
            } else {
                bookmarkArea.setImageResource(R.drawable.bookmark_white)
            }

            Glide.with(context)
                .load(item.imageUrl)
                .into(imageViewArea)
        }
    }
}