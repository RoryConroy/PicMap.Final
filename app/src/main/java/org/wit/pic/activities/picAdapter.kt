package org.wit.pic;

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.card_pic.view.*
import org.wit.pic.R.id.recyclerView
import org.wit.pic.helpers.readImageFromPath
import org.wit.pic.models.picModel
import android.widget.RatingBar
import kotlinx.android.synthetic.main.activity_pic.view.*

//represents click on card view
interface picListener {
    fun onpicClick(pic: picModel)
}

class picAdapter constructor(private var pics: List<picModel>,
                                   private val listener: picListener) : RecyclerView.Adapter<picAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(LayoutInflater.from(parent?.context).inflate(R.layout.card_pic, parent, false))
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val pic = pics[holder.adapterPosition]
        holder.bind(pic, listener)
    }


    override fun getItemCount(): Int = pics.size


    //to display into recycle view, must also be passed into card_pic.xml
    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(pic: picModel,  listener : picListener) {
            itemView.picTitleList.text= pic.title
            itemView.picDescriptionList.text = pic.description
            itemView.imageViewList.setImageBitmap(readImageFromPath(itemView.context, pic.image))
            itemView.setOnClickListener { listener.onpicClick(pic) }
        }
    }
}
