package com.example.bookfilterusingroom

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "BooksDetail",  indices = arrayOf(Index(value = ["title"],unique=true)), foreignKeys = [ForeignKey(entity = Authors::class,parentColumns = arrayOf("Aid"), childColumns = arrayOf("aid"), onDelete = ForeignKey.CASCADE)])
data class Book(@PrimaryKey(autoGenerate = true) var BookID:Int=0,
                var aid:Int,
                var imageLink: String,
                var language: String,
                var link:String,
                var pages: Int,
                var title: String,
                var year: Int,)
