package com.paryavarankavalu.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.paryavarankavalu.R
import com.paryavarankavalu.data.model.ReportStatus
import com.paryavarankavalu.data.model.WasteReport
import com.paryavarankavalu.data.model.UserRole
import com.paryavarankavalu.databinding.ItemReportBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReportListAdapter(
    private var userRole: UserRole = UserRole.USER,
    private val onMarkCleaned: (WasteReport) -> Unit,
    private val onItemClick: (WasteReport) -> Unit,
    private val onDeleteReport: (WasteReport) -> Unit
) : ListAdapter<WasteReport, ReportListAdapter.ViewHolder>(DIFF_CALLBACK) {

    fun setUserRole(role: UserRole) {
        this.userRole = role
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemReportBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(report: WasteReport) {
            binding.apply {
                tvWasteType.text = "${report.wasteType.emoji} ${report.wasteType.displayName}"
                tvDescription.text = report.description.ifEmpty { report.title }
                tvLocation.text = report.address.ifEmpty {
                    "Lat: ${"%.4f".format(report.latitude)}, Lng: ${"%.4f".format(report.longitude)}"
                }
                tvTimestamp.text = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
                    .format(Date(report.timestamp))

                // Status badge
                tvStatus.text = report.status.displayName
                
                // Admin Actions visibility
                val isCleaned = report.status == ReportStatus.CLEANED
                btnMarkCleaned.visibility = if (userRole == UserRole.ADMIN && !isCleaned) View.VISIBLE else View.GONE
                btnDelete.visibility = if (userRole == UserRole.ADMIN) View.VISIBLE else View.GONE

                when (report.status) {
                    ReportStatus.PENDING -> tvStatus.setBackgroundResource(R.drawable.bg_status_pending)
                    ReportStatus.IN_PROGRESS -> tvStatus.setBackgroundResource(R.drawable.bg_status_progress)
                    ReportStatus.VERIFIED -> tvStatus.setBackgroundResource(R.drawable.bg_status_verified)
                    ReportStatus.CLEANED -> tvStatus.setBackgroundResource(R.drawable.bg_status_cleaned)
                }

                tvKarmaPoints.text = "+${report.ecoKarmaPoints} pts"

                // Load photo
                if (report.photoPath.isNotEmpty() && File(report.photoPath).exists()) {
                    ivPhoto.visibility = View.VISIBLE
                    Glide.with(ivPhoto.context)
                        .load(report.photoPath)
                        .centerCrop()
                        .placeholder(R.drawable.ic_image_placeholder)
                        .into(ivPhoto)
                } else {
                    ivPhoto.visibility = View.GONE
                }

                btnMarkCleaned.setOnClickListener { onMarkCleaned(report) }
                btnDelete.setOnClickListener { onDeleteReport(report) }
                root.setOnClickListener { onItemClick(report) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<WasteReport>() {
            override fun areItemsTheSame(old: WasteReport, new: WasteReport) = old.id == new.id
            override fun areContentsTheSame(old: WasteReport, new: WasteReport) = old == new
        }
    }
}
