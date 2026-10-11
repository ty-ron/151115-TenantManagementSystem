package com.example.tms

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tms.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var lastTenant: Tenant? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Task 2: Welcome the user
        val loggedInEmail = intent.getStringExtra("EMAIL")
        if (loggedInEmail != null) {
            Toast.makeText(this, "Logged in as $loggedInEmail", Toast.LENGTH_LONG).show()
        }

        // SAVE, with Task 3 validation
        binding.saveButton.setOnClickListener {
            val name = binding.tenantNameEditText.text.toString().trim()
            val phone = binding.phoneEditText.text.toString().trim()
            val rent = binding.rentEditText.text.toString().trim()

            var valid = true
            if (name.isEmpty()) { binding.tenantNameEditText.error = "Required"; valid = false }
            if (phone.isEmpty()) { binding.phoneEditText.error = "Required"; valid = false }
            if (rent.isEmpty()) { binding.rentEditText.error = "Required"; valid = false }
            if (!valid) return@setOnClickListener

            val tenant = Tenant(name, phone, rent)
            binding.tenant = tenant
            lastTenant = tenant
        }

        // CALL: implicit Intent with ACTION_DIAL
        binding.callButton.setOnClickListener {
            val tenant = lastTenant
            if (tenant == null) {
                Toast.makeText(this, "Save a tenant first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${tenant.phone}"))
            startActivity(intent)
        }

        // Task 1: SHARE, implicit Intent with ACTION_SEND
        binding.shareButton.setOnClickListener {
            val tenant = lastTenant
            if (tenant == null) {
                Toast.makeText(this, "Save a tenant first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, tenant.summary())
            startActivity(Intent.createChooser(intent, "Share tenant"))
        }
    }
}