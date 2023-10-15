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
 *     SettingsFragment.kt : Copyright (c) 2021™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David W. Rick
 *      Date: 12/14/21, 10:32 AM
 *      Program Name: Pos
 *      File: SettingsFragment.kt
 *      Last Modified: 11/29/21, 9:27 PM
 *   ************************************************************************
 */

package com.toremetal.pos.ui.settings

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.toremetal.pos.R
import com.toremetal.pos.databinding.FragmentSettingsBinding

/**
 *
 */
class SlideshowFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    /**
     *
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        settingsViewModel =
            ViewModelProvider(this)[SettingsViewModel::class.java]

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.sign.setOnKeyListener { view, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                val signStr = binding.sign.text.toString()
                if (signStr != "") {
                    view?.rootView?.findViewById<FloatingActionButton>(R.id.fab3)?.performClick()
                }
            }
            return@setOnKeyListener false
        }
        binding.mes.setOnKeyListener { view, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                val signStr = binding.mes.text.toString()
                if (signStr != "") {
                    view?.rootView?.findViewById<FloatingActionButton>(R.id.fab4)?.performClick()
                }
                context?.hideKeyboard(view)
            }
            return@setOnKeyListener false
        }

        binding.salesReset.setOnClickListener {
            view?.rootView?.findViewById<FloatingActionButton>(R.id.resetSale)?.performClick()
        }
        val editText: TextInputEditText = binding.mes
        settingsViewModel.text.observe(viewLifecycleOwner, {
            editText.setText(it)
        })
        val editText2: TextInputEditText = binding.sign
        settingsViewModel.text2.observe(viewLifecycleOwner, {
            editText2.setText(it)
        })
        return root
    }

    /**
     *
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

/**
 *
 */
fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}