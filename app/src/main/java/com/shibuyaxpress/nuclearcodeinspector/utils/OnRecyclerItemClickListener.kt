package com.shibuyaxpress.nuclearcodeinspector.utils

import android.view.View

interface OnRecyclerItemClickListener {

    fun onItemClicked(item: Any, position: Int, view: View)
}