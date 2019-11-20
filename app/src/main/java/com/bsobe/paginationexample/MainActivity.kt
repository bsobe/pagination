package com.bsobe.paginationexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bsobe.paginationexample.ui.type_selection.TypeSelectionFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            replaceFragment(TypeSelectionFragment.newInstance())
        }
    }

}
