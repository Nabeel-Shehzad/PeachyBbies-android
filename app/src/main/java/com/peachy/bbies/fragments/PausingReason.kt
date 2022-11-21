package com.peachy.bbies.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import com.peachy.bbies.R

class PausingReason : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pausing_reason, container, false)

        val argument = this.arguments
        val fragment = argument?.getString("Fragment")

        view.findViewById<Button>(R.id.startPause).setOnClickListener {
            val bundle = Bundle()
            bundle.putString("Fragment",fragment)
            if (view.findViewById<CheckBox>(R.id.bathroom).isChecked)
                bundle.putString("Reason","Bathroom")
            else if (view.findViewById<CheckBox>(R.id.generalBreak).isChecked)
                bundle.putString("Reason","General Break")
            else if (view.findViewById<CheckBox>(R.id.meal).isChecked)
                bundle.putString("Reason","Meal")
            else if (view.findViewById<CheckBox>(R.id.shelving).isChecked)
                bundle.putString("Reason","Social Media")
            else if (view.findViewById<CheckBox>(R.id.mockups).isChecked){
                bundle.putString("Reason","Mockups")
            }
            else
                bundle.putString("Reason","Other task")

            val breakTime = BreakTimeView()
            breakTime.arguments = bundle
            fragmentManager?.beginTransaction()?.replace(R.id.workFragment, breakTime)
                ?.commit()
        }
        return view;
    }

}