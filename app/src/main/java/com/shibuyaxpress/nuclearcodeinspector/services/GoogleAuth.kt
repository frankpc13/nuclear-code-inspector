package com.shibuyaxpress.nuclearcodeinspector.services

import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.shibuyaxpress.nuclearcodeinspector.R

class GoogleAuth {

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(getString(R.string.default_web_client_id))
        .requestEmail()
        .build()
}