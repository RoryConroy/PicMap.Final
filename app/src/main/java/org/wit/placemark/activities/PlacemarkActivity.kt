package org.wit.placemark.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_placemark.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.wit.placemark.R
import org.wit.placemark.helpers.readImage
import org.wit.placemark.helpers.readImageFromPath
import org.wit.placemark.helpers.showImagePicker
import org.wit.placemark.main.MainApp
import org.wit.placemark.models.Location
import org.wit.placemark.models.PlacemarkModel
import android.widget.RatingBar
import org.wit.placemark.models.PlacemarkJSONStore
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS


class PlacemarkActivity : AppCompatActivity(), AnkoLogger {


    var displayList:MutableList<String> =ArrayList()
    var placemark = PlacemarkModel()
    lateinit var app: MainApp
    var edit = false
    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2
    //var location = Location(52.245696, -7.139102, 15f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placemark)
        app = application as MainApp

        if (intent.hasExtra("placemark_edit"))
        {
            placemark = intent.extras.getParcelable<PlacemarkModel>("placemark_edit")
            placemarkTitle.setText(placemark.title)
            placemarkDescription.setText(placemark.description)
            editText2.setText(placemark.editText2)
            editText3.setText(placemark.editText3)
            placemarkImage.setImageBitmap(readImageFromPath(this, placemark.image))
            btnAdd.setText(R.string.button_savePlacemark)
            chooseImage.setText(R.string.button_changeImage)
            edit = true
        }

        btnAdd.setOnClickListener() {
            placemark.title = placemarkTitle.text.toString()
            placemark.description = placemarkDescription.text.toString()
            placemark.editText2= editText2.text.toString()
            placemark.editText3=editText3.text.toString()
            if (placemark.title.isNotEmpty()) {
                if (edit){
                    app.placemarks.update(placemark.copy())
                }
                else {
                    app.placemarks.create(placemark.copy())
                }
                info("Add Button Pressed. name: ${placemark.title}")
                setResult(AppCompatActivity.RESULT_OK)
                finish()
            }
            else {
                toast ("please enter a title for your image")
            }
            if (placemark.description.isEmpty()){


                toast("please edit and enter a description")
            }
            if (placemark.editText3.isNullOrBlank()){
                toast("please enter a date for the image")
            }
            else {
                toast("successfully created")
            }
        }




        chooseImage.setOnClickListener{
            showImagePicker(this, IMAGE_REQUEST)

        }

        placemarkLocation.setOnClickListener{
            val location = Location(52.675332,  -6.295963, 15f)
            if (placemark.zoom != 0f) {
                location.lat =  placemark.lat
                location.lng = placemark.lng
                location.zoom = placemark.zoom
            }

            startActivityForResult(intentFor<MapsActivity>().putExtra("location", location), LOCATION_REQUEST)

        }



        //Add action bar and set title
        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_placemark, menu)
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
                app.placemarks.delete(placemark)
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
                    placemark.image = data.getData().toString()
                    placemarkImage.setImageBitmap(readImage(this, resultCode, data))
                    chooseImage.setText(R.string.button_changeImage)
                }

            }
            LOCATION_REQUEST -> {
                if (data != null){
                    val location = data.extras.getParcelable<Location>("location")
                    placemark.lat = location.lat
                    placemark.lng = location.lng
                    placemark.zoom = location.zoom
                }
            }
        }
    }




}