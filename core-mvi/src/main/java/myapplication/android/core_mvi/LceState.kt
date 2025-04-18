package myapplication.android.core_mvi

sealed interface LceState<out T> {

    data object Loading: LceState<Nothing>

    data class Content<out T>(val data: T): LceState<T>

    class Error(val throwable: Throwable): LceState<Nothing>
}