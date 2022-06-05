package com.example.vkstories

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.example.vkstories.databinding.FragmentStoriesBinding
import jp.shts.android.storiesprogressview.StoriesProgressView
import jp.wasabeef.glide.transformations.BlurTransformation
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

    private val stories = listOf(
        Story(
            title = "Красиво",
            city = "Нью-Йорк, США",
            date = "21 мая",
            description = "Не, ну красиво же",
            link = "https://www.figma.com/file/sMBZbnd3BFhVLgWuuXKnuJ/Vezdekod-2022-Tomsk-%2B-Irkutsk?node-id=0%3A1",
            image = R.drawable.img
        ),
        Story(
            title = "Тоже красиво",
            city = "Нью-Йорк2, США2",
            date = "30 июня",
            description = "Тоже Нью-Йорк, вроде",
            link = "https://stackoverflow.com/",
            image = R.drawable.img_1
        ),
        Story(
            title = "Небоскребы",
            city = "Иркутск, Россия",
            date = "5 июля",
            description = "Похорошел же Иркутск при IT-академии",
            link = "https://shikimori.one/",
            image = R.drawable.img_2
        ),
        Story(
            title = "Дорога",
            city = "Ангарск, Россия",
            date = "8 августа",
            description = "Дорога в иркутск",
            link = "https://www.istu.edu/",
            image = R.drawable.img_3
        ),
        Story(
            title = "Красиво",
            city = "Выдуманный город, Не знаю",
            date = "10 сентября",
            description = "Небо красивое",
            link = "https://skillbox.ru/,",
            image = R.drawable.img_4
        )
    )

    private lateinit var storiesProgressView: StoriesProgressView
    private lateinit var image: ImageView

    private var counter = 0

    private var x1 = 0f
    private var x2 = 0f
    private val minDistance = 50

    private var isNexted = true
    private var isPreved = false

    private var isNextByButton = false
    private var isPrevByButton = false

    private val swipeTouchListener = object : View.OnTouchListener {
        override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
            val action: Int = MotionEventCompat.getActionMasked(p1)

            return when (action) {
                MotionEvent.ACTION_DOWN -> {
                    x1 = p1!!.x
                    true
                }
                MotionEvent.ACTION_UP -> {
                    x2 = p1!!.x
                    val deltaX: Float = x2 - x1
                    if (abs(deltaX) > minDistance) {
                        if (x2 > x1) {
                            isPreved = false
                            storiesProgressView.reverse()
                            isPreved = true
                            isPrevByButton = true
                            onPrev()
                        } else {
                            isNexted = false
                            storiesProgressView.skip()
                            isNexted = true
                            isNextByButton = true
                            onNext()
                        }
                    } else {
                        val width = getScreenWidth()
                        Log.d("BRB", "$width - $x2")
                        if (x2 > width) {
                            isNexted = false
                            storiesProgressView.skip()
                            isNexted = true
                            isNextByButton = true
                            onNext()
                        } else {
                            isPreved = false
                            storiesProgressView.reverse()
                            isPreved = true
                            isPrevByButton = true
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

        init()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init() {

        storiesProgressView = binding.stories


        storiesProgressView.setStoriesCount(images.size)


        storiesProgressView.setStoryDuration(5000L)


        storiesProgressView.setStoriesListener(this)


        storiesProgressView.startStories(counter)

        image = binding.image

        image.setImageResource(images[counter])

        binding.layoutGestures.setOnTouchListener(swipeTouchListener)

        setImages(counter)
    }

    private fun getScreenWidth(): Int {
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels / 2
    }

    override fun onNext() {
        if (isNexted) {
            if (!isNextByButton) counter++
            isNextByButton = false
            if (counter >= images.size) {
                onComplete()
                return
            }
            setImages(counter)
        } else {
            counter++
            isNexted = true
        }
    }

    override fun onPrev() {
        if (isPreved) {
            isPreved = false
            setImages(counter)
        } else {
            if (counter > 0) counter--
            isPreved = true
        }
    }

    private fun setImages(index: Int) {
        val imageResource = stories[index].image
        Glide.with(requireContext())
            .load(imageResource)
            .placeholder(R.drawable.background_button_link)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .apply(bitmapTransform(BlurTransformation(50)))
            .into(binding.imageBlurry)

        image.setImageResource(imageResource)

        updateContent(index)
    }

    private fun updateContent(index: Int) {
        binding.tvCity.text = stories[index].city
        binding.tvDate.text = stories[index].date
        binding.tvDescription.text = stories[index].description
        binding.tvTitle.text = stories[index].title
        binding.btnLink.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(stories[index].link))
            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(browserIntent)
        }
    }

    override fun onComplete() {
        findNavController().popBackStack()
    }

    override fun onDestroy() {
        storiesProgressView.destroy()
        super.onDestroy()
    }
}