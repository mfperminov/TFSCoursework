package xyz.mperminov.tfscoursework.utils.views

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.widget.TextView
import xyz.mperminov.tfscoursework.R

class BadgeView : TextView {

    constructor(context: Context) : super(context) {
        init(context, null)
    }


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val bg = context.getDrawable(R.drawable.rounded_corner) as GradientDrawable
        bg.cornerRadius = (textSize / 1.5).toFloat()
        background = bg
        setPadding(20,5,20,5)
    }
}