package org.easy.gemini.database.dao

import androidx.room.Insert

interface BaseDao<Data> {
    @Insert
    suspend fun insert(vararg data: Data)
}