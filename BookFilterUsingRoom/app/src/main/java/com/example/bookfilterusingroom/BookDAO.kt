package com.example.bookfilterusingroom

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
@Dao
interface BookDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun InsertBooks(bookdetails: Book)
}