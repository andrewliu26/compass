package com.example.compass

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat

class MainActivity : AppCompatActivity() {
    private lateinit var gestureDetector: GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gestureDetector = GestureDetectorCompat(this, FlingGestureListener(this))
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
}
