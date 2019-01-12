package ima.cbedoy.ima2019

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.video_layout.*

class VideoHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    @SuppressLint("SetTextI18n")
    fun reload(item: VideoItem){
        title.text = item.title
        description.text = item.description

        views.text = "V: ${item.views}"
        likes.text = "L: ${item.like}"
        dislikes.text = "D: ${item.dislike}"

        Glide.with(thumbnail).load(item.thumbnail).into(thumbnail)
    }
}