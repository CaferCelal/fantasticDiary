package com.example.fantasticdiary


import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase.openOrCreateDatabase
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.diary_view.view.*

class diaryAdapter (val characterNameList:ArrayList<String>,val characterStoryList:ArrayList<String>, val blobList:ArrayList<ByteArray>)
    :RecyclerView.Adapter<diaryAdapter.diaryViewHolder>() {
    class diaryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): diaryViewHolder {
        val View = LayoutInflater.from(parent.context).inflate(R.layout.diary_view, parent, false)
        return diaryViewHolder(View)
    }

    override fun onBindViewHolder(holder: diaryViewHolder, position: Int) {
        holder.itemView.characterName.setText(characterNameList.get(position))
        holder.itemView.characterImage.setImageBitmap(BitmapFactory.decodeByteArray(blobList.get(position),0,blobList.get(position).size))
        holder.itemView.characterImage.setOnClickListener {
            val intent = Intent(holder.itemView.context,characterDisplayer::class.java)
            intent.putExtra("name",characterNameList.get(position))
            intent.putExtra("story",characterStoryList.get(position))
            intent.putExtra("pic",blobList.get(position))
            startActivity(holder.itemView.context,intent,null)
        }
    }

    override fun getItemCount(): Int {
        return characterNameList.size
    }
}