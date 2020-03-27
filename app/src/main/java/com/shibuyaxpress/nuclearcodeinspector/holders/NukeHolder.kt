package com.shibuyaxpress.nuclearcodeinspector.holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.shibuyaxpress.nuclearcodeinspector.R
import com.shibuyaxpress.nuclearcodeinspector.models.NukeCode
import com.shibuyaxpress.nuclearcodeinspector.utils.OnRecyclerItemClickListener
import kotlinx.android.synthetic.main.item_card_nuke_code.view.*

class NukeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var card: CardView? = null
    var title: TextView? = null
    var nukeCode: TextView? = null
    var description: TextView? = null
    var imageCode: ImageView? = null

    init {
        card = itemView.findViewById(R.id.nukeCard)
        title = itemView.findViewById(R.id.titleNukeText)
        nukeCode = itemView.findViewById(R.id.CodeNukeText)
        description = itemView.findViewById(R.id.nukeDescriptionText)
        imageCode = itemView.findViewById(R.id.nukeImageView)
    }

    fun bind(item: NukeCode, clickListener: OnRecyclerItemClickListener, position: Int){
        title!!.text = item.name
        nukeCode!!.text = item.nukeCode
        description!!.text = item.description
        imageCode!!.load(item.image) {
            placeholder(R.drawable.wachan)
            crossfade(true)
        }
    }
}