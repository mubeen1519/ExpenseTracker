package dev.spikeysanju.expensetracker.view.invitefriend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.spikeysanju.expensetracker.R
import dev.spikeysanju.expensetracker.databinding.ActivityInviteFriendsBinding

class InviteFriends : AppCompatActivity() {
    private val binding by lazy {
        ActivityInviteFriendsBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.inviteBtn.setOnClickListener {

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/")
            startActivity(Intent.createChooser(intent, "Share link via"))

        }
    }
}
