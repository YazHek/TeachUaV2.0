package com.softserve.teachua.app.tools

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow


/*managing two states. For example when we need to load two piece of data
*and to show success only when everyone is ready and to show error when both
* are failed
* */
suspend fun <T, V> convertTwoStatesToOne(
    firstState: StateFlow<Resource<List<T>>>,
    secondState: StateFlow<Resource<List<V>>>
) = flow {
    firstState.collectLatest {
        secondState.collectLatest {
            if (firstState.value.status == Resource.Status.SUCCESS
                && secondState.value.status == Resource.Status.SUCCESS
            ) {
                emit(Resource.Status.SUCCESS)
            } else if (firstState.value.status == Resource.Status.FAILED
                || secondState.value.status == Resource.Status.FAILED
            ) {
                emit(Resource.Status.FAILED)
            } else {
                emit(Resource.Status.LOADING)
            }
        }
    }
}