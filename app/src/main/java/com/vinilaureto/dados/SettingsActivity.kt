package com.vinilaureto.dados

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.vinilaureto.dados.SettingsActivity.Constants.CONFIGURATION_FILE
import com.vinilaureto.dados.SettingsActivity.Constants.DICES_NUMBER
import com.vinilaureto.dados.SettingsActivity.Constants.FACES_NUMBER
import com.vinilaureto.dados.SettingsActivity.Constants.VALUE_NOT_FOUND
import com.vinilaureto.dados.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var activitySettingsBinding: ActivitySettingsBinding
    private object Constants {
        val CONFIGURATION_FILE = "configurations"
        val DICES_NUMBER = "dicesNumber"
        val FACES_NUMBER = "facesNumber"
        val VALUE_NOT_FOUND = -1
    }

    private lateinit var configurationSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySettingsBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(activitySettingsBinding.root)

        // Abre ou cria o arquivo de configurações
        configurationSharedPreferences = getSharedPreferences(CONFIGURATION_FILE, MODE_PRIVATE)
        loadConfigurations()

        activitySettingsBinding.saveBt.setOnClickListener {
            val numDices: Int = (activitySettingsBinding.numberOfDicesSp.selectedView as TextView).text.toString().toInt()
            val numFaces: Int = activitySettingsBinding.numberOfFacesEt.text.toString().toInt()

            // Salvar as configurações no arquivo
            saveConfigurations(numDices, numFaces)

            val configuration = Configuration(numDices, numFaces)
            val responseIntent = Intent()
            responseIntent.putExtra(Intent.EXTRA_USER, configuration)
            setResult(RESULT_OK, responseIntent)
            finish()
        }
    }

    private fun loadConfigurations() {
        val dicesNumber: Int = configurationSharedPreferences.getInt(DICES_NUMBER, VALUE_NOT_FOUND)
        val facesNumber: Int = configurationSharedPreferences.getInt(FACES_NUMBER, VALUE_NOT_FOUND)

        if (dicesNumber != VALUE_NOT_FOUND) {
            activitySettingsBinding.numberOfDicesSp.setSelection(dicesNumber -1)
        }

        if (facesNumber != VALUE_NOT_FOUND) {
            activitySettingsBinding.numberOfFacesEt.setText(facesNumber.toString())
        }
    }

    private fun saveConfigurations(numDices: Int, numFaces: Int) {
        val editorSharedPreferences = configurationSharedPreferences.edit()
        editorSharedPreferences.putInt(DICES_NUMBER, numDices)
        editorSharedPreferences.putInt(FACES_NUMBER, numFaces)
        editorSharedPreferences.commit()
    }
}