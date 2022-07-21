package com.zerox.randomuserapp.ui.view

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.squareup.picasso.Picasso
import com.zerox.randomuserapp.BuildConfig
import com.zerox.randomuserapp.R
import com.zerox.randomuserapp.data.model.entities.user.User
import com.zerox.randomuserapp.databinding.ActivityUserDetailsBinding
import com.zerox.randomuserapp.ui.view_model.UserViewModel
import com.zerox.randomuserapp.ui.view_model.exceptions.FailedApiResponseException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserDetailsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityUserDetailsBinding
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var userEmail: String
    private lateinit var context: Context
    private var userPage = 0
    private lateinit var user: User

    private var map: GoogleMap? = null
    private var cameraPosition: CameraPosition? = null

    // The entry point to the Places API.
    private lateinit var placesClient: PlacesClient

    // The entry point to the Fused Location Provider.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // location retrieved by the Fused Location Provider.
    private var lastKnownLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.title = resources.getString(R.string.details_activity_title)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        userEmail = intent.extras!!.getString("user_email").toString()
        userPage = intent.extras!!.getInt("user_page")
        context = this
        userViewModel
        loadData()
        userViewModel.userModel.observe(this, Observer {
            user = it!!
            showDetails()
            setUserLocation()
        })
        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }
        // Construct a PlacesClient
        Places.initialize(applicationContext, BuildConfig.MAPS_API_KEY)
        placesClient = Places.createClient(this)

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.frag_map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    private fun showDetails() {
        binding.pbLoadingUser.visibility = View.GONE
        binding.cvUser.visibility = View.VISIBLE
        binding.tvUserName.text = user.name.title + ". " + user.name.first + " " + user.name.last
        binding.tvUserEmail.text = user.email
        Picasso.get().load(user.picture.large).into(binding.ivUserImage)
    }

    private fun loadData() {
        binding.pbLoadingUser.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            try {
                userViewModel.getUserByEmail(
                    "?results=50&seed=abc&page=$userPage&inc=name,email,picture,location",
                    userEmail
                )
            } catch (exception: Exception) {
                runOnUiThread {
                    Toast.makeText(context,exception.message,Toast.LENGTH_SHORT).show()
                    Picasso.get().load(R.drawable.user_not_found).into(binding.ivUserImage)
                    binding.pbLoadingUser.visibility = View.GONE
                    binding.cvUser.visibility = View.VISIBLE
                    binding.tvUserName.text = resources.getString(R.string.user_not_found)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    // methods for google maps
    override fun onSaveInstanceState(outState: Bundle) {
        map?.let { map ->
            outState.putParcelable(KEY_CAMERA_POSITION, map.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastKnownLocation)
        }
        super.onSaveInstanceState(outState)
    }

    private fun setUserLocation() {
        map?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    user.location.coordinates.latitude,
                    user.location.coordinates.longitude
                ), DEFAULT_ZOOM.toFloat()
            )
        )
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
    }

    companion object {
        private val TAG = UserDetailsActivity::class.java.simpleName
        private const val DEFAULT_ZOOM = 15

        // Keys for storing activity state.
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
    }
}