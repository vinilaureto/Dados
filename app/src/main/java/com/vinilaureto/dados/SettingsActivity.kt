package com.vinilaureto.dados

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.vinilaureto.dados.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var activitySettingsBinding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySettingsBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(activitySettingsBinding.root)

        activitySettingsBinding.saveBt.setOnClickListener {
            val numDices: Int = (activitySettingsBinding.numberOfDicesSp.selectedView as TextView).text.toString().toInt()
            val numFaces: Int = activitySettingsBinding.numberOfFacesEt.text.toString().toInt()
            val configuration = Configuration(numDices, numFaces)
            val responseIntent = Intent()
            responseIntent.putExtra(Intent.EXTRA_USER, configuration)
            setResult(RESULT_OK, responseIntent)
            finish()
        }
    }
}