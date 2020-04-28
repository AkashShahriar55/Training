package com.example.contraintlayoutpracticekotlin

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.TransitionManager
import androidx.constraintlayout.widget.ConstraintSet
import kotlinx.android.synthetic.main.keyframe1.*

class MainActivity : AppCompatActivity() {

    private val constraintSet1 = ConstraintSet()
    private val constraintSet2 = ConstraintSet()

    private var isOffscreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.keyframe1)

        constraintSet1.clone(constraintLayout) //1
        constraintSet2.clone(this, R.layout.activity_main) //2

        departButton.setOnClickListener { //3
            //apply the transition
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                TransitionManager.beginDelayedTransition(constraintLayout)
            } //4
            val constraint = if (!isOffscreen) constraintSet1 else constraintSet2
            isOffscreen = !isOffscreen
            constraint.applyTo(constraintLayout) //5
        }
    }
}
