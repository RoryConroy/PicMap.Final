package org.wit.pic.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import org.wit.pic.models.picJSONStore
import org.wit.pic.models.picStore

class MainApp: Application(), AnkoLogger {

    lateinit var pics :picStore

    override fun onCreate() {
        super.onCreate()
        pics = picJSONStore(applicationContext)
        info("pic Started")
        //appears when app is launched
        toast("Welcome to PicApp")
    }
}