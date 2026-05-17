package com.paryavarankavalu.ui.report

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.paryavarankavalu.R
import com.paryavarankavalu.data.model.WasteReport
import com.paryavarankavalu.data.model.WasteType
import com.paryavarankavalu.databinding.ActivityReportBinding
import com.paryavarankavalu.utils.ImageUtils
import com.paryavarankavalu.utils.LocationHelper
import com.paryavarankavalu.viewmodel.ReportViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class ReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportBinding
    private val viewModel: ReportViewModel by viewModels()
    private lateinit var locationHelper: LocationHelper

    private var currentLat = 0.0
    private var currentLng = 0.0
    private var currentAddress = ""
    private var photoPath = ""
    private var cameraImageUri: Uri? = null
    private var selectedWasteType = WasteType.MIXED

    // Permission launchers
    private val locationPermLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        if (perms[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            fetchLocation()
        } else {
            Toast.makeText(this, "Location permission needed for geo-tagging", Toast.LENGTH_SHORT).show()
        }
    }

    private val cameraPermLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) launchCamera() else
            Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
    }

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            cameraImageUri?.let { uri ->
                CoroutineScope(Dispatchers.IO).launch {
                    val compressed = ImageUtils.compressFromFilePath(
                        uri.path ?: return@launch
                    )
                    photoPath = compressed
                    withContext(Dispatchers.Main) { showPhotoPreview(compressed) }
                }
            }
        }
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            CoroutineScope(Dispatchers.IO).launch {
                val compressed = ImageUtils.compressImage(this@ReportActivity, it)
                    ?: return@launch
                photoPath = compressed
                withContext(Dispatchers.Main) { showPhotoPreview(compressed) }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationHelper = LocationHelper(this)

        setupToolbar()
        setupWasteTypeSpinner()
        setupClickListeners()
        setupObservers()
        requestLocationAndFetch()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "New Waste Report"
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupWasteTypeSpinner() {
        val types = WasteType.values().map { "${it.emoji} ${it.displayName}" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerWasteType.adapter = adapter
        binding.spinnerWasteType.setSelection(WasteType.MIXED.ordinal)
        binding.spinnerWasteType.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, pos: Int, id: Long) {
                selectedWasteType = WasteType.values()[pos]
            }
            override fun onNothingSelected(p: android.widget.AdapterView<*>?) {}
        }
    }

    private fun setupClickListeners() {
        binding.btnTakePhoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
                launchCamera()
            } else {
                cameraPermLauncher.launch(Manifest.permission.CAMERA)
            }
        }

        binding.btnGallery.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        binding.btnRefreshLocation.setOnClickListener {
            fetchLocation()
        }

        binding.btnSubmitReport.setOnClickListener {
            submitReport()
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(this) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            binding.btnSubmitReport.isEnabled = !loading
        }

        viewModel.insertResult.observe(this) { id ->
            if (id != null && id > 0) {
                Toast.makeText(this, "✅ Report submitted! +10 Eco-Karma", Toast.LENGTH_LONG).show()
                viewModel.clearInsertResult()
                finish()
            }
        }

        viewModel.errorMessage.observe(this) { msg ->
            if (msg != null) {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                viewModel.clearError()
            }
        }
    }

    private fun requestLocationAndFetch() {
        if (locationHelper.hasLocationPermission()) {
            fetchLocation()
        } else {
            locationPermLauncher.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        }
    }

    private fun fetchLocation() {
        binding.tvLocationStatus.text = "📍 Fetching location..."
        binding.tvCoordinates.text = ""
        binding.progressLocation.visibility = View.VISIBLE

        locationHelper.getLastKnownLocation { locationData ->
            binding.progressLocation.visibility = View.GONE
            if (locationData != null) {
                currentLat = locationData.latitude
                currentLng = locationData.longitude
                currentAddress = locationData.address
                binding.tvLocationStatus.text = "📍 ${locationData.address}"
                binding.tvCoordinates.text =
                    "Lat: ${"%.6f".format(locationData.latitude)}  |  Lng: ${"%.6f".format(locationData.longitude)}"
            } else {
                binding.tvLocationStatus.text = "⚠️ Could not get location"
                // Try full location request
                CoroutineScope(Dispatchers.IO).launch {
                    val loc = locationHelper.getCurrentLocation()
                    withContext(Dispatchers.Main) {
                        if (loc != null) {
                            currentLat = loc.latitude
                            currentLng = loc.longitude
                            currentAddress = loc.address
                            binding.tvLocationStatus.text = "📍 ${loc.address}"
                            binding.tvCoordinates.text =
                                "Lat: ${"%.6f".format(loc.latitude)}  |  Lng: ${"%.6f".format(loc.longitude)}"
                        }
                    }
                }
            }
        }
    }

    private fun launchCamera() {
        val photoFile = ImageUtils.createImageFile(this)
        val uri = FileProvider.getUriForFile(
            this, "${packageName}.fileprovider", photoFile
        )
        cameraImageUri = uri
        cameraLauncher.launch(uri)
    }

    private fun showPhotoPreview(path: String) {
        binding.ivPhotoPreview.visibility = View.VISIBLE
        binding.tvPhotoSize.visibility = View.VISIBLE
        com.bumptech.glide.Glide.with(this).load(File(path)).centerCrop().into(binding.ivPhotoPreview)
        binding.tvPhotoSize.text = "Photo size: ${ImageUtils.getFileSizeKB(path)} KB ✓"
    }

    private fun submitReport() {
        val title = binding.etTitle.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()

        if (title.isEmpty()) {
            binding.etTitle.error = "Please enter a title"
            binding.etTitle.requestFocus()
            return
        }
        if (currentLat == 0.0 && currentLng == 0.0) {
            Toast.makeText(this, "Please wait for location to be fetched", Toast.LENGTH_SHORT).show()
            return
        }

        val report = WasteReport(
            title = title,
            description = description,
            wasteType = selectedWasteType,
            latitude = currentLat,
            longitude = currentLng,
            address = currentAddress,
            photoPath = photoPath
        )
        viewModel.submitReport(report)
    }
}
