package com.egco428.ex12_accelerometer

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var color = false
    private var lastUpdate = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView.setBackgroundColor(Color.GREEN)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lastUpdate = System.currentTimeMillis()
    }

    override fun onSensorChanged(event: SensorEvent) {
        if(event.sensor.type == Sensor.TYPE_ACCELEROMETER){
            getAccelerometer(event)
        }
    }

    private fun getAccelerometer(event: SensorEvent){
        val values= event.values
        val x = values[0]
        val y = values[1]
        val z = values[2]

        val accel = (x*x + y*y + z*z)/(SensorManager.GRAVITY_EARTH*SensorManager.GRAVITY_EARTH)
        val actualTime = System.currentTimeMillis()
        if (accel>2){
            if(actualTime-lastUpdate < 200){
                return
            }
            lastUpdate = actualTime
            Toast.makeText(this,"Device was shuffled",Toast.LENGTH_SHORT).show()

            if(color){
                textView.setBackgroundColor(Color.GREEN)
            }else{
                textView.setBackgroundColor(Color.RED)
            }
            color = !color
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(this, sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL)
    }
}
