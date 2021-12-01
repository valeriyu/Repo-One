package com.valeriyu.permissionsanddate

import android.location.Location
import org.threeten.bp.Instant


data class LocationTime(
        var id: Long = 0,
        var createdAt: Instant,
        var loccation: Location,
        val url: String = ""
)