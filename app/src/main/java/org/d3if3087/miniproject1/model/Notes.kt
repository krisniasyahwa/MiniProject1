package org.d3if3087.miniproject1.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Notes(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val location: String,
    val date: String,
    val story: String
)