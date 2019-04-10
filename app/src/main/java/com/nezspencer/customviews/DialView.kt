package com.nezspencer.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class DialView : View{
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val SELECTION_COUNT : Int = 4
    private var mWidth : Float = 0f
    private var mHeight : Float = 0f
    private lateinit var mTextPaint: Paint
    private lateinit var mDialPaint: Paint
    private var mRadius : Float = 0f
    private var mActiveSelection : Int = 0
    private var mTempLabel : StringBuffer = StringBuffer(8)
    private var mTempResult = Array(2){0f}
    init {
        init()
    }
    private fun init(){
        mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint.color = Color.BLACK
        mTextPaint.style = Paint.Style.FILL_AND_STROKE
        mTextPaint.textAlign = Paint.Align.CENTER
        mTextPaint.textSize = 40f
        mDialPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mDialPaint.color = Color.GRAY

        setOnClickListener {
            mActiveSelection = (mActiveSelection + 1) % SELECTION_COUNT

            if (mActiveSelection >= 1){
                mDialPaint.color = Color.GREEN
            }
            else{
                mDialPaint.color = Color.GRAY
            }

            invalidate()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mWidth = w.toFloat()
        mHeight = h.toFloat()

        mRadius = (Math.min(mWidth, mHeight) /2 * 0.8).toFloat()

    }

    private fun computeXYForPosition(pos : Int, radius : Float): Array<Float> {
        val result = mTempResult
        val startAngle = Math.PI * (9 / 8)
        val angle = startAngle + (pos * (Math.PI / 4))

        result[0] = ((radius * Math.cos(angle)) + (mWidth / 2)).toFloat()
        result[1] = ((radius * Math.sin(angle)) + (mHeight / 2)).toFloat()
        return result
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            it.drawCircle(mWidth / 2, mHeight / 2, mRadius, mDialPaint)
            val labelRadius = mRadius + 20
            val label = mTempLabel

            for (i in 0 until SELECTION_COUNT){
                val xyData = computeXYForPosition(i, labelRadius)
                label.setLength(0)
                label.append(i)
                it.drawText(label, 0, label.length, xyData[0], xyData[1], mTextPaint)
            }

            val markerRadius = mRadius - 35
            val xyData = computeXYForPosition(mActiveSelection, markerRadius)
            it.drawCircle(xyData[0], xyData[1], 20f, mTextPaint)
        }
    }
}