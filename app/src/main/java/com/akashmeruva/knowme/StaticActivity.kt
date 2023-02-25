package com.akashmeruva.knowme

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Display
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import com.akashmeruva.knowme.databinding.FragmentCameraBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.FirebaseDatabase
import org.tensorflow.lite.task.vision.classifier.Classifications

class StaticActivity : AppCompatActivity()  , ImageClassifierHelper.ClassifierListener{

    lateinit var  image_camera : ImageView
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    val db = FirebaseDatabase.getInstance()
    val refrence = db.reference.child("Models")
    lateinit var descreption : TextView
    var model_name = " "
    lateinit var name_project : TextView
    lateinit var Name_Object : TextView
    lateinit var Accuracy : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_static)

         image_camera = findViewById<ImageView>(R.id.pic_image_view)
        name_project = findViewById<TextView>(R.id.name_tv1)
        descreption = findViewById<TextView>(R.id.descreption_tv2)
        Name_Object = findViewById<TextView>(R.id.Name_Object)
        Accuracy = findViewById<TextView>(R.id.Accuracy)

        ImagePicker.with(this)
            .crop()
            .cameraOnly()
            .crop(1080F, 2120F)
            .compress(1024)			//Final image size will be less than 1 MB(Optional)
            .maxResultSize(1080, 1920)	//Final image resolution will be less than 1080 x 1080(Optional)
            .start()

        val google_btn = findViewById<ImageView>(R.id.google_img)
        val github_btn = findViewById<ImageView>(R.id.github_img)

        google_btn.setOnClickListener {

            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com/search?q=$model_name"))
            startActivity(browserIntent)
        }

        github_btn.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/search?q=$model_name"))
            startActivity(browserIntent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {

            if (data != null) {
               image_camera.setImageURI(data.dataString?.toUri())
                imageClassifierHelper = ImageClassifierHelper(context = applicationContext, imageClassifierListener = this)
                imageClassifierHelper.classify(image_camera.drawable.toBitmap() , getScreenOrientation())
            }
        }
    }
    private fun getScreenOrientation() : Int {
        val outMetrics = DisplayMetrics()



        return display?.rotation ?: 0
    }

    override fun onError(error: String) {
        TODO("Not yet implemented")
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResults(results: List<Classifications>?, inferenceTime: Long) {

            try {
                val str = results?.get(0)!!.categories.toString().substringAfter("index").subSequence(1, 2).toString()
                val str1 = results?.get(0)!!.categories.toString().substringAfter("0.").subSequence(0 , 2)
                if(str != "]") {
                    refrence.child(str).child("name").get().addOnSuccessListener {
                        model_name = it.value.toString()
                        name_project.text = it.value.toString()
                        Name_Object.text = it.value.toString()
                        refrence.child(str).child("descreption").get().addOnSuccessListener {
                            descreption.text = it.value.toString()
                        }
                    }
                    Accuracy.text  = "$str1%"
                }
            }catch(E:Exception){}
    }

}