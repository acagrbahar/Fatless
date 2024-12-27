package com.acagribahar.bitirme


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.acagribahar.bitirme.databinding.ActivityProfileBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        auth = Firebase.auth

        binding.saveProfileButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val age = binding.ageEditText.text.toString().toIntOrNull()
            val weight = binding.weightEditText.text.toString().toDoubleOrNull()
            val height = binding.heightEditText.text.toString().toDoubleOrNull()

            if (name.isNotEmpty() && age != null && weight != null && height != null) {
                saveUserProfile(name, age, weight, height)
            } else {
                Toast.makeText(this, "Lütfen tüm bilgileri doğru girin.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserProfile(name: String, age: Int, weight: Double, height: Double) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userProfile = hashMapOf(
                "name" to name,
                "age" to age,
                "weight" to weight,
                "height" to height
            )

            firestore.collection("users").document(userId).set(userProfile)
                .addOnSuccessListener {
                    Toast.makeText(this, "Profil kaydedildi.", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Profil kaydedilemedi: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun girisEkraninaDon(view: View){
        val intent = Intent(this@ProfileActivity, MainActivity::class.java)
        startActivity(intent)
    }

    fun nextActivity(view : View){
        val intent = Intent(this@ProfileActivity, GoalActivity::class.java)
        startActivity(intent)
    }
}

