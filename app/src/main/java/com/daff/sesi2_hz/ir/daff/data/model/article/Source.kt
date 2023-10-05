package com.daff.sesi2_hz.ir.daff.data.model.article


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
@Entity(tableName = "source")
data class Source(
    @PrimaryKey(autoGenerate = true) var idx: Long = 0,
    @ColumnInfo("id")
    @SerialName("id")
    var id: String? = "",
    @ColumnInfo("name")
    @SerialName("name")
    var name: String? = ""
): Parcelable