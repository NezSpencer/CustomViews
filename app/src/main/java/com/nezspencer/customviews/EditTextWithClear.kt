package com.nezspencer.customviews

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.AppCompatEditText
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class EditTextWithClear : AppCompatEditText{

    lateinit var mClearButtonImage : Drawable

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        init()
    }
    private fun init(){
        mClearButtonImage = ResourcesCompat.getDrawable(resources, R.drawable.ic_clear_black_24dp, null)!!
        addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0?.let {
                    if (!TextUtils.isEmpty(it))
                        showClearButton()
                    else
                        hideClearButton()
                }
            }
        })

        setOnTouchListener { p0, p1 ->
            var isClearButtonClicked = false
            if (compoundDrawablesRelative[2] != null) {
                var clearButtonPosition : Int
                if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                    clearButtonPosition = mClearButtonImage.intrinsicWidth + paddingStart
                    if (p1.x < clearButtonPosition)
                        isClearButtonClicked = true
                }
                else {
                    clearButtonPosition = width - mClearButtonImage.intrinsicWidth - paddingEnd
                    if (p1.x > clearButtonPosition)
                        isClearButtonClicked = true
                }

                if (isClearButtonClicked){
                    if (p1.action == MotionEvent.ACTION_DOWN) {
                        mClearButtonImage = ResourcesCompat.getDrawable(resources, R.drawable.ic_clear_black_24dp, null)!!
                        showClearButton()
                    }
                    else if (p1.action == MotionEvent.ACTION_UP) {
                        mClearButtonImage = ResourcesCompat.getDrawable(resources, R.drawable.ic_clear_opaque_24dp, null)!!
                        hideClearButton()
                        text?.clear()
                        true
                    }
                }
                else
                    false
            }
            false
        }
    }

    private fun showClearButton(){
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, mClearButtonImage, null)
    }

    private fun hideClearButton(){
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null)
    }

}
