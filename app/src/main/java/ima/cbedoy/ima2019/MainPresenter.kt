package ima.cbedoy.ima2019

class MainPresenter{
    fun loadVideos() {
        interactor?.requestVideos()
    }

    fun loadedVideo(videoItem: VideoItem) {
        viewController?.onLoadedVideo(videoItem)
    }

    var interactor: MainInteractor? = null
    var viewController : MainActivity? = null
}