package dev.spikeysanju.expensetracker.view.profile

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import dev.spikeysanju.expensetracker.R
import dev.spikeysanju.expensetracker.databinding.FragmentProfileBinding
import dev.spikeysanju.expensetracker.model.User
import dev.spikeysanju.expensetracker.utils.viewState.uploadImageToFirebase
import dev.spikeysanju.expensetracker.view.accountinfo.AccountInfo
import dev.spikeysanju.expensetracker.view.base.BaseFragment
import dev.spikeysanju.expensetracker.view.editprofile.EditProfile
import dev.spikeysanju.expensetracker.view.invitefriend.InviteFriends
import dev.spikeysanju.expensetracker.view.main.viewmodel.TransactionViewModel
import dev.spikeysanju.expensetracker.view.signUpLogin.Login

@AndroidEntryPoint
class ProfileFragment() : BaseFragment<FragmentProfileBinding, TransactionViewModel>() {

    override val viewModel: TransactionViewModel by activityViewModels()

    var user = User()


    val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { ImageUri ->

        if (ImageUri == null) {
            Toast.makeText(requireContext(), "Image is Must....", Toast.LENGTH_SHORT).show()
        } else {

            // saveImageUri(requireContext(), ImageUri.toString())

            uploadImageToFirebase(requireContext(), ImageUri) {

                user.profileImage = ImageUri.toString()
                binding.profileImage.setImageURI(ImageUri)
            }
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }


    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentProfileBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        showProfileImage()
        getDataFromRealTimeDatabase()

        binding.profileImage.setOnClickListener {

            galleryLauncher.launch("image/*")
            // getSaveImageUri(requireContext())
        }




        binding.logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(), Login::class.java))
        }

        binding.invitelayout.setOnClickListener {

            showDialogBox()
        }

        binding.accountInfoLayout.setOnClickListener {
            startActivity(Intent(requireContext(), AccountInfo::class.java))
        }
        binding.editProfileLayout.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfile::class.java))
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


    // profileImage store in sharedPreference
    fun saveImageUri(context: Context, imageUri: String) {

        val appContext = context.applicationContext
        var sharedPreferences: SharedPreferences =
            appContext.getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
        var editor = sharedPreferences.edit()

        editor.putString("profileImageUri", imageUri)
        editor.apply()
    }

    fun getSaveImageUri(context: Context): String? {
        val appContext = context.applicationContext
        var sharedPreferences = appContext.getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("profileImageUri", null)
    }
    //sharePreference



    //DialogBox
    fun showDialogBox() {

        var dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.custom_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        var btn = dialog.findViewById<Button>(R.id.invite_btn)


        btn.setOnClickListener {

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/")
            startActivity(Intent.createChooser(intent, "Share link via"))

        }
        dialog.show()


    }


    //retrive data from real time database
    fun getDataFromRealTimeDatabase() {
        var db = FirebaseDatabase.getInstance().reference
        db.child("Users").child(Firebase.auth.uid!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()) {

                        val user = snapshot.getValue(User::class.java)
                        var name = user?.name
                        var username = user?.username
                        binding.name.text = name
                        binding.username.text = username

                    }

                }

                override fun onCancelled(error: DatabaseError) {

                    Toast.makeText(applicationContext(), "Some thing is wrong", Toast.LENGTH_SHORT).show()
                }

            })

    }


//    override fun onPause() {
//        super.onPause()
//        var saveImageUri = getSaveImageUri(requireContext())
//        saveImageUri?.let {
//            binding.profileImage.setImageURI(Uri.parse(saveImageUri))
//        }
//    }
//    override fun onDestroy() {
//        super.onDestroy()
//        var saveImageUri = getSaveImageUri(requireContext())
//        saveImageUri?.let {
//            binding.profileImage.setImageURI(Uri.parse(saveImageUri))
//        }
//    }
//    override fun onResume() {
//        super.onResume()
//        var saveImageUri = getSaveImageUri(requireContext())
//        saveImageUri?.let {
//            binding.profileImage.setImageURI(Uri.parse(saveImageUri))
//        }
//    }

}
