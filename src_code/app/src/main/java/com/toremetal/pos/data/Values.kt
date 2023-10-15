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
 *     Values.kt : Copyright (c) 2021™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David W. Rick
 *      Date: 12/14/21, 10:32 AM
 *      Program Name: Pos
 *      File: Values.kt
 *      Last Modified: 12/14/21, 9:56 AM
 *   ************************************************************************
 */

package com.toremetal.pos.data

import java.util.*

/** Update checkmark
 * to prevent the update manager from checking for updates everytime the main form loads.
 */
var checkUpdate: Boolean = true

/**
 *
 */
var npAds: Boolean = false

/**
 *
 */
var itemEdit: Boolean = true

/**
 *
 */
val saleItem = mutableListOf<String>()

/**
 *
 */
val saleItems = mutableListOf<Item>()

/**
 *
 */
val saleItemLog = mutableListOf<String>()

/**
 *
 */
val itemInventory = mutableListOf<String>()

/**
 *
 */
val myCopyright = "©${Calendar.getInstance().get(Calendar.YEAR)}"

/**
 *
 */
val dateViewStr = (Calendar.getInstance()
    .get(Calendar.MONTH) + 1).toString() + "-" + Calendar.getInstance()
    .get(Calendar.DATE).toString() + "-" + Calendar.getInstance()
    .get(Calendar.YEAR)
    .toString()

/**
 *
 */
var smsSend: Boolean = false

/**
 *
 */
var emailSend: Boolean = false

/**
 *
 */
var itemsHeader: String = ""

/**
 *
 */
var mySignature: String = "™T©ReMeTaL"

/**
 *
 */
var myMes: String = "™T©ReMeTaL POS"

/**
 *
 */
var salesFile: String = ""

/**
 *
 */
var salePrice: Double = 0.0

/**
 *
 */
var saleCost: Double = 0.0

/**
 *
 */
var taxRate: Double = 0.0

