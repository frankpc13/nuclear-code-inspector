package com.shibuyaxpress.nuclearcodeinspector.ui.home.detail

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.TextView
import coil.api.load
import com.google.firebase.firestore.FirebaseFirestore
import com.shibuyaxpress.nuclearcodeinspector.DetailNukeFragmentArgs.Companion.fromBundle
import com.shibuyaxpress.nuclearcodeinspector.R
import com.shibuyaxpress.nuclearcodeinspector.models.Preview
import khronos.Dates
import khronos.toString


class DetailNukeFragment : Fragment() {

    companion object {
        fun newInstance() =
            DetailNukeFragment()
    }

    private lateinit var titleText: TextView
    private lateinit var descriptionText: TextView
    private lateinit var codeText: TextView
    private lateinit var imagePreview: ImageView
    private lateinit var viewModel: DetailNukeViewModel
    private lateinit var db: FirebaseFirestore

    private val preview by lazy {
        fromBundle(arguments!!).item
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_detail_nuke, container, false)
        imagePreview = root.findViewById(R.id.imagePreview)
        titleText = root.findViewById(R.id.textTitle)
        descriptionText = root.findViewById(R.id.textDescription)
        codeText = root.findViewById(R.id.textCode)
        db = FirebaseFirestore.getInstance()
        imagePreview.load(preview.image)
        titleText.text = preview.title
        descriptionText.text = preview.description
        codeText.text = preview.code

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DetailNukeViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.top_save -> {
            createNukeCodeOnFirestore(preview)
            true
        }
        R.id.top_share -> {
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun createNukeCodeOnFirestore(item: Preview) {
        val currentDate = Dates.today.toString("yyyy-MM-dd HH:mm:ss")
        val data = hashMapOf(
            "title" to item.title,
            "image" to item.image,
            "code" to item.code,
            "categories" to item.description,
            "createdAt" to currentDate
        )

        db.collection("codes")
            .add(data)
            .addOnSuccessListener {
                Log.d("DetailNukeFragment","member with ID :${it.id}")
            }
            .addOnFailureListener {
                Log.w("DetailNukeFragment","Error adding doc", it)
            }
    }

}
