package com.valeriyu.hellovaleriy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val t = findViewById<TextView>(R.id.txt)
        t.text = """
    BuildType =  ${BuildConfig.BUILD_TYPE}  
    flavor = ${BuildConfig.FLAVOR}  
    buildType = ${BuildConfig.BUILD_TYPE}  
    versionCode = ${BuildConfig.VERSION_CODE}  
    versionName = ${BuildConfig.VERSION_NAME}  
    applicationId =
    ${BuildConfig.APPLICATION_ID}  
"""
    }
}
