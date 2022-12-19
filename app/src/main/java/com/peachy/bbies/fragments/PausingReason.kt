package com.peachy.bbies.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.peachy.bbies.R
import com.peachy.bbies.progressbar.CustomProgressBar
import org.json.JSONObject

class PausingReason : Fragment() {

    val breaks = "https://nabeelshehzad.com/peachybbies/mobile/breaks.php"
    private val progressBar = CustomProgressBar()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pausing_reason, container, false)
        val breakReason = view.findViewById<Spinner>(R.id.breaks)

        progressBar.show(requireActivity(), "Please wait...!!!")
        val list = ArrayList<String>()
        list.add("Choose Break")
        val request = object : StringRequest(
            Method.POST, breaks,
            com.android.volley.Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val success = jsonObject.getString("success")
                val jsonArray = jsonObject.getJSONArray("data")
                if (success == "1") {
                    for (i in 0 until jsonArray.length()) {
                        val singleObject = jsonArray.getJSONObject(i)
                        val id = singleObject.getString("id")
                        val name = singleObject.getString("name")
                        list.add("$id $name")
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
                val map = HashMap<String, String>()
                map["username"] = "nabeel"
                return map
            }
        }
        Volley.newRequestQueue(requireActivity()).add(request)
        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_activated_1, list)
        breakReason.adapter = adapter


        val argument = this.arguments
        val fragment = argument?.getString("Fragment")

        view.findViewById<Button>(R.id.startPause).setOnClickListener {
            val bundle = Bundle()
            bundle.putString("Fragment",fragment)
            bundle.putString("Reason",breakReason.selectedItem.toString())
            val breakTime = BreakTimeView()
            breakTime.arguments = bundle
            fragmentManager?.beginTransaction()?.replace(R.id.workFragment, breakTime)
                ?.commit()
        }
        return view;
    }

}