package com.example.method_channel_demo

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity: FlutterActivity(){
    private val CHANNEL = "samples.flutter.dev/battery"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        fun getBatteryLevel():Int{
            val batterLevel: Int
            if(VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP){
                val batteryManager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
                batterLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
            }else{
                val intent = ContextWrapper(applicationContext).registerReceiver(null,
                    IntentFilter(Intent.ACTION_BATTERY_CHANGED))
                batterLevel = intent!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100 / intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            }
            return batterLevel
        }

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger,CHANNEL).setMethodCallHandler { call, result ->
            if (call.method == "getBatteryLevel") {
                val batteryLevel = getBatteryLevel()
                result.success(batteryLevel)
            } else {
                result.error("UNAVAILABLE", "Battery level not  available.", null)
            }
        }
      
    }
}
