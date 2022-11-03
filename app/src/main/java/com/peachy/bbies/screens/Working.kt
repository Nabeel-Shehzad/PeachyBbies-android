package com.peachy.bbies.screens
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.peachy.bbies.R
import com.peachy.bbies.fragments.CountDown
import com.peachy.bbies.fragments.StopWatch
import com.peachy.bbies.model.BreakTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Working : AppCompatActivity() {
    private var packerName:String = ""
    private var productSKU:String = ""
    private var targetNumber:String = ""
    private var targetPacking:String = ""
    private var username:String = ""

    private var isPaused = false
    private var resumeFromMillis: Long = 0
    private val breakTime:BreakTime = BreakTime()
    private var currentTime: String? = null
    private var millisTime: Long = System.currentTimeMillis()
    private var timeInSeconds = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_working)

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        currentTime = current.format(formatter).toString()

        val fragment = intent.getStringExtra("Fragment")
        username = intent.getStringExtra("Username").toString()

        if(fragment == "CountDown") {
            packerName = intent.getStringExtra("PackerName").toString()
            productSKU = intent.getStringExtra("ProductSKU").toString()
            targetNumber = intent.getStringExtra("TargetNumber").toString()
            targetPacking = intent.getStringExtra("TargetPacking").toString()
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.workFragment, CountDown())
            fragmentTransaction.commit()
        }else{
            packerName = intent.getStringExtra("PackerName").toString()
            productSKU = intent.getStringExtra("ProductSKU").toString()
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.workFragment, StopWatch())
            fragmentTransaction.commit()
        }
    }
    fun setTimeInSeconds(time: Long){
        timeInSeconds = time
    }
    fun getTimeInSeconds():Long{
        return timeInSeconds
    }
    fun setPause(flag:Boolean){
        isPaused = flag
    }
    fun isPaused():Boolean{
        return isPaused
    }
    fun setMillis(millis:Long){
        resumeFromMillis = millis
    }
    fun getMillis():Long{
        return resumeFromMillis
    }
    fun setMillisTime(time:Long){
        this.millisTime= time
    }
    fun getMillisTime():Long{
        return millisTime
    }

    fun setTime(reason:String,time:String){
        breakTime.putTime(reason,time)
    }
    fun getBreakTime():String{
        var result = ""
        if (breakTime.breakTime.isNotEmpty()) {
            for ((key, value) in breakTime.breakTime) {
                result += "$key::$value,"
            }
            result = result.substring(0, result.length - 1)
        }
        return result
    }

    override fun onBackPressed() {
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    fun getCurrentTime():String?{
        return currentTime
    }
    fun getPackerName():String{
        return packerName
    }
    fun getProductSKU():String{
        return productSKU
    }
    fun getTargetNumber():String{
        return targetNumber
    }
    fun getTargetPacking():String{
        return targetPacking
    }
    fun getUsername():String{
        return username
    }
}