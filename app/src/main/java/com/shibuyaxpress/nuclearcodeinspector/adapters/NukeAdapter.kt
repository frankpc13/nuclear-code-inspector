package com.shibuyaxpress.nuclearcodeinspector.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shibuyaxpress.nuclearcodeinspector.R
import com.shibuyaxpress.nuclearcodeinspector.holders.NukeHolder
import com.shibuyaxpress.nuclearcodeinspector.models.NukeCode
import com.shibuyaxpress.nuclearcodeinspector.utils.OnRecyclerItemClickListener

class NukeAdapter(val itemClickListener: OnRecyclerItemClickListener) : RecyclerView.Adapter<NukeHolder>() {

    private var list = ArrayList<NukeCode>()

    fun setList(list:ArrayList<NukeCode>){
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NukeHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_card_nuke_code, parent, false)
        return NukeHolder(itemView)
    }

    override fun getItemCount(): Int {
        return if (list.isNotEmpty()){
            list.size
        }else {
            0
        }
    }

    override fun onBindViewHolder(holder: NukeHolder, position: Int) {
       val item = list[position]
        holder.bind(item, itemClickListener, position)
    }

}