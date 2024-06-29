package com.android.project4.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.project4.R
import com.android.project4.databinding.FragmentOTPBinding
import com.android.project4.databinding.FragmentSplashBinding


class OTPFragment : Fragment() {

    private lateinit var binding: FragmentOTPBinding
    private lateinit var userNumber: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOTPBinding.inflate(layoutInflater)

        getUserNumber()
        return binding.root
    }

    private fun getUserNumber() {
        val bundle =arguments
        userNumber = bundle?.getString("number").toString()

        binding.tvUserNumber.text = userNumber
    }


}