package com.shibuyaxpress.nuclearcodeinspector.ui.codes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.shibuyaxpress.nuclearcodeinspector.R
import com.shibuyaxpress.nuclearcodeinspector.adapters.NukeAdapter
import com.shibuyaxpress.nuclearcodeinspector.models.NukeCode
import com.shibuyaxpress.nuclearcodeinspector.utils.OnRecyclerItemClickListener

class CodesFragment : Fragment(), OnRecyclerItemClickListener {

    private lateinit var codesViewModel: CodesViewModel
    private lateinit var nukeCodeAdapter: NukeAdapter
    private lateinit var nukeCodeRecycler: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*notificationsViewModel =
            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)*/
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        //val textView: TextView = root.findViewById(R.id.text_notifications)
        /*notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/
        setupUI(root)
        return root
    }

    private fun setupUI(root: View) {
        nukeCodeAdapter = NukeAdapter(this)
        nukeCodeRecycler = root.findViewById(R.id.nukeCodesRecycler)
        nukeCodeRecycler.layoutManager = LinearLayoutManager(activity!!.applicationContext)
        nukeCodeRecycler.setHasFixedSize(true)
        var decorator = DividerItemDecoration(activity!!.applicationContext,VERTICAL)
        nukeCodeRecycler.addItemDecoration(decorator)
        nukeCodeRecycler.adapter = nukeCodeAdapter
        getCodeListFromDatabase()
    }
    private fun getCodeListFromDatabase() {
        val db = FirebaseFirestore.getInstance()
        db.collection("codes")
            .get()
            .addOnSuccessListener {
                var list: ArrayList<NukeCode> = ArrayList()
                for(document in it) {
                    var item = document.data
                    list.add(NukeCode(document.getString("code"), document.getString("title"),document.getString("categories"),document.getString("image")))
                    Log.d("Fragment Codes","${document.data}")
                }
                nukeCodeAdapter.setList(list)
                nukeCodeAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Log.d("Fragment Codes", "error getting data", it)
            }
    }

    override fun onItemClicked(item: Any, position: Int, view: View) {
        Toast.makeText(context,"dont work",Toast.LENGTH_SHORT).show()
    }
}
