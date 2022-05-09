package com.softserve.teachua.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.R
/*
Compose UI function that based on Resource STATUS show either data/loading/error
 */
@Composable
fun <T>  ResourceWrapper(
    resource: Resource<T>?,
    loadingContent: @Composable () -> Unit = { DefaultLoadingContent() },
    onReloadButtonClick : () -> Unit,
    errorContent: @Composable (msg : String?) -> Unit = { errorMessage ->
        DefaultErrorContent(errorMessage) { onReloadButtonClick() }
    },
    content : @Composable (T) -> Unit
)
{
    when (resource?.status){
        Resource.Status.LOADING -> {
            loadingContent()
        }
        Resource.Status.SUCCESS -> {
            content(resource.data!!)
        } else -> {
            errorContent(msg = resource?.message)
        }
    }
}

@Composable
fun DefaultLoadingContent(){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        CircularProgressIndicator(color = MaterialTheme.colors.onBackground)
    }
}


@Composable
fun DefaultErrorContent(
    errorMessage: String?,
    function: () -> Unit
){
    Surface(color = MaterialTheme.colors.background, contentColor = MaterialTheme.colors.onBackground, modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(R.string.connection_problem.toString())
                IconButton(onClick = {function()}) {
                    Icon(imageVector = Icons.Filled.Refresh, contentDescription = "")
                }
            }
        }
    }
}