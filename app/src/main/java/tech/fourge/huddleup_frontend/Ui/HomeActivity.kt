package tech.fourge.huddleup_frontend.Ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import tech.fourge.huddleup_frontend.Utils.openIntent
import tech.fourge.huddleup_frontend.databinding.HomePageBinding

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = HomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.testbutton.setOnClickListener {
            Log.d("MainActivity", "Join Team button clicked")
            openIntent(this, SettingsActivity::class.java)
        }


    }
}