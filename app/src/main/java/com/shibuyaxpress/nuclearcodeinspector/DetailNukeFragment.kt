package com.shibuyaxpress.nuclearcodeinspector

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import coil.api.load
import com.shibuyaxpress.nuclearcodeinspector.DetailNukeFragmentArgs.Companion.fromBundle


class DetailNukeFragment : Fragment() {

    companion object {
        fun newInstance() = DetailNukeFragment()
    }

    private lateinit var titleText: TextView
    private lateinit var descriptionText: TextView
    private lateinit var imagePreview: ImageView
    private lateinit var viewModel: DetailNukeViewModel
    private val preview by lazy {
        fromBundle(arguments!!).item
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_detail_nuke, container, false)
        imagePreview = root.findViewById(R.id.imagePreview)
        titleText = root.findViewById(R.id.textTitle)
        descriptionText = root.findViewById(R.id.textDescription)

        imagePreview.load(preview.image)
        titleText.text = preview.title
        descriptionText.text = preview.description
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DetailNukeViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
