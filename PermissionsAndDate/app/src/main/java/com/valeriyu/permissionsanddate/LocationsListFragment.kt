package com.valeriyu.permissionsanddate

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.valeriyu.permissionsanddate.databinding.FragmentLocationsListBinding
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import kotlin.random.Random

const val LOGTAG = "_permissionsanddate_inside"

@SuppressLint("LongLogTag")

class LocationsListFragment : Fragment(R.layout.fragment_locations_list) {

    private  var binding: FragmentLocationsListBinding? = null


    private lateinit var mLocationRequest: LocationRequest
    private var mLastLocation: Location? = null

    private val UPDATE_INTERVAL = (10 * 1000).toLong() /* 10 secs */
    private val FASTEST_INTERVAL: Long = 5 * 1000 /* 2 sec */


    private var rationaleDialog: AlertDialog? = null

    private var permissionStatus: Int = 0
    private var locTimes: List<LocationTime> = listOf()
    private var locationsAdaper: LocationsAdapter? = null
    private var selectedInstant: Instant? = null


    private val urls = listOf(
        "https://krivoe-zerkalo.ru/images/thumbnails/images/2018_1/IVmheKXegrY-fit-1200x788.jpg",
        "https://i0.wp.com/dronomania.ru/wp-content/uploads/2016/12/%D0%94%D1%80%D0%BE%D0%BD%D1%8B-USA.png",
        "https://avatars.mds.yandex.net/get-zen_doc/52716/pub_59f0d0057ddde894b72cd06f_59f0db244bf1610270f47a98/scale_1200",
        "https://s3-eu-west-1.amazonaws.com/files.surfory.com/uploads/2015/3/30/54dce0da1f395de3098b463f/55195dc61f395d0c0e8b4614.jpg",
        "https://anband.spb.ru/images/200/DSC100222318.jpg",
        "https://www.tripsoul.ru/Destinations/IMG_Australia-Oceania/Australia/Melbourne/Melbourne_02.jpg",
        "https://mykaleidoscope.ru/uploads/posts/2020-01/1579933117_30-p-shokoladnie-torti-71.jpg",
        "https://i.pinimg.com/736x/02/cc/77/02cc771415ccb6f9825f88288ccd78bc--owl.jpg",
        "https://cdn.pixabay.com/photo/2019/10/02/07/50/rhino-4520317_1280.jpg"
    )

