package com.example.smkcoding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentActivity: FragmentActivity)
    : FragmentStateAdapter(fragmentActivity){

    private val JumlahMenu = 2
    override fun getItemCount(): Int {
        return JumlahMenu
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0->{return HomeFragment()}
            1->{return SearchFragment()}
            else->{return HomeFragment()}
        }
    }

}