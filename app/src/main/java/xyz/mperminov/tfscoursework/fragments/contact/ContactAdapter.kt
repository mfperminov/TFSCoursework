package xyz.mperminov.tfscoursework.fragments.contact


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.viewholder_contact.view.*
import kotlinx.android.synthetic.main.viewholder_contact_grid.view.*
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.models.Contact

class ContactAdapter(
    private val contacts: List<Contact>
) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {
    private var viewType: Int = LIST_ITEM

    fun changeLayout(viewType: Int) {
        this.viewType = viewType
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            LIST_ITEM -> ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.viewholder_contact, parent, false)
            )

            else -> ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.viewholder_contact_grid, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(contacts[position], position)

    override fun getItemCount(): Int = contacts.size

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {

        fun bind(contact: Contact, position: Int) {
            when (viewType) {
                LIST_ITEM -> with(itemView) {
                    round_view.setInitials(contact.getInitials())
                    name.text = "${contact.firstName} ${contact.lastName}"
                    if (position % 2 == 0) round_view.setColor(R.color.colorPrimary) else round_view.setColor(R.color.accent_material_dark)
                    points.text = context.getString(R.string.points_count, contact.points)
                }
                GRID_ITEM -> {
                    with(itemView) {
                        round_view_grid.setInitials(contact.getInitials())
                        name_grid.text = "${contact.firstName} ${contact.lastName}"
                        if (position % 2 == 0) round_view_grid.setColor(R.color.colorPrimary) else round_view_grid.setColor(
                            R.color.accent_material_dark
                        )

                    }
                }
            }
        }
    }

    companion object {
        const val LIST_ITEM = 1
        const val GRID_ITEM = 2
    }
}
