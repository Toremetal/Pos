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
 *     InventoryViewModel.kt : Copyright (c) 2021™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David W. Rick
 *      Date: 12/14/21, 10:32 AM
 *      Program Name: Pos
 *      File: InventoryViewModel.kt
 *      Last Modified: 11/29/21, 9:18 PM
 *   ************************************************************************
 */

package com.toremetal.pos.ui.inventory

import androidx.lifecycle.*
import com.toremetal.pos.data.Item
import com.toremetal.pos.data.ItemDao
import kotlinx.coroutines.launch

/**
 * View Model to keep a reference to the inventory repository
 * and an up-to-date list of all items.
 */
class InventoryViewModel(private val itemDao: ItemDao) : ViewModel() {

    /**
     * Cache all items and form the database using LiveData.
     */
    val allItems: LiveData<List<Item>> = itemDao.getItems().asLiveData()

    /**
     * Returns true if stock is available to sell, false otherwise.
     */
    fun isStockAvailable(item: Item): Boolean {
        return (item.quantityInStock > 0)
    }

    /**
     * Updates an existing Item in the database.
     */
    fun updateItem(
        itemId: Int,
        itemName: String,
        itemDes: String,
        itemPrice: String,
        itemCount: String,
        itemCost: String
    ) {
        val updatedItem =
            getUpdatedItemEntry(itemId, itemName, itemDes, itemPrice, itemCount, itemCost)
        updateItem(updatedItem)
    }


    /**
     * Launching a new coroutine to update an item in a non-blocking way
     */
    private fun updateItem(item: Item) {
        viewModelScope.launch {
            itemDao.update(item)
        }
    }

    /**
     * Decreases the stock by one unit and updates the database.
     */
    fun sellItem(item: Item, count: Int = 1) {
        //if (item.quantityInStock > 0) {
        // Decrease the quantity by 1
        val newItem = item.copy(quantityInStock = item.quantityInStock - count)
        updateItem(newItem)
        //}
    }

    /**
     * Inserts the new Item into database.
     */
    fun addNewItem(
        itemName: String,
        itemDes: String,
        itemPrice: String,
        itemCount: String,
        itemCost: String
    ) {
        val newItem = getNewItemEntry(itemName, itemDes, itemPrice, itemCount, itemCost)
        insertItem(newItem)
    }

    /**
     * Launching a new coroutine to insert an item in a non-blocking way
     */
    private fun insertItem(item: Item) {
        viewModelScope.launch {
            itemDao.insert(item)
        }
    }

    /**
     * Launching a new coroutine to delete an item in a non-blocking way
     */
    fun deleteItem(item: Item) {
        viewModelScope.launch {
            itemDao.delete(item)
        }
    }

    /**
     * Retrieve an item from the repository.
     */
    fun retrieveItem(id: Int): LiveData<Item> {
        return itemDao.getItem(id).asLiveData()
    }

    /**
     * Returns true if the EditTexts are not empty
     */
    fun isEntryValid(
        itemName: String,
        itemDes: String,
        itemPrice: String,
        itemCount: String,
        itemCost: String
    ): Boolean {
        if (itemName.isBlank() || itemDes.isBlank() || itemPrice.isBlank() || itemCount.isBlank() || itemCost.isBlank()) {
            return false
        }
        return true
    }

    /**
     * Returns an instance of the [Item] entity class with the item info entered by the user.
     * This will be used to add a new entry to the inventory database.
     */
    private fun getNewItemEntry(
        itemName: String,
        itemDes: String,
        itemPrice: String,
        itemCount: String,
        itemCost: String
    ): Item {
        return Item(
            itemName = itemName,
            itemDes = itemDes,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt(),
            itemCost = itemCost.toDouble()
        )
    }

    /**
     * Called to update an existing entry in the inventory database.
     * Returns an instance of the [Item] entity class with the item info updated by the user.
     */
    private fun getUpdatedItemEntry(
        itemId: Int,
        itemName: String,
        itemDes: String,
        itemPrice: String,
        itemCount: String,
        itemCost: String
    ): Item {
        return Item(
            id = itemId,
            itemName = itemName,
            itemDes = itemDes,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt(),
            itemCost = itemCost.toDouble()
        )
    }
}

/**
 * Factory class to instantiate the [ViewModel] instance.
 */
class InventoryViewModelFactory(private val itemDao: ItemDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

