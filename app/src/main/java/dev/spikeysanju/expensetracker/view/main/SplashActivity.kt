package dev.spikeysanju.expensetracker.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dev.spikeysanju.expensetracker.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

      goToNext()


    }
    private fun goToNext(){

        if (Firebase.auth.currentUser == null){

            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, OnboardingActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000)

        }else{

            startActivity(Intent(this,MainActivity::class.java))



        }
    }
}
