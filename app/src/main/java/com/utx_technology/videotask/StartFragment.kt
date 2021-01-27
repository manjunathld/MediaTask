package com.utx_technology.videotask

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.utx_technology.videotask.model.MediaData


class StartFragment : Fragment() {

    private enum class VolumeState {ON, OFF}

    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private lateinit var mMediaRecyclerView: RecyclerView
    private lateinit var mVideoPlayerRecyclerAdapter: VideoPlayerRecyclerAdapter
    private lateinit var mListOfMediaData: ArrayList<MediaData>

    private lateinit var postCoverImage: ImageView
    private lateinit var postAudioIcon: ImageView
    private lateinit var viewHolderParent: View
    private lateinit var frameLayout: FrameLayout
    private lateinit var videoSurfaceView: PlayerView
    private lateinit var videoPlayer: SimpleExoPlayer

    private var videoSurfaceDefaultHeight: Int = 0;
    private var screenDefaultHeight: Int = 0;
    private var playPosition: Int = -1
    private var isVideoViewAdded: Boolean = false
    private lateinit var mContext: Context

    private lateinit var volumeState: VolumeState

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("onAttach", "Start Fragment")
        init(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("onCreate", "Start Fragment")
        mVideoPlayerRecyclerAdapter = VideoPlayerRecyclerAdapter()

        mListOfMediaData = arrayListOf<MediaData>()
        mListOfMediaData.add(MediaData("", "Post Title100", "#Description 100", 100))
        mListOfMediaData.add(MediaData("", "Post Title200", "#Description 200", 100))
        mListOfMediaData.add(MediaData("", "Post Title300", "#Description 300", 100))
        mListOfMediaData.add(MediaData("", "Post Title400", "#Description 400", 100))
        mListOfMediaData.add(MediaData("", "Post Title500", "#Description 500", 100))
        mListOfMediaData.add(MediaData("", "Post Title600", "#Description 600", 100))
        mListOfMediaData.add(MediaData("", "Post Title700", "#Description 700", 100))
        mListOfMediaData.add(MediaData("", "Post Title800", "#Description 800", 100))
        mListOfMediaData.add(MediaData("", "Post Title900", "#Description 900", 100))
        mListOfMediaData.add(MediaData("", "Post Title110", "#Description 110", 100))

        mVideoPlayerRecyclerAdapter.setVideoPlayerRecyclerAdapterData(mListOfMediaData)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("onCreateView", "Start Fragment")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("onViewCreated", "Start Fragment")
        view.apply {
            mMediaRecyclerView = findViewById(R.id.rv_media_fragment_start)
        }

        mMediaRecyclerView.addOnScrollListener(mOnScrollListener)
        mMediaRecyclerView.addOnChildAttachStateChangeListener(mOnAttachStateChangeListener)

        mLinearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mMediaRecyclerView.layoutManager = mLinearLayoutManager
        mMediaRecyclerView.adapter = mVideoPlayerRecyclerAdapter
        mMediaRecyclerView.hasFixedSize()

    }

    override fun onStart() {
        super.onStart()
        Log.d("onStart", "Start Fragment")
    }

    override fun onResume() {
        super.onResume()
        Log.d("onResume", "Start Fragment")
    }

    override fun onPause() {
        super.onPause()
        Log.d("onPause", "Start Fragment")
    }

    override fun onStop() {
        super.onStop()
        Log.d("onStop", "Start Fragment")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("onDestroyView", "Start Fragment")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("onDestroy", "Start Fragment")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("onDetach", "Start Fragment")
    }

