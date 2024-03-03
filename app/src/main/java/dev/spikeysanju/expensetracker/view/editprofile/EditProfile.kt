package dev.spikeysanju.expensetracker.view.editprofile

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import dev.spikeysanju.expensetracker.R
import dev.spikeysanju.expensetracker.databinding.ActivityEditProfileBinding
import dev.spikeysanju.expensetracker.model.User
import dev.spikeysanju.expensetracker.utils.viewState.loadImageFromFirebase
import dev.spikeysanju.expensetracker.utils.viewState.uploadImageToFirebase
import dev.spikeysanju.expensetracker.view.profile.ProfileFragment

class EditProfile : AppCompatActivity() {
    private val binding by lazy {
        ActivityEditProfileBinding.inflate(layoutInflater)
    }

    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

          showProfileImage()


        //user data show on textfield view
        val userId = Firebase.auth.currentUser?.uid
        if (userId != null) {
            val database = FirebaseDatabase.getInstance().getReference("Users")
            val userRef = database.child(userId)

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userData = snapshot.getValue(User::class.java) // Assuming User is your data class representing user data
                    if (userData != null) {
                        // Populate EditText fields with existing user data
                        binding.email.setText(userData.email)
                        binding.name.setText(userData.name)
                        binding.password.setText(userData.password)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error

                }
            })
        }

        binding.update.setOnClickListener {

            var email = binding.email.text.toString()
            var name = binding.name.text.toString()
            var password = binding.password.text.toString()

            updateProfileData(email,name,password)
            Toast.makeText(this, "Data successfully Updated", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,ProfileFragment::class.java))

        }



    }


    //showProfileImage on Profile
    fun showProfileImage() {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.getReference()

        storageRef.child("ProfileImages").child(Firebase.auth.uid!!).downloadUrl.addOnSuccessListener { uri ->

            Glide.with(binding.profileImage.context)
                .load(uri)
                .into(binding.profileImage)

        }.addOnFailureListener { exception ->
            exception.printStackTrace()
        }
    }


    fun updateProfileData(email : String,name : String, password : String){


        database = FirebaseDatabase.getInstance().getReference("Users")

        var user = mapOf<String,String>(

            "email" to email,
            "name" to name,
            "password" to password,
            //"profileImage" to profileImage
        )
        database.child(Firebase.auth.uid!!).updateChildren(user).addOnSuccessListener {

        }.addOnFailureListener {
            Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
        }

    }


}








//val storage = FirebaseStorage.getInstance()
//val storageRef = storage.getReference()
//
//storageRef.child("ProfileImages").child(Firebase.auth.uid!!).downloadUrl.addOnSuccessListener { uri ->
//
//    Glide.with(binding.profileImage.context)
//        .load(uri)
//        .into(binding.profileImage)
//
//}.addOnFailureListener { exception ->
//    exception.printStackTrace()
//}
