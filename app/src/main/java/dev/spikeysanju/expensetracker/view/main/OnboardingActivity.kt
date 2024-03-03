package dev.spikeysanju.expensetracker.view.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dev.spikeysanju.expensetracker.R
import dev.spikeysanju.expensetracker.databinding.ActivityOnboardingBinding
import dev.spikeysanju.expensetracker.view.signUpLogin.Login
import dev.spikeysanju.expensetracker.view.signUpLogin.SignUp

class OnboardingActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityOnboardingBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        if (isFirstTimeLaunch()){

            setContentView(binding.root)

            binding.button.setOnClickListener {
                startActivity(Intent(this,SignUp::class.java))
            }
            binding.loginTextviewBtn.setOnClickListener {
                startActivity(Intent(this,Login::class.java))
            }
            markFirstTimeLaunch(false)
        }else{
            startActivity(Intent(this,MainActivity::class.java))
        }





    }

    private fun isFirstTimeLaunch():Boolean{

        var sharedPreferences : SharedPreferences = getSharedPreferences("", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isFirstTimeLaunch",true)
    }

    private fun markFirstTimeLaunch(isFirstTime : Boolean){

        var sharedPreferences : SharedPreferences = getSharedPreferences("", Context.MODE_PRIVATE)
        var editor = sharedPreferences.edit()
        editor.putBoolean("isFirstTime",isFirstTime)
        editor.apply()

    }

//    fun goToNext(){
//
//        if (Firebase.auth != null){
//
//            startActivity(Intent(this,MainActivity::class.java))
//
//        }else{
//
//            startActivity(Intent(this,SignUp::class.java))
//
//        }
//    }

}
