package com.sampah.banksampahdigital.common.onboarding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.sampah.banksampahdigital.R

class OnboardingAdapter(private val context: Context, private val onBoardingItemsList: List<OnboardingItem>):
    PagerAdapter() {
    override fun getCount(): Int {
        return onBoardingItemsList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View = LayoutInflater.from(context).inflate(R.layout.onboarding_screen_layout, null)

        val imageView: ImageView = view.findViewById(R.id.imageViewOnboarding);
        val title: TextView = view.findViewById(R.id.tv_titleOnboarding);
        val subtitle: TextView = view.findViewById(R.id.tv_subtitleOnboarding);

        imageView.setImageResource(onBoardingItemsList[position].imageResId)
        title.text = onBoardingItemsList[position].title
        subtitle.text = onBoardingItemsList[position].subtitle

        container.addView(view)
        return view
    }
}