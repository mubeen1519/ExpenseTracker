package dev.spikeysanju.expensetracker.model

data class User(

    var name : String="",
    var email : String="",
    var username : String="",
    var profileImage : String="",
    var password : String=""
) {
    fun toMap(): Map<String, String> {
        return mapOf(
            "name" to name,
            "email" to email,
            "username" to username,
            "profileImage" to profileImage,
            "password" to password
        )
    }
}
