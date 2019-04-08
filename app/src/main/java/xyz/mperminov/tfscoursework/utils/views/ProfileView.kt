package xyz.mperminov.tfscoursework.utils.views

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.profile_view.view.*
import xyz.mperminov.tfscoursework.R

class ProfileView : ConstraintLayout {

    private val DEFAULT_PROFILE_COLOR = R.color.colorPrimary
    private val DEFAULT_BADGE = 0
    private val DEFAULT_PROFILE_NAME = context.getString(R.string.user)
    private val BORDER_OPTIMAL_WIDTH = 6

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
        inflate(context, R.layout.profile_view, this)
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.ProfileView)
            val profileColor = a.getColor(
                R.styleable.ProfileView_profile_color,
                ContextCompat.getColor(context, DEFAULT_PROFILE_COLOR)
            )
            (imageView.drawable as GradientDrawable).setColor(profileColor)
            val badge = a.getInt(R.styleable.ProfileView_badge, DEFAULT_BADGE)
            setBadge(badge)
            val profileName = a.getString(R.styleable.ProfileView_profile_name)
            setName(profileName ?: DEFAULT_PROFILE_NAME)
            val isBorderSet = a.getBoolean(R.styleable.ProfileView_border_on, false)
            if (isBorderSet) enableBorder() else disableBorder()
            a.recycle()
        }
    }


    fun setBadge(value: Int) {
        badgeView.text = value.toString()
        if (value == 0) badgeView.visibility = View.GONE else badgeView.visibility = View.VISIBLE
        invalidate()
    }

    fun setName(name: String) {
        profile_name.text = name
        invalidate()
    }

    fun enableBorder() {
        val borderColor = ContextCompat.getColor(context, R.color.color_profile_selection)
        (imageView.drawable as GradientDrawable).setStroke(BORDER_OPTIMAL_WIDTH, borderColor)
        invalidate()
    }

    fun disableBorder() {
        val borderColor = ContextCompat.getColor(context, android.R.color.white)
        (imageView.drawable as GradientDrawable).setStroke(BORDER_OPTIMAL_WIDTH, borderColor)
        invalidate()
    }

}