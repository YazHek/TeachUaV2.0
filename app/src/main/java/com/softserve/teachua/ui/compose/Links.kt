package com.softserve.teachua.ui.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softserve.teachua.app.TeachUaLinksConstants
import com.softserve.teachua.R

@Preview
@Composable
fun ShowLinks(){
    val uriHandler = LocalUriHandler.current
    Surface(contentColor = MaterialTheme.colors.onBackground, color = MaterialTheme.colors.background) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Наші контакти")
            IconButton(onClick = {
                uriHandler.openUri(TeachUaLinksConstants.Youtube)
            }) {
                Icon(painter = painterResource(id = R.drawable.ic_baseline_youtube_24), contentDescription = "")
            }
            IconButton(onClick = {
                uriHandler.openUri(TeachUaLinksConstants.Facebook)
            }) {
                Icon(painter = painterResource(id = R.drawable.ic_baseline_facebook_24), contentDescription = "")
            }
            IconButton(onClick = {
                uriHandler.openUri(TeachUaLinksConstants.Instagram)
            }) {
                Icon(painter = painterResource(id = R.drawable.ic_baseline_instagram_50), contentDescription = "", modifier = Modifier.height(24.dp))
            }
            IconButton(onClick = {
                uriHandler.openUri(TeachUaLinksConstants.Mail)
            }) {
                Icon(imageVector = Icons.Outlined.MailOutline, contentDescription ="" )
            }
        }
    }

}