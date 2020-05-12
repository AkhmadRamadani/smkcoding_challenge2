package com.example.smkcoding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val judulMenu = arrayOf("Home", "Search")
    val iconMenu =  arrayOf(R.drawable.ic_home_black_24dp,R.drawable.ic_search_black_24dp)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val adapter = ViewPagerAdapter(this)
        viewpager.adapter = adapter
        TabLayoutMediator(tablayout, viewpager,
            TabLayoutMediator.TabConfigurationStrategy {tab, position ->
                tab.text = judulMenu[position]
                tab.icon = ResourcesCompat.getDrawable(
                    resources, iconMenu[position], null
                )
            }).attach()
    }
}
