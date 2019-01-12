package ima.cbedoy.ima2019

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class VideoAdapter : RecyclerView.Adapter<VideoHolder>() {

    var dataModel = ArrayList<VideoItem>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): VideoHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.video_layout, viewGroup, false)

        return VideoHolder(view)
    }

    override fun getItemCount(): Int {
        return dataModel.size
    }

    override fun onBindViewHolder(holder: VideoHolder, position: Int) {
        holder.reload(dataModel[position])
    }

}