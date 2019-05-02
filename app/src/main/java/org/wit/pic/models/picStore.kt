package org.wit.pic.models

interface picStore {
    fun findAll(): List<picModel>
    fun create(pic: picModel)
    fun update(pic: picModel)
    fun delete(pic: picModel)

}
