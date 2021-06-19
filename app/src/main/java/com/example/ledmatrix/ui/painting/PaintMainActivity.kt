package com.example.ledmatrix.ui.painting

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ledmatrix.R
import com.example.ledmatrix.ui.home.CommutorActivity
import com.example.ledmatrix.utils.Utils.toast
import com.google.android.material.slider.RangeSlider
import kotlinx.android.synthetic.main.activity_paint.*
import java.io.OutputStream


lateinit var paint: DrawView
lateinit var rangeSlider: RangeSlider


 class PaintMainActivity : AppCompatActivity(), CommunicatorColor {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paint)
        //getting the reference of the views from their ids
        paint = findViewById<View>(R.id.draw_view_paint) as DrawView

        //creating a OnClickListener for each button, to perform certain actions
        //the undo button will remove the most recent stroke from the canvas
        btn_paint_earse.setOnClickListener {
            paint.undo()
        }

        btn_paint_back.setOnClickListener{
            val intent = Intent(this, CommutorActivity::class.java)
            startActivity(intent)
        }
        btnClear.setOnClickListener {
            paint.clear()
        }
        /*----------------------------------------------------
         SAVE
        in form of PNG, in the storage
        the save button will save the current canvas which is actually a bitmap
        in form of PNG, in the storage*/
        btn_paint_save.setOnClickListener {
            //getting the bitmap from DrawView class
            val bmp = paint.save()
            //opening a OutputStream to write into the file
            var imageOutStream: OutputStream? = null
            val cv = ContentValues()
            //name of the file
            cv.put(MediaStore.Images.Media.DISPLAY_NAME, "drawing.png")
            //type of the file
            cv.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            //location of the file to be saved
            cv.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)

            //ge the Uri of the file which is to be v=created in the storage
            val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv)
            try {
                //open the output stream with the above uri
                imageOutStream = contentResolver.openOutputStream(uri!!)
                //this method writes the files in storage
                bmp!!.compress(Bitmap.CompressFormat.PNG, 100, imageOutStream)
                //close the output stream after use
                imageOutStream!!.close()
                toast("Saved",this)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

//-----------------------------------------------------
//        COLOR
        btn_paint_color.setOnClickListener {
            var dialog = CustomDialogFragment()
            dialog.show(supportFragmentManager, "CustomDialog")
        }

//---------------------------------------------------------------------------------------
        // the button will toggle the visibility of the RangeBar/RangeSlider
        btn_paint_stroke.setOnClickListener {
            if (rangeSlider.visibility == View.VISIBLE) rangeSlider.visibility = View.GONE
            else rangeSlider.visibility = View.VISIBLE
        }
        //set the range of the RangeSlider
        rangeSlider.valueFrom = 0.0f
        rangeSlider.valueTo = 100.0f
        //adding a OnChangeListener which will change the stroke width
        //as soon as the user slides the slider
        //adding a OnChangeListener which will change the stroke width
        //as soon as the user slides the slider
        rangeSlider.addOnChangeListener(
                RangeSlider.OnChangeListener { slider, value, fromUser ->
                    paint.setStrokeWidth(value.toInt())
                })

        //pass the height and width of the custom view to the init method of the DrawView object
        val vto = paint.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                paint.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val width = paint.measuredWidth
                val height = paint.measuredHeight
                paint.init(height, width)
            }
        })
    }

    override fun passData(editTextInput: String) {
        val bundle = Bundle()
        bundle.putString("message", editTextInput)
        val transaction = this.supportFragmentManager.beginTransaction()
    }



}


