package com.example.asd.todos

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Todo::class], version = 6)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}