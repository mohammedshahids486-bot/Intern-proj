package com.paryavarankavalu.ui.map

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.paryavarankavalu.R
import com.paryavarankavalu.data.model.ReportStatus
import com.paryavarankavalu.data.model.WasteReport
import com.paryavarankavalu.databinding.ActivityMapBinding
import com.paryavarankavalu.viewmodel.ReportViewModel

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapBinding
    private val viewModel: ReportViewModel by viewModels()
    private var googleMap: GoogleMap? = null
    private val markerReportMap = HashMap<Marker, WasteReport>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Cleanliness Map"
        binding.toolbar.setNavigationOnClickListener { finish() }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupFilterChips()
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        map.uiSettings.apply {
            isZoomControlsEnabled = true
            isCompassEnabled = true
            isMyLocationButtonEnabled = true
        }

        // Map click to show info
        map.setOnMarkerClickListener { marker ->
            val report = markerReportMap[marker]
            report?.let {
                showReportBottomSheet(it)
            }
            false
        }

        observeReports()
    }

    private fun observeReports() {
        viewModel.allReports.observe(this) { reports ->
            updateMapMarkers(reports)
            updateLegendCounts(reports)
        }
    }

    private fun updateMapMarkers(reports: List<WasteReport>) {
        googleMap?.let { map ->
            map.clear()
            markerReportMap.clear()

            if (reports.isEmpty()) return

            val boundsBuilder = LatLngBounds.Builder()

            reports.forEach { report ->
                val position = LatLng(report.latitude, report.longitude)
                val markerColor = when (report.status) {
                    ReportStatus.PENDING -> BitmapDescriptorFactory.HUE_RED
                    ReportStatus.IN_PROGRESS -> BitmapDescriptorFactory.HUE_ORANGE
                    ReportStatus.VERIFIED -> BitmapDescriptorFactory.HUE_YELLOW
                    ReportStatus.CLEANED -> BitmapDescriptorFactory.HUE_GREEN
                }

                val marker = map.addMarker(
                    MarkerOptions()
                        .position(position)
                        .title("${report.wasteType.emoji} ${report.wasteType.displayName}")
                        .snippet("${report.status.displayName} • ${report.address.take(40)}")
                        .icon(BitmapDescriptorFactory.defaultMarker(markerColor))
                )
                marker?.let { markerReportMap[it] = report }
                boundsBuilder.include(position)
            }

            try {
                val bounds = boundsBuilder.build()
                map.animateCamera(
                    CameraUpdateFactory.newLatLngBounds(bounds, 100),
                    1000, null
                )
            } catch (e: Exception) {
                // Single point - zoom in directly
                reports.firstOrNull()?.let {
                    map.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(it.latitude, it.longitude), 14f
                        )
                    )
                }
            }
        }
    }

    private fun showReportBottomSheet(report: WasteReport) {
        binding.bottomSheet.apply {
            root.visibility = View.VISIBLE
            tvBsWasteType.text = "${report.wasteType.emoji} ${report.wasteType.displayName}"
            tvBsStatus.text = report.status.displayName
            tvBsAddress.text = report.address.ifEmpty {
                "Lat: ${"%.5f".format(report.latitude)}, Lng: ${"%.5f".format(report.longitude)}"
            }
            tvBsCoords.text = "📍 ${"%.5f".format(report.latitude)}, ${"%.5f".format(report.longitude)}"

            when (report.status) {
                ReportStatus.CLEANED -> btnBsMarkCleaned.visibility = View.GONE
                else -> {
                    btnBsMarkCleaned.visibility = View.VISIBLE
                    btnBsMarkCleaned.setOnClickListener {
                        viewModel.markAsCleaned(report.id)
                        Toast.makeText(this@MapActivity, "✅ Marked as Cleaned!", Toast.LENGTH_SHORT).show()
                        root.visibility = View.GONE
                    }
                }
            }

            btnBsClose.setOnClickListener { root.visibility = View.GONE }
        }
    }

    private fun updateLegendCounts(reports: List<WasteReport>) {
        val pending = reports.count { it.status == ReportStatus.PENDING }
        val cleaned = reports.count { it.status == ReportStatus.CLEANED }
        binding.tvLegendPending.text = "🔴 Pending ($pending)"
        binding.tvLegendCleaned.text = "🟢 Cleaned ($cleaned)"
    }

    private fun setupFilterChips() {
        binding.chipAll.setOnClickListener {
            viewModel.allReports.value?.let { updateMapMarkers(it) }
        }
        binding.chipPending.setOnClickListener {
            viewModel.allReports.value?.let { reports ->
                updateMapMarkers(reports.filter { it.status == ReportStatus.PENDING })
            }
        }
        binding.chipCleaned.setOnClickListener {
            viewModel.allReports.value?.let { reports ->
                updateMapMarkers(reports.filter { it.status == ReportStatus.CLEANED })
            }
        }
    }
}
