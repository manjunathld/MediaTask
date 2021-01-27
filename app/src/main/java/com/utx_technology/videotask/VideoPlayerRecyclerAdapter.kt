package com.utx_technology.videotask

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.utx_technology.videotask.model.MediaData

class VideoPlayerRecyclerAdapter : RecyclerView.Adapter<VideoPlayerRecyclerAdapter.MediaViewHolder>() {

    private lateinit var mListOfMediaData: ArrayList<MediaData>

    class MediaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        public val postTitle: TextView = itemView.findViewById(R.id.tv_post_title_fragment_start_list_item)
        public val postDescription: TextView = itemView.findViewById(R.id.tv_post_description_fragment_start_list_item)
        public val postCoverImage: ImageView = itemView.findViewById(R.id.iv_post_cover_image_fragment_start)
        public val postAudioIcon: ImageView = itemView.findViewById(R.id.iv_volume_fragment_start)
        public val mediaContainer: FrameLayout = itemView.findViewById(R.id.fl_media_container_fragment)

        private val context: Context = itemView.context
        val parent: View = itemView

        public fun bindData(mediaData: MediaData) {
            val title: String = mediaData.postTitle
            val description: String = mediaData.postDescription
            parent.tag = this

            if (title.isNotEmpty()) {
                postTitle.text = mediaData.postTitle
            } else {
                postTitle.text = context.getString(R.string.hyphen)
            }

            if (description.isNotEmpty()) {
                postDescription.text = description
            } else {
                postDescription.text = context.getString(R.string.hyphen)
            }
        }
    }

    public fun setVideoPlayerRecyclerAdapterData(listOfMediaData: ArrayList<MediaData>) {
        mListOfMediaData = listOfMediaData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoPlayerRecyclerAdapter.MediaViewHolder {
        val fragmentStartListItemView: View = LayoutInflater.from(parent.context).inflate(
            R.layout.fragment_start_list_item,
            parent, false
        )
        return MediaViewHolder(fragmentStartListItemView)
    }

    override fun onBindViewHolder(holder: VideoPlayerRecyclerAdapter.MediaViewHolder, position: Int) {
        holder.bindData(mListOfMediaData.get(position))
    }

    override fun getItemCount(): Int {
        if (::mListOfMediaData.isInitialized) {
            return mListOfMediaData.size
        } else {
            return 0
        }
    }

}