package com.peachy.bbies.model

class BreakTime {
    var breakTime :HashMap<String,String> = HashMap()

    public fun putTime(reason:String,time:String){
        breakTime[reason] = time
    }
    public fun getTime(reason:String):String?{
        return breakTime[reason]
    }
}