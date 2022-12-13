package com.daejin.gamejoa.contensList

import android.content.Context
import android.content.Intent
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.daejin.gamejoa.R
import com.daejin.gamejoa.utils.FBAuth
import com.daejin.gamejoa.utils.FBRef

class  ContentsRVAdapter (val context : Context,
                         val items : ArrayList<ContentModel>,
                         val itemKeyList : ArrayList<String>,
                         val bookmarkIdList : MutableList<String>)
    : RecyclerView.Adapter<ContentsRVAdapter.Viewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentsRVAdapter.Viewholder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.contents_rv_item, parent, false)

        Log.d("ContentRVAdapter", itemKeyList.toString())
        Log.d("ContentRVAdapter", bookmarkIdList.toString())
        return Viewholder(v)
    }

    override fun onBindViewHolder(holder: ContentsRVAdapter.Viewholder, position: Int) {


        holder.bindItems(items[position], itemKeyList[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(item : ContentModel, key : String) {

            itemView.setOnClickListener{
                val intent = Intent(context, ContentShowActivity::class.java)
                intent.putExtra("url", item.webUrl)
                itemView.context.startActivity(intent)
            }

            val contentTitle = itemView.findViewById<TextView>(R.id.textArea)
            contentTitle.text = item.title

            val imageViewArea = itemView.findViewById<ImageView>(R.id.imageArea)

            val bookmarkArea = itemView.findViewById<ImageView>(R.id.bookmarkArea)

            if(bookmarkIdList.contains(key)) {
                bookmarkArea.setImageResource(R.drawable.bookmark_color)
            } else {
                bookmarkArea.setImageResource(R.drawable.bookmark_white)
            }

            bookmarkArea.setOnClickListener {
//                Toast.makeText(context, key, Toast.LENGTH_SHORT).show()

                if(bookmarkIdList.contains(key)) { // 북마크가 있을 때
                    FBRef.bookmarkRef
                        .child(FBAuth.getUid())
                        .child(key)
                        .removeValue()
                }
                else { // 북마크가 없을 떄
                FBRef.bookmarkRef
                    .child(FBAuth.getUid())
                    .child(key)
                    .setValue(BookmarkModel(true))
                }
            }


            Glide.with(context)
                .load(item.imageUrl)
                .into(imageViewArea)
        }
    }
}