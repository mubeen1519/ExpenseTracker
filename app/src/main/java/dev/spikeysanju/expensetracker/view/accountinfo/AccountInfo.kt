package dev.spikeysanju.expensetracker.view.accountinfo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import dev.spikeysanju.expensetracker.databinding.ActivityAccountInfoBinding
import dev.spikeysanju.expensetracker.model.User

class AccountInfo : AppCompatActivity() {

    private val binding by lazy {
        ActivityAccountInfoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        showProfileImage()
        getDataFromRealTimeDatabase()


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

    fun getDataFromRealTimeDatabase() {
        var db = FirebaseDatabase.getInstance().reference
        db.child("Users").child(Firebase.auth.uid!!).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {

                    val user = snapshot.getValue(User::class.java)
                    var email = user?.email
                    var name = user?.name
                    binding.email.text = email
                    binding.name.text = name

                }

            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(this@AccountInfo, "Some thing is wrong", Toast.LENGTH_SHORT).show()
            }

        })

    }

}


