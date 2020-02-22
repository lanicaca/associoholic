package associoholic.com.client.repository.network.base

enum class Status {
    LOADING,
    SUCCESS,
    ERROR
}

@Suppress("DataClassPrivateConstructor")
data class Resource<T> private constructor(
        val data: T?,
        val status: Status,
        val e: Exception?,
        val msg: String?,
        val request: BaseRequest<T>? = null
) {
    companion object {
        @JvmStatic
        fun <T> success(data: T, request: BaseRequest<T>? = null) = Resource(data, Status.SUCCESS, null, null, request)

        @JvmStatic
        fun <T> loading(data: T?, request: BaseRequest<T>? = null) = Resource(data, Status.LOADING, null, null, request)

        @JvmStatic
        fun <T> error(data: T?, e: Exception?, msg: String?, request: BaseRequest<T>? = null) =
                Resource(data, Status.ERROR, e, msg, request)
    }
}