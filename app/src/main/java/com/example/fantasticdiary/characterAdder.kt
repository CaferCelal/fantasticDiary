package com.example.fantasticdiary

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_character_adder.*
import kotlinx.android.synthetic.main.diary_view.*
import kotlinx.android.synthetic.main.permission_denied_alert.view.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class characterAdder : AppCompatActivity() {

    var chosenPic : Uri? =null
    var chosenBitmap :Bitmap? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_adder)
        addPic.setOnClickListener{
            askPermission()
        }
        saveBtn.setOnClickListener {
            save()
        }
    }






    private fun save(){
        val heroName = getCharacterName.text.toString()
        val heroStory =story.text.toString()
        val heroPic=reSizeBitmap(chosenBitmap!!,200)

        val outputStream = ByteArrayOutputStream()
        heroPic.compress(Bitmap.CompressFormat.PNG,50,outputStream)

        val heroByteArray = outputStream.toByteArray()

        try {
            val diaryDatabase = this.openOrCreateDatabase("diary" ,  Context.MODE_PRIVATE,null)
            diaryDatabase.execSQL("CREATE TABLE IF NOT EXISTS characters (id INTEGER PRIMARY KEY, name VARCHAR ,story VARCHAR, pic BLOB)")

            val sqlString = "INSERT INTO characters (name ,story,pic) VALUES(?,?,?)"
            val statement =diaryDatabase.compileStatement(sqlString)
            statement.bindString(1,heroName)
            statement.bindString(2,heroStory)
            statement.bindBlob(3,heroByteArray)
            statement.execute()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }catch (e:Exception){
            println("Saving error")
        }
    }


    private fun askPermission(){
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA),1)

        }
        else{
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

            val chooserIntent = Intent.createChooser(galleryIntent, "Select Image")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))

            startActivityForResult(chooserIntent, 2)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode==1){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

                val chooserIntent = Intent.createChooser(galleryIntent, "Select Image")
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))

                startActivityForResult(chooserIntent, 2)
            }
            else{
                val builder = AlertDialog.Builder(this)
                val inflater = LayoutInflater.from(this)
                val view = inflater.inflate(R.layout.permission_denied_alert, null)
                builder.setView(view)
                val alert = builder.create()
                alert.show()
                view.ok.setOnClickListener {
                    alert.dismiss()
                }

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    private fun saveFullSizedCameraImage(bitmap: Bitmap): Uri? {
        val imageFile = File(externalCacheDir, "temp_image.jpg")
        try {
            val outputStream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 500, outputStream)
            outputStream.flush()
            outputStream.close()
            return Uri.fromFile(imageFile)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (data.data != null) {
                    // Photo selected from the gallery
                    chosenPic = data.data
                    try {
                        chosenBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, chosenPic)
                        addPic.setImageBitmap(chosenBitmap)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    // Photo captured from the camera
                    val thumbnailBitmap = data.extras?.get("data") as? Bitmap
                    if (thumbnailBitmap != null) {
                        chosenBitmap = thumbnailBitmap
                        addPic.setImageBitmap(chosenBitmap)

                        // Save the camera image to a file
                        chosenPic = saveFullSizedCameraImage(thumbnailBitmap)
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    fun reSizeBitmap(innerBitmap: Bitmap,maxSize:Int) :Bitmap{
        var width = innerBitmap.width
        var height =innerBitmap.height

        val ratio :Float = width.toFloat() /height.toFloat()

        if(ratio>1){
            //yatay
            width= maxSize
            height=(maxSize/ratio).toInt()
        }
        else{
            //dikey
            height=maxSize
            width=(height*ratio).toInt()
        }

        return Bitmap.createScaledBitmap(innerBitmap,width,height,true)
    }

}


