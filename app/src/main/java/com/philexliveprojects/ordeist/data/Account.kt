package com.philexliveprojects.ordeist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "accounts",
    indices = [
        Index(value = ["email"], unique = true),
        Index(value = ["phone_number"], unique = true)
    ]
)
data class Account(
    @PrimaryKey(autoGenerate = true) @ColumnInfo("id") val id: Int = 0,
    @ColumnInfo("first_name") val firstName: String,
    @ColumnInfo("second_name") val secondName: String,
    @ColumnInfo("phone_number") val phoneNumber: String,
    @ColumnInfo("email") val email: String,
    @ColumnInfo("password") val password: String
)
