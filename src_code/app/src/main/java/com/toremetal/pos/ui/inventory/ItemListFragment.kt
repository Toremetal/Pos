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
 *     ItemListFragment.kt : Copyright (c) 2021™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David W. Rick
 *      Date: 12/14/21, 10:32 AM
 *      Program Name: Pos
 *      File: ItemListFragment.kt
 *      Last Modified: 12/14/21, 1:16 AM
 *   ************************************************************************
 */

package com.toremetal.pos.ui.inventory

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.toremetal.pos.PosApplication
import com.toremetal.pos.R
import com.toremetal.pos.data.*
import com.toremetal.pos.databinding.ItemListFragmentBinding

/**
 * Main fragment displaying details for all items in the database.
 */
class ItemListFragment : Fragment() {
    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as PosApplication).database.itemDao()
        )
    }

    private var _binding: ItemListFragmentBinding? = null
    private val binding get() = _binding!!

    /**
     *
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ItemListFragmentBinding.inflate(inflater, container, false)
        //val navController = findNavController()
        //navController.currentBackStackEntry?.arguments?.clear()
        return binding.root
    }

    /**
     *
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ItemListAdapter {
            val action =
                ItemListFragmentDirections.actionItemListFragment2ToItemDetailFragment2(it.id)
            this.findNavController().navigate(action)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.adapter = adapter
        // Attach an observer on the allItems list to update the UI automatically when the data
        // changes.
        viewModel.allItems.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }
            saleItems.clear()
            saleItems.addAll(items)
            if (saleItems.count() == 0) {
                binding.floatingActionButtonExport.visibility = View.GONE
            } else {
                binding.floatingActionButtonExport.visibility = View.VISIBLE
            }
        }
        binding.manSearch.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                try {
                    val xx: Int = binding.manSearch.text.toString().toInt()
                    //numbers(binding.manSearch.text.toString())
                    if (binding.manSearch.text.toString() != "") {
                        binding.manSearch.setText("")
                        if (xx > 0) {
                            if (xx <= saleItems.count()) {
                                try {
                                    val action =
                                        ItemListFragmentDirections.actionItemListFragment2ToItemDetailFragment2(
                                            xx
                                        )
                                    this.findNavController().navigate(action)
                                } catch (e: Exception) {
                                    Snackbar.make(
                                        view,
                                        "ERROR: ${e.message}",
                                        Snackbar.LENGTH_LONG
                                    )
                                        .setAction("Action", null).show()
                                }
                            } else {
                                Snackbar.make(
                                    view,
                                    "Item Not Found!",
                                    Snackbar.LENGTH_LONG
                                )
                                    .setAction("Action", null).show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    /*Snackbar.make(view.rootView, "ERROR: ${e.message}", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()*/
                }
            }
            return@setOnKeyListener false
        }

        binding.floatingActionButton.setOnClickListener {
            itemEdit = false
            val action = ItemListFragmentDirections.actionItemListFragment2ToAddItemFragment2(
                getString(R.string.add_fragment_title)
            )
            this.findNavController().navigate(action)
        }
        binding.floatingActionButtonExport.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = getString(R.string.textCsv)
                    putExtra(Intent.EXTRA_TITLE, getString(R.string.inventory))
                }
            this.activity?.startActivityIfNeeded(intent, 3)
        }
    }
}
