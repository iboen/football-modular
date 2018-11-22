package id.gits.football

interface BaseView<T> {

    var presenter: T
    fun showError(message:String?)

}