package com.peachy.bbies.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.peachy.bbies.R
import com.peachy.bbies.databinding.ActivityHomeBinding
import com.peachy.bbies.fragments.TargetMode
import com.peachy.bbies.fragments.TargetModeOff

class Home : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("username")
        replaceFragment(TargetModeOff(),username.toString())

        binding.targetMode.setOnClickListener {
            if(binding.targetMode.isChecked){
                replaceFragment(TargetMode(),username.toString())
            }else{
                replaceFragment(TargetModeOff(),username.toString())
            }
        }
    }
    private fun replaceFragment(fragment: Fragment,username:String){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val bundle: Bundle = Bundle()
        bundle.putString("username",username)
        fragment.arguments = bundle
        fragmentTransaction.replace(R.id.targetFragment,fragment)
        fragmentTransaction.commit()
    }
    fun setState(state: Boolean){
        binding.targetMode.isClickable = state
    }
    override fun onBackPressed() {
        setState(true)
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

}