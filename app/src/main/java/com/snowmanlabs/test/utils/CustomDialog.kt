package com.snowmanlabs.test.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.snowmanlabs.test.R
import kotlinx.android.synthetic.main.dialog_loading.*

class CustomDialog(context: Context, private val msg: String) :
    Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_loading)
        window?.setBackgroundDrawableResource(android.R.color.transparent)

        setCancelable(false)

        failedMsg.text = msg

        cancelBtn.setOnClickListener { this.dismiss() }
    }
}