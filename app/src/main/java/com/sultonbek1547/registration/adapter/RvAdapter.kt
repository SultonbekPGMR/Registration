package com.sultonbek1547.registration.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.model.User
import com.sultonbek1547.registration.databinding.RvItemBinding

class RvAdapter(
    val context: Context,
     val usersList: ArrayList<User>,
    val rvClick: RvClick
) :
    RecyclerView.Adapter<RvAdapter.Vh>() {

    inner class Vh(private val itemRvBinding: RvItemBinding) :
        RecyclerView.ViewHolder(itemRvBinding.root) {
        fun onBind(user: User, position: Int) {
            itemRvBinding.apply {
                tvName.text = user.fullName
                tvNumber.text = user.number
                image.setImageURI(Uri.parse(user.image))

                itemCard.setOnClickListener {
                    rvClick.itemClicked(user)
                }

                menu.setOnClickListener {
                    rvClick.itemMenuClicked(user,menu,position)
                }

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(RvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) =
        holder.onBind(usersList[position], position)


    override fun getItemCount(): Int = usersList.size


}

interface RvClick {

    fun itemClicked(user: User)
    fun itemMenuClicked(user: User,view: View,position: Int)

}