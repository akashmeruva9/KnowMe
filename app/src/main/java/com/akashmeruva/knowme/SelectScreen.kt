package com.akashmeruva.knowme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SelectScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_screen)

        var static_btn = findViewById<Button>(R.id.statict_btn)
        var realtime_btn = findViewById<Button>(R.id.Realtime_btn)

        realtime_btn.setOnClickListener {
            val intent = Intent(applicationContext , MainActivity::class.java)
            startActivity(intent)
        }

        static_btn.setOnClickListener {
            val intent = Intent(applicationContext , StaticActivity::class.java)
            startActivity(intent)
        }

    }
}