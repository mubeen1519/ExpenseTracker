package dev.spikeysanju.expensetracker.view.signUpLogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import com.google.firebase.firestore.firestore
import dev.spikeysanju.expensetracker.R
import dev.spikeysanju.expensetracker.databinding.ActivitySignUpBinding
import dev.spikeysanju.expensetracker.model.User

class SignUp : AppCompatActivity() {

    private val binding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnCreateAccount.setOnClickListener {
            createAccount()
        }
        binding.loginTextviewBtn.setOnClickListener {
            startActivity(Intent(this,Login::class.java))
        }

    }

    private fun createAccount() {

        val name :String = binding.name.text.toString()
        val emil : String = binding.emil.text.toString()
        val password : String = binding.password.text.toString()
        val conformPassword : String = binding.conformPassword.text.toString()

        val isvalidate= validateData(name,emil,password,conformPassword)

        if (!isvalidate){
            return
        }

        createAccountInFirebase(emil,password)

    }

    private fun createAccountInFirebase(email : String, password : String){

        changeProgressBar(true)
       // val firebaseAuth  = FirebaseAuth.getInstance()
        val firebaseAuth  = Firebase.auth
        var db = Firebase.database
        var userData = User(
            name = binding.name.text.toString(),
            email= binding.emil.text.toString(),
            password = binding.password.text.toString()
        )
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
            if (it.isSuccessful){
                changeProgressBar(false)

                db.reference.child("Users").child(Firebase.auth.uid!!).setValue(userData).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(this,"User successfully added",Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this,it.localizedMessage,Toast.LENGTH_SHORT).show()
                }

               // Toast.makeText(this, "Successfully create Account", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,Login::class.java))
                firebaseAuth.signOut()
            }
        }.addOnFailureListener {
            Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
        }

    }

    private fun changeProgressBar(inProgressBar : Boolean){

        if (inProgressBar){
            binding.progressBar.visibility = View.VISIBLE
            binding.btnCreateAccount.visibility = View.GONE
        }else{
            binding.progressBar.visibility= View.GONE
            binding.btnCreateAccount.visibility= View.VISIBLE
        }
    }





    private fun validateData(name : String , emil: String, password : String, conformPassword: String) : Boolean {

        //validate the data that are input by user

        if (name == null){
            binding.name.error = "Please Name!"
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emil).matches()){
            binding.emil.error = "Email is invalid"
            return false
        }

        if (password.length<6){
            binding.password.error = "Password length is so small"
            return false
        }
        if (password != conformPassword){

            binding.conformPassword.error = "Password doesn't matches"
            return false
        }

        return true

    }



}

