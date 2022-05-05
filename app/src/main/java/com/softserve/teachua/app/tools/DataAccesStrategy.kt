package com.softserve.teachua.app.tools



suspend fun <T, V> performGetFromRemoteAndMapData(
    networkCall: suspend () -> Resource<T>,
    map: suspend (T) -> V
) : Resource<V>{
    val call = networkCall.invoke()
    if (call.status == Resource.Status.SUCCESS){
        val let = call.data?.let { map.invoke(it) }
        if (let != null){
            return Resource.success(let)
        }
    }
    return Resource.error()
}