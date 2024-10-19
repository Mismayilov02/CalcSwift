package com.imcalculator.calculator.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import com.imcalculator.calculator.R


class CustomButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    var listener:(View) -> Unit = {}
    private var textField: String
    private var buttonColor: Int


    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.CalculatorButtonView)
        textField = array.getString(R.styleable.CalculatorButtonView_textField).toString()
        buttonColor = array.getColor(R.styleable.CalculatorButtonView_buttonColor, 0)
        array.recycle()
        initViews()
    }

    private fun initViews() {
        textField?.let { txt->
            setText(txt)
        }
        buttonColor?.let { color->
          setButtonTextColor(color)
        }
        setOnClickListener {
            listener(it)
        }
    }
    fun setButtonSize(width: Int, height: Int) {
        val params = layoutParams
        params.width = width
        params.height = height
        layoutParams = params
    }
    fun setButtonColor(color: Int) {
        setBackgroundColor(color)
    }
    fun setButtonTextColor(color: Int) {
        setTextColor(color)
    }
    fun setButtonText(text: String) {
        setText(text)
    }
    fun getButtonText(): String {
        return text.toString()
    }
}