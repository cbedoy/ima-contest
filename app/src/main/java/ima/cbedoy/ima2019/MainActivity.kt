package ima.cbedoy.ima2019

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Intent
import android.net.Uri
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.text.Html




class MainActivity : AppCompatActivity() {

    var presenter: MainPresenter? = null
    var adapter: VideoAdapter = VideoAdapter()
    var dataModel = ArrayList<VideoItem>()

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


        credit_view.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/cbedoy"))
            startActivity(browserIntent)
        }
    }

    override fun onResume() {
        super.onResume()

        Handler().post {
            presenter?.loadVideos()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_views -> sortByCriteria("views")
            R.id.action_likes -> sortByCriteria("likes")
            R.id.action_dislikes -> sortByCriteria("dislikes")
            R.id.action_credit -> showCredit()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showCredit() {
        val builder = AlertDialog.Builder(this)

        builder.setMessage(R.string.description)

        builder.setCancelable(true)
        builder.setPositiveButton("Let me see") { dialog, which ->
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/cbedoy"))
            startActivity(browserIntent)
        }
        builder.create().show()
    }

    private fun sortByCriteria(criteria: String){
        if (dataModel.size >= 47){
            runOnUiThread {
                adapter.dataModel.clear()

                adapter.notifyDataSetChanged()


                when (criteria) {
                    "likes" -> dataModel.sortBy { it.like }
                    "views" -> dataModel.sortBy { it.views }
                    else -> dataModel.sortBy { it.dislike }
                }

                dataModel.asReversed().forEach {
                    adapter.dataModel.add(it)
                }


                adapter.notifyDataSetChanged()
            }
        }
    }

    fun onLoadedVideos(videos: ArrayList<VideoItem>) {
        dataModel = videos

        runOnUiThread {
            sortByCriteria("likes")
        }
    }
}
