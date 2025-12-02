package com.jeelpatel.mytodo.ui.customView


import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.withStyledAttributes
import com.jeelpatel.mytodo.R


class CircularProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private var progress = 0
    private var strokeWidth = 20f
    private var progressColor = Color.BLUE
    private var backgroundColor = Color.LTGRAY
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)


    init {
        context.withStyledAttributes(attrs, R.styleable.CircularProgressView) {
            progress = getInt(R.styleable.CircularProgressView_cp_progress, 0)
            progressColor = getColor(R.styleable.CircularProgressView_cp_progressColor, Color.BLUE)
            backgroundColor =
                getColor(R.styleable.CircularProgressView_cp_backgroundColor, Color.LTGRAY)
            strokeWidth = getDimension(R.styleable.CircularProgressView_cp_strokeWidth, 20f)
        }
    }


    fun setProgress(value: Int) {
        progress = value.coerceIn(0, 100)
        invalidate()    // redraw view
    }


//    fun setProgress(to: Int, duration: Long = 800) {
//        val from = progress
//
//        ValueAnimator.ofInt(from, to).apply {
//            this.duration = duration
//            interpolator = AccelerateDecelerateInterpolator()
//
//            addUpdateListener { animator ->
//                progress = animator.animatedValue as Int
//                invalidate()
//            }
//
//            start()
//        }
//    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minW = paddingLeft + paddingRight + suggestedMinimumWidth
        val minH = paddingTop + paddingBottom + suggestedMinimumHeight

        val width = resolveSize(minW, widthMeasureSpec)
        val height = resolveSize(minH, heightMeasureSpec)

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val radius = (width.coerceAtMost(height) / 2f) - strokeWidth

        val centerX = width / 2f
        val centerY = height / 2f

        // Draw background circle
        paint.color = backgroundColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = strokeWidth
        canvas.drawCircle(centerX, centerY, radius, paint)

        // Draw progress arc
        paint.color = progressColor
        val rect = RectF(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius
        )
        val sweepAngle = (progress * 360 / 100).toFloat()
        canvas.drawArc(rect, -90f, sweepAngle, false, paint)

        // Draw text
        paint.color = Color.BLACK
        paint.textSize = 50f
        paint.style = Paint.Style.FILL
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText("$progress%", centerX, centerY + 20f, paint)
    }

}