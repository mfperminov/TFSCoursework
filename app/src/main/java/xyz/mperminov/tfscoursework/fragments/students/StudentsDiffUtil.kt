package xyz.mperminov.tfscoursework.fragments.students

import androidx.recyclerview.widget.DiffUtil
import xyz.mperminov.tfscoursework.models.Contact

class ContactsDiffUtil(val oldList: List<Contact>, val newList: List<Contact>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]

}