package org.wit.pic.models

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class picMemStore : picStore, AnkoLogger {

    val pics = ArrayList<picModel>()

    override fun findAll(): List<picModel> {
        return pics
    }

    override fun create(pic: picModel) {
        pic.id = getId()
        pics.add(pic)
        logAll()
    }

    override fun update(pic: picModel) {
        var foundpic: picModel? = pics.find { p -> p.id == pic.id }
        if (foundpic != null) {
            foundpic.title = pic.title
            foundpic.description = pic.description
            foundpic.image = pic.image
            foundpic.lat = pic.lat
            foundpic.lng = pic.lng
            foundpic.zoom = pic.zoom
            foundpic.editText2 =pic.editText2
            foundpic.editText3=pic.editText3
            logAll()
        }
    }

    fun logAll() {
        pics.forEach { info("${it}") }
    }

    override fun delete(pic: picModel) {
        pics.remove(pic)
    }
}
