package com.shibuyaxpress.nuclearcodeinspector.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.shibuyaxpress.nuclearcodeinspector.R
import com.shibuyaxpress.nuclearcodeinspector.holders.NukeHolder
import com.shibuyaxpress.nuclearcodeinspector.models.NukeCode
import com.shibuyaxpress.nuclearcodeinspector.utils.OnRecyclerItemClickListener
import java.util.*
import kotlin.collections.ArrayList

class NukeAdapter(private val itemClickListener: OnRecyclerItemClickListener)
    : RecyclerView.Adapter<NukeHolder>(), Filterable {

    private var list = ArrayList<NukeCode>()
    private var filterList = ArrayList<NukeCode>()
    fun setList(list:ArrayList<NukeCode>){
        this.list = list
        list.sortBy { it.nukeCode }
        filterList = list
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                Log.d("SearchTEXT", constraint.toString())
                val charSearch = constraint.toString()
                filterList = if (charSearch.isEmpty()) {
                    list
                } else {
                    val resultList = list
                    /*for (row in list) {
                        if (row.name!!.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                            || row.description!!.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }*/
                    resultList.filter {
                        it.description!!.trim().toLowerCase(Locale.getDefault())
                            .contains(charSearch.trim().toLowerCase(
                                Locale.getDefault())) ||
                                it.description!!.split(",").map { x -> x.trim() }
                                    .containsAll(charSearch.split(",").map {y -> y.trim() })
                                ||
                                it.name!!.trim().toLowerCase(Locale.getDefault()).trim()
                                    .contains(charSearch.trim().toLowerCase(Locale.getDefault()))
                    } as ArrayList<NukeCode>
                }
                val filterResults = FilterResults()
                filterResults.values = filterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterList = results?.values as ArrayList<NukeCode>
                notifyDataSetChanged()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NukeHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card_nuke_code, parent, false)
        return NukeHolder(itemView)
    }

    override fun getItemCount(): Int {
        return if (filterList.isNotEmpty()){
            filterList.size
        }else {
            0
        }
    }

    override fun onBindViewHolder(holder: NukeHolder, position: Int) {
       val item = filterList[position]
        holder.bind(item, itemClickListener, position)
    }

}