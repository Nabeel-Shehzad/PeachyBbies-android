package com.peachy.bbies.screens

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.peachy.bbies.R
import com.peachy.bbies.progressbar.CustomProgressBar
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit

import java.time.temporal.ChronoUnit.MINUTES


class CheckOut : AppCompatActivity() {
    private val progressBar = CustomProgressBar()
    val url = "https://apptreo.com/peachybbies/mobile/upload.php"
    val breaksTaken = "https://apptreo.com/peachybbies/mobile/breaksTaken.php"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_out)
        val text: TextView = findViewById(R.id.hoursSpent)
        val checkOut = findViewById<Button>(R.id.checkOutBtn)
        val actualPacked = findViewById<EditText>(R.id.itemPacked)

        val timePacking = intent.getStringExtra("Time Packing")
        val username = intent.getStringExtra("Username")


        var hour = String.format("%02d", timePacking?.split(":")?.get(0)?.toInt())
        var minute = String.format("%02d", timePacking?.split(":")?.get(1)?.toInt())
        var second = String.format("%02d", timePacking?.split(":")?.get(2)?.toInt())

        var timeTaken = LocalTime.parse(
            "$hour:$minute:$second",
            DateTimeFormatter.ofPattern("HH:mm:ss")
        ).plusSeconds(2)


        val pauseTimer = intent.getStringExtra("Pause")


        var value = ""
        if (pauseTimer?.isNotEmpty() == true) {
            val list = pauseTimer.split(",")
            for (j in list) {
                var h = String.format("%02d", j.split("::")[1].split(":")[0].toInt())
                var m = String.format("%02d", j.split("::")[1].split(":")[1].toInt())
                var s = String.format("%02d", j.split("::")[1].split(":")[2].toInt())

                val t = LocalTime.parse("$h:$m:$s", DateTimeFormatter.ofPattern("HH:mm:ss"))
                val l = (s.toLong() * 1000) + (m.toLong() * 60000) + (h.toLong() * 3600000)
                timeTaken = timeTaken.minus(l, ChronoUnit.MILLIS)
                value += "${timeTaken}, "
            }
        }
        text.text = "$timeTaken hours"

        checkOut.setOnClickListener {
            //if (intent.getStringExtra("Type") == "CountDown") {

            val packerName = intent.getStringExtra("Packer Name")
            val startTime = intent.getStringExtra("Start Time")
            val endTime = intent.getStringExtra("End Time")
            val workingTime = intent.getStringExtra("Working Time")
            val pause = intent.getStringExtra("Pause")
            val target = intent.getStringExtra("Target")
            val sku = intent.getStringExtra("SKU")

            val map = HashMap<String, String>()
            if (pause?.isNotEmpty() == true) {
                val list = pause.split(",")
                for (j in list) {
                    map[j.split("::")[0]] = j.split("::")[1]
                }
            }

            progressBar.show(this, "Please Wait..!!")
            val request: StringRequest = object : StringRequest(
                Method.POST, url,
                com.android.volley.Response.Listener { response ->
                    uploadBreaks(map, packerName.toString())
                    progressBar.dialog.dismiss()
                    Toast.makeText(this, "$response", Toast.LENGTH_LONG).show()
                    if (response.equals("submitted", ignoreCase = true)) {
                        val i = Intent(this, MainActivity::class.java)
                        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(i)
                    }
                },
                com.android.volley.Response.ErrorListener { error ->
                    progressBar.dialog.dismiss()
                    text.text = error.message
                }
            ) {
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["date"] = LocalDate.now().toString()
                    params["username"] = packerName.toString().split(" ")[0]
                    params["total_working_time"] = timeTaken.toString()
                    params["start_time"] = startTime.toString()
                    params["end_time"] = endTime.toString()
                    params["target_working_time"] = workingTime.toString()
                    params["target_quota"] = target.toString()
                    params["actual_quota"] = actualPacked.text.toString()
                    params["sku_packed"] = sku.toString().split(" ")[0]
                    return params
                }
            }
            val requestQueue = Volley.newRequestQueue(this)
            requestQueue.add(request)
        }
    }

    fun uploadBreaks(map: HashMap<String, String>, user: String) {
        for ((key, value) in map) {
            val request: StringRequest = object : StringRequest(
                Method.POST, breaksTaken,
                com.android.volley.Response.Listener { response ->
                },
                com.android.volley.Response.ErrorListener { error ->
                    progressBar.dialog.dismiss()
                }
            ) {
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["date"] = LocalDate.now().toString()
                    params["break_id"] = key.split(" ")[0]
                    params["employee_id"] = user.split(" ")[0]
                    params["time"] = value
                    return params
                }
            }
            val requestQueue = Volley.newRequestQueue(this)
            requestQueue.add(request)
        }

    }
}