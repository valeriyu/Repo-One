package com.valeriyu.permissionsanddate

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.*


const val TAG = "_permissionsanddate_inside"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (isGooglePlayServicesAvailable()) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.MainActivityContainer, LocationsListFragment(), "LIST")
                .commit()
        } else {
        }
    }

    private fun isGooglePlayServicesAvailable(): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val connectionResult = googleApiAvailability.isGooglePlayServicesAvailable(this)
        when (connectionResult) {
            ConnectionResult.SUCCESS -> return true
            ConnectionResult.SERVICE_INVALID -> {
                val dialog =
                    googleApiAvailability.getErrorDialog(
                        this,
                        connectionResult,
                        11
                    ) { finish() }
                dialog?.setCancelable(false)
                dialog?.show()
            }
            /*    ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED -> {
                    val dialog = googleApiAvailability.getErrorDialog(this, connectionResult, 0)
                    dialog?.show()
                }*/
            //ConnectionResult.SERVICE_MISSING,
            //ConnectionResult.SERVICE_DISABLED,
            else -> {
                val dialog = googleApiAvailability.getErrorDialog(this, connectionResult, 44)
                dialog?.show()
            }
        }
        return false
    }
}