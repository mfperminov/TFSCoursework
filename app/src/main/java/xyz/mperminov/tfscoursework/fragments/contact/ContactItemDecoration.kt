package xyz.mperminov.tfscoursework.fragments.contact

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import xyz.mperminov.tfscoursework.R

class ContactItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val BORDER_WIDTH = 4
    private val rect: Rect = Rect(BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH)
    private val paint: Paint = Paint()

    init {
        paint.color = ContextCompat.getColor(context, R.color.color_item_decoration)
        paint.style = Paint.Style.STROKE
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(rect.left, rect.top, rect.right, rect.bottom)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            c.drawRect(
                child.left.toFloat(),
                child.top.toFloat(),
                child.right.toFloat(),
                child.bottom.toFloat(),
                paint
            )
        }
    }
}
