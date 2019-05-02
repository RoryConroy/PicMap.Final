package org.wit.pic.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.wit.pic.helpers.*
import java.util.*
//implementations
//gson used to convert java to or from java

val JSON_FILE = "pics.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<picModel>>() {}.type


//generates random id for each pic
fun generateRandomId(): Long {
    return Random().nextLong()
}

class picJSONStore : picStore, AnkoLogger {

    val context: Context
    //array list
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

//update the json collection
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

    //delete from json collection
    override fun delete(pic: picModel) {
        pics.remove(pic)
        serialize()
    }

}