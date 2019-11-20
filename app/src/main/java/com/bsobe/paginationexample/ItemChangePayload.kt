package com.bsobe.paginationexample

class ItemChangePayload(val position: Int, val payloadObject: Any, val changeType: ChangeType) {

    inline fun <reified T> getPayload(): T? {
        return payloadObject as? T
    }

    enum class ChangeType {
        ADD,
        REMOVE,
        MODIFY
    }
}