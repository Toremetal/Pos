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
 *     ItemDetailFragment.kt : Copyright (c) 2021™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David W. Rick
 *      Date: 12/14/21, 10:32 AM
 *      Program Name: Pos
 *      File: ItemDetailFragment.kt
 *      Last Modified: 11/29/21, 9:18 PM
 *   ************************************************************************
 */

package com.toremetal.pos.ui.inventory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.toremetal.pos.PosApplication
import com.toremetal.pos.R
import com.toremetal.pos.data.*
import com.toremetal.pos.databinding.FragmentItemDetailBinding

/**
 * [ItemDetailFragment] displays the details of the selected item.
 */
class ItemDetailFragment : Fragment() {
    private val navigationArgs: ItemDetailFragmentArgs by navArgs()

    /**
     *  var [item] holds the values for the item currently displayed.
     */
    lateinit var item: Item

    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as PosApplication).database.itemDao()
        )
    }

    private var _binding: FragmentItemDetailBinding? = null
    private val binding get() = _binding!!

    /**
     *  The initial entry point for the fragment container.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Binds views with the passed in item data.
     */
    private fun bind(item: Item) {
        binding.apply {
            itemId.setText(item.id.toString())
            itemName.setText(item.itemName)
            itemPrice.setText(item.getFormattedPrice())
            itemCount.text = item.quantityInStock.toString()
            sellItem.isEnabled = viewModel.isStockAvailable(item)
            addStock.isVisible = sellItem.isEnabled == false
            val saleStr = "${
                getString(
                    R.string.saleList,
                    saleItem.toString().replace("[", "").replace("]", "")
                )
            } \n${getString(R.string.sub, salePrice.toString())}"
            textView.text = saleStr
            sellItem.setOnClickListener {
                val ip = itemPrice.text.toString().replace("$", "")
                val purchase: Int = purchaseQty.text.toString().toInt()
                if (purchase <= itemCount.text.toString().toInt()) {
                    saleItem.add("${itemId.text}, ${itemName.text}, ${purchaseQty.text} @ ${itemPrice.text}\n")
                    saleItemLog.add(
                        """${itemId.text},${itemName.text},${item.itemDes},${purchaseQty.text},${itemPrice.text}
|""".trimMargin()
                    )
                    salePrice += ip.toDouble() * purchase.toDouble()
                    saleCost += item.itemCost * purchase.toDouble()
                    val saleClickedStr = "${
                        getString(
                            R.string.saleList,
                            saleItem.toString().replace("[", "").replace("]", "")
                        )
                    } \n${getString(R.string.sub, salePrice.toString())}"
                    textView.text = saleClickedStr
                    purchaseQty.setText("1")
                    viewModel.sellItem(item, purchase)
                } else {
                    Snackbar.make(it, "Not enough in Stock", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                }
            }
            addStock.setOnClickListener {
                val iCount = purchaseQty.text.toString().toInt().times(-1)
                purchaseQty.setText("1")
                viewModel.sellItem(item, iCount)
            }
            deleteItem.setOnClickListener {
                findNavController().navigateUp()
            }
            checkout.setOnClickListener {
                val action = ItemDetailFragmentDirections.actionItemDetailFragment2ToNavHome()
                findNavController().navigate(action)
            }
            editItem.setOnClickListener { editItem(item.id) }
        }
    }

    /**
     * Navigate to the Edit item screen.
     */
    private fun editItem(item: Int) {
        val action = ItemDetailFragmentDirections.actionItemDetailFragment2ToAddItemFragment2(
            getString(R.string.edit_item),
            item//.id
        )
        this.findNavController().navigate(action)
    }

    /**
     * Displays an alert dialog to get the user's confirmation before deleting the item.
     */
    /*private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteItem()
            }
            .show()
    }*/

    /**
     * Deletes the current item and navigates to the list fragment (Removed).
     */
    /*fun deleteItem() {
        viewModel.deleteItem(item)
        findNavController().navigateUp()
    }*/

    /**
     * Called when fragment is created.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.itemId
        // Retrieve the item details using the itemId.
        // Attach an observer on the data (instead of polling for changes) and only update the
        // the UI when the data actually changes.
        viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
            item = selectedItem
            bind(item)
        }
    }

    /**
     * Called when fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
