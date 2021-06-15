package com.example.ledmatrix

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.ledmatrix.databinding.ActivityPaintBinding
import com.example.ledmatrix.ui.painting.CommunicatorColor
import com.example.ledmatrix.ui.painting.CustomDialogFragment
import com.example.ledmatrix.utils.toast
import com.google.android.material.slider.RangeSlider
import kotlinx.android.synthetic.main.activity_paint.*
import java.io.OutputStream

//import kotlinx.android.synthetic.main.fragment_material_color_picker.*
//import kotlinx.android.synthetic.main.colorpicker.*


//lateinit var paint: DrawView
//lateinit var save: ImageButton
//lateinit var color: ImageButton
//lateinit var stroke: ImageButton
//lateinit var undo: ImageButton
//lateinit var rangeSlider: RangeSlider
private lateinit var binding: ActivityPaintBinding

class PaintMainActivity : AppCompatActivity(), CommunicatorColor {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_paint)

        //creating a OnClickListener for each button, to perform certain actions

        //the undo button will remove the most recent stroke from the canvas
        btn_paint_erase.setOnClickListener {
            draw_view_paint.undo()
        }
        //----------------------------------------------------
        //SAVE
        //in form of PNG, in the storage


        //the save button will save the current canvas which is actually a bitmap
        //in form of PNG, in the storage
        btn_paint_save.setOnClickListener {

            checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_PERMISSION_CODE)
        }

        //-----------------------------------------------------
        //COLOR


        btn_paint_color.setOnClickListener {
            var dialog = CustomDialogFragment()
            dialog.show(supportFragmentManager, "CustomDialog")
        }


        //---------------------------------------------------------------------------------------
        // the button will toggle the visibility of the RangeBar/RangeSlider
        btn_paint_stroke.setOnClickListener {
            if (rangebar_paint_stroke.visibility == View.VISIBLE) rangebar_paint_stroke.visibility =
                View.GONE
            else rangebar_paint_stroke.visibility = View.VISIBLE
        }


        //set the range of the RangeSlider
        rangebar_paint_stroke.valueFrom = 0.0f
        rangebar_paint_stroke.valueTo = 100.0f
        //adding a OnChangeListener which will change the stroke width
        //as soon as the user slides the slider
        //adding a OnChangeListener which will change the stroke width
        //as soon as the user slides the slider
        rangebar_paint_stroke.addOnChangeListener(
            RangeSlider.OnChangeListener { slider, value, fromUser ->
                draw_view_paint.setStrokeWidth(value.toInt())
            })

        //pass the height and width of the custom view to the init method of the DrawView object
        val vto = draw_view_paint.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                draw_view_paint.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val width = draw_view_paint.measuredWidth
                val height = draw_view_paint.measuredHeight
                draw_view_paint.init(height, width)
            }
        })
    }

    private fun saveImage() {
        //getting the bitmap from DrawView class
        val bmp = draw_view_paint.save()
        //opening a OutputStream to write into the file
        var imageOutStream: OutputStream? = null
        val cv = ContentValues()
        //name of the file
        cv.put(MediaStore.Images.Media.DISPLAY_NAME, "drawing.png")
        //type of the file
        cv.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        //location of the file to be saved
        cv.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)

        //get the Uri of the file which is to be v=created in the storage
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv)
        //open the output stream with the above uri
        imageOutStream = contentResolver.openOutputStream(uri!!)
        Log.e(TAG, "imageOutStream = " + imageOutStream)
        Log.e(TAG, "uri = " + uri)
        //this method writes the files in storage
        bmp!!.compress(Bitmap.CompressFormat.PNG, 100, imageOutStream)
        //close the output stream after use
        imageOutStream!!.close()
    }

    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        } else {
            saveImage()
            toast("Permission already granted")
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == WRITE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                toast("Storage Permission Granted")
                saveImage()
            } else {
                toast("Storage Permission Denied")
            }
        }
    }

    override fun passData(editTextInput: String) {
        val bundle = Bundle()
        bundle.putString("message", editTextInput)

        val transaction = this.supportFragmentManager.beginTransaction()


    }

    companion object{
        const val TAG = "PaintMainActivity"

        @RequiresApi(Build.VERSION_CODES.M)

        private const val WRITE_PERMISSION_CODE = 101
    }

}


