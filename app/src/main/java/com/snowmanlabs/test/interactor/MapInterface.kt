package com.snowmanlabs.test.interactor

interface MapInterface {
    interface UI {
        fun onError(msg: String)
        fun onLoading()
        fun onSuccess(msg: String)
        fun onFailure(msg: String)
    }
}