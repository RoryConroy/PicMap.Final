package org.wit.pic.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.wit.pic.helpers.*
import java.util.*

val JSON_FILE = "pics.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<picModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class picJSONStore : picStore, AnkoLogger {

    val context: Context
    var pics = mutableListOf<picModel>()

    constructor (context: Context) {
        this.context = context
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<picModel> {
        return pics
    }

    override fun create(pic: picModel) {
        pic.id = generateRandomId()
        pics.add(pic)
        serialize()
    }


    override fun update(pic: picModel) {
        val picsList = findAll() as ArrayList<picModel>
        var foundpic: picModel? = picsList.find { p -> p.id == pic.id }
        if (foundpic != null) {
            foundpic.title = pic.title
            foundpic.description = pic.description
            foundpic.image = pic.image
            foundpic.lat = pic.lat
            foundpic.lng = pic.lng
            foundpic.zoom = pic.zoom
            foundpic.editText2 =pic.editText2
            foundpic.editText3=pic.editText3
        }
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(pics, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        pics = Gson().fromJson(jsonString, listType)
    }
    override fun delete(pic: picModel) {
        pics.remove(pic)
        serialize()
    }

}