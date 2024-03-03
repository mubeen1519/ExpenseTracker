package dev.spikeysanju.expensetracker.utils.viewState

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.widget.ImageView
import android.widget.Toast
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage


@SuppressLint("SuspiciousIndentation")
fun uploadImageToFirebase(context: Context, fileUri : Uri, function : (imageurl: String)->Unit){

  var ref : StorageReference = FirebaseStorage.getInstance().reference

    ref.child("ProfileImages").child(Firebase.auth.uid!!).putFile(fileUri).addOnCompleteListener{

        if (it.isSuccessful){
            it.result.storage.downloadUrl.addOnSuccessListener {
                function(it.toString())
            }
        }

    }.addOnFailureListener{
        Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT).show()
    }

}


//fun getImageFromFirebase(context: Context,imageurl: String,function: (imageurl: Uri?) -> Unit){
//
//    var storageRef: StorageReference = FirebaseStorage.getInstance().reference
//    var imageRef = storageRef.child(imageurl)
//
//
//
//}


fun loadImageFromFirebase(imageUrl: String, imageView: ImageView) {
    val storageReference = FirebaseStorage.getInstance().reference
    val imageRef = storageReference.child(imageUrl)

    imageRef.downloadUrl.addOnSuccessListener { uri ->

        Glide.with(imageView.context)
            .load(uri)
            .into(imageView)
    }.addOnFailureListener { exception ->
        exception.printStackTrace()
    }
}


