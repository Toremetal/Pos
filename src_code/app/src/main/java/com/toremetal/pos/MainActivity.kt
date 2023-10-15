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
 *     MainActivity.kt : Copyright (c) 2021™T©ReMeTaL.
 *   ************************************************************************
 *      Computer Scientist: David W. Rick
 *      Date: 12/15/21, 8:23 AM
 *      Program Name: Pos
 *      File: MainActivity.kt
 *      Last Modified: 12/15/21, 8:23 AM
 *   ************************************************************************
 */

package com.toremetal.pos

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.*
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.ump.*
import com.toremetal.pos.data.*
import com.toremetal.pos.databinding.ActivityMainBinding
import com.toremetal.pos.ui.home.HomeFragmentDirections
import com.toremetal.pos.ui.pdf.PdfRendererViewModel.Companion.FILENAME2
import com.toremetal.pos.ui.pdf.PdfRendererViewModel.Companion.FILENAME2URI
import java.io.*
import java.text.NumberFormat
import java.util.*


/**
 * Application Activity [MainActivity] is the primary container for
 * all fragment activities and processes initiated by the application.
 * [consentForm] provides user consent data exchange interoperability.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var consentForm: ConsentForm
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var mAdView: AdView

    /**
     * Public Function [viewWeb] triggers the device default web browser
     * to view Developer websites provided for application support.
     * @param [view] identifies the specific website request parameters.
     */
    fun viewWeb(view: View) {
        try {
            if (view.id == R.id.webTextView) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.website))
                    )
                )
            } else if (view.id == R.id.titleImageView) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.playStoreLink))
                    )
                )
            }
        } catch (e: Exception) {

        }
    }

    /**
     * The initial entry point for the application.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        /* Pass each menu ID as a set of Ids so each
           menu is considered as a top level destination. */
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_inventory, R.id.nav_pdf, R.id.nav_settings
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.menu.getItem(2).setOnMenuItemClickListener {
            binding.navView.menu.getItem(5).isVisible = false
            //openSaleLog(getString(R.string.salesTxt))
            val theFile = getString(R.string.salesTxt)
            if (getFileStreamPath(theFile).isFile) {
                try {
                    val open = true
                    var w = 800
                    var h = 1000
                    var pgHw = 750F
                    var pgh1 = 900F
                    var pgLn = 400F
                    var h1 = 940F
                    var h2 = 970F
                    var h3 = 992F
                    if (open) {
                        w = 400
                        h = 500
                        pgHw = 350F
                        pgh1 = 450F
                        pgLn = 200F
                        h1 = 470F
                        h2 = 485F
                        h3 = 496F
                    }
                    var pgNum = 1
                    val document = PdfDocument()
                    var pageInfo: PdfDocument.PageInfo =
                        PdfDocument.PageInfo.Builder(w, h, pgNum).create()
                    var page: PdfDocument.Page = document.startPage(pageInfo)
                    var canvas = page.canvas
                    val paint = Paint()
                    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                    paint.color = ContextCompat.getColor(this, R.color.grey)
                    var y = 20F
                    paint.textAlign = Paint.Align.LEFT
                    paint.textSize = 14F
                    canvas.drawText(
                        "$mySignature ${getString(R.string.sales)}",
                        50F,
                        y,
                        paint
                    )
                    paint.color = ContextCompat.getColor(this, R.color.black)
                    paint.textAlign = Paint.Align.RIGHT
                    canvas.drawText("${getString(R.string.pg)}${pgNum}", pgHw, y, paint)
                    y += 40F
                    paint.textAlign = Paint.Align.CENTER
                    val openTheFile2 = openFileInput(theFile)
                    openTheFile2.reader().forEachLine {
                        canvas.drawText(it, pgLn, y, paint)
                        y += 20F
                        if (y >= pgh1) {
                            paint.textSize = 12F
                            canvas.drawText(
                                "$mySignature ${getString(R.string.sales)}",
                                pgLn,
                                h1,
                                paint
                            )
                            canvas.drawText(mySignature, pgLn, h2, paint)
                            canvas.drawText("$myCopyright$mySignature", pgLn, h3, paint)
                            document.finishPage(page)
                            paint.textSize = 14F
                            y = 20F
                            pgNum++
                            pageInfo = PdfDocument.PageInfo.Builder(w, h, pgNum).create()
                            page = document.startPage(pageInfo)
                            canvas = page.canvas
                            paint.textAlign = Paint.Align.LEFT
                            paint.color = ContextCompat.getColor(this, R.color.grey)
                            canvas.drawText(
                                "$mySignature ${getString(R.string.sales)}",
                                50F,
                                y,
                                paint
                            )
                            paint.color = ContextCompat.getColor(this, R.color.black)
                            paint.textAlign = Paint.Align.RIGHT
                            canvas.drawText("${getString(R.string.pg)}${pgNum}", pgHw, y, paint)
                            y += 40F
                            paint.textAlign = Paint.Align.CENTER
                        }
                    }
                    openTheFile2.close()
                    paint.textSize = 12F
                    canvas.drawText(
                        "$mySignature ${getString(R.string.sales)}",
                        pgLn,
                        h1,
                        paint
                    )
                    canvas.drawText(mySignature, pgLn, h2, paint)
                    canvas.drawText("$myCopyright$mySignature", pgLn, h3, paint)
                    document.finishPage(page)
                    document.writeTo(
                        openFileOutput(getString(R.string.tmpPdf), 0)
                    )
                    document.close()
                    FILENAME2 = theFile
                    FILENAME2URI =
                        getFileStreamPath(getString(R.string.tmpPdf)).toUri()
                } catch (e: Exception) {
                }
                val action =
                    R.id.nav_pdf//HomeFragmentDirections.actionNavHomeToPdfRendererBasicFragment(getString(R.string.sales))
                findNavController(R.id.nav_host_fragment_content_main).navigate(action)
                binding.drawerLayout.close()
            } else {
                showMessage("No Sales Logged!")
            }
            return@setOnMenuItemClickListener true
        }
        navView.menu.getItem(4).setOnMenuItemClickListener {
            // Open Pdf file with app Method.
            val browseStorage = Intent(Intent.ACTION_GET_CONTENT)
            browseStorage.addCategory(Intent.CATEGORY_OPENABLE)
            browseStorage.type = "application/pdf"
            startActivityIfNeeded(
                Intent.createChooser(
                    browseStorage,
                    "Select a File"
                ), 4
            )
            return@setOnMenuItemClickListener true
        }
        navView.menu.getItem(5).setOnMenuItemClickListener {
            it.isVisible = false
            val action =
                R.id.nav_pdf//HomeFragmentDirections.actionNavHomeToPdfRendererBasicFragment("File")
            findNavController(R.id.nav_host_fragment_content_main).navigate(action)
            binding.drawerLayout.close()
            return@setOnMenuItemClickListener true
        }

        binding.appBarMain.fabF.setOnClickListener {
            val action =
                R.id.nav_pdf//HomeFragmentDirections.actionNavHomeToPdfRendererBasicFragment("File")
            findNavController(R.id.nav_host_fragment_content_main).navigate(action)
            binding.drawerLayout.close()
        }

        itemsHeader =
            ",${getString(R.string.inventoryCsvHeader)}\n,${getString(R.string.inventoryCsvHeaderL2)}\n"
        salesFile = getString(R.string.salesCsv).replace(".csv", "")
        val vName = packageManager.getPackageInfo(packageName, 0).versionName
        binding.appVersion.text = getString(R.string.version_display_name, vName)
        binding.webText.setOnClickListener {
            binding.webText.isEnabled = false
            try {
                FILENAME2 = "1"
                val action =
                    R.id.nav_pdf
                findNavController(R.id.nav_host_fragment_content_main).navigate(action)
                binding.drawerLayout.close()
                /* startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(getString(R.string.privacy_policy_link))
                        )
                    )*/
            } catch (e: Exception) {
                showMessage(e.message.toString())
            }
            binding.webText.isEnabled = true
        }
        if (getFileStreamPath("sign").isFile) {
            val in1 = InputStreamReader(
                openFileInput(
                    "sign"
                )
            )
            mySignature = in1.readText()
            in1.close()
        }
        if (getFileStreamPath("mes").isFile) {
            val in1 = InputStreamReader(
                openFileInput(
                    "mes"
                )
            )
            myMes = in1.readText()
            in1.close()
        }
        if (getFileStreamPath("tax").isFile) {
            val in1 = InputStreamReader(
                openFileInput(
                    "tax"
                )
            )
            taxRate = in1.readText().toDouble()
            in1.close()
        }
        if (getFileStreamPath("sms").isFile) {
            val in1 = InputStreamReader(
                openFileInput(
                    "sms"
                )
            )
            val smsVal = in1.readText()
            in1.close()
            smsSend = smsVal == "true"
        }
        if (getFileStreamPath("email").isFile) {
            val in1 = InputStreamReader(
                openFileInput(
                    "email"
                )
            )
            val eVal = in1.readText()
            in1.close()
            emailSend = eVal == "true"
        }

        findViewById<SwitchMaterial>(R.id.switch3).setOnClickListener {
            if (findViewById<SwitchMaterial>(R.id.switch3).isChecked) {
                smsSend = true
                val outFile =
                    OutputStreamWriter(openFileOutput("sms", 0)) // Context.MODE_APPEND
                outFile.write("true")
                outFile.close()
            } else {
                smsSend = false
                val outFile =
                    OutputStreamWriter(openFileOutput("sms", 0)) // Context.MODE_APPEND
                outFile.write("false")
                outFile.close()
            }
        }
        findViewById<SwitchMaterial>(R.id.email).setOnClickListener {
            if (findViewById<SwitchMaterial>(R.id.email).isChecked) {
                emailSend = true
                val outFile =
                    OutputStreamWriter(openFileOutput("email", 0)) // Context.MODE_APPEND
                outFile.write("true")
                outFile.close()
            } else {
                emailSend = false
                val outFile =
                    OutputStreamWriter(openFileOutput("email", 0)) // Context.MODE_APPEND
                outFile.write("false")
                outFile.close()
            }
        }

        binding.appBarMain.fab3.setOnClickListener {
            val signStr = findViewById<TextInputEditText>(R.id.sign).text.toString()
            if (signStr != "") {
                val outFile =
                    OutputStreamWriter(openFileOutput("sign", 0)) // Context.MODE_APPEND
                outFile.write(signStr)
                outFile.close()
                showMessage("Signature Saved!")
            }

        }
        binding.appBarMain.fab4.setOnClickListener {
            val signStr = findViewById<TextInputEditText>(R.id.mes).text.toString()
            if (signStr != "") {
                val outFile =
                    OutputStreamWriter(openFileOutput("mes", 0)) // Context.MODE_APPEND
                outFile.write(signStr)
                outFile.close()
                showMessage("Message Saved!")
            }
        }
        binding.appBarMain.fab.setOnClickListener {
            if (findViewById<EditText>(R.id.editTextTaxRate).text.toString() != "") {
                convertTax(findViewById<EditText>(R.id.editTextTaxRate).text.toString())
            }
            val outFile =
                OutputStreamWriter(openFileOutput("tax", 0)) // Context.MODE_APPEND
            outFile.write(taxRate.toString())
            outFile.close()
            showMessage("Tax Rate Saved!")
        }
        binding.appBarMain.fab2.setOnClickListener {
            // findViewById<MaterialTextView>(R.id.floatingActionButtonEndSale)
            val today = Calendar.getInstance().time.toString()
            val sub = NumberFormat.getCurrencyInstance().format(salePrice).toString()
            val saleSub = getString(R.string.sub, sub)
            val tax =
                NumberFormat.getCurrencyInstance().format(salePrice.times(taxRate)).toString()
            val saleTax = getString(R.string.tax, tax)
            val saleTaxRate = "@${taxRate.times(100)}%"
            val total =
                NumberFormat.getCurrencyInstance().format(salePrice.plus(salePrice.times(taxRate)))
                    .toString()
            val saleTotal = getString(R.string.total, total)
            val profit =
                NumberFormat.getCurrencyInstance().format(salePrice.minus(saleCost)).toString()
            val cost = NumberFormat.getCurrencyInstance().format(saleCost).toString()

            var saleLog = """$today,
|${saleItem.toString().replace("[", "").replace("]", "")}
|$saleSub, $saleTax ${saleTaxRate}, $saleTotal
|_
|""".trimMargin()
            if (getFileStreamPath(getString(R.string.salesTxt)).isFile) {
                val out1 = OutputStreamWriter(
                    openFileOutput(
                        getString(R.string.salesTxt),
                        MODE_APPEND
                    )
                )
                out1.write(saleLog)
                out1.close()
            } else {
                val out1 = OutputStreamWriter(
                    openFileOutput(
                        getString(R.string.salesTxt),
                        0
                    )
                )
                out1.write(saleLog)
                out1.close()
            }

            if (getFileStreamPath(getString(R.string.salesCsv)).isFile) {
                saleLog = """$today,
|,${saleItemLog.toString().replace("[", "").replace("]", "")}
|,,,,,,$sub,${tax},${saleTaxRate},${total},${cost},${profit}
|""".trimMargin()
                val out1 = OutputStreamWriter(
                    openFileOutput(
                        getString(R.string.salesCsv),
                        MODE_APPEND
                    )
                )
                out1.write(saleLog)
                out1.close()
            } else {
                saleLog =
                    """${getString(R.string.salesCsvHeader1)}
                    |${getString(R.string.salesCsvHeaderL2)}
|$today,
|,${saleItemLog.toString().replace("[", "").replace("]", "")}
|,,,,,,$sub,${tax},${saleTaxRate},${total},${cost},${profit}
|""".trimMargin()
                val out1 = OutputStreamWriter(openFileOutput(getString(R.string.salesCsv), 0))
                out1.write(saleLog)
                out1.close()
            }
            val extSub = "${getString(R.string.receipt)}: ${Calendar.getInstance().time}"
            val extText = """
                ${getString(R.string.receipt)}: $dateViewStr
                        
                    $saleItem
                 $saleSub
                      $saleTax
                    $saleTotal
                        
                ${getString(R.string.receipt)}: ${Calendar.getInstance().time}
                       $myMes
                       $myCopyright$mySignature"""
            //findViewById<MaterialTextView>(R.id.floatingActionButton10).performClick()
            if (findViewById<SwitchMaterial>(R.id.switch3).isChecked) {
                try {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        //type = "application/pdf"
                        //putExtra(Intent.EXTRA_STREAM, uri)
                        putExtra(
                            Intent.EXTRA_SUBJECT,
                            extSub
                        )
                        putExtra(
                            Intent.EXTRA_TEXT,
                            extText
                        )
                    }
                    Intent.createChooser(
                        intent,
                        "${getString(R.string.app_name)} ${getString(R.string.receipt)}"
                    ).apply {
                        startActivity(intent)
                    }
                } catch (e: Exception) {
                    showMessage(e.message.toString())
                }
            } else if (findViewById<SwitchMaterial>(R.id.email).isChecked) {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    type = ContactsContract.CommonDataKinds.Email.MIMETYPE
                    data = Uri.parse(getString(R.string.mailTo))
                    //   putExtra(android.content.Intent.EXTRA_EMAIL, addresses)
                    putExtra(
                        Intent.EXTRA_SUBJECT,
                        extSub
                    )
                    putExtra(
                        Intent.EXTRA_TEXT,
                        extText
                    )
                }
                Intent.createChooser(
                    intent,
                    "${getString(R.string.app_name)} ${getString(R.string.receipt)}"
                ).apply {
                    startActivity(intent)
                }
            }
            salePrice = 0.0
            saleCost = 0.0
            saleItem.clear()
            saleItemLog.clear()
            /*findViewById<TextView>(R.id.currentSaleItems).text = ""
            findViewById<TextView>(R.id.textViewTax).text =
                NumberFormat.getCurrencyInstance().format(0).toString()
            findViewById<TextView>(R.id.textViewSubTotal).text =
                NumberFormat.getCurrencyInstance().format(0).toString()
            findViewById<TextView>(R.id.textViewTotal).text =
                NumberFormat.getCurrencyInstance().format(0).toString()
            binding.root.rootView.refreshDrawableState()*/
            findViewById<MaterialTextView>(R.id.floatingActionButton10).performClick()
            showMessage("Sale Logged!")
        }
        binding.appBarMain.clearSale.setOnClickListener {
            saleCost = 0.0
            salePrice = 0.0
            saleItem.clear()
            saleItemLog.clear()
            /*findViewById<TextView>(R.id.currentSaleItems).text = ""
            findViewById<TextView>(R.id.textViewTax).text =
                NumberFormat.getCurrencyInstance().format(0).toString()
            findViewById<TextView>(R.id.textViewSubTotal).text =
                NumberFormat.getCurrencyInstance().format(0).toString()
            findViewById<TextView>(R.id.textViewTotal).text =
                NumberFormat.getCurrencyInstance().format(0).toString()
            binding.root.rootView.refreshDrawableState()*/
        }
        binding.appBarMain.resetSale.setOnClickListener {
            saleReset()
        }
        if (checkUpdate) {
            checkUpdate = false
            // Instantiate the RequestQueue.
            val queue = Volley.newRequestQueue(this)
            val url = "https://toremetal.github.io/Pos/update.txt"
            // Request a string response from the provided URL.
            val stringRequest = StringRequest(
                Request.Method.GET, url,
                { response ->
                    // Display the first 500 characters of the response string.
                    if (response.substring(0, 8) == "flexible") {
                        fUpdate()
                    } else {
                        iUpdate()
                    }
                },
                {
                    iUpdate()
                })
            // Add the request to the RequestQueue.
            queue.add(stringRequest)
            /*val debugSettings = ConsentDebugSettings.Builder(this)
                .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                .addTestDeviceHashedId("937B8A3255686014E0F3F200ECECE2F4").build()

            val params = ConsentRequestParameters.Builder()
                .setConsentDebugSettings(debugSettings)
                .setTagForUnderAgeOfConsent(false)
                .build()*/
            val params = ConsentRequestParameters.Builder()
                .setTagForUnderAgeOfConsent(false)
                .build()
            val consentInformation = UserMessagingPlatform.getConsentInformation(this)
            //consentInformation.reset()
            consentInformation.requestConsentInfoUpdate(
                this,
                params,
                {
                    // The consent information state was updated, check if a form is available.
                    if (consentInformation.consentStatus == ConsentInformation.ConsentStatus.REQUIRED) {
                        npAds = true
                        if (consentInformation.isConsentFormAvailable) {
                        //    loadForm()
                        }
                    }
                },
                {
                    // Handle the error.
                    //showMessage(it.message)
                }
            )
        }
        //showAd()
    }

    /** Function [loadForm] : ConsentForm
     *
     *  Load or show User Data Consent form.
     *  Provides user consent services for use with the application. */
    private fun loadForm() {
        UserMessagingPlatform.loadConsentForm(
            this,
            { consentForm ->
                //this.consentForm.show(this.parent) {}
                consentForm.show(
                    this@MainActivity
                ) {
                    //loadForm()
                }
            },
            {
                /// Handle Error.
                loadForm()
            }
        )

    }

    /**
     * Requests the update availability for the current app,
     * and an intent to start an IMMEDIATE update flow.*/
    private fun iUpdate() {
        val appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(
                    AppUpdateType.IMMEDIATE
                )
            ) {
                appUpdateManager.startUpdateFlow(
                    appUpdateInfo, this, AppUpdateOptions.newBuilder(
                        AppUpdateType.IMMEDIATE
                    )
                        .build()
                )
            }
        }
    }

    /**
     * Requests the update availability for the current app,
     * and an intent to start a FLEXIBLE update flow.*/
    private fun fUpdate() {
        val appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(
                    AppUpdateType.FLEXIBLE
                )
            ) {
                appUpdateManager.startUpdateFlow(
                    appUpdateInfo, this, AppUpdateOptions.newBuilder(
                        AppUpdateType.FLEXIBLE
                    )
                        .setAllowAssetPackDeletion(true)
                        .build()
                )
                appUpdateManager.appUpdateInfo.addOnCompleteListener {
                    Snackbar.make(
                        binding.root.rootView,
                        "Update downloaded.",
                        Snackbar.LENGTH_INDEFINITE
                    ).apply {
                        setAction("RESTART") {
                            appUpdateManager.completeUpdate()
                        }
                        show()
                    }
                }
            }
        }
    }

    /**
     * Function showAd() initializes the ad server iterations.
     */
    private fun showAd() {
        // if (applicationInfo.packageName == "com.toremetal.pos") {
        MobileAds.initialize(this) {}
        mAdView = findViewById(R.id.adView)
        // adUnitId="ca-app-pub-8927593002808864/7430275914"
        // ID for testing banner adUnit: ca-app-pub-3940256099942544/6300978111
        val extras = Bundle()
        extras.putString("npa", "1")
        val adRequest = if (npAds) {
            AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter::class.java, extras).build()
        } else {
            AdRequest.Builder().build()
        }
        /*
        val testDeviceIds = listOf("937B8A3255686014E0F3F200ECECE2F4")
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)
        com.facebook.ads.AdSettings.addTestDevice("769d188b-fec2-49fe-9423-07347f5b4a4a")
        */
        mAdView.loadAd(adRequest)

        mAdView.adListener = object : AdListener() {
            override fun onAdClosed() {
                mAdView.postInvalidate()//Response.close()
                super.onAdClosed()
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                mAdView.invalidate()
                val adRequest2 = AdRequest.Builder().build()
                mAdView = findViewById(R.id.adView)
                mAdView.loadAd(adRequest2)
                //Log.e("adError", adError.toString())
                //noItem(0, adError.toString(), 1)
            }
        }
        //  } else {
        //  mAdView.visibility = View.GONE
        //  }
    }

    /**
     * Create the option dropdown menu containing file creation options.
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_activity, menu)
        return true
    }

    /**
     * Assign actions to the option dropdown menu items.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_export_csv -> {
                exportFile("csv")
                return true
            }
            R.id.action_export_pdf -> {
                if (openSaleLog(getString(R.string.salesTxt), false)) {
                    exportFile("pdf")
                    binding.navView.menu.getItem(4).isVisible = true
                    binding.navView.menu.getItem(5).isVisible = false
                }
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /*private fun exportDatabase() {
        try {
            val sd: File = Environment.getDownloadCacheDirectory()//.getRootDirectory()//.getStorageDirectory()//.getExternalStorageDirectory()
            val data: File = Environment.getDataDirectory()
            val currentDBPath = "/com.toremetal.pos/databases/item_database.db"
            val backupDBPath = "item_database.db"
            val currentDB = File(data, currentDBPath)
            val backupDB = File(sd, backupDBPath)
            val src: FileChannel = FileInputStream(currentDB).channel
            val dst: FileChannel = FileOutputStream(backupDB).channel
            dst.transferFrom(src, 0, src.size())
            src.close()
            dst.close()
        } catch (e: java.lang.Exception) {
        }
    }*/

    /*private fun importDatabase() {
        try {
            val sd: File = Environment.getDownloadCacheDirectory()//.getExternalStorageDirectory()
            val data: File = Environment.getDataDirectory()
            val currentDBPath =
                "/" + "com.toremetal.pos" + "/databases/" + "item_database.db"
            val backupDBPath = "item_database.db"
            val backupDB = File(data, currentDBPath)
            val currentDB = File(sd, backupDBPath)
            val src: FileChannel = FileInputStream(currentDB).channel
            val dst: FileChannel = FileOutputStream(backupDB).channel
            dst.transferFrom(src, 0, src.size())
            src.close()
            dst.close()
        } catch (e: java.lang.Exception) {
        }
    }*/

    /** File Handler for document display.
     * SuppressLint("MissingSuperCall")
     * (super.onActivityResult())
     * (Depreciated in java).
     * @param resultData contains a URI for the document or directory that the user selected.
     * @param requestCode determines the operation to perform on the document.
     * @param resultCode determines if the user accepted or cancelled the operation.
     */
    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(
        requestCode: Int, resultCode: Int, resultData: Intent?
    ) {
        //super.onActivityResult(requestCode, resultCode, resultData)
        if (requestCode == 1
            && resultCode == RESULT_OK
        ) {
            resultData?.data?.also { uri ->
                try {
                    if (getFileStreamPath(FILENAME2).isFile) {
                        application.contentResolver.openFileDescriptor(
                            FILENAME2URI,
                            "r"
                        )?.use { it ->
                            FileInputStream(it.fileDescriptor).use {
                                applicationContext.contentResolver.openFileDescriptor(uri, "w")
                                    ?.use { it2 ->
                                        FileOutputStream(it2.fileDescriptor).use { it3 ->
                                            it3.write(it.readBytes())
                                            it3.close()
                                        }
                                        it2.close()
                                    }
                                it.close()
                            }
                            it.close()
                        }
                        /*val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "application/pdf"
                                putExtra(Intent.EXTRA_STREAM, uri)
                                putExtra(
                                    Intent.EXTRA_SUBJECT,
                                    "${uri.lastPathSegment}: ${Calendar.getInstance().time}"
                                )
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "$myMes\n$myCopyright$mySignature"
                                )
                            }
                            Intent.createChooser(intent, getString(R.string.trademark)).apply {
                                startActivity(intent)
                            }*/
                    } else {
                        showMessage("No Sales Logged!")
                    }
                } catch (e: Exception) {
                    showMessage(e.message.toString())
                }
            }
        } else if (requestCode == 2
            && resultCode == RESULT_OK
        ) {
            resultData?.data?.also { uri ->
                try {
                    if (getFileStreamPath(getString(R.string.salesCsv)).isFile) {
                        val in1 = InputStreamReader(
                            openFileInput(
                                getString(R.string.salesCsv)
                            )
                        )
                        applicationContext.contentResolver.openFileDescriptor(uri, "w")
                            ?.use { it ->
                                FileOutputStream(it.fileDescriptor).use {
                                    it.write(
                                        in1.readText()//Overwritten at ${System.currentTimeMillis()}\n")
                                            .toByteArray()
                                    )
                                }
                            }
                        in1.close()
                    }
                } catch (e: Exception) {
                    showMessage(e.message.toString())
                }
            }
        } else if (requestCode == 3
            && resultCode == RESULT_OK
        ) {
            resultData?.data?.also { uri ->
                try {
                    itemInventory.clear()
                    itemInventory.add(itemsHeader)
                    for (item in saleItems) {
                        itemInventory.add(
                            "${item.id},${item.itemName},${item.itemDes},${item.quantityInStock},${item.itemPrice},${item.itemCost},${
                                item.itemCost.times(
                                    item.quantityInStock
                                )
                            },${item.itemPrice.times(item.quantityInStock)},${
                                (item.itemPrice.times(item.quantityInStock)).minus(
                                    item.itemCost.times(item.quantityInStock)
                                )
                            }\n"
                        )
                    }
                    val outFile =
                        OutputStreamWriter(openFileOutput("inv.tmp", 0))
                    outFile.write(
                        itemInventory.toString().replace("[", "").replace("]", "")
                    )
                    outFile.close()
                    val in1 = InputStreamReader(
                        openFileInput("inv.tmp")
                    )
                    applicationContext.contentResolver.openFileDescriptor(uri, "w")
                        ?.use { it ->
                            FileOutputStream(it.fileDescriptor).use {
                                it.write(
                                    in1.readText()
                                        .toByteArray()
                                )
                                it.close()
                            }
                        }
                    in1.close()
                    deleteFile("inv.tmp")
                } catch (e: Exception) {
                    showMessage(e.message.toString())
                }
            }
        } else if (requestCode == 4
            && resultCode == RESULT_OK
        ) {
            resultData?.data?.also { uri ->
                try {
                    binding.navView.menu.getItem(5).isVisible = true
                    FILENAME2 = getString(R.string.tmpPdf)
                    FILENAME2URI = uri
                } catch (e: Exception) {
                }
            }
        }
    }

    /**
     * Function [onSupportNavigateUp]
     *
     * Set the navigation backstack parameters for the application.
     */
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        //navController.currentBackStackEntry?.arguments?.clear()
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    /**
     *  Open the Sales Pdf.
     *
     *  Value [theFile]: String.
     *    Holds the Uri to the stored Data used to create the pdf.
     *
     *  Value [open]: Boolean.
     *    Determines if the file is displayed after creation.
     */
    private fun openSaleLog(theFile: String, open: Boolean = true): Boolean {
        if (getFileStreamPath(theFile).isFile) {
            try {
                var w = 800
                var h = 1000
                var pgHw = 750F
                var pgh1 = 900F
                var pgLn = 400F
                var h1 = 940F
                var h2 = 970F
                var h3 = 992F
                if (open) {
                    w = 400
                    h = 500
                    pgHw = 350F
                    pgh1 = 450F
                    pgLn = 200F
                    h1 = 470F
                    h2 = 485F
                    h3 = 496F
                }
                var pgNum = 1
                val document = PdfDocument()
                var pageInfo: PdfDocument.PageInfo =
                    PdfDocument.PageInfo.Builder(w, h, pgNum).create()
                var page: PdfDocument.Page = document.startPage(pageInfo)
                var canvas = page.canvas
                val paint = Paint()
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                paint.color = ContextCompat.getColor(this, R.color.grey)
                var y = 20F
                paint.textAlign = Paint.Align.LEFT
                paint.textSize = 14F
                canvas.drawText(
                    "$mySignature ${getString(R.string.sales)}",
                    50F,
                    y,
                    paint
                )
                paint.color = ContextCompat.getColor(this, R.color.black)
                paint.textAlign = Paint.Align.RIGHT
                canvas.drawText("${getString(R.string.pg)}${pgNum}", pgHw, y, paint)
                y += 40F
                paint.textAlign = Paint.Align.CENTER
                val openTheFile2 = openFileInput(theFile)
                openTheFile2.reader().forEachLine {
                    canvas.drawText(it, pgLn, y, paint)
                    y += 20F
                    if (y >= pgh1) {
                        paint.textSize = 12F
                        canvas.drawText(
                            "$mySignature ${getString(R.string.sales)}",
                            pgLn,
                            h1,
                            paint
                        )
                        canvas.drawText(mySignature, pgLn, h2, paint)
                        canvas.drawText("$myCopyright$mySignature", pgLn, h3, paint)
                        document.finishPage(page)
                        paint.textSize = 14F
                        y = 20F
                        pgNum++
                        pageInfo = PdfDocument.PageInfo.Builder(w, h, pgNum).create()
                        page = document.startPage(pageInfo)
                        canvas = page.canvas
                        paint.textAlign = Paint.Align.LEFT
                        paint.color = ContextCompat.getColor(this, R.color.grey)
                        canvas.drawText(
                            "$mySignature ${getString(R.string.sales)}",
                            50F,
                            y,
                            paint
                        )
                        paint.color = ContextCompat.getColor(this, R.color.black)
                        paint.textAlign = Paint.Align.RIGHT
                        canvas.drawText("${getString(R.string.pg)}${pgNum}", pgHw, y, paint)
                        y += 40F
                        paint.textAlign = Paint.Align.CENTER
                    }
                }
                openTheFile2.close()
                paint.textSize = 12F
                canvas.drawText(
                    "$mySignature ${getString(R.string.sales)}",
                    pgLn,
                    h1,
                    paint
                )
                canvas.drawText(mySignature, pgLn, h2, paint)
                canvas.drawText("$myCopyright$mySignature", pgLn, h3, paint)
                document.finishPage(page)
                document.writeTo(
                    openFileOutput(getString(R.string.tmpPdf), 0)
                )
                document.close()
                FILENAME2 = theFile
                FILENAME2URI =
                    getFileStreamPath(getString(R.string.tmpPdf)).toUri()
                if (open) {
                    val action =
                        HomeFragmentDirections.actionNavHomeToPdfRendererBasicFragment(getString(R.string.sales))
                    findNavController(R.id.nav_host_fragment_content_main).navigate(action)
                    try {
                        binding.drawerLayout.close()
                    } catch (e: Exception) {
                    }
                }
            } catch (e: Exception) {
            }
            return true
        } else {
            showMessage("No Sales Logged!")
            return false
        }
    }

    /** Function [exportFile(]theFile: String[)]:
     *
     * Create a sale log PDF or CSV file.
     *
     *  Variable [theFile]:String {Option value}.
     *
     *  Note:
     *    File is created at user selected location.
     *    Therefore requesting storage access permission
     *    only in the event the user requests it for
     *    saving user selected data. */
    private fun exportFile(theFile: String) {
        if (getFileStreamPath(getString(R.string.salesTxt)).isFile) {
            val amPm = if (Calendar.getInstance().get(Calendar.AM_PM).toString() == "0") {
                getString(R.string.am)
            } else {
                getString(R.string.pm)
            }
            var secTest = Calendar.getInstance().get(Calendar.MINUTE).toString()
            val minute = when (secTest) {
                "0" -> {
                    "00"
                }
                "1" -> {
                    "01"
                }
                "2" -> {
                    "02"
                }
                "3" -> {
                    "03"
                }
                "4" -> {
                    "04"
                }
                "5" -> {
                    "05"
                }
                "6" -> {
                    "06"
                }
                "7" -> {
                    "07"
                }
                "8" -> {
                    "08"
                }
                "9" -> {
                    "09"
                }
                else -> {
                    secTest
                }
            }
            secTest = Calendar.getInstance().get(Calendar.SECOND).toString()
            val second = when (secTest) {
                "0" -> {
                    "00"
                }
                "1" -> {
                    "01"
                }
                "2" -> {
                    "02"
                }
                "3" -> {
                    "03"
                }
                "4" -> {
                    "04"
                }
                "5" -> {
                    "05"
                }
                "6" -> {
                    "06"
                }
                "7" -> {
                    "07"
                }
                "8" -> {
                    "08"
                }
                "9" -> {
                    "09"
                }
                else -> {
                    secTest
                }
            }
            val invoice: String =
                (Calendar.getInstance()
                    .get(Calendar.MONTH) + 1).toString() + "_" + Calendar.getInstance()
                    .get(Calendar.DATE).toString() + "_" + Calendar.getInstance()
                    .get(Calendar.YEAR)
                    .toString() + "[" + Calendar.getInstance()
                    .get(Calendar.HOUR)
                    .toString() + "-" + minute + "-" + second + " " + amPm + "]"
            if (theFile == "csv") {
                val intent =
                    Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                        type = getString(R.string.textCsv)
                        putExtra(Intent.EXTRA_TITLE, "${getString(R.string.sales)}$invoice")
                    }
                startActivityIfNeeded(intent, 2)
            } else {
                val intent =
                    Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                        type = "application/pdf"
                        putExtra(Intent.EXTRA_TITLE, "${getString(R.string.sales)}$invoice")
                    }
                startActivityIfNeeded(intent, 1)
            }
        } else {
            showMessage("No Sales Logged!")
        }
    }

    private fun convertTax(xx: String) {
        taxRate = xx.toDouble().times(0.01)
    }

    private fun saleReset() {
        if (getFileStreamPath("${salesFile}.csv").isFile) {
            try {
                deleteFile("${salesFile}.csv")
            } catch (e: Exception) {
                showMessage("Error Deleting File:${salesFile}.csv (${e.message})")
            }
        }
        if (getFileStreamPath("${salesFile}.log").isFile) {
            try {
                deleteFile("${salesFile}.log")
                showMessage("Delete Sales Log: Successful")
            } catch (e: Exception) {
                showMessage("Error Deleting File:${salesFile}.log (${e.message})")
            }
        }
    }

    private fun showMessage(m: String) {
        Snackbar.make(binding.root.rootView, m, Snackbar.LENGTH_SHORT)
            .setAction("Action", null)
            .show()
    }
}