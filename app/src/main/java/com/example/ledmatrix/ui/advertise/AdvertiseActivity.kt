package com.example.advertise

import android.animation.ObjectAnimator
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.graphics.scale
import androidx.databinding.DataBindingUtil
import com.example.ledmatrix.R
import com.example.ledmatrix.databinding.ActivityAdvertiseBinding
import com.example.ledmatrix.utils.iToast
import com.example.ledmatrix.utils.toast
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.dialog_advertise_history.*

private lateinit var binding: ActivityAdvertiseBinding
private lateinit var storageReference: StorageReference
private lateinit var auth: FirebaseAuth
private var numberOfPic: Int? = null // generate file name

class AdvertiseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_advertise)
        binding.ivImage.setImageResource(R.drawable.ic_image)
        storageReference = FirebaseStorage.getInstance().getReference()
        auth = Firebase.auth


        SetInformationHide(true)
        SetPreViewButtonHide(true)
        SetPushButtonHide(true)
        supportActionBar?.hide()
        binding.btnChoose.setOnClickListener {
            ImagePicker.with(this)
                .crop()	    			                //Crop image(Optional), Check Customization for more option
                .compress(1024)			        //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }

        binding.btnAdvertiseHistory.setOnClickListener {
            openHistoryDialog()
        }

    }

    private fun openHistoryDialog() {
        val view = View.inflate(this, R.layout.dialog_advertise_history, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view)

        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        Log.e("openHistoryDialog", "numberOfPic = " + numberOfPic)
        updateToDialog(numberOfPic!!)
//        btn_advertise_dialog_ok.setOnClickListener {
//            dialog.dismiss()
//        }

    }

    private fun pickImageFromGallary() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    private fun SetInformationHide(status: Boolean){
        if (status == true) {
            binding.cardViewInfo.setVisibility(View.INVISIBLE)
            binding.tvImgName.setVisibility(View.INVISIBLE)
            binding.tvImgSize.setVisibility(View.INVISIBLE)
            binding.tvImgFormat.setVisibility(View.INVISIBLE)
            binding.tvImgLocation.setVisibility(View.INVISIBLE)
            binding.tvImgTitle.setVisibility(View.INVISIBLE)
            binding.tvInfoName.setVisibility(View.INVISIBLE)
            binding.tvInfoSize.setVisibility(View.INVISIBLE)
            binding.tvInfoFormat.setVisibility(View.INVISIBLE)
            binding.tvInfoLocation.setVisibility(View.INVISIBLE)
            binding.ivPreview.setVisibility(View.INVISIBLE)
        } else {
            binding.cardViewInfo.setVisibility(View.VISIBLE)
            binding.tvImgName.setVisibility(View.VISIBLE)
            binding.tvImgSize.setVisibility(View.VISIBLE)
            binding.tvImgFormat.setVisibility(View.VISIBLE)
            binding.tvImgLocation.setVisibility(View.VISIBLE)
            binding.tvImgTitle.setVisibility(View.VISIBLE)
            binding.tvInfoName.setVisibility(View.VISIBLE)
            binding.tvInfoSize.setVisibility(View.VISIBLE)
            binding.tvInfoFormat.setVisibility(View.VISIBLE)
            binding.tvInfoLocation.setVisibility(View.VISIBLE)
            binding.ivPreview.setVisibility(View.VISIBLE)
        }

    }

    private fun SetPreViewButtonHide(status: Boolean){
        if (status == true){
            binding.btnPreview.setVisibility(View.INVISIBLE)
        } else {
            binding.btnPreview.setVisibility(View.VISIBLE)
        }
    }

    private fun SetPushButtonHide(status: Boolean){
        if (status == true){
            binding.btnPush.setVisibility(View.INVISIBLE)
            binding.btnAdvertiseHistory.setVisibility(View.INVISIBLE)
        } else {
            binding.btnPush.setVisibility(View.VISIBLE)
            binding.btnAdvertiseHistory.setVisibility(View.VISIBLE)
        }
    }

    companion object{
        //image pick code
        private val IMAGE_PICK_CODE = 1000
        //Permission code
        private val PERMISSION_CODE = 1001
        private val TAG = "AdvertiseAcitivty"
    }

    // handle the permission dialog
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    //permission from popup granted
                    pickImageFromGallary()
                } else {
                    // permission from popup denied
                    iToast("Permission denied")
                }
            }
        }
    }

    //handle pick image result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK ) { // && requestCode == IMAGE_PICK_CODE
            val uri: Uri = data?.data!!
            binding.ivImage.setImageURI(uri)
            ConvertImageToHex(uri)
            SetPreViewButtonHide(false)
            SetPushButtonHide(false)
            val animation = AnimationUtils.loadAnimation(this, R.anim.shake)
            binding.btnPush.animation = animation
            binding.btnPush.setOnClickListener {
                val result = ConvertImageToHex(uri)
                toast("Push done!")

                // generate the file name randomly to store to the Firebase, avoiding duplicate name
                val rnds = (0..100000).random()
                numberOfPic = rnds

                Log.e("onActivityResult", "numberOfPic = " + numberOfPic)
                uploadtoFirebase(uri, numberOfPic!!)
            }
            PreviewImage(uri)
        } else if (resultCode == ImagePicker.RESULT_ERROR){
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            iToast("Cancelled")
        }
    }

    private fun uploadtoFirebase(imageUri: Uri, numberOfPic: Int) {
        val fileRef: StorageReference = storageReference.child("advertising/" + auth.currentUser?.uid + "/" + numberOfPic.toString() + ".jpg")
        Log.e("uploadToFireBase", "numberOfPic = " + numberOfPic)
        fileRef.putFile(imageUri).addOnSuccessListener(object :
            OnSuccessListener<UploadTask.TaskSnapshot> {
            override fun onSuccess(p0: UploadTask.TaskSnapshot?) {
                toast("Image uploaded.")
//                updateToDialog(numberOfPic)
            }
        }).addOnFailureListener{
            iToast("Failed to upload the image to FireBase.")
        }
    }

    private fun updateToDialog(NOP: Int) {
        val imageViewNumber = (1..4).random()
        val profileRef: StorageReference = storageReference.child("advertising/" + auth.currentUser?.uid + "/" + NOP.toString() + ".jpg")
        Log.e("updateToDialog", "NOP = " + NOP)
        profileRef.downloadUrl.addOnSuccessListener(object: OnSuccessListener<Uri>{
            override fun onSuccess(uri: Uri?) {
                iv_advertise_dialog_1.setImageURI(uri)
//                Picasso.get().load(uri).into(iv_profile_avatar)
//                when(imageViewNumber){
//                    1->{
//                        Picasso.get().load(uri).into(iv_advertise_dialog_1) // check uri not null
//                    }
//                    2->{
//                        Picasso.get().load(uri!!).into(iv_advertise_dialog_2)
//                    }
//                    3->{
//                        Picasso.get().load(uri!!).into(iv_advertise_dialog_3)
//                    }
//                }
            }

        }).addOnFailureListener {
            iToast("fail to update to dialog")
        }

    }

    fun ConvertImageToHex(uri: Uri): ByteArray{
        var bitmapImage: Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        bitmapImage = Bitmap.createScaledBitmap(bitmapImage, 64, 64, false)
        val width = bitmapImage.width
        val height = bitmapImage.height
        Log.e(TAG, "width = " + width)
        Log.e(TAG, "height = " + height)
        val BufferRGB24 = ByteArray(width * height * 3)
        var R: Int
        var G: Int
        var B: Int
        for (i in 0 until height) {
            for (j in 0 until width) {
                val pixel = bitmapImage.getPixel(i, j)
                R = (pixel shr 16) and 0xff
                G = (pixel shr 8) and 0xff
                B = pixel and 0xff
                BufferRGB24[i * width + j] = R.toByte()
                BufferRGB24[(i * width)+64*64 + j] = G.toByte()
                BufferRGB24[(i * width) + ((64 * 64)*2) + j] = B.toByte()
            }
        }
        Log.e(TAG, "BufferRGB24 = " + BufferRGB24[1])
        Log.e(TAG, "BufferRGB24 = " + BufferRGB24[2])
        Log.e(TAG, "BufferRGB24 = " + BufferRGB24[3])
        binding.btnPreview.setOnClickListener {
            val imgToPreView = PreviewImage(uri)
            binding.ivPreview.setImageBitmap(Bitmap.createScaledBitmap(imgToPreView, 300, 300, false))
            LookupInfo(uri)
        }
        return BufferRGB24
    }

    fun PreviewImage(uri: Uri): Bitmap {
        val originBitmap: Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        val emptyImg: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.image_for_preview)

