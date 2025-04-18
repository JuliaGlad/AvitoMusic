package myapplication.android.core_mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.cancellation.CancellationException

inline fun <T> runCatchingNonCancellation(block: () -> T): Result<T>{
    return try {
        Result.success(block())
    } catch (e: CancellationException){
        throw e
    } catch (e: Exception){
        Result.failure(e)
    }
}

suspend fun <T, R> asyncAwait(
    s1: suspend CoroutineScope.() -> T,
    transform: suspend (T) -> R
): R {
    return coroutineScope{
        val result = async(block = s1)
        transform(result.await())
    }
}

suspend fun <T1, T2, R> asyncAwait(
    s1: suspend CoroutineScope.() -> T1,
    s2: suspend CoroutineScope.() -> T2,
    transform: suspend (T1, T2) -> R
): R {
    return coroutineScope{
        val result1 = async(block = s1)
        val result2 = async(block = s2)
        transform(result1.await(), result2.await())
    }
}