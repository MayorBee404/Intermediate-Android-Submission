package com.example.storyapplication.view.dashboard.googlemaps

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController

import com.example.storyapplication.R
import com.example.storyapplication.ViewModelFactory
import com.example.storyapplication.databinding.MapsFragmentBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    private var _binding: MapsFragmentBinding? = null
    private lateinit var factory: ViewModelFactory
    private val mapsViewModel: MapsViewModel by activityViewModels { factory }

    private val binding get() = _binding!!

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        googleMap.setPadding(0, 0, 0, 180)
        googleMap.uiSettings.isCompassEnabled = true
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isMapToolbarEnabled = true
        googleMap.uiSettings.isIndoorLevelPickerEnabled = true

        mapsViewModel.myLocationPermission.observe(viewLifecycleOwner) {
            googleMap.isMyLocationEnabled = it

            setMapStyle(googleMap)
        }

        mapsViewModel.userStories.observe(viewLifecycleOwner) {
            Log.e("MapsFragment", "userStories: $it")
            it?.let {
                for (story in it) {
                    val lat: Double = story.lat!!
                    val lon: Double = story.lon!!

                    val latLng = LatLng(lat, lon)
                    googleMap.addMarker(MarkerOptions().position(latLng).title(story.name))?.tag =
                        story.id
                }
                val latLng = LatLng(it[0].lat!!, it[0].lon!!)
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))
            }
        }

        mapsViewModel.getMapType().observe(viewLifecycleOwner) {
            when (it) {
                MapType.NORMAL -> setMapType(googleMap, MapType.NORMAL)
                MapType.SATELLITE -> setMapType(googleMap, MapType.SATELLITE)
                MapType.TERRAIN -> setMapType(googleMap, MapType.TERRAIN)
                else -> setMapType(googleMap, MapType.NORMAL)
            }
        }


        googleMap.setOnMarkerClickListener { marker ->
            val story = mapsViewModel.userStories.value?.find { it.id == marker.tag }
            story?.let {
                val latLog = LatLng(it.lat!!, it.lon!!)
                val toDialogDetailStoryFragment =
                    MapsFragmentDirections.actionNavigationMapsToDetailMapsFragment2(
                        it.image,
                        it.description,
                        it.name
                    )
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLog, 10f))
                findNavController().navigate(toDialogDetailStoryFragment)
            }
            true
        }
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mapsViewModel.setMyLocationPermission(true)
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = MapsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        factory = ViewModelFactory.getInstance(requireActivity())
        getMyLocation()


        binding.toolbar.inflateMenu(R.menu.map_options)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.normal_type-> {
                    mapsViewModel.saveMapType(MapType.NORMAL)
                    Toast.makeText(context, getString(R.string.normal_type), Toast.LENGTH_SHORT).show()
                }
                R.id.satellite_type-> {
                    mapsViewModel.saveMapType(MapType.SATELLITE)
                    Toast.makeText(context,getString(R.string.satellite_type),Toast.LENGTH_SHORT).show()
                }
                R.id.terrain_type-> {
                    mapsViewModel.saveMapType(MapType.TERRAIN)
                    Toast.makeText(context,getString(R.string.terrain_type),Toast.LENGTH_SHORT).show()
                }
            }
            true
        }


        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        fetchUserStories()
        mapsViewModel.message.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }


    private fun fetchUserStories() {
        mapsViewModel.getUserToken().observe(viewLifecycleOwner) {
            mapsViewModel.getUserStoriesWithLocation(it)
            Log.e("MapView", "Token: $it")
        }
    }

    private fun setMapType(mMap: GoogleMap, mapType: MapType) {
        when (mapType) {
            MapType.NORMAL -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            }
            MapType.SATELLITE -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
            }
            MapType.TERRAIN -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
            }
        }
    }
    private fun setMapStyle(mMap: GoogleMap){
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireActivity(), R.raw.map_style_silver))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        OnMapReadyCallback {
            it.clear()
        }
    }
}