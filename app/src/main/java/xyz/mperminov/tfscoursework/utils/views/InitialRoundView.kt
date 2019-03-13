package xyz.mperminov.tfscoursework.utils.views

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.initial_round_view.view.*
import xyz.mperminov.tfscoursework.R


class InitialRoundView : ConstraintLayout {

    private val DEFAULT_ROUND_COLOR = R.color.colorPrimary

    constructor(context: Context) : super(context) {
        init(context, null)
    }


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }


    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs!!)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        inflate(context, R.layout.initial_round_view, this)

        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.InitialRoundView)
            val roundColor = a.getColor(
                R.styleable.InitialRoundView_roundColor,
                ContextCompat.getColor(context, DEFAULT_ROUND_COLOR)
            )
            (imageView.drawable as GradientDrawable).setColor(roundColor)
            val initials = a.getString(R.styleable.InitialRoundView_initials)
            initials?.let { setInitials(it) }
            a.recycle()
        }
    }

    private fun setInitialsSize(viewWidth: Int, viewHeight: Int) {
        val limit = Math.min(viewWidth, viewHeight)
        inititals.textSize = limit.toFloat() / 8
        invalidate()
    }

    fun setColor(color: Int) {
        (imageView.drawable as GradientDrawable).setColor(ContextCompat.getColor(context, color))
        invalidate()
    }

    fun setInitials(initials: String) {
        inititals.text = initials
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val sizeWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        val sizeHeight = View.MeasureSpec.getSize(heightMeasureSpec)
        setInitialsSize(sizeWidth, sizeHeight)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }
}