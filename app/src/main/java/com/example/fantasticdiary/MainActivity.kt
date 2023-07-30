package com.example.fantasticdiary

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val characterNames = arrayListOf<String>()
    val characterStories = arrayListOf<String>()
    val idAddresses = arrayListOf<Int>()
    val blobList = arrayListOf<ByteArray>()
    private lateinit var adapter: diaryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*
        val pictureByteArray = R.drawable.geralt
        val diaryDatabase =this.openOrCreateDatabase("diary", Context.MODE_PRIVATE,null)
        diaryDatabase.execSQL("CREATE TABLE IF NOT EXISTS characters (id INTEGER PRIMARY KEY, name TEXT, pic BLOB)")

        val cursor =diaryDatabase.rawQuery("SELECT * FROM diary",null)
        diaryDatabase.execSQL("INSERT INTO diary(name,pıc) VALUES ('geralt',pictureByteArray) ")
        val idColumnIndex = cursor.getColumnIndex("id")
        val idNameColumnIndex = cursor.getColumnIndex("name")
        val idPictureColumnIndex =cursor.getColumnIndex("pic")

         */

        addCharacter.setOnClickListener {
            addCharacterAction()
        }

        getData()

        val diaryRecyclerView =findViewById<RecyclerView>(R.id.diary)
        val adapter = diaryAdapter(characterNames,characterStories,blobList)


        val layoutManager = GridLayoutManager(this,2)
        diaryRecyclerView.adapter=adapter
        diaryRecyclerView.layoutManager=layoutManager



    }


    fun getData(){
        try{
            val diaryDatabase = this.openOrCreateDatabase("diary" ,  Context.MODE_PRIVATE,null)
            val cursor = diaryDatabase.rawQuery("SELECT * FROM characters",null)
            val heroNameIndex = cursor.getColumnIndex("name")
            val heroStoryIndex = cursor.getColumnIndex("story")
            val idIndex = cursor.getColumnIndex("id")
            val picIndex = cursor.getColumnIndex("pic")

            while (cursor.moveToNext()){
                characterNames.add(cursor.getString(heroNameIndex))
                characterStories.add(cursor.getString(heroStoryIndex))
                blobList.add(cursor.getBlob(picIndex))
            }
            adapter.notifyDataSetChanged()
            cursor.close()

        }catch (e:Exception){

        }

    }


    private fun addCharacterAction(){
    val intent = Intent(this,characterAdder::class.java)
        startActivity(intent)
    }
}