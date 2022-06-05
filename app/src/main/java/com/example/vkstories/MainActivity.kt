package com.example.vkstories

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MotionEventCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.vkstories.databinding.ActivityMainBinding
import jp.shts.android.storiesprogressview.StoriesProgressView


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
    }

}