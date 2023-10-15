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
 *     PdfRendererViewModel.kt : Copyright (c) 2021™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David W. Rick
 *      Date: 12/14/21, 10:32 AM
 *      Program Name: Pos
 *      File: PdfRendererViewModel.kt
 *      Last Modified: 11/29/21, 9:18 PM
 *   ************************************************************************
 */

package com.toremetal.pos.ui.pdf

import android.app.Application
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Pdf Viewer.
 */
class PdfRendererViewModel @JvmOverloads constructor(
    application: Application,
    useInstantExecutor: Boolean = false
) : AndroidViewModel(application) {

    companion object {

        /**
         * Const val for app asset.
         */
        const val FILENAME: String = "pos.pdf"

        /**
         * Const val for app asset.
         */
        const val FILENAME1: String = "slide.pdf"

        /**
         * variable used to store current file name.
         */
        var FILENAME2: String = "1"

        /**
         * var used to store file Uri.
         */
        lateinit var FILE_LOC: Uri

        /**
         * var used to store file path.
         */
        lateinit var FILELOC: String

        /**
         * The byte stream for processing files.
         */
        lateinit var FILENAME2URI: Uri
    }

    private val job = Job()
    private val executor = if (useInstantExecutor) {
        Executor { it.run() }
    } else {
        Executors.newSingleThreadExecutor()
    }
    private val scope = CoroutineScope(executor.asCoroutineDispatcher() + job)
    private var fileDescriptor: ParcelFileDescriptor? = null
    private var pdfRenderer: PdfRenderer? = null
    private var currentPage: PdfRenderer.Page? = null
    private var cleared = false

    /**
     * Private modifiable access to Bitmap data streams.
     */
    private val _pageBitmap = MutableLiveData<Bitmap>()

    /**
     *  External access to Bitmap data streams.
     */
    val pageBitmap: LiveData<Bitmap>
        get() = _pageBitmap

    /**
     *  Private modifiable access to the previous page data.
     */
    private val _previousEnabled = MutableLiveData<Boolean>()

    /**
     *  External access to the previous page data.
     */
    val previousEnabled: LiveData<Boolean>
        get() = _previousEnabled

    /**
     * Private modifiable access to the next page data.
     */
    private val _nextEnabled = MutableLiveData<Boolean>()

    /**
     * External access to the next page data.
     */
    val nextEnabled: LiveData<Boolean>
        get() = _nextEnabled

    /**
     * Page Info LiveData.
     */
    private val _pageInfo = MutableLiveData<Pair<Int, Int>>()

    /**
     * parameters for storing height and width data.
     */
    val pageInfo: LiveData<Pair<Int, Int>>
        get() = _pageInfo

    /**
     * Initialize Coroutine.
     */
    init {
        scope.launch {
            openPdfRenderer()
            showPage(0)
            if (cleared) {
                closePdfRenderer()
            }
        }
    }

    /**
     * activity job disposer.
     */
    override fun onCleared() {
        super.onCleared()
        scope.launch {
            closePdfRenderer()
            cleared = true
            job.cancel()
        }
    }

    /**
     * Show Previous page function.
     */
    fun showPrevious() {
        scope.launch {
            currentPage?.let { page ->
                if (page.index > 0) {
                    showPage(page.index - 1)
                }
            }
        }
    }

    /**
     * Show next page function
     */
    fun showNext() {
        scope.launch {
            pdfRenderer?.let { renderer ->
                currentPage?.let { page ->
                    if (page.index + 1 < renderer.pageCount) {
                        showPage(page.index + 1)
                    }
                }
            }
        }
    }

    private fun openPdfRenderer() {
        val application = getApplication<Application>()
        val file: File
        if (FILENAME2 == "1") {
            file = File(application.cacheDir, FILENAME)
            if (!file.exists()) {
                application.assets.open(FILENAME).use { asset ->
                    file.writeBytes(asset.readBytes())
                }
            }
        } else if (FILENAME2 == "2") {
            file = File(application.cacheDir, FILENAME1)
            if (!file.exists()) {
                application.assets.open(FILENAME1).use { asset ->
                    file.writeBytes(asset.readBytes())
                }
            }
        } else {
            file = File(application.cacheDir, FILENAME2)
            FILE_LOC = file.toUri()
            FILELOC = file.path
            application.contentResolver.openFileDescriptor(FILENAME2URI, "r")?.use { it ->
                FileInputStream(it.fileDescriptor).use {
                    file.writeBytes(it.readBytes())
                    it.close()
                }
            }
        }
        fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY).also {
            pdfRenderer = PdfRenderer(it)
        }
    }

    private fun showPage(index: Int) {
        // Make sure to close the current page before opening another one.
        currentPage?.let { page ->
            currentPage = null
            page.close()
        }
        pdfRenderer?.let { renderer ->
            // Use `openPage` to open a specific page in PDF.
            val page = renderer.openPage(index).also {
                currentPage = it
            }
            // Important: the destination bitmap must be ARGB (not RGB).
            val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
            // Here, we render the page onto the Bitmap.
            // To render a portion of the page, use the second and third parameter.Pass nulls to get
            // the default result.
            // Pass either RENDER_MODE_FOR_DISPLAY or RENDER_MODE_FOR_PRINT for the last parameter.
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            _pageBitmap.postValue(bitmap)
            val count = renderer.pageCount
            _pageInfo.postValue(index to count)
            _previousEnabled.postValue(index > 0)
            _nextEnabled.postValue(index + 1 < count)
        }
    }

    private fun closePdfRenderer() {
        currentPage?.close()
        pdfRenderer?.close()
        fileDescriptor?.close()
    }

}