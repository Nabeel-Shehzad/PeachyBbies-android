package com.peachy.bbies.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.peachy.bbies.R
import com.peachy.bbies.screens.CheckOut
import com.peachy.bbies.screens.Working
import com.peachy.bbies.timer.Utility.getFormattedStopWatch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class StopWatch : Fragment() {

    private val mInterval = 1000
    private var mHandler: Handler? = null

    private var startButtonClicked = false
    private var timeInSeconds = 0L
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_stop_watch, container, false)
        val packerName = (activity as Working).getPackerName()
        val productSKU = (activity as Working).getProductSKU()
        view.findViewById<TextView>(R.id.name).text = packerName

//        timeInSeconds = (activity as Working).getTimeInSeconds()
//        startTimer()
//        startTimerView()
        val startTime: Long = (activity as Working).getMillisTime()
        view.findViewById<TextView>(R.id.stopWatchTimer).text =
            String.format("%1\$tH:%1\$tM:%1\$tS", startTime)

        view.findViewById<Button>(R.id.pauseBtn).setOnClickListener {
            (activity as Working).setMillisTime(startTime)
            val bundle = Bundle()
            bundle.putString("Fragment", "StopWatch")
            val reason = PausingReason()
            reason.arguments = bundle
            fragmentManager?.beginTransaction()?.replace(R.id.workFragment, reason)
                ?.addToBackStack("Setup")
                ?.commit()

        }
        view.findViewById<Button>(R.id.finishBtn).setOnClickListener {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

            (activity as Working).setTimeInSeconds(timeInSeconds)
            val milliseconds = System.currentTimeMillis()

            val intent = Intent(activity, CheckOut::class.java)
            intent.putExtra("Type", "StopWatch")
            intent.putExtra("Packer Name", packerName)
            intent.putExtra("Start Time", (activity as Working).getCurrentTime())
            intent.putExtra("End Time", current.format(formatter).toString())
            intent.putExtra("Pause", (activity as Working).getBreakTime())
            intent.putExtra(
                "Time Packing",
                "${TimeUnit.MILLISECONDS.toHours(milliseconds - (activity as Working).getMillisTime())}" +
                        ":${
                            TimeUnit.MILLISECONDS.toMinutes(milliseconds - (activity as Working).getMillisTime()) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(milliseconds - (activity as Working).getMillisTime())
                            )
                        }:" +
                        "${
                            TimeUnit.MILLISECONDS.toSeconds(milliseconds - (activity as Working).getMillisTime()) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(milliseconds - (activity as Working).getMillisTime())
                            )
                        }"
            )
            intent.putExtra("SKU", productSKU)
            intent.putExtra("username", (activity as Working).getUsername())
            startActivity(intent)
            activity?.finish()
        }


        return view
    }

    private fun stopTimerView() {
        startButtonClicked = false
    }

    private fun startTimerView() {
        startButtonClicked = true

    }

    private fun startTimer() {
        mHandler = Handler(Looper.getMainLooper())
        mStatusChecker.run()
    }

    private var mStatusChecker: Runnable = object : Runnable {

        override fun run() {
            try {
                timeInSeconds += 1
                updateStopWatchView(timeInSeconds)
            } finally {
                mHandler!!.postDelayed(this, mInterval.toLong())
            }
        }
    }

    private fun stopTimer() {
        mHandler?.removeCallbacks(mStatusChecker)
    }

    private fun updateStopWatchView(timeInSeconds: Long) {
        (activity as Working).setTimeInSeconds(timeInSeconds)
        val formattedTime = getFormattedStopWatch((timeInSeconds * 1000))
        view?.findViewById<TextView>(R.id.stopWatchTimer)?.text = formattedTime
    }

}