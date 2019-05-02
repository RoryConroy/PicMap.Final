package org.wit.pic.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_pic.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.wit.pic.R
import org.wit.pic.helpers.readImage
import org.wit.pic.helpers.readImageFromPath
import org.wit.pic.helpers.showImagePicker
import org.wit.pic.main.MainApp
import org.wit.pic.models.Location
import org.wit.pic.models.picModel
import android.widget.RatingBar
import org.wit.pic.models.picJSONStore
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS


class picActivity : AppCompatActivity(), AnkoLogger {


    var displayList:MutableList<String> =ArrayList()
    var pic = picModel()
    lateinit var app: MainApp
    var edit = false
    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2
    //var location = Location(52.245696, -7.139102, 15f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pic)
        app = application as MainApp

        if (intent.hasExtra("pic_edit"))
        {
            pic = intent.extras.getParcelable<picModel>("pic_edit")
            picTitle.setText(pic.title)
            picDescription.setText(pic.description)
            editText2.setText(pic.editText2)
            editText3.setText(pic.editText3)
            picImage.setImageBitmap(readImageFromPath(this, pic.image))
            btnAdd.setText(R.string.button_savepic)
            chooseImage.setText(R.string.button_changeImage)
            edit = true
        }

        btnAdd.setOnClickListener() {
            pic.title = picTitle.text.toString()
            pic.description = picDescription.text.toString()
            pic.editText2= editText2.text.toString()
            pic.editText3=editText3.text.toString()
            if (pic.title.isNotEmpty()) {
                if (edit){
                    app.pics.update(pic.copy())
                }
                else {
                    app.pics.create(pic.copy())
                }
                info("Add Button Pressed. name: ${pic.title}")
                setResult(AppCompatActivity.RESULT_OK)
                finish()
            }
            else {
                toast ("please enter a title for your image")
            }
            if (pic.description.isEmpty()){


                toast("please edit and enter a description")
            }
            if (pic.editText3.isNullOrBlank()){
                toast("please enter a date for the image")
            }
            else {
                toast("successfully created")
            }
        }




        chooseImage.setOnClickListener{
            showImagePicker(this, IMAGE_REQUEST)

        }

        picLocation.setOnClickListener{
            val location = Location(52.675332,  -6.295963, 15f)
            if (pic.zoom != 0f) {
                location.lat =  pic.lat
                location.lng = pic.lng
                location.zoom = pic.zoom
            }

            startActivityForResult(intentFor<MapsActivity>().putExtra("location", location), LOCATION_REQUEST)

        }



        //Add action bar and set title
        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_pic, menu)
        if (edit && menu != null) menu.getItem(0).setVisible(true)
        return super.onCreateOptionsMenu(menu)

        val searchItem = menu?.findItem(R.id.menu_search)
        if (searchItem != null) {
            val searchView = searchItem.actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if(newText!!.isNotEmpty()){

                    }else{
                        displayList.clear()
                    }
                    return true
                }

            })

        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_delete -> {
                app.pics.delete(pic)
                finish()
            }
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode){
            IMAGE_REQUEST -> {
                if (data !=null){
                    pic.image = data.getData().toString()
                    picImage.setImageBitmap(readImage(this, resultCode, data))
                    chooseImage.setText(R.string.button_changeImage)
                }

            }
            LOCATION_REQUEST -> {
                if (data != null){
                    val location = data.extras.getParcelable<Location>("location")
                    pic.lat = location.lat
                    pic.lng = location.lng
                    pic.zoom = location.zoom
                }
            }
        }
    }




}