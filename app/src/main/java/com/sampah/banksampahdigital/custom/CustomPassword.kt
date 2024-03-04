package com.sampah.banksampahdigital.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.sampah.banksampahdigital.R
class CustomPassword: CustomUsername {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (text?.length in 1..7){
                    error = resources.getString(R.string.min_length_char, 8)
                }
            }
            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
    }
}