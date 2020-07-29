package com.shibuyaxpress.nuclearcodeinspector.ui.codes

import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.shibuyaxpress.nuclearcodeinspector.R
import com.shibuyaxpress.nuclearcodeinspector.adapters.NukeAdapter
import com.shibuyaxpress.nuclearcodeinspector.models.NukeCode
import com.shibuyaxpress.nuclearcodeinspector.utils.OnRecyclerItemClickListener
import khronos.Dates
import khronos.toString

class CodesFragment : Fragment(), OnRecyclerItemClickListener {

    private lateinit var codesViewModel: CodesViewModel
    private lateinit var nukeCodeAdapter: NukeAdapter
    private lateinit var nukeCodeRecycler: RecyclerView
    private var user: FirebaseUser? = null
    private lateinit var emptyState: Group
    private lateinit var loadingProgress: ProgressBar
    private lateinit var searchView: SearchView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar_codes, menu)
        val searchItem = menu.findItem(R.id.action_search)
        if (searchItem != null) {
            searchView = MenuItemCompat.getActionView(searchItem) as SearchView
            searchView.setOnCloseListener {
                searchView.onActionViewCollapsed()
                true
            }
            val searchPlate = searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
            searchPlate.hint = "Search by code or description"
            val searchPlateView: View = searchView.findViewById(androidx.appcompat.R.id.search_plate)
            searchPlateView.setBackgroundColor(
                ContextCompat.getColor(
                    context!!,
                    android.R.color.transparent
                )
            )
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    //make logic to sort by here
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    nukeCodeAdapter.filter.filter(newText)
                    return true
                }

            })
            //val searchManager = getSystemService(context!!, Context.SEARCH_SERVICE) as SearchManager
            //searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }
        return super.onCreateOptionsMenu(menu, inflater)

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*notificationsViewModel =
            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)*/
        val root = inflater.inflate(R.layout.fragment_codes, container, false)
        emptyState = root.findViewById(R.id.emptyStateGroup)
        loadingProgress = root.findViewById(R.id.loadingDataProgress)
        emptyState.visibility = View.INVISIBLE
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
        user = FirebaseAuth.getInstance().currentUser
        getCodeListFromDatabase()
    }
    private fun getCodeListFromDatabase() {
        if (user != null) {
            val db = FirebaseFirestore.getInstance()
            db.collection("users")
                .document(user!!.uid).collection("codes")
                .get()
                .addOnSuccessListener {
                    val list: ArrayList<NukeCode> = ArrayList()
                    for(document in it) {
                        //var item = document.data
                        list
                            .add(NukeCode(
                                document.getString("code"),
                                document.getString("title"),
                                document.getString("categories"),
                                document.getString("image"))
                            )
                        Log.d("Fragment Codes","${document.data}")
                    }
                    nukeCodeAdapter.setList(list)
                    loadingProgress.visibility = View.GONE
                    nukeCodeAdapter.notifyDataSetChanged()
                    if (list.isEmpty()) {
                        emptyState.visibility = View.VISIBLE
                    }
                }
                .addOnFailureListener {
                    Log.d("Fragment Codes", "error getting data", it)
                }
        } else {
            loadingProgress.visibility = View.GONE
            emptyState.visibility = View.VISIBLE
        }
    }

    override fun onItemClicked(item: Any, position: Int, view: View) {
        //Toast.makeText(context,"dont work",Toast.LENGTH_SHORT).show()
        val x  = item as NukeCode
        val direction = CodesFragmentDirections.actionNavigationCodesToWebPreviewFragment(x.url!!)
        Navigation.findNavController(view).navigate(direction)
    }

    override fun onItemLongClicked(item: Any, position: Int, view: View) {
        val code = item as NukeCode
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle("Want to save to cloud DB?")
        builder.setMessage("the changes will be on your user data")
        builder.setPositiveButton("Backup", DialogInterface.OnClickListener { dialog, which ->
            val data = hashMapOf(
                "code" to code.nukeCode,
                "categories" to code.description,
                "createdAt" to Dates.today.toString("yyyy-MM-dd HH:mm:ss"),
                "image" to code.image,
                "title" to code.name,
                "source" to code.url
            )
            val query = Firebase.firestore.collection("users")
                .document(user!!.uid).collection("codes")
                .add(data)
                .addOnSuccessListener {
                    Log.d("CodesFragmentDialog","DocumentSnapshot written with ID: ${it.id}")
                    dialog.dismiss()
                }
                .addOnFailureListener { e ->
                    Log.w("CodesFragmentDialog", "Error adding document", e)
                }
        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
            dialog.cancel()
        })
        val dialog = builder.create()
        //dialog.show()
    }
}
