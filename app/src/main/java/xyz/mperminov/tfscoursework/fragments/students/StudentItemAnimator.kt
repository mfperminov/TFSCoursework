package xyz.mperminov.tfscoursework.fragments.students

import android.animation.AnimatorInflater
import android.content.Context
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import xyz.mperminov.tfscoursework.R

class StudentItemAnimator(val context: Context) : SimpleItemAnimator() {
    private var removeAnimationRunning = false
    override fun animateAdd(holder: RecyclerView.ViewHolder?): Boolean {
        val set = AnimatorInflater.loadAnimator(
            context,
            R.animator.zoom_in
        )
        set.interpolator = BounceInterpolator()
        set.setTarget(holder?.itemView)
        set.start()
        return true
    }

    override fun runPendingAnimations() {
    }

    override fun animateMove(holder: RecyclerView.ViewHolder?, fromX: Int, fromY: Int, toX: Int, toY: Int): Boolean {
        return true
    }

    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder?,
        newHolder: RecyclerView.ViewHolder?,
        fromLeft: Int,
        fromTop: Int,
        toLeft: Int,
        toTop: Int
    ): Boolean {
        return false
    }

    override fun isRunning(): Boolean {
        return false
    }

    override fun endAnimation(item: RecyclerView.ViewHolder) {

    }

    override fun animateRemove(holder: RecyclerView.ViewHolder?): Boolean {
        val set = AnimatorInflater.loadAnimator(
            context,
            R.animator.zoom_out
        )
        set.interpolator = DecelerateInterpolator()
        set.setTarget(holder?.itemView)
        set.start()
        return true
    }

    override fun endAnimations() {

    }

}