package com.softserve.teachua.app.tools

suspend fun <T> performGetFromRemote(
    networkCall: suspend () -> Resource<T>
) : Resource<T> {
    return networkCall.invoke()
}