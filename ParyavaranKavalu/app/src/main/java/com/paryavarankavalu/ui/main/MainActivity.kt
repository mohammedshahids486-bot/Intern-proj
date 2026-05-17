package com.paryavarankavalu.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.paryavarankavalu.databinding.ActivityMainBinding
import com.paryavarankavalu.ui.map.MapActivity
import com.paryavarankavalu.ui.profile.ProfileActivity
import com.paryavarankavalu.ui.report.ReportActivity
import com.paryavarankavalu.viewmodel.ReportViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: ReportViewModel by viewModels()
    private lateinit var adapter: ReportListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupObservers()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        adapter = ReportListAdapter(
            onMarkCleaned = { report -> viewModel.markAsCleaned(report.id) },
            onItemClick = { /* optional: detail view */ },
            onDeleteReport = { report -> viewModel.deleteReport(report) }
        )
        binding.rvReports.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            this.adapter = this@MainActivity.adapter
        }
    }

    private fun setupObservers() {
        viewModel.allReports.observe(this) { reports ->
            adapter.submitList(reports)
            binding.tvEmptyState.visibility =
                if (reports.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
            binding.tvTotalReports.text = "${reports.size} Reports"
            val cleaned = reports.count { it.status.name == "CLEANED" }
            binding.tvCleanedCount.text = "$cleaned Cleaned"
        }

        viewModel.userProfile.observe(this) { profile ->
            profile?.let {
                binding.tvKarmaPoints.text = "${it.ecoKarmaPoints} Eco-Karma"
                binding.tvUserLevel.text = "${it.level.emoji} ${it.level.title}"
                adapter.setUserRole(it.role)
            }
        }
    }

    private fun setupClickListeners() {
        binding.fabNewReport.setOnClickListener {
            startActivity(Intent(this, ReportActivity::class.java))
        }
        binding.btnViewMap.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
        }
        binding.btnProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        binding.cardMap.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
        }
    }
}
