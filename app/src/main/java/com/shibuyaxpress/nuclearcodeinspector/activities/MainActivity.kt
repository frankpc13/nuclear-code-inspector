package com.shibuyaxpress.nuclearcodeinspector.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.shibuyaxpress.nuclearcodeinspector.R

class MainActivity : AppCompatActivity() {

    private var PRIVATE_MODE = 0
    private val PREF_NAME = "nuclear-code-inspector"
    private val SPLASH_TIME_OUT : Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        goToMain()
    }

    fun goToMain() {
        Handler().postDelayed({
            startActivity(Intent(this, MenuActivity::class.java))
            //close this activity
            finish()
        }, SPLASH_TIME_OUT)
    }
}
