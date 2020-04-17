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

class CustomDialog(context: Context, private val status: LiveData<Pair<Int, String?>>, private val lifeCycle: LifecycleOwner) :
    Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_loading)
        window?.setBackgroundDrawableResource(android.R.color.transparent)

        status.observe(lifeCycle, Observer { value ->
            dialogFlipper.displayedChild = value.first

            if (value.first == 2) this.dismiss()

            value.second.let { msg ->
                failedMsg.text = msg
            }
        })
    }
}