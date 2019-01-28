package ima.cbedoy.ima2019

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainInteractor{
    private val KEY = "AIzaSyC5NDNAsd8yVmzmFrERfsJnIQYfPW0eZRg"

    fun requestVideos() {
        val call = service?.getChannel(
            KEY,
            "UCV3sr3fVEX4CDZxxl-W4Q8Q",
            "snippet,id",
            "date",
            50
        )
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
        val identifiers = ArrayList<String>()
        items.forEach { item ->
            val id = item["id"] as Map<String, Any>
            if (id.containsKey("videoId")){

                val identifier = id["videoId"] as String?

                if (identifier != null)
                    identifiers.add(identifier)
            }
        }

        getStatistics(identifiers)
    }

    private fun getStatistics(identifiers: ArrayList<String>) {
        val joinToString = identifiers.joinToString { it }

        val call = service?.getStatistics("statistics, snippet", joinToString, KEY)
        call?.enqueue(object : Callback<Map<String, Any>>{
            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {

            }

            override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                prepareStatistics(response)
            }

        })
    }

    private fun prepareStatistics(response: Response<Map<String, Any>>) {
        val videos = ArrayList<VideoItem>()
        if (response.isSuccessful){
            val body = response.body()
            if (body != null) {
                if (body.containsKey("items")){
                    val items = body["items"] as ArrayList<Map<String, Any>>
                    items.forEach {
                        val videoItem = prepareEntry(it)

                        if (videoItem != null)
                            videos.add(videoItem)
                    }
                }
            }
        }
        presenter?.loadedVideos(videos)
    }


    private fun prepareEntry(data: Map<String, Any>?) : VideoItem? {
        if (data != null){
            val id = data["id"] as String
            val statistics : Map<String, Any> = data["statistics"] as Map<String, Any>
            val snippet = data["snippet"] as Map<String, Any>

            val videoItem = VideoItem()

            videoItem.id = id
            videoItem.views = statistics["viewCount"].toString().toInt()
            videoItem.like = statistics["likeCount"].toString().toInt()
            videoItem.dislike = statistics["dislikeCount"].toString().toInt()
            videoItem.favorite = statistics["favoriteCount"].toString().toInt()
            videoItem.url = "https://www.youtube.com/watch?v=$id"
            videoItem.title = snippet["title"] as String?
            videoItem.description = snippet["description"] as String?
            videoItem.thumbnail = getThumbnailFromSnippet(snippet["thumbnails"] as Map<String, Any>?)

            return videoItem
        }
        return null
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