package org.wit.pic.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import kotlinx.android.synthetic.main.activity_pic_list.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult
import org.wit.pic.picAdapter
import org.wit.pic.picListener
import org.wit.pic.R
import org.wit.pic.main.MainApp
import org.wit.pic.models.picModel

class picListActivity : AppCompatActivity(), picListener {

    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pic_list)
        app = application as MainApp

        //layout and populate for display
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager   //recyclerView is a widget in activity_pic_list.xml
        loadpics()

        //enable action bar and set title
        toolbarMain.title = title
        setSupportActionBar(toolbarMain)
    }
//inflate menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }


    //implements menu event handler
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_add -> startActivityForResult<picActivity>(0)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onpicClick(pic: picModel) {
        startActivityForResult(intentFor<picActivity>().putExtra("pic_edit", pic), 0)
    }


    //updates view to show new information entered
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        loadpics()
        super.onActivityResult(requestCode, resultCode, data)
    }

    //loads images,
    private fun loadpics() {
        showpics(app.pics.findAll())
    }


    //show images in recycle view
    fun showpics (pics: List<picModel>) {
        //recyclerView is a widget in activity_pic_list.xml
        recyclerView.adapter = picAdapter(pics, this)
        recyclerView.adapter?.notifyDataSetChanged()

    }
}