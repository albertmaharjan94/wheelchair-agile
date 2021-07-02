package com.softwarica.wheelchairapp.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.softwarica.wheelchairapp.R
import com.softwarica.wheelchairapp.ui.main.Dash.ModelFragment
import com.softwarica.wheelchairapp.ui.main.Maps.MapsFragment
import com.softwarica.wheelchairapp.ui.main.Stats.PlaceholderFragment
import com.softwarica.wheelchairapp.ui.main.Stats.PlaceholderFragment.Companion.newInstance
import com.softwarica.wheelchairapp.ui.main.User.AccountsFragment

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_3,
    R.string.tab_text_2,
    R.string.tab_text_4
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager, val mode : String) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        when(position){
            0 -> return ModelFragment.newInstance(position + 1, mode)
            1 -> return MapsFragment.newInstance(position + 1)
            2 -> return PlaceholderFragment.newInstance(position + 1)
            3 -> return AccountsFragment.newInstance(position + 1)
            else -> {

            }
        }
        return ModelFragment.newInstance(position + 1, mode)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 4
    }
}