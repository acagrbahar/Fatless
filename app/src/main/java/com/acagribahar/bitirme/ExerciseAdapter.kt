package com.acagribahar.bitirme


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.acagribahar.bitirme.databinding.ItemExerciseBinding

class ExerciseAdapter (private val exerciseList: List<Exercise>) :
    RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>()  {

    // ViewHolder için ViewBinding'i kullanıyoruz
    class ExerciseViewHolder(val binding: ItemExerciseBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        // ViewBinding ile item_exercise layout'unu bağla
        val binding = ItemExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExerciseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exerciseList[position]

        // ViewBinding ile XML'deki görünümleri kullanıyoruz
        holder.binding.apply {
            exerciseNameTextView.text = exercise.exerciseName
            exerciseDurationTextView.text = "Süre: ${exercise.duration} dakika"
            exerciseDifficultyTextView.text = "Zorluk: ${exercise.difficulty}"
        }
    }

    override fun getItemCount(): Int {
        return exerciseList.size
    }
}