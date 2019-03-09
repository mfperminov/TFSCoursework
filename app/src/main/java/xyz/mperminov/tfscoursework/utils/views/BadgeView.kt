package xyz.mperminov.tfscoursework.utils.views

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.widget.TextView
import androidx.core.content.ContextCompat
import xyz.mperminov.tfscoursework.R

class BadgeView : TextView {

    private val DEFAULT_COLOR = R.color.colorPrimary

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        val bg = context.getDrawable(R.drawable.rounded_corner) as GradientDrawable
        bg.setColor(
            ContextCompat.getColor(context, DEFAULT_COLOR)
        )
        bg.cornerRadius = (textSize / 1.5).toFloat()
        background = bg
        setPadding(20, 5, 20, 5)
    }
}