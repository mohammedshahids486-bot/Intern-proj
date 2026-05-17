package com.paryavarankavalu.ui.profile

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.paryavarankavalu.data.model.EcoLevel
import com.paryavarankavalu.databinding.ActivityProfileBinding
import com.paryavarankavalu.viewmodel.ReportViewModel

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val viewModel: ReportViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "My Eco Profile"
        binding.toolbar.setNavigationOnClickListener { finish() }

        observeProfile()
        observeStats()
    }

    private fun observeProfile() {
        viewModel.userProfile.observe(this) { profile ->
            profile?.let {
                binding.tvUserName.text = it.name
                binding.tvLevel.text = "${it.level.emoji} ${it.level.title}"
                binding.tvKarmaPoints.text = "${it.ecoKarmaPoints}"
                binding.tvTotalReports.text = "${it.totalReports}"
                binding.tvCleanedReports.text = "${it.cleanedReports}"

                // Update Role Toggle Button
                val isUser = it.role == com.paryavarankavalu.data.model.UserRole.USER
                binding.btnToggleRole.text = if (isUser) "Switch to Admin Mode" else "Switch to User Mode"
                binding.btnToggleRole.setOnClickListener { _ ->
                    viewModel.toggleUserRole(it.role)
                }

                // Progress to next level
                val nextLevel = EcoLevel.values().firstOrNull { lvl -> lvl.minPoints > it.ecoKarmaPoints }
                if (nextLevel != null) {
                    val current = it.level.minPoints
                    val next = nextLevel.minPoints
                    val progress = ((it.ecoKarmaPoints - current).toFloat() / (next - current) * 100).toInt()
                    binding.progressLevel.progress = progress
                    binding.tvNextLevel.text = "Next: ${nextLevel.emoji} ${nextLevel.title} (${nextLevel.minPoints - it.ecoKarmaPoints} pts away)"
                } else {
                    binding.progressLevel.progress = 100
                    binding.tvNextLevel.text = "🏆 Maximum Level Achieved!"
                }

                // Level achievements
                setupLevelMilestones(it.ecoKarmaPoints)
            }
        }
    }

    private fun observeStats() {
        viewModel.allReports.observe(this) { reports ->
            val byType = reports.groupBy { it.wasteType }
            val topType = byType.maxByOrNull { it.value.size }
            binding.tvTopWasteType.text = topType?.let {
                "${it.key.emoji} ${it.key.displayName} (${it.value.size})"
            } ?: "No reports yet"
        }
    }

    private fun setupLevelMilestones(currentPoints: Int) {
        val levels = EcoLevel.values()
        binding.tvMilestones.text = levels.joinToString("\n") { level ->
            val achieved = currentPoints >= level.minPoints
            val check = if (achieved) "✅" else "⬜"
            "$check ${level.emoji} ${level.title} — ${level.minPoints} pts"
        }
    }
}
