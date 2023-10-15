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
 *     HomeViewModel.kt : Copyright (c) 2021™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David W. Rick
 *      Date: 12/14/21, 10:32 AM
 *      Program Name: Pos
 *      File: HomeViewModel.kt
 *      Last Modified: 11/29/21, 9:18 PM
 *   ************************************************************************
 */

package com.toremetal.pos.ui.home

import androidx.lifecycle.ViewModel

/**
 *  View container for the primary Fragment Activities.
 */
class HomeViewModel : ViewModel() /*{

    private val _text = MutableLiveData<String>().apply {
        value = saleItem.toString().replace("[", "").replace("]", "")
    }
    val text: LiveData<String> = _text

    private val _text2 = MutableLiveData<String>().apply {
        value = NumberFormat.getCurrencyInstance().format(salePrice).toString()
    }
    val text2: LiveData<String> = _text2

    private val _textTr = MutableLiveData<String>().apply {
        if (taxRate != 0.0) {
            value = taxRate.times(100).toString()
        }
    }
    val taxR: LiveData<String> = _textTr

    private val _text3 = MutableLiveData<String>().apply {
        value = if (taxRate != 0.0) {
            NumberFormat.getCurrencyInstance().format(salePrice.times(taxRate)).toString()
        } else {
            NumberFormat.getCurrencyInstance().format(0).toString()
        }
    }
    val tax: LiveData<String> = _text3

    private val _text4 = MutableLiveData<String>().apply {
        value = if (taxRate != 0.0) {
            NumberFormat.getCurrencyInstance().format((salePrice.times(taxRate)).plus(salePrice))
                .toString()
        } else {
            NumberFormat.getCurrencyInstance().format(salePrice).toString()
        }
    }
    val total: LiveData<String> = _text4

    private val _textDate = MutableLiveData<String>().apply {
        value = dateViewStr
    }
    val textDate: LiveData<String> = _textDate
}*/