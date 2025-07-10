package com.philexliveprojects.ordeist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo("category") val category: String,
    @ColumnInfo("client_name") val clientName: String,
    @ColumnInfo("phone_number") val phoneNumber: String?,
    @ColumnInfo("email") val email: String?,
    @ColumnInfo("date") val date: String
)