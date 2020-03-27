package com.shibuyaxpress.nuclearcodeinspector.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputLayout
import com.leocardz.link.preview.library.LinkPreviewCallback
import com.leocardz.link.preview.library.SourceContent
import com.leocardz.link.preview.library.TextCrawler
import com.shibuyaxpress.nuclearcodeinspector.R
import com.shibuyaxpress.nuclearcodeinspector.models.Preview
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    var direction: NavDirections? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        var inputCode = ""
        val editTextCode = root.findViewById<TextInputLayout>(R.id.inputCodes)
        val buttonSearch: Button = root.findViewById(R.id.buttonSearch);
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            //textView.text = it
            //inputCode = inputCodes.editText!!.text.toString()
        })

        //create instance of the text crawler to parse your url into a preview
        val crawler = TextCrawler()
        //Create the callback to handle pre and post execution of the preview generation.
        val linkPreview = object : LinkPreviewCallback {
            override fun onPre() {
                //any work that needs to be done before generating the preview. Usually inflate
                //your custom preview layout here.
            }

            override fun onPos(param: SourceContent?, check: Boolean) {
                //Populate your preview layout with the results of sourceContent.
                Log.d(this@HomeFragment::class.java.canonicalName, param!!.title)
                val item = Preview()
                item.description = param.description
                item.image = param.images.first()
                item.title = param.title
                direction = HomeFragmentDirections.actionNavigationHomeToDetailNukeFragment(item)
            }

        }

        buttonSearch.setOnClickListener {
            inputCode = editTextCode.editText!!.text.toString()
            crawler.makePreview(linkPreview, "https://nhentai.net/g/${inputCode}/")
            Toast.makeText(context, inputCode, Toast.LENGTH_SHORT).show()

            Navigation.findNavController(it).navigate(direction!!)
        }

        return root
    }
}
