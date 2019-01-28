package ima.cbedoy.ima2019

class MainPresenter{
    fun loadVideos() {
        interactor?.requestVideos()
    }

    fun loadedVideos(videos: ArrayList<VideoItem>) {
        viewController?.onLoadedVideos(videos)
    }

    var interactor: MainInteractor? = null
    var viewController : MainActivity? = null
}