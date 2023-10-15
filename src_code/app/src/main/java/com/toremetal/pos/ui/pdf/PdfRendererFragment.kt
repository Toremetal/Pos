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
 *     PdfRendererFragment.kt : Copyright (c) 2021™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David W. Rick
 *      Date: 12/14/21, 10:32 AM
 *      Program Name: Pos
 *      File: PdfRendererFragment.kt
 *      Last Modified: 11/29/21, 9:18 PM
 *   ************************************************************************
 */

package com.toremetal.pos.ui.pdf

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.toremetal.pos.R

/**
 * Data processing functions for creating and viewing pdf files.
 */
class PdfRendererFragment : Fragment(R.layout.fragment_pdf_renderer) {

    private val viewModel: PdfRendererViewModel by viewModels()

    /**
     * Initial Entry point.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val image: ImageView = view.findViewById(R.id.image)
        val buttonPrevious: Button = view.findViewById(R.id.previous)
        val buttonNext: Button = view.findViewById(R.id.next)
        viewModel.pageInfo.observe(
            viewLifecycleOwner,
            { (index, count) ->
                activity?.title = getString(R.string.app_name_with_index, index + 1, count)
            })
        viewModel.pageBitmap.observe(
            viewLifecycleOwner,
            { image.setImageBitmap(it) })
        viewModel.previousEnabled.observe(viewLifecycleOwner, {
            buttonPrevious.isEnabled = it
        })
        viewModel.nextEnabled.observe(viewLifecycleOwner, {
            buttonNext.isEnabled = it
        })

        buttonPrevious.setOnClickListener { viewModel.showPrevious() }
        buttonNext.setOnClickListener { viewModel.showNext() }
    }
}