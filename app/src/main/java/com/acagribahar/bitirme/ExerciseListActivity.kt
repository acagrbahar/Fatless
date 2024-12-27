package com.acagribahar.bitirme


import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.acagribahar.bitirme.databinding.ActivityExerciseListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class ExerciseListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExerciseListBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore : FirebaseFirestore
    private lateinit var exerciseAdapter: ExerciseAdapter
    private val exerciseList = mutableListOf<Exercise>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityExerciseListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        firestore = FirebaseFirestore.getInstance()
        auth = Firebase.auth

        // RecyclerView'e adapter bağlama
        exerciseAdapter = ExerciseAdapter(exerciseList)
        binding.exerciseRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.exerciseRecyclerView.adapter = exerciseAdapter

        loadExercises()
    }

    private fun loadExercises() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            firestore.collection("users").document(userId).collection("exercises")
                .get()
                .addOnSuccessListener { result ->
                    exerciseList.clear()
                    for (document in result) {
                        val exercise = document.toObject(Exercise::class.java)
                        exerciseList.add(exercise)
                    }
                    exerciseAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Egzersizler yüklenemedi: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}