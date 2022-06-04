package com.example.vkstories

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.MotionEventCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.vkstories.databinding.FragmentStoriesBinding
import jp.shts.android.storiesprogressview.StoriesProgressView
import kotlin.math.abs


class StoriesFragment : Fragment(), StoriesProgressView.StoriesListener {

    private val binding by viewBinding(FragmentStoriesBinding::bind)

    private val images = listOf(
        R.drawable.img,
        R.drawable.img_1,
        R.drawable.img_2,
        R.drawable.img_3,
        R.drawable.img_4,
    )

    private var pressTime = 0L
    private var limit = 500L

    private lateinit var storiesProgressView: StoriesProgressView
    private lateinit var image: ImageView

    private var counter = 0

    private var x1 = 0f
    private var x2 = 0f
    private val minDistance = 50

    private var isNexted = false
    private var isPreved = false

    private val onTouchListener: View.OnTouchListener = object : View.OnTouchListener {
        override fun onTouch(v: View?, event: MotionEvent): Boolean {
            // inside on touch method we are
            // getting action on below line.
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d("BRUHHH", "DOWN")

                    // on action down when we press our screen
                    // the story will pause for specific time.
                    pressTime = System.currentTimeMillis()

                    // on below line we are pausing our indicator.
                    storiesProgressView.pause()
                    return false
                }
                MotionEvent.ACTION_UP -> {
                    Log.d("BRUHHH", "UP")

                    // in action up case when user do not touches
                    // screen this method will skip to next image.
                    val now = System.currentTimeMillis()

                    // on below line we are resuming our progress bar for status.
                    storiesProgressView.resume()

                    // on below line we are returning if the limit < now - presstime
                    return limit < now - pressTime
                }
            }
            return false
        }
    }

    private val swipeTouchListener = object : View.OnTouchListener {
        override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
            val action: Int = MotionEventCompat.getActionMasked(p1)

            return when (action) {
                MotionEvent.ACTION_DOWN -> {
                    x1 = p1!!.x
                    //Log.d("BRUHH", "DOWN = $x1")
                    true
                }
                MotionEvent.ACTION_UP -> {
                    x2 = p1!!.x
                    val deltaX: Float = x2 - x1
                    if (abs(deltaX) > minDistance) {
                        if (x2 > x1) {
                            //Log.d("BRUHH", "UP NEXT = $x2")
                            storiesProgressView.reverse()
                            isPreved = true
                            onPrev()
                        } else {
                            //Log.d("BRUHH", "UP PREV = $x2")
                            storiesProgressView.skip()
                            isNexted = true
                            onNext()
                        }
                    } else {
                        val width = getScreenWidth()
                        Log.d("BRB", "$width - $x2")
                        if (x2 > width) {
                            //Log.d("BRUHH", "====UP NEXT = $x2")
                            storiesProgressView.skip()
                            isNexted = true
                            onNext()
                        } else {
                            //Log.d("BRUHH", "====UP PREV = $x2")
                            storiesProgressView.reverse()
                            isPreved = true
                            onPrev()
                        }
                    }
                    true
                }
                else -> return false
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init(view: View) {
         //on below line we are initializing our variables .
        storiesProgressView = binding.stories

        // on below line we are setting the total count for our stories.

        // on below line we are setting the total count for our stories.
        storiesProgressView.setStoriesCount(images.size)

        // on below line we are setting story duration for each story.

        // on below line we are setting story duration for each story.
        storiesProgressView.setStoryDuration(5000L)

        // on below line we are calling a method for set
        // on story listener and passing context to it.

        // on below line we are calling a method for set
        // on story listener and passing context to it.
        storiesProgressView.setStoriesListener(this)

        // below line is use to start stories progress bar.

        // below line is use to start stories progress bar.
        storiesProgressView.startStories(counter)

        // initializing our image view.

        // initializing our image view.

        image = binding.image
        // on below line we are setting image to our image view.

        // on below line we are setting image to our image view.
        image.setImageResource(images[counter])

        // below is the view for going to the previous story.
        // initializing our previous view.

        // below is the view for going to the previous story.
        // initializing our previous view.
        val reverse = binding.reverse

        // adding on click listener for our reverse view.

        // adding on click listener for our reverse view.
//        reverse.setOnClickListener { // inside on click we are
//            // reversing our progress view.
//            //storiesProgressView.reverse()
//            isPreved = true
//            onPrev()
//        }

        // on below line we are calling a set on touch
        // listener method to move towards previous image.

        // on below line we are calling a set on touch
        // listener method to move towards previous image.
        //reverse.setOnTouchListener(swipeTouchListener)

        // on below line we are initializing
        // view to skip a specific story.

        // on below line we are initializing
        // view to skip a specific story.
        val skip = binding.skip
//        skip.setOnClickListener { // inside on click we are
//            // skipping the story progress view.
//            //storiesProgressView.skip()
//            isNexted = true
//            onNext()
//        }
        // on below line we are calling a set on touch
        // listener method to move to next story.
        // on below line we are calling a set on touch
        // listener method to move to next story.
        //skip.setOnTouchListener(swipeTouchListener)
        //binding.layoutStories.setOnTouchListener(onTouchSwipeListener)
        binding.layoutGestures.setOnTouchListener(swipeTouchListener)

    }

    private fun getScreenWidth(): Int {
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels / 2
    }

    override fun onNext() {
        Log.d("BRUH", "NEXT $counter")
        // this method is called when we move
        // to next progress view of story.
        if (isNexted) {
            isNexted = false
            //Log.d("BRUH", "${counter + 1}")
            if (counter >= images.size) {
                onComplete()
                return
            }
            image.setImageResource(images[counter])
        } else {

             counter++
        }
        //
    }

    override fun onPrev() {
        Log.d("BRUH", "PREV $counter")
        // this method id called when we move to previous story.
        // on below line we are decreasing our counter
        if (isPreved) {
            isPreved = false
            //if (counter < 0) return;
            //Log.d("BRUH", "${counter - 1}")
            // on below line we are setting image to image view
            image.setImageResource(images[counter])
            //storiesProgressView.reverse()
        } else {
            //Log.d("BRUH", "MINUS")
            if (counter > 0) counter--
        }
        //
    }

    override fun onComplete() {
        findNavController().popBackStack()
    }

    override fun onDestroy() {
        storiesProgressView.destroy()
        super.onDestroy()
    }
}