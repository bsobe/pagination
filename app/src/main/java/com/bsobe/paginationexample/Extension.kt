package com.bsobe.paginationexample

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

inline fun FragmentActivity.replaceFragment(fragment: Fragment, addToBackStack: Boolean = false) {
    val fragmentTransaction =
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
    if (addToBackStack) {
        fragmentTransaction.addToBackStack(fragment::class.java.simpleName)
    }
    fragmentTransaction.commit()
}