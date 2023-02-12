package com.sultonbek1547.registration.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.model.User
import com.sultonbek1547.registration.R
import com.sultonbek1547.registration.adapter.RvAdapter
import com.sultonbek1547.registration.adapter.RvClick
import com.sultonbek1547.registration.database.MyDbHelper
import com.sultonbek1547.registration.databinding.BottomSheetBinding
import com.sultonbek1547.registration.databinding.FragmentShowUsersBinding
import com.sultonbek1547.registration.utils.MySharedPreference
import com.sultonbek1547.registration.utils.MySharedPreference.LOG_IN_STATE
import java.io.File


class ShowUsers : Fragment(), RvClick {
    private val binding by lazy { FragmentShowUsersBinding.inflate(layoutInflater) }
    private lateinit var myDbHelper: MyDbHelper
    private lateinit var rvAdapter: RvAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        myDbHelper = MyDbHelper(requireContext())
        val users = myDbHelper.getAllUser()

        rvAdapter = RvAdapter(requireContext(), users, this)
        binding.myRv.adapter = rvAdapter

        MySharedPreference.init(requireContext())
        if (!LOG_IN_STATE) {
            LOG_IN_STATE = true
        }

        binding.toolbar.setOnMenuItemClickListener {
            showAlertDialog()

            true
        }


        return binding.root
    }

    override fun itemClicked(user: User) {
        val bottomSheetDialog =
            BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
        val bottomSheetBinding = BottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetDialog.show()


    }

    override fun itemMenuClicked(user: User, view: View, position: Int) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.inflate(R.menu.pop_up_menu)
        popupMenu.setOnMenuItemClickListener {
            rvAdapter.usersList.removeAt(position)
            rvAdapter.notifyItemRemoved(position)
            rvAdapter.notifyItemRangeChanged(0, rvAdapter.itemCount)
            myDbHelper.deleteUser(user)
            val file = File(requireActivity().filesDir, "${user.id}.jpg")
            file.delete()
            return@setOnMenuItemClickListener true
        }

        popupMenu.show()


    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Tizimdan chiqmoqchimisiz")
        builder.setPositiveButton("Xa") { _, _ ->
            LOG_IN_STATE = false
            val logIn = Login()
            parentFragmentManager.beginTransaction().replace(R.id.my_container, logIn).commit()

        }
        builder.setNegativeButton("Yo'q", null)
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

}