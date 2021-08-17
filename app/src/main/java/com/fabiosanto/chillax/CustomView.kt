package com.fabiosanto.chillax

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

// finger follow the top of the flow
// flow follows direction of finger
// flow starts/stop with finger down

// game idea: fingers must be inside the flow, flow moves crazy

interface MessageCallback {
    fun onUpdate(text: String)
}

enum class Modes(val text: String){
    IN("Breath In"), OUT("Breath Out")
}

class CustomView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    // Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes

    lateinit var callback: MessageCallback

    private val paint = Paint().apply {
        color = Color.BLACK
    }

    private var variable = 0f
    private val animator = ValueAnimator.ofFloat(0.0f, 1.0f)
        .apply {
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
            duration = 4000
            addUpdateListener {
                variable = it.animatedValue as Float
                invalidate()
            }
        }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRect(
            0f,
            measuredHeight - (measuredHeight * variable),
            measuredWidth.toFloat(),
            measuredHeight.toFloat(),
            paint
        )
    }

    private var lastMoveY = 0f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (animator.isPaused) animator.resume()
                else if (!animator.isRunning) animator.start()
                return true
            }
            MotionEvent.ACTION_UP -> {
                animator.pause()
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                animator.pause()
                if (lastMoveY > event.y)
                    Log.e("ACTION_MOVE", "GOING UP  ${event.y}")
                else Log.e("ACTION_MOVE", "GOING DOWN  ${event.y}")

                lastMoveY = event.y

                return true
            }
        }
        return false
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }
}