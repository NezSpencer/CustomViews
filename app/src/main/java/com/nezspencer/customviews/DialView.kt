package com.nezspencer.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class DialView : View{
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        val typedArray = context!!.obtainStyledAttributes(attrs, R.styleable.DialView, 0, 0)
        fanOffColor = typedArray.getColor(R.styleable.DialView_fanOffColor, fanOffColor)
        fanOnColor = typedArray.getColor(R.styleable.DialView_fanOnColor, fanOnColor)
        mSelectionCount = typedArray.getInt(R.styleable.DialView_selection_count, mSelectionCount)
        typedArray.recycle()
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        val typedArray = context!!.obtainStyledAttributes(attrs, R.styleable.DialView, 0, 0)
        fanOffColor = typedArray.getColor(R.styleable.DialView_fanOffColor, fanOffColor)
        fanOnColor = typedArray.getColor(R.styleable.DialView_fanOnColor, fanOnColor)
        mSelectionCount = typedArray.getInt(R.styleable.DialView_selection_count, mSelectionCount)
        typedArray.recycle()
    }

    private var mWidth : Float = 0f
    private var mHeight : Float = 0f
    private lateinit var mTextPaint: Paint
    private lateinit var mDialPaint: Paint
    private var mRadius : Float = 0f
    private var mActiveSelection : Int = 0
    private var mTempLabel : StringBuffer = StringBuffer(8)
    private var mTempResult = Array(2){0f}
    private var fanOnColor = Color.parseColor("#22FFFF")
    private var fanOffColor = Color.parseColor("#8888AA")
    private var mSelectionCount = 4
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
        mDialPaint.color = fanOffColor

        setOnClickListener {
            mActiveSelection = (mActiveSelection + 1) % mSelectionCount

            if (mActiveSelection >= 1){
                mDialPaint.color = fanOnColor
            }
            else{
                mDialPaint.color = fanOffColor
            }

            invalidate()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mWidth = w.toFloat()
        mHeight = h.toFloat()

        mRadius = (Math.min(mWidth, mHeight) /2 * 0.8).toFloat()

    }

    private fun computeXYForPosition(pos : Int, radius : Float, isLabel : Boolean): Array<Float> {
        val result = mTempResult
        val startAngle : Double
        val angle : Double

        if (mSelectionCount > 4) {
            startAngle = Math.PI * (3 / 2)
            angle = startAngle + (pos * (Math.PI / mSelectionCount))
            result[0] = (radius * Math.cos(angle * 2) + (mWidth / 2)).toFloat()
            result[1] = (radius * Math.sin(angle * 2) + (mHeight / 2)).toFloat()
            if((angle > Math.toRadians(360.0)) && isLabel) {
                result[1] = result[1] + 20
            }
        }
        else{
            startAngle = Math.PI * (9/ 8)
            angle = startAngle + (pos * (Math.PI / mSelectionCount))
            result[0] = ((radius * Math.cos(angle)) + (mWidth / 2)).toFloat()
            result[1] = ((radius * Math.sin(angle)) + (mHeight / 2)).toFloat()
        }

        return result
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            it.drawCircle(mWidth / 2, mHeight / 2, mRadius, mDialPaint)
            val labelRadius = mRadius + 20
            val label = mTempLabel

            for (i in 0 until mSelectionCount){
                val xyData = computeXYForPosition(i, labelRadius, true)
                label.setLength(0)
                label.append(i)
                it.drawText(label, 0, label.length, xyData[0], xyData[1], mTextPaint)
            }

            val markerRadius = mRadius - 35
            val xyData = computeXYForPosition(mActiveSelection, markerRadius, false)
            it.drawCircle(xyData[0], xyData[1], 20f, mTextPaint)
        }
    }

    fun setSelectionCount(selection : Int){
        mSelectionCount = selection
        mActiveSelection = 0
        mDialPaint.color = fanOffColor
        invalidate()
    }
}