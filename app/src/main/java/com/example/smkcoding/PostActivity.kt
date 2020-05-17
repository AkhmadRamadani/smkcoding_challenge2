package com.example.smkcoding

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.loader.content.CursorLoader
import com.example.smkcoding.data.DataServices
import com.example.smkcoding.data.apiRequests
import com.example.smkcoding.data.httpClient
import com.example.smkcoding.util.tampilToast
import kotlinx.android.synthetic.main.activity_post.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class PostActivity : AppCompatActivity() {

    var newText : String = ""
    var uri: Uri? = Uri.EMPTY
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        btnPickUpload.setOnClickListener {
            pickFromGallery()
        }
        btnUpload.setOnClickListener {
            post(uri!!)
        }
    }
    fun checkPermission(): Boolean {
        var result: Int? = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 100);
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            1001 -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1000){
            imgUpload.visibility = ImageView.VISIBLE
            imgUpload.setImageURI(data?.data)
            uri = data?.data!!
//            post(data?.data!!)
        }
    }

    fun pickFromGallery(){
        if (checkPermission()){
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1000)
        }
        else{
            requestPermission()
        }
    }

    fun getRealPath(contentUri: Uri): String? {
        val img = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(this, contentUri, img, null, null, null)
        val cursor: Cursor? = loader.loadInBackground()
        val columnIndex : Int? = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        val reesult: String? = cursor?.getString(columnIndex!!)
        cursor?.close()
        return reesult
    }

    fun post(contentUri: Uri?){
        val sharedPreference:SharedPreference=SharedPreference(this)


        val idUser = sharedPreference.getValueString(sharedPreference.idUSer)
        newText = text.text.toString()

        val text: RequestBody? = RequestBody.create(MediaType.parse("text/plain"), newText)
        val id_user: RequestBody? = RequestBody.create(MediaType.parse("text/plain"), idUser)
        val img: RequestBody? = RequestBody.create(null,"")


        var part: MultipartBody.Part? = MultipartBody.Part.createFormData("img","",img)
        Log.d("part1", part.toString())
        if (contentUri != Uri.EMPTY){
            val filePath = getRealPath(contentUri!!)
            val file: File? =  File(filePath)

            val img: RequestBody? = RequestBody.create(MediaType.parse("multipart/form-data"), file)

            part = MultipartBody.Part.createFormData("img", file!!.name, img)
            Log.d("part2", part.toString())
        }

        val httpClient = httpClient()
        val apiRequest = apiRequests<DataServices>(httpClient)
        val call = apiRequest.post(part!!,text!!, id_user!!)
        call.enqueue(object : Callback<PostResponse>{
            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                tampilToast(this@PostActivity,"Gagal Post")
                Log.d("errorupload", t.toString())
            }

            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                tampilToast(this@PostActivity, "Sukses Upload")
                Log.d("suksesuplaod", response.toString())
                imgUpload.setImageResource(0)
                imgUpload.setImageURI(Uri.EMPTY)
                val intent = Intent(this@PostActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }

        })
    }

}
