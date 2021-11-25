package com.vinilaureto.dados

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.vinilaureto.dados.databinding.ActivityMainBinding
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var randomNumberGenerator: Random

    private lateinit var settingsActivityLauncher: ActivityResultLauncher<Intent>
    private var contextConfiguration = Configuration()
    private lateinit var configurationSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        configurationSharedPreferences = getSharedPreferences(SettingsActivity.Constants.CONFIGURATION_FILE, MODE_PRIVATE)
        startWithPreviousConfiguration()

        randomNumberGenerator = Random(System.currentTimeMillis())

        activityMainBinding.playBt.setOnClickListener {
            val result: Int = randomNumberGenerator.nextInt(1..contextConfiguration.numFace)
            if (contextConfiguration.numDices == 1) {
                "O número sorteado foi $result".also { activityMainBinding.resultsTv.text = it }
                val imageName = "dice_$result"
                activityMainBinding.resultsIvFirst.setImageResource(
                    resources.getIdentifier(imageName, "mipmap", packageName)
                )
            } else {
                val secondResult: Int = randomNumberGenerator.nextInt(1..contextConfiguration.numFace)
                "Os números sorteados foram $result e $secondResult ".also { activityMainBinding.resultsTv.text = it }
                val firstImageName: String = "dice_$result"
                val secondImageName: String = "dice_$secondResult"
                activityMainBinding.resultsIvFirst.setImageResource(
                    resources.getIdentifier(firstImageName, "mipmap", packageName)
                )
                activityMainBinding.resultsIvSecond.setImageResource(
                    resources.getIdentifier(secondImageName, "mipmap", packageName)
                )
            }
        }

        settingsActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result ->
            if (result.resultCode == RESULT_OK) {
                // Modificações na main view
                if (result.data != null) {
                    val configuration: Configuration? = result.data?.getParcelableExtra<Configuration>(Intent.EXTRA_USER)
                    contextConfiguration.numDices = configuration?.numDices!!
                    contextConfiguration.numFace = configuration?.numFace!!

                    if (configuration?.numDices!! == 1) {
                        activityMainBinding.resultsIvSecond.visibility = View.GONE
                    } else {
                        activityMainBinding.resultsIvSecond.visibility = View.VISIBLE
                    }

                    if (configuration?.numFace!! > 6) {
                        activityMainBinding.dicesLl.visibility = View.GONE
                    } else {
                        activityMainBinding.dicesLl.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settingsMi) {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            settingsActivityLauncher.launch(settingsIntent)
            return true
        }
        return false
    }

    private fun startWithPreviousConfiguration() {
        val dicesNumber: Int = configurationSharedPreferences.getInt(
            SettingsActivity.Constants.DICES_NUMBER,
            SettingsActivity.Constants.VALUE_NOT_FOUND
        )
        val facesNumber: Int = configurationSharedPreferences.getInt(
            SettingsActivity.Constants.FACES_NUMBER,
            SettingsActivity.Constants.VALUE_NOT_FOUND
        )

        if (dicesNumber != SettingsActivity.Constants.VALUE_NOT_FOUND) {
            contextConfiguration.numDices = dicesNumber
        }

        if (facesNumber != SettingsActivity.Constants.VALUE_NOT_FOUND) {
            contextConfiguration.numFace = facesNumber
        }
    }
}