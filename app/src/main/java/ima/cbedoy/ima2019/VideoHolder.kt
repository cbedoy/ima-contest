package ima.cbedoy.ima2019

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
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

        views.text = "▶️: ${formatViews(item.views)}"
        likes.text = "\uD83D\uDC4D: ${item.like}"
        dislikes.text = "\uD83D\uDC4E: ${item.dislike}"

        Glide.with(thumbnail).load(item.thumbnail).into(thumbnail)

        itemView.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
            containerView.context.startActivity(browserIntent)
        }

        currentPosition.text = (adapterPosition + 1).toString()
    }

    private fun formatViews(views: Int) : String {
        return if (views >= 1000) "%.1f".format((views.toFloat() / 1000)) + "K" else views.toString()
    }
}