package com.acagribahar.bitirme


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.acagribahar.bitirme.databinding.ActivityGoalBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class GoalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGoalBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGoalBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        firestore = FirebaseFirestore.getInstance()
        auth = Firebase.auth

        //Butona tıklandığında kontrolleri yaparak hedefleri kaydeder.
        binding.saveGoalButton.setOnClickListener {
            val workoutDays = binding.workoutDaysEditText.text.toString().toIntOrNull()
            val time = binding.workoutTimeEditText.text.toString().toIntOrNull()
            val exType = binding.workoutTypeEditText.text.toString()

            if (exType.isNotEmpty() && workoutDays != null && time != null) {
                saveUserGoal(workoutDays,time,exType)
            } else {
                Toast.makeText(this, "Lütfen geçerli bir hedef girin.", Toast.LENGTH_SHORT).show()
            }
        }
        // Adım sayar butonuna tıklayınca Adım sayar ekranına geçiş yapar.
        binding.stepButton.setOnClickListener {
            val intent = Intent(this@GoalActivity,StepCounterActivity::class.java)
            startActivity(intent)

        }
    }

    //
    private fun saveUserGoal(workoutDays: Int , time : Int , exType : String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userGoal = hashMapOf(
                "workoutDays" to workoutDays,
                "time" to time,
                "exType" to exType
            )

            firestore.collection("users").document(userId).update(userGoal as Map<String, Any>)
                .addOnSuccessListener {
                    Toast.makeText(this, "Hedef kaydedildi.", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Hedef kaydedilemedi: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    //Butona tıklandıgında onceki sayfaya donus yapilacak.
    fun oncekiSayfayaDon(view : View){
        val intent = Intent(this@GoalActivity , ProfileActivity::class.java)
        startActivity(intent)
    }

    fun devamEt(view: View){
        val intent = Intent(this@GoalActivity,ExercisePlanActivity::class.java)
        startActivity(intent)
    }
}