    private fun startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = LocationRequest.create()
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        mLocationRequest.setInterval(UPDATE_INTERVAL)
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL)

        // Create LocationSettingsRequest object using location request
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)
        val locationSettingsRequest = builder.build()

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        val settingsClient = LocationServices.getSettingsClient(requireContext())
        settingsClient.checkLocationSettings(locationSettingsRequest)

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        LocationServices.getFusedLocationProviderClient(requireContext()).requestLocationUpdates(
            mLocationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    // do work here
                    onLocationChanged(locationResult.lastLocation)
                }
            },
            //Looper.myLooper()
            Looper.getMainLooper()
        )
    }

    private fun onLocationChanged(location: Location) {

        if (mLastLocation == null) {
            mLastLocation = location
            return
        }

        //if (location === lastLocation) {
        if (location.latitude == mLastLocation?.latitude &&
            location.longitude == mLastLocation?.longitude &&
            location.altitude == mLastLocation?.altitude
        ) {
            return
        }
        var newLT = LocationTime(
            id = Random.nextLong(),
            createdAt = Instant.now(),
            url = urls.random(),
            loccation = location
        )

        locTimes += listOf(newLT)
        locationsAdaper?.submitList(locTimes)

        Handler()
            .postDelayed(Runnable {
                binding!!.locationsList.scrollToPosition(locTimes.size - 1)
            }, 100)

        mLastLocation = location
        updStatus()
    }


    private fun getLastLocation() {
        // Get last known recent location using new Google Play Services SDK (v11+)
        val locationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationClient.lastLocation
            .addOnSuccessListener { location -> // GPS location can be null if GPS is switched off
                location?.let { onLocationChanged(it) }
            }
            .addOnFailureListener { e ->
                Log.d(LOGTAG, "Error trying to get last GPS location")
                e.printStackTrace()
            }
    }

    private fun initTimePicker(position: Int) {
        val itemDateTime =
            LocalDateTime.ofInstant(locTimes[position].createdAt, ZoneId.systemDefault())

        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                TimePickerDialog(
                    requireContext(),
                    { _, hourOfDay, minute ->
                        val zonedDateTime =
                            LocalDateTime.of(year, month + 1, dayOfMonth, hourOfDay, minute)
                                .atZone(ZoneId.systemDefault())

                        selectedInstant = zonedDateTime.toInstant()

                        if (selectedInstant != null) {
                            locTimes[position].createdAt = selectedInstant as Instant
                            locationsAdaper?.notifyItemChanged(position)
                        }
                    },
                    itemDateTime.hour,
                    itemDateTime.minute,
                    true
                )
                    .show()
            },
            itemDateTime.year,
            itemDateTime.month.value - 1,
            itemDateTime.dayOfMonth
        )
            .show()
    }

    private fun requestLocationPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_CODE
        )
    }

    private fun showLocationRationaleDialog() {
        rationaleDialog = AlertDialog.Builder(requireContext())
            .setMessage("Необходимо одобрение разрешения для отображения информации по локации")
            .setPositiveButton("OK", { _, _ -> requestLocationPermission() })
            .setNegativeButton("Отмена", null)
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            chStatus()
        }
    }


    fun addLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
            return
        }

        LocationServices.getFusedLocationProviderClient(requireContext())
            .lastLocation
            .addOnSuccessListener {
                it?.let {
                    var newLT = LocationTime(
                        id = Random.nextLong(),
                        createdAt = Instant.now(),
                        url = urls.random(),
                        loccation = it
                    )
                    locTimes += listOf(newLT)
                    locationsAdaper?.submitList(locTimes)
                    Handler()
                        .postDelayed(Runnable {
                            binding!!.locationsList.scrollToPosition(locTimes.size - 1)
                        }, 100)
                    updStatus()
                } ?: toast("Локация отсутствует")
            }
            .addOnCanceledListener {
                toast("Запрос локации был отменен")
            }
            .addOnFailureListener {
                toast("Запрос локации завершился неудачно")
            }
    }

    /* var mLocationCallback: LocationCallback = object : LocationCallback() {
         override fun onLocationResult(locationResult: LocationResult) {
             for (location in locationResult.locations) {
                 Log.i(
                     "MainActivity",
                     "Location: " + location.latitude.toString() + " " + location.longitude
                 )
             }
         }
     }*/

    fun initList() {
        locationsAdaper = LocationsAdapter({ position -> initTimePicker(position) })
        with(binding!!.locationsList) {
            layoutManager = LinearLayoutManager(context)
            adapter = locationsAdaper
            setHasFixedSize(true)
        }
        updStatus()

        binding!!.addLocationButton.setOnClickListener({
            addLocation()
        })

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startLocationUpdates()
        }
    }

    private fun updStatus() {
        binding!!.statusTextView.isVisible = locTimes.isEmpty()
    }

    fun chStatus() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            binding?.statusTextView!!.text = "Нет локаций\n для отображения"
            binding?.addLocationButton!!.text = "ПОЛУЧИТЬ ЛОКАЦИЮ"
            permissionStatus = 1

        } else {
            binding?.statusTextView!!.text   =
                "Для отображения\n списка локаций\n необходимо разрешение !"
            binding!!.addLocationButton.text = "РАЗРЕШИТЬ"
            permissionStatus = 0
        }
        updStatus()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        chStatus()
        initList()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_locations_list, container, false)

        binding = FragmentLocationsListBinding.inflate(inflater, container, false)
        val view = binding!!.root
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        locationsAdaper = null
        binding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 7374

        @JvmStatic
        fun newInstance() =
            LocationsListFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}