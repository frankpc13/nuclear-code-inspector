package com.shibuyaxpress.nuclearcodeinspector.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import coil.api.load
import coil.transform.CircleCropTransformation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.ktx.Firebase
import com.shibuyaxpress.nuclearcodeinspector.R

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var rootLayout: View
    private lateinit var groupLogin: Group
    private lateinit var groupProfile: Group
    private lateinit var buttonSignIn: SignInButton
    private lateinit var imageProfile: ImageView
    private lateinit var codesText: TextView
    private lateinit var usernameText: TextView
    private lateinit var regDateText: TextView
    private var user: FirebaseUser? = null

    val TAG = DashboardFragment::class.java.simpleName
    val RC_SIGN_IN: Int = 2020

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        rootLayout = inflater.inflate(R.layout.fragment_dashboard, container, false)
        groupLogin = rootLayout.findViewById(R.id.loginGroup)
        groupProfile = rootLayout.findViewById(R.id.profileGroup)
        buttonSignIn = rootLayout.findViewById(R.id.sign_in_button)

        imageProfile = rootLayout.findViewById(R.id.imageProfile)
        usernameText = rootLayout.findViewById(R.id.usernameText)
        codesText = rootLayout.findViewById(R.id.textCodesAmount)

        groupProfile.visibility = View.INVISIBLE
        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(context!!, gso)

        buttonSignIn.setOnClickListener{
            signIn()
        }
        return rootLayout
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar_dashboard, menu)
        if (user == null){
            menu.findItem(R.id.top_logout).isVisible = false
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.top_about -> {
            //go to about app
            true
        }
        R.id.top_logout -> {
            googleSignInClient.signOut()
            //sign out user
            FirebaseAuth.getInstance().signOut()
            activity!!.finishAffinity()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun fillUserData(user:FirebaseUser?){
        imageProfile.load(user!!.photoUrl){
            crossfade(true)
            placeholder(R.drawable.profile)
            error(R.drawable.profile)
            transformations(CircleCropTransformation())
        }
        usernameText.text = "${user.displayName}"
    }

    private fun updateUI(user: FirebaseUser?){
        this.user = user
        if (user != null){
            groupLogin.visibility = View.GONE
            groupProfile.visibility = View.VISIBLE
            //fill user data
            fillUserData(user)
        } else {
            Toast.makeText(context!!, "Necesita logearse :3", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        //val user = GoogleSignIn.getLastSignedInAccount(context!!)
        updateUI(currentUser)
    }

    private fun signIn(){
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle "+acct.id!!)
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    //success
                    Log.d(TAG,"signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", it.exception)
                    Snackbar.make(rootLayout, "Authentication failed.", Snackbar.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

}
