package com.peachy.bbies.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import com.android.volley.toolbox.StringRequest
import com.google.android.material.snackbar.Snackbar
import com.peachy.bbies.R
import com.peachy.bbies.progressbar.CustomProgressBar
import com.peachy.bbies.screens.Home
import org.json.JSONObject


class TargetMode : Fragment() {
    val slimes = "https://nabeelshehzad.com/peachybbies/mobile/slimes.php"
    private val progressBar = CustomProgressBar()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_target_mode, container, false)
        val username = arguments?.getString("username")

        //set text to packerName
        view.findViewById<EditText>(R.id.packerName).setText(username)

        progressBar.show(requireActivity(), "Please Wait..!!")
        val list = ArrayList<String>()
        list.add("Choose slime")
        val request = object : StringRequest(
            Method.POST, slimes,
            com.android.volley.Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val success = jsonObject.getString("success")
                val jsonArray = jsonObject.getJSONArray("data")
                if (success == "1") {
                    for (i in 0 until jsonArray.length()) {
                        val singleObject = jsonArray.getJSONObject(i)
                        val id = singleObject.getString("id")
                        val name = singleObject.getString("slimeName")
                        val texture = singleObject.getString("slimeTexture")
                        list.add("$id $name $texture")
                    }

                }
                progressBar.dialog.dismiss()
            },
            com.android.volley.Response.ErrorListener { error ->
                progressBar.dialog.dismiss()
                Toast.makeText(requireActivity(), "Something went wrong try again later", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["username"] = username.toString()
                return params
            }
        }
        val requestQueue = com.android.volley.toolbox.Volley.newRequestQueue(requireActivity())
        requestQueue.add(request)
        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_activated_1, list)
        view.findViewById<Spinner>(R.id.productSKU).adapter = adapter

        view.findViewById<Button>(R.id.confirm).setOnClickListener {
            if(view.findViewById<EditText>(R.id.packerName).text.isEmpty() || view.findViewById<EditText>(R.id.packerName).text.isBlank() ||
                view.findViewById<Spinner>(R.id.productSKU).selectedItem.toString() == "Choose Slime" ||
                view.findViewById<EditText>(R.id.targetNumberPacked).text.isEmpty() || view.findViewById<EditText>(R.id.targetNumberPacked).text.isBlank() ||
                view.findViewById<EditText>(R.id.TargetPackingTime).text.isEmpty() || view.findViewById<EditText>(R.id.TargetPackingTime).text.isBlank()) {
                activity?.let {
                    Snackbar.make(
                        it.findViewById(android.R.id.content),
                        "Please fill all the fields",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }else {
                val bundle = Bundle()
                bundle.putString("Target", "ON")
                bundle.putString(
                    "Packer Name",
                    view.findViewById<EditText>(R.id.packerName).text.toString()
                )
                bundle.putString(
                    "Product SKU",
                    view.findViewById<Spinner>(R.id.productSKU).selectedItem.toString()
                )
                bundle.putString(
                    "Target Number",
                    view.findViewById<EditText>(R.id.targetNumberPacked).text.toString()
                )
                bundle.putString(
                    "Target Packing",
                    view.findViewById<EditText>(R.id.TargetPackingTime).text.toString()
                )
                bundle.putString("username", username)

                (activity as Home).setState(false)
                val startPacking = StartPacking()
                startPacking.arguments = bundle
                fragmentManager?.beginTransaction()?.replace(R.id.targetFragment, startPacking)
                    ?.addToBackStack("Setup")
                    ?.commit()
            }
        }
        return view;
    }

}