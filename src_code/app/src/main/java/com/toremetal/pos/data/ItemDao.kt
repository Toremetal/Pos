/*
 *    ™T©ReMeTaL Pos.
 *    Copyright (C) 2021™T©ReMeTaL.
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *
 *    Some functionality created with modified (code) lessons provided by:
 *    The Android Open Source Project.
 *    Copyright (C) 2021 The Android Open Source Project.
 *    Licensed under the Apache License, Version 2.0 (the "License").
 *
 *    You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 *   ************************************************************************
 *     ItemDao.kt : Copyright (c) 2021™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David W. Rick
 *      Date: 12/14/21, 10:32 AM
 *      Program Name: Pos
 *      File: ItemDao.kt
 *      Last Modified: 12/13/21, 1:34 PM
 *   ************************************************************************
 */
package com.toremetal.pos.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 *  Database access object to access the inventory database
 */
@Dao
interface ItemDao {

    /**
     *  Retrieve the list of items from the database.
     */
    @Query("SELECT * from item ORDER BY name ASC")
    fun getItems(): Flow<List<Item>>

    /**
     *  Retrieve a specified item from the database.
     */
    @Query("SELECT * from item WHERE id = :id")
    fun getItem(id: Int): Flow<Item>

    /**
     *  Specify the conflict strategy as IGNORE, if the user tries to add an
     *   existing Item into the database Room ignores the conflict.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    /**
     *  Update the item information contained in the database.
     */
    @Update
    suspend fun update(item: Item)

    /**
     *  Delete the item information contained in the database.
     */
    @Delete
    suspend fun delete(item: Item)


}
