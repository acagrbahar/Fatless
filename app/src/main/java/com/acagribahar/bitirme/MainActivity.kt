package com.acagribahar.bitirme

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.acagribahar.bitirme.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            registerUser(email, password)
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            loginUser(email, password)
        }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Kullanıcı başarıyla kayıt oldu
                    val user = auth.currentUser
                    Toast.makeText(this, "Kayıt Başarılı", Toast.LENGTH_SHORT).show()
                } else {
                    // Kayıt başarısız
                    Toast.makeText(this, "Kayıt Hatası: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Giriş başarılı
                    val user = auth.currentUser
                    Toast.makeText(this, "Giriş Başarılı", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity , ProfileActivity::class.java)
                    startActivity(intent)
                } else {
                    // Giriş hatası
                    Toast.makeText(this, "Giriş Hatası: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}




