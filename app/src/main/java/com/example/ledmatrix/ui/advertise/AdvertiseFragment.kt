package com.example.advertise

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.provider.MediaStore.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.ledmatrix.R
import com.example.ledmatrix.ui.bluetooth.ScanDevicesFragment.Companion.m_bluetoothSocket
import com.example.ledmatrix.ui.bluetooth.ScanDevicesFragment.Companion.m_isConnected
import com.example.ledmatrix.ui.home.HomeFragment
import com.example.ledmatrix.utils.Utils
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_advertise.*
import kotlinx.android.synthetic.main.dialog_advertise_history.view.*
import java.io.IOException


private lateinit var storageReference: StorageReference
private lateinit var auth: FirebaseAuth

class AdvertiseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_advertise, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iv_ad_image.setImageResource(R.drawable.ic_image)
        storageReference = FirebaseStorage.getInstance().getReference()
        auth = Firebase.auth

        SetInformationHide(true)
        SetPreViewButtonHide(true)
        SetPushButtonHide(true)
        Utils.progressBar(progressbar_bg_advertise, "hide")

        btn_ad_choose.setOnClickListener {
            ImagePicker.with(this)
                .crop()                                    //Crop image(Optional), Check Customization for more option
                .compress(1024)                    //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }

        btn_ad_back.setOnClickListener {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.slide_out
                )
                replace<HomeFragment>(R.id.fragment_commutor)
            }
        }

        btn_advertise_history.setOnClickListener {
            // inflate the dialog
            val view = View.inflate(requireActivity(), R.layout.dialog_advertise_history, null)
            val builder = AlertDialog.Builder(requireActivity())
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            Utils.progressBar(progressbar_bg_advertise, "show")
            // recall the history from firebase

            val profileRef: StorageReference =
                storageReference.child("advertising/" + auth.currentUser?.uid + "/" + "image.jpg")
            profileRef.downloadUrl.addOnSuccessListener(object : OnSuccessListener<Uri> {
                override fun onSuccess(uri: Uri?) {
                    view.tv_advertise_dialog.setText("Choose image below for pushing.")
                    Picasso.get().load(uri).into(view.iv_advertise_dialog)
                    Utils.progressBar(progressbar_bg_advertise, "hide")
                    view.iv_advertise_dialog.setOnClickListener {
                        dialog.dismiss()
                        Utils.progressBar(progressbar_bg_advertise, "show")
                        Handler(Looper.getMainLooper()).postDelayed({
                            Utils.progressBar(progressbar_bg_advertise, "hide")
                        }, 2000)
                        lookupInfoFirebase(uri!!)
                    }
                }

            }).addOnFailureListener {
                Utils.iToast("fail to update to dialog", requireActivity())
                view.tv_advertise_dialog.setText("No history required. Please push somthing.")
            }

            view.btn_advertise_dialog_cancel.setOnClickListener {
                dialog.dismiss()
            }
        }

    }

    private fun lookupInfoFirebase(uri: Uri) {
        SetInformationHide(false)
        SetPreViewButtonHide(false)
        SetPushButtonHide(false)
        Picasso.get().load(uri).into(iv_ad_image)
        Picasso.get().load(uri).into(iv_ad_preview)
        tv_info_name.setText("unformatted")
        tv_info_size.setText("unformatted")
        tv_info_format.setText("unformatted")
        tv_info_location.setText("Firebase")
        buttonPushListener(uri, true)
    }

    private fun pickImageFromGallary() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    private fun SetInformationHide(status: Boolean) {
        if (status == true) {
            cardView_info.setVisibility(View.INVISIBLE)
        } else {
            cardView_info.setVisibility(View.VISIBLE)
        }

    }

    private fun SetPreViewButtonHide(status: Boolean) {
        if (status == true) {
            btn_ad_preview.setVisibility(View.INVISIBLE)
        } else {
            btn_ad_preview.setVisibility(View.VISIBLE)
        }
    }

    private fun SetPushButtonHide(status: Boolean) {
        if (status == true) {
            btn_ad_push.setVisibility(View.INVISIBLE)
        } else {
            btn_ad_push.setVisibility(View.VISIBLE)
        }
    }

    companion object {
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
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    //permission from popup granted
                    pickImageFromGallary()
                } else {
                    // permission from popup denied
                    Utils.iToast("Permission denied", requireActivity())
                }
            }
        }
    }

    //handle pick image result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) { // && requestCode == IMAGE_PICK_CODE
            val uri: Uri = data?.data!!
            iv_ad_image.setImageURI(uri)
            //ConvertImageToHex(uri)
            SetPreViewButtonHide(false)
            SetPushButtonHide(false)

            // when push is pressed, generate bitmap and upload to FireBase
            buttonPushListener(uri, false)
            buttonPreviewListener(uri)

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
//            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            Utils.toast(ImagePicker.getError(data), requireActivity())
        } else {
            Utils.iToast("Cancelled", requireActivity())
        }
    }

    // This function is generate Bitmap array
    private fun buttonPushListener(uri: Uri, isFireBase: Boolean){
        val animation = AnimationUtils.loadAnimation(requireActivity(), R.anim.shake)
        btn_ad_push.animation = animation
        btn_ad_push.setOnClickListener {
            sendCommand(ConvertImageToHex(iv_ad_image))
            Utils.toast("Push done!", requireActivity())
            uploadtoFirebase(uri)
        }
    }
    private fun sendCommand(input: ByteArray){
        //  binding.progressBar.visibility = View.VISIBLE
        if(m_bluetoothSocket!=null){
            try {
                m_bluetoothSocket!!.outputStream.write(input)
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }
    private fun buttonPreviewListener(uri: Uri){
        btn_ad_preview.setOnClickListener {
            Utils.progressBar(progressbar_bg_advertise,"show")
            Handler(Looper.getMainLooper()).postDelayed({
                Utils.progressBar(progressbar_bg_advertise, "hide")
                lookupInfo(uri)
            }, 1000)
        }
    }
/*
    private fun ConvertImageToHexFirebase(uri: Uri) {
        val BufferRGB24 = ByteArray(64 * 64 * 3)
        Glide.with(requireActivity())
            .asBitmap()
            .load(uri)
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val bitmapImageFB = Bitmap.createScaledBitmap(resource, 64, 64, false)
                    val width = bitmapImageFB.width
                    val height = bitmapImageFB.height
                    Log.e(TAG, "width = " + width)
                    Log.e(TAG, "height = " + height)
                    val BufferRGB24 = ByteArray(width * height * 3)
                    var R: Int
                    var G: Int
                    var B: Int
                    for (i in 0 until height) {
                        for (j in 0 until width) {
                            val pixel = bitmapImageFB.getPixel(i, j)
                            R = (pixel shr 16) and 0xff
                            G = (pixel shr 8) and 0xff
                            B = pixel and 0xff
                            BufferRGB24[i * width + j] = R.toByte()
                            BufferRGB24[(i * width) + 64 * 64 + j] = G.toByte()
                            BufferRGB24[(i * width) + ((64 * 64) * 2) + j] = B.toByte()
                            Log.e(TAG, "BufferRGB24" + BufferRGB24)
                            Log.e(TAG, "BufferRGB24[0]" + BufferRGB24[0])
                            Log.e(TAG, "BufferRGB24[0]" + BufferRGB24[1])
                        }
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // this is called when imageView is cleared on lifecycle call or for
                    // some other reason.
                    // if you are referencing the bitmap somewhere else too other than this imageView
                    // clear it here as you can no longer have the bitmap
                }
            })
    }
*/
    fun uploadtoFirebase(imageUri: Uri) {
        val fileRef: StorageReference =
            storageReference.child("advertising/" + auth.currentUser?.uid + "/" +  "image.jpg")
        fileRef.putFile(imageUri).addOnSuccessListener(object :
            OnSuccessListener<UploadTask.TaskSnapshot> {
            override fun onSuccess(p0: UploadTask.TaskSnapshot?) {
                Utils.toast("Image uploaded.", activity!!)
            }
        }).addOnFailureListener {
            Utils.iToast("Failed to upload the image to FireBase.", requireActivity())
        }
    }
    fun Bitmap.flip(): Bitmap {
        val matrix = Matrix().apply { postScale(-1f, 1f) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }
    fun ConvertImageToHex(img : ImageView) : ByteArray {
        var bitmapImage = (img.getDrawable() as BitmapDrawable).bitmap
        bitmapImage = Bitmap.createScaledBitmap(bitmapImage, 64, 64, true)
        val flippedBitmap = bitmapImage.flip()
        val width = flippedBitmap.width
        val height = flippedBitmap.height
        val BufferRGB24 = ByteArray(width * height * 3)
        var R: Int
        var G: Int
        var B: Int
        for (i in 0 until height) {
            for (j in 0 until width) {
                val pixel = flippedBitmap.getPixel(i, j)
                R = (pixel shr 16) and 0xff
                G = (pixel shr 8) and 0xff
                B = pixel and 0xff
                BufferRGB24[i * width + j] = R.toByte()
                BufferRGB24[(i * width) + 64 * 64 + j] = G.toByte()
                BufferRGB24[(i * width) + ((64 * 64) * 2) + j] = B.toByte()
            }
        }
        return BufferRGB24
    }

    fun lookupInfo(uri: Uri) {
        val imgToPreview: Bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, uri)
        iv_ad_preview.setImageBitmap(Bitmap.createScaledBitmap(imgToPreview, 300, 300, false))
        val path: String? = uri.path
        val listString = path?.split("/")
        Log.e(TAG, "path = " + path)
        Log.e(TAG, "listString = " + listString)
        Log.e(TAG, "listString size = " + listString?.size)
        Log.e(TAG, "listString[1] = " + listString!![1])
        SetInformationHide(false)
        val imageName: String = listString[listString.size - 1]
        val imageNameString = imageName?.split(".")
        tv_info_name.setText(imageNameString[0])
        val originBitmap: Bitmap = Images.Media.getBitmap(activity?.contentResolver, uri)
        val sizeImage: String =
            originBitmap.width.toString() + " x " + originBitmap.height.toString()
        tv_info_size.setText(sizeImage)
        tv_info_format.setText(imageNameString[1].toUpperCase())
        tv_info_location.setText(path)
    }

    override fun onStop() {
        super.onStop()
        Log.e("TAG------------","STOPPPPPPPPPPPPPPPPP")
        if(m_bluetoothSocket == null || !m_isConnected) {
            m_bluetoothSocket!!.close()
        }
    }
}