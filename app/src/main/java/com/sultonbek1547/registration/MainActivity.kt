package com.sultonbek1547.registration

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sultonbek1547.registration.databinding.ActivityMainBinding
import com.sultonbek1547.registration.fragment.Login
import com.sultonbek1547.registration.fragment.ShowUsers
import com.sultonbek1547.registration.fragment.SignUp
import com.sultonbek1547.registration.utils.MySharedPreference
import com.sultonbek1547.registration.utils.MySharedPreference.LOG_IN_STATE

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        MySharedPreference.init(this)
        if (LOG_IN_STATE){
            val showUsers = ShowUsers()
            supportFragmentManager.beginTransaction().add(binding.myContainer.id, showUsers).commit()

        }else{
            val logIn = Login()
            supportFragmentManager.beginTransaction().add(binding.myContainer.id, logIn).commit()
        }



    }
}