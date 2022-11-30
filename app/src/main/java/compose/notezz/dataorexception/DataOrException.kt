package compose.notezz.dataorexception
import kotlin.jvm.internal.Intrinsics.Kotlin

class DataOrException<T, Boolean, E:Exception> (
    var data: T? = null,
    var loading : kotlin.Boolean? = null,
    var e: E? = null
)
