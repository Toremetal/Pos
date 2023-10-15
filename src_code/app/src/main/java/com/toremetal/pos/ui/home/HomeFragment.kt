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
 *     HomeFragment.kt : Copyright (c) 2021™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David W. Rick
 *      Date: 12/14/21, 10:32 AM
 *      Program Name: Pos
 *      File: HomeFragment.kt
 *      Last Modified: 11/29/21, 9:18 PM
 *   ************************************************************************
 */

package com.toremetal.pos.ui.home

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.toremetal.pos.*
import com.toremetal.pos.data.*
import com.toremetal.pos.databinding.FragmentHomeBinding
import java.text.NumberFormat
import java.util.*

/**
 * This is the [HomeFragment] of the containing
 * Activity's lifecycle.
 * This is the main UI of the Activity.
 * Responsible for handling primary sale functions.
 */
class HomeFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is tied to the [HomeFragment] of the containing
     * Activity's lifecycle.
     */
    override fun onResume() {
        binding.dateView.text = dateViewStr
        setValues()
        binding.addItems.requestFocus()
        binding.addItems.findFocus()
        super.onResume()
    }

    private fun setValues() {
        binding.currentSaleItems.text = saleItem.toString().replace("[", "").replace("]", "")
        binding.textViewSubTotal.text =
            NumberFormat.getCurrencyInstance().format(salePrice).toString()
        if (taxRate != 0.0) {
            binding.editTextTaxRate.setText(taxRate.times(100).toString())
        }
        binding.textViewTax.text = if (taxRate != 0.0) {
            NumberFormat.getCurrencyInstance().format(salePrice.times(taxRate)).toString()
        } else {
            NumberFormat.getCurrencyInstance().format(0).toString()
        }
        binding.textViewTotal.text = if (taxRate != 0.0) {
            NumberFormat.getCurrencyInstance().format((salePrice.times(taxRate)).plus(salePrice))
                .toString()
        } else {
            NumberFormat.getCurrencyInstance().format(salePrice).toString()
        }
    }

    /**
     * Called when the fragment is created.
     * This is tied to the [HomeFragment] of the containing
     * Activity's lifecycle.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        // val root: View = binding.root
        binding.switch3.isChecked = smsSend
        binding.email.isChecked = emailSend
        binding.switch1.setOnClickListener {
            binding.ManualEntry.isVisible = binding.switch1.isChecked
        }
        binding.addManEntry.setOnClickListener {
            binding.addManEntry.isEnabled = false
            if (binding.newId.text.toString() != "") {
                var qty = binding.newQty.text.toString()
                if (qty == "") {
                    qty = "1"
                }
                val ipNf = binding.newPrice.text.toString().toDouble()
                val ip = NumberFormat.getCurrencyInstance().format(ipNf)//formatMoney(ipNf)
                saleItem.add("N/A, ${binding.newId.text}, $qty @ ${ip}\n")
                saleItemLog.add(
                    """N/A,${binding.newId.text},N/A,${qty},${ip}
|""".trimMargin()
                )
                salePrice += ipNf.times(qty.toDouble())
                binding.newId.setText("")
                binding.newQty.setText("1")
                binding.newPrice.setText("")
                setValues()
            }
            binding.addManEntry.isEnabled = true
        }
        binding.editTextTaxRate.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                when (binding.editTextTaxRate.text.toString()) {
                    "" -> {
                        taxRate = 0.0
                    }
                    "0" -> {
                        taxRate = 0.0
                    }
                    "0.0" -> {
                        taxRate = 0.0
                    }
                    "0.00" -> {
                        taxRate = 0.0
                    }
                }
                view?.rootView?.findViewById<FloatingActionButton>(R.id.fab)?.performClick()
            }
            return@setOnKeyListener false
        }
        binding.addItems.setOnClickListener {
            val action =
                HomeFragmentDirections.actionNavHomeToItemListFragment2()
            this.findNavController().navigate(action)
        }
        binding.floatingActionButton10.setOnClickListener {
            saleCost = 0.0
            salePrice = 0.0
            saleItem.clear()
            saleItemLog.clear()
            binding.currentSaleItems.text = ""
            binding.textViewTax.text = NumberFormat.getCurrencyInstance().format(0).toString()
            binding.textViewSubTotal.text = NumberFormat.getCurrencyInstance().format(0).toString()
            binding.textViewTotal.text = NumberFormat.getCurrencyInstance().format(0).toString()
            /* try {
                view?.rootView?.findViewById<FloatingActionButton>(R.id.clearSale)?.performClick()
            } catch (e: Exception) {
                view?.rootView?.findViewById<FloatingActionButton>(R.id.clearSale)?.performClick()
            }*/
        }
        binding.floatingActionButtonEndSale.setOnClickListener {
            try {
                view?.rootView?.findViewById<FloatingActionButton>(R.id.fab2)?.performClick()
            } catch (e: Exception) {
                view?.rootView?.findViewById<FloatingActionButton>(R.id.fab2)?.performClick()
            }
        }
        return binding.root
    }

    /**
     * Optimize device memory allocations by freeing resources.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}