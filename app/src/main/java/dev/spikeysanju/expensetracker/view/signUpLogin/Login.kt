package dev.spikeysanju.expensetracker.view.signUpLogin

import ShowToast
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import dev.spikeysanju.expensetracker.R
import dev.spikeysanju.expensetracker.databinding.ActivityLoginBinding
import dev.spikeysanju.expensetracker.model.User
import dev.spikeysanju.expensetracker.view.dashboard.DashboardFragment
import dev.spikeysanju.expensetracker.view.main.MainActivity

class Login : AppCompatActivity() {
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            loginUser()
        }
        binding.createAccountTextviewBtn.setOnClickListener {
            startActivity(Intent(this,SignUp::class.java))
        }

    }

    private fun loginUser() {

        var emil : String = binding.emil.text.toString()
        var password : String = binding.password.text.toString()

        var isvalidate= validateData(emil,password)

        if (!isvalidate){
            return
        }

        createAccountInFirebase(emil,password)
    }

    private fun createAccountInFirebase(emil: String, password: String) {
        changeProgressBar(true)
       // var firebaseAuth = FirebaseAuth.getInstance()
        var firebaseAuth = Firebase.auth

        firebaseAuth.signInWithEmailAndPassword(emil,password).addOnCompleteListener {
            if (it.isSuccessful){
                changeProgressBar(false)

                startActivity(Intent(this,MainActivity::class.java))

            }else{
                ShowToast(this,it.exception!!.localizedMessage)
            }
        }

    }


    private fun changeProgressBar(inProgressBar : Boolean){

        if (inProgressBar){
            binding.progressBar.visibility = View.VISIBLE
            binding.btnLogin.visibility = View.GONE
        }else{
            binding.progressBar.visibility= View.GONE
            binding.btnLogin.visibility= View.VISIBLE
        }
    }
    private fun validateData(emil: String, password : String) : Boolean {

        //validate the data that are input by user
        if(!Patterns.EMAIL_ADDRESS.matcher(emil).matches()){
            binding.emil.setError("Email is invalid")
            return false
        }

        if (password.length<6){
            binding.password.setError("Password length is so small")
            return false
        }

        return true

    }

}

