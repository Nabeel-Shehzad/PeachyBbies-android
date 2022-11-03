package com.peachy.bbies.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.peachy.bbies.R
import com.peachy.bbies.progressbar.CustomProgressBar
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    lateinit var unlockBtn: Button
    lateinit var username: Spinner
    val usernames = "https://nabeelshehzad.com/peachybbies/mobile/users.php"
    private val progressBar = CustomProgressBar()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup()

        progressBar.show(this, "Please Wait..!!")
        val list = ArrayList<String>()
        list.add("Choose user")
        val request = object : StringRequest(
            Method.POST, usernames,
            com.android.volley.Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val success = jsonObject.getString("success")
                val jsonArray = jsonObject.getJSONArray("data")
                if (success == "1") {
                    for (i in 0 until jsonArray.length()) {
                        val singleObject = jsonArray.getJSONObject(i)
                        val id = singleObject.getString("id")
                        val firstName = singleObject.getString("firstName")
                        val lastName = singleObject.getString("lastName")
                        list.add("$id $firstName $lastName")
                    }

                }
                progressBar.dialog.dismiss()
            },
            com.android.volley.Response.ErrorListener { error ->
                progressBar.dialog.dismiss()
                Toast.makeText(this, "Something went wrong try again later", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val map = HashMap<String, String>()
                map["username"] = "nabeel"
                return map
            }
        }
        Volley.newRequestQueue(this).add(request)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_activated_1, list)
        username.adapter = adapter

        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE
            ), PackageManager.PERMISSION_GRANTED
        )

        unlockBtn.setOnClickListener {
            if (username.selectedItem.toString() != "Choose user") {
                Toast.makeText(this, "Welcome ${username.selectedItem}", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Home::class.java)
                intent.putExtra("username", username.selectedItem.toString())
                startActivity(intent)
                finish()
            } else {
                Snackbar.make(it, "Please select a username", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun setup() {
        unlockBtn = findViewById(R.id.unlockBtn)
        username = findViewById(R.id.spinner)
    }
}