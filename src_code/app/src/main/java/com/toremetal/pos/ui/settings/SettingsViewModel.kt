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
 *     SettingsViewModel.kt : Copyright (c) 2021™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David W. Rick
 *      Date: 12/14/21, 10:32 AM
 *      Program Name: Pos
 *      File: SettingsViewModel.kt
 *      Last Modified: 11/29/21, 9:18 PM
 *   ************************************************************************
 */

package com.toremetal.pos.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.toremetal.pos.data.myMes
import com.toremetal.pos.data.mySignature

/**
 *
 */
class SettingsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = myMes
    }

    /**
     *
     */
    val text: LiveData<String> = _text
    private val _text2 = MutableLiveData<String>().apply {
        value = mySignature
    }

    /**
     *
     */
    val text2: LiveData<String> = _text2
}