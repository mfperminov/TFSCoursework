package xyz.mperminov.tfscoursework.utils.views


import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.widget.TextView
import androidx.core.content.ContextCompat
import xyz.mperminov.tfscoursework.R


class CourseStateTextView : TextView {

    private val DEFAULT_COLOR = R.color.color_course_done

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
        var color: Int? = null
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CourseStateTextView)
            color = a.getColor(R.styleable.CourseStateTextView_background_color, ContextCompat.getColor(context, DEFAULT_COLOR))
            a.recycle()
        }
        (this.background as GradientDrawable).setColor(color!!)
        setPadding(30,10,30,10)

    }

    fun setColor(colorRes: Int) {
        val color = ContextCompat.getColor(context, colorRes)
        (this.background as GradientDrawable).setColor(color)
        invalidate()
    }

}