package com.example.fantasticdiary

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_character_displayer.*

class characterDisplayer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_displayer)
        val intent = intent
        val name =intent.getStringExtra("name")
        val story =intent.getStringExtra("story")
        val blobpic =intent.getByteArrayExtra("pic")

        val bitmap =BitmapFactory.decodeByteArray(blobpic,0,blobpic!!.size)

        displayPic.setImageBitmap(bitmap)

        displayName.setText(name)
        displayStory.setText(story)
    }
}