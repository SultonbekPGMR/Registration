package com.sultonbek1547.registration.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.sultonbek1547.registration.R
import com.sultonbek1547.registration.database.MyDbHelper
import com.sultonbek1547.registration.databinding.FragmentLoginBinding
import com.sultonbek1547.registration.utils.Constraints.UZB_PHONE_NUM_PREFIX

class Login : Fragment() {

    private val binding by lazy { FragmentLoginBinding.inflate(layoutInflater) }
    private lateinit var myDbHelper: MyDbHelper
    var state = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        myDbHelper = MyDbHelper(requireContext())
        var number: String
        var password: String
        val users = myDbHelper.getAllUser()


        /** log in */
        binding.btnLogIn.setOnClickListener {
            number = binding.edtNumber.text.toString().trim()
            password = binding.edtPassword.text.toString().trim()

            if (number.length > 1 && password.isNotEmpty()) {
                if (!UZB_PHONE_NUM_PREFIX.contains(number.substring(0, 2).toInt())) {
                    binding.edtNumber.error = "Raqam xato!"
                } else {

                    /** checking user started  */
                    users.forEach {
                        if (it.number == number && password == it.password) {
                            state = true
                            val showUsers = ShowUsers()
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.my_container, showUsers).commit()
                        }
                    }
                    if (!state) {
                        Toast.makeText(context, "Parol yoki raqam xato", Toast.LENGTH_SHORT).show()
                    }

                }
            }


        }

        /** going to Sign Up */
        binding.tvSignUp.setOnClickListener {
            val signUp = SignUp()
            parentFragmentManager.beginTransaction()
                .replace(R.id.my_container, signUp)
                .addToBackStack("back")
                .commit()
        }

        return binding.root
    }

}