package com.robistim.marsquiz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.robistim.marsquiz.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    private lateinit var questions: List<Question>
    private var current = 0
    private var score = 0
    private var answered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        questions = QuestionRepo.loadRandomFive(this)

        binding.btnNext.setOnClickListener {
            if (!answered) {
                Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            current++
            if (current >= questions.size) {
                val i = Intent(this, ResultActivity::class.java)
                i.putExtra("score", score)
                i.putExtra("total", questions.size)
                startActivity(i)
                finish()
            } else {
                showQuestion()
            }
        }

        showQuestion()
    }

    private fun showQuestion() {
        answered = false
        val q = questions[current]
        binding.tvProgress.text = "Question ${current+1}/${questions.size}"
        binding.tvQuestion.text = q.text
        binding.btnA.text = q.choices[0]
        binding.btnB.text = q.choices[1]
        binding.btnC.text = q.choices[2]
        binding.btnD.text = q.choices[3]

        val buttons = listOf(binding.btnA, binding.btnB, binding.btnC, binding.btnD)
        buttons.forEachIndexed { idx, btn ->
            btn.isEnabled = true
            btn.alpha = 1.0f
            btn.setOnClickListener {
                if (answered) return@setOnClickListener
                answered = true
                val correctIndex = q.correctIndex
                if (idx == correctIndex) {
                    score += 20 // 5 question * 20 = 100 max
                    Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Wrong! Correct: ${q.choices[correctIndex]}", Toast.LENGTH_SHORT).show()
                }
                buttons.forEachIndexed { j, b ->
                    b.isEnabled = false
                    if (j == correctIndex) b.alpha = 1.0f else b.alpha = 0.5f
                }
            }
        }
    }
}
