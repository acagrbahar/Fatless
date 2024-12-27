package com.acagribahar.bitirme


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.acagribahar.bitirme.databinding.ActivityExercisePlanBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExercisePlanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExercisePlanBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var analytics : FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityExercisePlanBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        firestore = FirebaseFirestore.getInstance()
        auth = Firebase.auth
        analytics = Firebase.analytics

        //Egzersizi kaydet butonuna tıklandığı zaman yapılacaklar.
        binding.saveExerciseButton.setOnClickListener {
            val exerciseName = binding.exerciseNameEditText.text.toString()
            val duration = binding.exerciseDurationEditText.text.toString().toIntOrNull()
            val difficulty = binding.exerciseDifficultyEditText.text.toString()

            if (exerciseName.isNotEmpty() && duration != null && difficulty.isNotEmpty()) {
                saveExercisePlan(exerciseName, duration, difficulty)
            } else {
                Toast.makeText(this, "Tüm alanları doldurun", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun saveExercisePlan(exerciseName: String, duration: Int, difficulty: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val exercise = hashMapOf(
                "exerciseName" to exerciseName,
                "duration" to duration,
                "difficulty" to difficulty
            )

            firestore.collection("users").document(userId).collection("exercises")
                .add(exercise)
                .addOnSuccessListener {
                    Toast.makeText(this, "Egzersiz başarıyla kaydedildi", Toast.LENGTH_LONG).show()

                    //Firebase Analytics'e gonderme islemi

                    saveDailyProgress(exerciseName,duration,difficulty)
                    val bundle = Bundle().apply {

                        putString("exercise_name",exerciseName)
                        putInt("duration",duration)
                        putString("difficulty",difficulty)
                    }
                    analytics.logEvent("exercise_added",bundle)

                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Egzersiz kaydedilemedi: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }

    //Firebase Analytics Kısmı
    private fun saveDailyProgress(exerciseName: String, duration: Int, difficulty: String) {
        val userId = auth.currentUser?.uid
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        if (userId != null) {
            val progressData = hashMapOf(
                "exerciseName" to exerciseName,
                "duration" to duration,
                "difficulty" to difficulty,
                "timestamp" to FieldValue.serverTimestamp()
            )

            firestore.collection("users").document(userId)
                .collection("progress").document(currentDate)
                .collection("exercises").add(progressData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Günlük ilerleme kaydedildi.", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Günlük ilerleme kaydedilemedi: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun geriGit(view : View){
        val intent = Intent(this@ExercisePlanActivity,GoalActivity::class.java)
        startActivity(intent)
    }

    fun devam(view : View){
        val intent = Intent(this@ExercisePlanActivity,ExerciseListActivity::class.java)
        startActivity(intent)

    }
}