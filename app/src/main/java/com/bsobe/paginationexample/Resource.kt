package com.bsobe.paginationexample

data class Resource<T> constructor(
    val status: Status,
    val data: T?,
    @get:Deprecated("use getResourceError()") val error: Throwable? = null
) {

    val isStatusSuccess: Boolean
        get() = status == Status.SUCCESS

    val isStatusLoading: Boolean
        get() = status == Status.LOADING

    val isStatusNotLoading: Boolean
        get() = status != Status.LOADING

    val isDataNull: Boolean
        get() = data == null

    val isStatusError: Boolean
        get() = status == Status.ERROR

    val isStatusNotError: Boolean
        get() = status != Status.ERROR

    companion object {

        @JvmStatic
        fun <T> error(throwable: Throwable): Resource<T> {
            return Resource(Status.ERROR, null, throwable)
        }

        @JvmStatic
        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        @JvmStatic
        fun <T> loading(data: T): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }

        @JvmStatic
        fun <T> loading(): Resource<T> {
            return Resource(Status.LOADING, null, null)
        }
    }
}