//        val operation = Bitmap.createBitmap(200, 200, emptyImg.config)
//        val width: Int = emptyImg.width
//        val height: Int = emptyImg.height
//        Log.e(TAG,"width = " + width )
//        Log.e(TAG,"height = " + height )
//        for (y in 0..64) {
//            for (x in 0..64) {
//                val p: Int = originBitmap.getPixel(x, y)
//                var oriRed: Int = Color.red(p)
//                var oriGreen: Int = Color.green(p)
//                var oriBlue: Int = Color.blue(p)
//                var oriAlpha: Int = Color.alpha(p)
//                for (xpos in (x*8) .. (x+1)*8){
//                    for (ypos in (y*8) .. (y+1)*8){
//                        operation.setPixel(xpos, ypos, Color.argb(oriAlpha, oriRed, oriGreen, oriBlue))
//                    }
//                }
//
//            }
//        }
        return originBitmap
    }

    fun LookupInfo(uri: Uri){
        val path: String? = uri.path
        val listString = path?.split("/")
        Log.e(TAG, "path = " + path)
        Log.e(TAG, "listString = " + listString)
        Log.e(TAG, "listString size = " + listString?.size)
        Log.e(TAG, "listString[1] = " + listString!![1])
        SetInformationHide(false)
        val imageName: String = listString[listString.size - 1]
        val imageNameString = imageName?.split(".")
        binding.tvInfoName.setText(imageNameString[0])
        val originBitmap: Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        val sizeImage: String = originBitmap.width.toString() + " x " + originBitmap.height.toString()
        binding.tvInfoSize.setText(sizeImage)
        binding.tvInfoFormat.setText(imageNameString[1].toUpperCase())
        binding.tvInfoLocation.setText(path)
    }
}