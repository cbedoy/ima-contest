package ima.cbedoy.ima2019

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    var presenter: MainPresenter? = null
    var adapter: VideoAdapter = VideoAdapter()
    var rawItems = ArrayList<VideoItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/youtube/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        val service = retrofit.create<Service>(Service::class.java)

        presenter = MainPresenter()
        val interactor = MainInteractor()

        presenter?.interactor = interactor
        presenter?.viewController = this
        interactor.presenter = presenter
        interactor.service = service

        main_activity_recycler_view.layoutManager = LinearLayoutManager(this)
        main_activity_recycler_view.setHasFixedSize(true)
        main_activity_recycler_view.adapter = adapter

    }

    override fun onResume() {
        super.onResume()

        rawItems.clear()

        presenter?.loadVideos()
    }

    fun onLoadedVideos(videos: ArrayList<String>) {
        videos.forEach { it ->
            it.toString()
        }
    }

    fun onLoadedVideo(videoItem: VideoItem) {
        rawItems.add(videoItem)

        if (rawItems.size >= 47){
            runOnUiThread {
                rawItems.sortBy { it.like }

                rawItems.asReversed().forEach {
                    adapter.dataModel.add(it)
                }


                adapter.notifyDataSetChanged()
            }
        }
    }
}