    private val mOnScrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                postCoverImage.visibility = View.VISIBLE
            }

            //there's a special case when thr end of thr list has been reached
            if (!mMediaRecyclerView.canScrollVertically(1)) {
                playVideo(true)
            }else {
                playVideo(false)
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
        }

    }

    private val mOnAttachStateChangeListener = object : RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewDetachedFromWindow(view: View) {
            if (::viewHolderParent.isInitialized && viewHolderParent.equals(view)) {
                resetVideoView()
            }
        }

        override fun onChildViewAttachedToWindow(view: View) {
        }

    }

    private val mPlayerListener = object : Player.EventListener {

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            super.onPlayerStateChanged(playWhenReady, playbackState)
            when (playbackState) {
                Player.STATE_IDLE -> {

                }
                Player.STATE_BUFFERING -> {

                }
                Player.STATE_READY -> {
                    if (!isVideoViewAdded) {
                        addVideoView()
                    }
                }
                Player.STATE_ENDED -> {
                    videoPlayer.seekTo(0)
                }
                else -> {}
            }
        }

        override fun onIsLoadingChanged(isLoading: Boolean) {
            super.onIsLoadingChanged(isLoading)
        }

        override fun onTracksChanged(trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray) {
            super.onTracksChanged(trackGroups, trackSelections)
        }

        override fun onTimelineChanged(timeline: Timeline, reason: Int) {
            super.onTimelineChanged(timeline, reason)
        }
    }

    private val mVideoViewClickListener = object : View.OnClickListener {
        override fun onClick(p0: View?) {
            toggleVolume()
        }

    }

    private fun init(context: Context) {
        mContext = context
        val display: Display = requireActivity().windowManager.defaultDisplay
        val point: Point = Point()
        display.getSize(point)
        videoSurfaceDefaultHeight = point.x
        screenDefaultHeight = point.y

        videoSurfaceView = PlayerView(context)
        videoSurfaceView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM

        //Create player
        videoPlayer = SimpleExoPlayer.Builder(context).build()
        //Bind player to view
        videoSurfaceView.useController = false
        videoSurfaceView.player = videoPlayer
        setVolumeControl(VolumeState.ON)

       /* mMediaRecyclerView.addOnScrollListener(mOnScrollListener)
        mMediaRecyclerView.addOnChildAttachStateChangeListener(mOnAttachStateChangeListener)*/
        videoPlayer.addListener(mPlayerListener)

    }

    private fun playVideo(isEndOfList: Boolean) {
        var targetPosition = 0

        if (!isEndOfList) {
            var startPosition: Int = mLinearLayoutManager.findFirstVisibleItemPosition()
            var endPosition: Int = mLinearLayoutManager.findLastVisibleItemPosition()

            //if there is more than 2 list-item on the screen, set the difference ot be one
            if (endPosition - startPosition > 1) {
                endPosition = startPosition + 1
            }

            //something went wrong
            if (startPosition < 0 || endPosition < 0) {
                return
            }

            //if there is more than one list-item on the screen
            if (startPosition != endPosition) {
                var startPositionVideoHeight: Int = getVisibleVideoSurfaceHeight(startPosition)
                var endPositionVideoHeight: Int = getVisibleVideoSurfaceHeight(endPosition)

                if (startPositionVideoHeight > endPositionVideoHeight) {
                    targetPosition = startPosition
                } else {
                    targetPosition = endPosition
                }

            } else {
                targetPosition = mListOfMediaData.size - 1
            }

            Log.d("PlayVideo", "Target Position$targetPosition")

            //Video is already playing so return
            if (targetPosition == playPosition) {
                return
            }

            //Set the position of thr list-item that is to be played
            videoSurfaceView.visibility = View.INVISIBLE
            removeVideoView(videoSurfaceView)

            var currentPosition: Int = targetPosition - mLinearLayoutManager.findFirstVisibleItemPosition()

            var child: View = mMediaRecyclerView.getChildAt(currentPosition)

            var holder: VideoPlayerRecyclerAdapter.MediaViewHolder = child.tag as VideoPlayerRecyclerAdapter.MediaViewHolder

            postCoverImage = holder.postCoverImage
            postAudioIcon = holder.postAudioIcon
            viewHolderParent = holder.itemView
            frameLayout = holder.mediaContainer

            videoSurfaceView.player = videoPlayer

            viewHolderParent.setOnClickListener(mVideoViewClickListener)

            var mediaUrl: String = ""
            if (mediaUrl.isNotEmpty()) {
                val mediaItem: MediaItem = MediaItem.fromUri(RawResourceDataSource.buildRawResourceUri(R.raw.tu_hi_hai))
                //Set the media item to player
                videoPlayer.setMediaItem(mediaItem)
                //Prepare the player
                videoPlayer.prepare()
                videoPlayer.playWhenReady = true
            }

        }

    }
    /**
     * Returns the visibility region of thr video surface on the screen.
     * If come is cut-off, it will return liss than the @videoSurfaceDefaultHeight
     * @param playPosition
     * @return
     **/
    private fun getVisibleVideoSurfaceHeight(playPosition: Int): Int {
        var at: Int = playPosition - mLinearLayoutManager.findFirstVisibleItemPosition()
        Log.d("getVisibleSurfaceHeight", "" + at)

        val child: View = mMediaRecyclerView.getChildAt(at)

        val location: IntArray = IntArray(2)
        if (location[0] < 0) {
            return location[1] + videoSurfaceDefaultHeight
        } else {
            return screenDefaultHeight - location[1]
        }
    }

    //Remove the old player
    private fun removeVideoView(videoView: PlayerView) {
        if (videoView.parent != null) {
            val parent: ViewGroup = videoView.parent as ViewGroup
            var index: Int = parent.indexOfChild(videoView)
            if (index >= 0) {
                parent.removeViewAt(index)
                isVideoViewAdded = false
                viewHolderParent.setOnClickListener(null)
            }
        }
    }

    private fun addVideoView() {
        frameLayout.addView(videoSurfaceView)
        isVideoViewAdded = true
        videoSurfaceView.requestFocus()
        videoSurfaceView.visibility = View.VISIBLE
        //videoSurfaceView.alpha = 1
        postCoverImage.visibility = View.GONE
    }

    private fun resetVideoView() {
        if (isVideoViewAdded) {
            removeVideoView(videoSurfaceView)
            playPosition = -1
            videoSurfaceView.visibility = View.INVISIBLE
            postCoverImage.visibility = View.VISIBLE
        }
    }

    private fun releasePlayer() {
        if (::videoPlayer.isInitialized) {
            videoPlayer.release()
            videoPlayer
        }
    }

    private fun toggleVolume() {
        if (::videoPlayer.isInitialized) {
            if (volumeState == VolumeState.OFF) {
                Log.d("Volume state", "enable")
                setVolumeControl(VolumeState.ON)
            } else if (volumeState == VolumeState.ON) {
                Log.d("Volume state", "disable")
                setVolumeControl(VolumeState.OFF)
            }
        }
    }

    private fun setVolumeControl(state: VolumeState) {
        volumeState = state
        if (state == VolumeState.OFF) {
            videoPlayer.volume = 0f
            animationControl()
        } else if (state == VolumeState.ON) {
            videoPlayer.volume = 1f
            animationControl()
        }
    }

    private fun animationControl() {
        if (::postAudioIcon.isInitialized) {
            postAudioIcon.bringToFront()
            if (volumeState == VolumeState.OFF) {

            } else if (volumeState == VolumeState.ON) {

            }

            postAudioIcon.animate().cancel()
            postAudioIcon.alpha = 1f
            postAudioIcon.animate()
                .alpha(0f)
                .setDuration(600).setStartDelay(1000)
        }
    }

}