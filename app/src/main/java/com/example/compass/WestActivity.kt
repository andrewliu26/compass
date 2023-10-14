package com.example.compass

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat

class WestActivity : AppCompatActivity() {

    private lateinit var gestureDetector: GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_west)
        gestureDetector = GestureDetectorCompat(this, SwipeGestureListener(this))
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            gestureDetector.onTouchEvent(event)
        }
        return super.onTouchEvent(event)
    }

    private class SwipeGestureListener(private val activity: Activity) : GestureDetector.SimpleOnGestureListener() {

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            val diffX = e2.x - e1.x
            if (diffX > 0) {
                activity.startActivity(Intent(activity, MainActivity::class.java))
            }
            return true
        }
    }
}
