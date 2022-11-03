package com.peachy.bbies.fragments

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.peachy.bbies.R
import com.peachy.bbies.screens.CheckOut
import com.peachy.bbies.screens.Working
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import kotlin.math.min


class CountDown : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_count_down, container, false)
        val packerName = (activity as Working).getPackerName()
        val productSKU = (activity as Working).getProductSKU()
        val targetNumber = (activity as Working).getTargetNumber()
        val targetPacking = (activity as Working).getTargetPacking()
        view.findViewById<TextView>(R.id.name).text = packerName
        view.findViewById<TextView>(R.id.goal).text = "Goal: $targetNumber in $targetPacking hours"
        val milliseconds = targetPacking.toLong() * 3_600_000L

        if((activity as Working).getMillis() != 0L) {
            countDownTimer((activity as Working).getMillis(), view).start()
        }else{
            countDownTimer(milliseconds,view).start()
        }

        view.findViewById<Button>(R.id.pausedBtn).setOnClickListener {
            if ((activity as Working).isPaused()){
                (activity as Working).setPause(false)
                countDownTimer((activity as Working).getMillis(),view).start()
            }else{
                (activity as Working).setPause(true)
                val bundle = Bundle()
                bundle.putString("Fragment","CountDown")
                val reason = PausingReason()
                reason.arguments = bundle
                fragmentManager?.beginTransaction()?.replace(R.id.workFragment, reason)
                    ?.addToBackStack("Setup")
                    ?.commit()
            }
        }
        view.findViewById<Button>(R.id.finishedBtn).setOnClickListener {
            (activity as Working).setPause(true)
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

            val intent = Intent(activity,CheckOut::class.java)
            intent.putExtra("Type","CountDown")
            intent.putExtra("Packer Name",packerName)
            intent.putExtra("Start Time",(activity as Working).getCurrentTime())
            intent.putExtra("End Time",current.format(formatter).toString())
            intent.putExtra("Working Time",targetPacking)
            intent.putExtra("Pause",(activity as Working).getBreakTime())

            intent.putExtra("Time Packing","${TimeUnit.MILLISECONDS.toHours(milliseconds - (activity as Working).getMillis())}" +
                    ":${
                        TimeUnit.MILLISECONDS.toMinutes(milliseconds - (activity as Working).getMillis()) - TimeUnit.HOURS.toMinutes(
                            TimeUnit.MILLISECONDS.toHours(milliseconds - (activity as Working).getMillis())
                        )
                    }:" +
                    "${
                        TimeUnit.MILLISECONDS.toSeconds(milliseconds - (activity as Working).getMillis()) - TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(milliseconds - (activity as Working).getMillis())
                        )
                    }")
            intent.putExtra("Target",targetNumber)
            intent.putExtra("SKU",productSKU)
            intent.putExtra("username", (activity as Working).getUsername())
            startActivity(intent)
            activity?.finish()
        }
        return view
    }
    private fun countDownTimer(milliseconds:Long, view: View):CountDownTimer{
        return object : CountDownTimer(milliseconds, 1000) {
            override fun onTick(millisUnitFinished: Long) {
                if ((activity as Working).isPaused()) {
                    (activity as Working).setMillis(millisUnitFinished)
                    cancel()
                }
                view.findViewById<TextView>(R.id.timer).text =
                    "${TimeUnit.MILLISECONDS.toHours(millisUnitFinished)}" +
                            ":${
                                TimeUnit.MILLISECONDS.toMinutes(millisUnitFinished) - TimeUnit.HOURS.toMinutes(
                                    TimeUnit.MILLISECONDS.toHours(millisUnitFinished)
                                )
                            }:" +
                            "${
                                TimeUnit.MILLISECONDS.toSeconds(millisUnitFinished) - TimeUnit.MINUTES.toSeconds(
                                    TimeUnit.MILLISECONDS.toMinutes(millisUnitFinished)
                                )
                            }"
            }

            override fun onFinish() {
                view.findViewById<TextView>(R.id.timer).text = "Time Up"
            }
        }
    }

}