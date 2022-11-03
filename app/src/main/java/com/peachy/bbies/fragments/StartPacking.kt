package com.peachy.bbies.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.peachy.bbies.R
import com.peachy.bbies.screens.Working


class StartPacking : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_start_packing, container, false)
        val bundle = this.arguments
        val target = bundle?.getString("Target")
        val username = bundle?.getString("Username")

        var packerName:String = ""
        var productSKU:String = ""
        var targetNumber:String = ""
        var targetPacking:String = ""
        if (bundle != null && target == "ON"){
            packerName = bundle.getString("Packer Name")!!
            productSKU = bundle.getString("Product SKU")!!
            targetNumber = bundle.getString("Target Number")!!
            targetPacking = bundle.getString("Target Packing")!!

            view.findViewById<TextView>(R.id.welcome).text = "Welcome ${packerName.substring(2)}"
            view.findViewById<TextView>(R.id.details).text = "Today you need to pack $targetNumber units " +
                    "of ${productSKU.substring(2)} in $targetPacking hours or less"

        }else if(bundle != null && bundle.getString("Target") == "OFF"){
            packerName = bundle.getString("Packer Name")!!
            productSKU = bundle.getString("Product SKU")!!
            view.findViewById<TextView>(R.id.welcome).text = "Welcome ${packerName.substring(2)}"
        }
        view.findViewById<Button>(R.id.start).setOnClickListener {
            if (target == "ON"){
                val intent = Intent(activity,Working::class.java)
                intent.putExtra("Fragment","CountDown")
                intent.putExtra("PackerName",packerName)
                intent.putExtra("ProductSKU",productSKU)
                intent.putExtra("TargetNumber",targetNumber)
                intent.putExtra("TargetPacking",targetPacking)
                startActivity(intent)
            }else{
                val intent = Intent(activity,Working::class.java)
                intent.putExtra("Fragment","StopWatch")
                intent.putExtra("PackerName",packerName)
                intent.putExtra("ProductSKU",productSKU)
                intent.putExtra("username",username)
                startActivity(intent)
            }
        }
        return view;
    }


}