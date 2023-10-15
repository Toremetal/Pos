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
 *     Item.kt : Copyright (c) 2021™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David W. Rick
 *      Date: 12/14/21, 10:32 AM
 *      Program Name: Pos
 *      File: Item.kt
 *      Last Modified: 11/29/21, 9:18 PM
 *   ************************************************************************
 */
package com.toremetal.pos.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.NumberFormat

/**
 * Entity data class represents a single row in the database.
 */
@Entity
data class Item(
    /**
     *  Primary key to identify items in the database.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /**
     *  Identifier for the name value of items.
     */
    @ColumnInfo(name = "name")
    val itemName: String,

    /**
     *  Identifier for the description value of items.
     */
    @ColumnInfo(name = "description")
    val itemDes: String,

    /**
     *  Identifier for the price value of items.
     */
    @ColumnInfo(name = "price")
    val itemPrice: Double,

    /**
     *  Identifier for the Quantity value of items.
     */
    @ColumnInfo(name = "quantity")
    val quantityInStock: Int,

    /**
     *  Identifier for the cost value of items.
     */
    @ColumnInfo(name = "cost")
    val itemCost: Double,
)

/**
 * Returns the passed in price in currency format.
 */
fun Item.getFormattedPrice(): String =
    NumberFormat.getCurrencyInstance().format(itemPrice)

