package ima.cbedoy.ima2019

import android.annotation.SuppressLint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainInteractor{

    private val videos : HashMap<String, Any> = HashMap()
    private val videoItems =  ArrayList<VideoItem>()

    fun requestVideos() {
        val call = service?.getChannel(
            "AIzaSyC5NDNAsd8yVmzmFrERfsJnIQYfPW0eZRg",
            "UCV3sr3fVEX4CDZxxl-W4Q8Q",
            "snippet,id",
            "date",
            50
        )

        videos.clear()
        videoItems.clear()

        call?.enqueue(object : Callback<Map<String, Any>>{
            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {

            }

            override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                prepareItems(response.body())


            }
        })
    }

    private fun prepareItems(body: Map<String, Any>?) {
        val items = body?.get("items") as ArrayList<Map<String, Any>>
        val calls = ArrayList<Call<Map<String, Any>>>()
        items.forEach { item ->
            val id = item["id"] as Map<String, Any>
            if (id.containsKey("videoId")){

                val identifier = id["videoId"] as String

                val call =
                    service?.getVideo("statistics", identifier, "AIzaSyC5NDNAsd8yVmzmFrERfsJnIQYfPW0eZRg")

                if (call != null)
                    calls.add(call)

                videos[identifier] = item
            }
        }

        getVideoCount(calls)
    }

    @SuppressLint("CheckResult")
    private fun getVideoCount(requests: ArrayList<Call<Map<String, Any>>>) {
        requests.forEach { call ->
            call.enqueue(object : Callback<Map<String, Any>>{
                override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {

                }

                override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                    prepareEntry(response.body())
                }
            })
        }
    }

    private fun prepareEntry(body: Map<String, Any>?) {
        if (body != null){
            val items :List<Map<String, Any>> = body["items"] as List<Map<String, Any>>
            if (items.isNotEmpty()){
                val data = items[0]
                val id = data["id"] as String
                val statistics : Map<String, Any> = data["statistics"] as Map<String, Any>

                if (videos.containsKey(id)) {

                    val video = videos[id] as Map<String, Any>

                    val snippet = video["snippet"] as Map<String, Any>

                    val videoItem = VideoItem()

                    videoItem.id = id
                    videoItem.views = statistics["viewCount"].toString().toInt()
                    videoItem.like = statistics["likeCount"].toString().toInt()
                    videoItem.dislike = statistics["dislikeCount"].toString().toInt()
                    videoItem.favorite = statistics["favoriteCount"].toString().toInt()
                    videoItem.title = snippet["title"] as String
                    videoItem.description = snippet["description"] as String
                    videoItem.thumbnail = getThumbnailFromSnippet(snippet["thumbnails"] as Map<String, Any>?)
                    videoItem.url = "https://www.youtube.com/watch?v=$id"

                    videoItems.add(videoItem)

                    presenter?.loadedVideo(videoItem)
                }
            }
        }
    }

    private fun getThumbnailFromSnippet(thumbnails: Map<String, Any>?): String {
        if (thumbnails == null || thumbnails.isEmpty())
            return "https://e-fisiomedic.com/wp-content/uploads/2013/11/default-placeholder-300x300.png"
        val high = thumbnails["high"] as Map<String, Any>
        return high["url"] as String
    }

    var presenter: MainPresenter? = null
    var service: Service? = null
}