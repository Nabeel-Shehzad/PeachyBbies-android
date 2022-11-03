package com.peachy.bbies.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.peachy.bbies.R
import com.peachy.bbies.screens.Working
import com.peachy.bbies.timer.Utility
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class BreakTimeView : Fragment() {

    private val mInterval = 1000
    private var mHandler: Handler? = null
    private var timeInSeconds = 0L
    private var startButtonClicked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_break_time_view, container, false)
        val bundle = this.arguments

        view.findViewById<TextView>(R.id.reason).text = bundle?.getString("Reason")

//        startTimer()
//        startTimerView()
        val startTime: Long = System.currentTimeMillis()
        view.findViewById<TextView>(R.id.breakTimer).text =
            String.format("%1\$tH:%1\$tM:%1\$tS", startTime)

        view.findViewById<Button>(R.id.backBtn).setOnClickListener {
//            stopTimer()
//            stopTimerView()
            val milliseconds = System.currentTimeMillis()

            val timeTaken = "${TimeUnit.MILLISECONDS.toHours(milliseconds - startTime)}" +
                    ":${
                        TimeUnit.MILLISECONDS.toMinutes(milliseconds - startTime) - TimeUnit.HOURS.toMinutes(
                            TimeUnit.MILLISECONDS.toHours(milliseconds - startTime)
                        )
                    }:" +
                    "${
                        TimeUnit.MILLISECONDS.toSeconds(milliseconds - startTime) - TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(milliseconds - startTime)
                        )
                    }"

            (activity as Working).setTime(view.findViewById<TextView>(R.id.reason).text.toString(),
            timeTaken)


            if(bundle?.getString("Fragment") == "CountDown") {
                fragmentManager?.beginTransaction()?.replace(R.id.workFragment, CountDown())
                    ?.commit()
            }else{
                fragmentManager?.beginTransaction()?.replace(R.id.workFragment, StopWatch())
                    ?.commit()
            }
        }
        return view
    }
    private fun initStopWatch() {
        view?.findViewById<TextView>(R.id.breakTimer)?.text = "00:00:00"
    }
    private fun resetTimerView() {
        timeInSeconds = 0
        startButtonClicked = false
        initStopWatch()
    }

    private fun stopTimerView() {
        startButtonClicked = false
    }
    fun startOrStopButtonClicked() {
        if (!startButtonClicked) {
            startTimer()
            startTimerView()
        } else {
            stopTimer()
            stopTimerView()
        }

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
        val formattedTime = Utility.getFormattedStopWatch((timeInSeconds * 1000))
        view?.findViewById<TextView>(R.id.breakTimer)?.text = formattedTime
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
    }

}