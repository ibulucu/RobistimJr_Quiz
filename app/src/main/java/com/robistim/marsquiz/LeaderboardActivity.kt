package com.robistim.marsquiz

import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.appcompat.app.AppCompatActivity
import com.robistim.marsquiz.databinding.ActivityLeaderboardBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LeaderboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLeaderboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaderboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        render()

        binding.btnReset.setOnClickListener {
            Leaderboard.clear(this)
            render()
        }
    }

    private fun render() {
        val list = Leaderboard.load(this)
        val sb = SpannableStringBuilder()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        list.forEachIndexed { idx, e ->
            sb.append("${idx+1}. ${e.name} — ${e.score} pts — ${sdf.format(Date(e.timestamp))}\n")
        }
        if (list.isEmpty()) sb.append("No scores yet.")
        binding.tvList.text = sb
    }
}
