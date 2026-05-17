package com.paryavarankavalu.data.local

import androidx.room.TypeConverter
import com.paryavarankavalu.data.model.EcoLevel
import com.paryavarankavalu.data.model.ReportStatus
import com.paryavarankavalu.data.model.WasteType

class Converters {
    @TypeConverter
    fun fromWasteType(value: WasteType): String = value.name

    @TypeConverter
    fun toWasteType(value: String): WasteType = WasteType.valueOf(value)

    @TypeConverter
    fun fromReportStatus(value: ReportStatus): String = value.name

    @TypeConverter
    fun toReportStatus(value: String): ReportStatus = ReportStatus.valueOf(value)

    @TypeConverter
    fun fromEcoLevel(value: EcoLevel): String = value.name

    @TypeConverter
    fun toEcoLevel(value: String): EcoLevel = EcoLevel.valueOf(value)
}
