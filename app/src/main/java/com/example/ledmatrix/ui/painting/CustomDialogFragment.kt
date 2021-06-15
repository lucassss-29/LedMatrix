package com.example.ledmatrix.ui.painting

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.DialogFragment
import com.example.ledmatrix.R
import kotlinx.android.synthetic.main.activity_paint.*
import kotlinx.android.synthetic.main.color_fragment.view.*

class CustomDialogFragment: DialogFragment() {
    private lateinit var communicator : CommunicatorColor

    @SuppressLint("ShowToast")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var rootView : View = inflater.inflate(R.layout.color_fragment, container, false)

        communicator = activity as CommunicatorColor

        rootView.canclebtn.setOnClickListener{
            dismiss()
        }
        val red = rootView.findViewById<RadioButton>(R.id.redbtn)
        val blue = rootView.findViewById<RadioButton>(R.id.bluebtn)
        val green = rootView.findViewById<RadioButton>(R.id.greenbtn)
        val black = rootView.findViewById<RadioButton>(R.id.blackbtn)
        val grey = rootView.findViewById<RadioButton>(R.id.greybtn)
        rootView.submitbtn.setOnClickListener{
            if (red.isChecked){ draw_view_paint.setColor(-0x10000) }
            else if (blue.isChecked){  draw_view_paint.setColor(-0xffff01) }
            else if (green.isChecked){ draw_view_paint.setColor(-0xff0100) }
            else if (black.isChecked){ draw_view_paint.setColor(-0x1000000) }
            else if (grey.isChecked){ draw_view_paint.setColor(-0x777778) }
            
            communicator.passData(rootView.toString())

            dismiss()
        }


        return rootView
    }
}