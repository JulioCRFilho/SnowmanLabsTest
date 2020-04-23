package com.snowmanlabs.test.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.snowmanlabs.test.R
import kotlinx.android.synthetic.main.dialog_interactor.*

class InteractorDialog(
    context: Context,
    private val msg: String? = null,
    private val owner: LifecycleOwner? = null,
    private val status: LiveData<Pair<Int, String?>>? = null
) :
    Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_interactor)
        window?.setBackgroundDrawableResource(android.R.color.transparent)

        msg.let { failedMsg.text = it }

        owner?.let {
            status?.observe(it, Observer {status ->
                setCancelable(status.first != 1)

                viewFlipper.displayedChild = status.first

                status.second?.let { msg ->
                    failedMsg.text = msg
                }
            })
        }

        cancelBtn.setOnClickListener { this.dismiss() }
    }
}