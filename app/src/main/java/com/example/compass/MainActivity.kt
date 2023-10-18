package com.example.compass

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.animation.ObjectAnimator
import android.view.animation.CycleInterpolator
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat

class MainActivity : AppCompatActivity() {

    private lateinit var textView1:TextView
    private lateinit var textView2:TextView
    private lateinit var gestureDetector: GestureDetectorCompat
    private lateinit var sensorManager: SensorManager
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gestureDetector = GestureDetectorCompat(this, FlingGestureListener(this))

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.registerListener(sensorListener, sensorManager
            .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)

        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH

        textView1 = findViewById(R.id.Home)
        textView2 = findViewById(R.id.prompt)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            gestureDetector.onTouchEvent(event)
        }
        return super.onTouchEvent(event)
    }

    private class FlingGestureListener(private val activity: Activity) : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            val diffY = e2.y - e1.y  // Vertical distance of the fling
            val diffX = e2.x - e1.x  // Horizontal distance of the fling
            when {

                kotlin.math.abs(diffX) > kotlin.math.abs(diffY) -> {
                    when {
                        // Left or Right fling?
                        diffX > 0 -> activity.startActivity(Intent(activity, EastActivity::class.java))
                        else -> activity.startActivity(Intent(activity, WestActivity::class.java))
                    }
                }
                else -> {
                    when {
                        // Up or Down fling?
                        diffY < 0 -> activity.startActivity(Intent(activity, NorthActivity::class.java))
                        else -> activity.startActivity(Intent(activity, SouthActivity::class.java))
                    }
                }
            }
            return true
        }
    }


    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {

            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            lastAcceleration = currentAcceleration

            currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta

            if (acceleration > 2.0f) {
                shakeTextView(textView1)
                shakeTextView(textView2)
            }
        }
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }
    override fun onResume() {
        sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(
            Sensor .TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL
        )
        super.onResume()
    }

    override fun onPause() {
        sensorManager.unregisterListener(sensorListener)
        super.onPause()
    }

    private fun shakeTextView(textView: TextView) {
        val shakeAnimator = ObjectAnimator.ofFloat(textView, "translationX", 0f, 10f, 0f, -10f)
        shakeAnimator.duration = 2000 // 2 seconds
        shakeAnimator.interpolator = CycleInterpolator(80f)
        shakeAnimator.start()
    }

}

