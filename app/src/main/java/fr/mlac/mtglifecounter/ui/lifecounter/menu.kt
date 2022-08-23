package fr.mlac.mtglifecounter.ui.lifecounter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Replay
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun MenuRow(
    modifier: Modifier = Modifier,
    menuIsVisible: Boolean = false,
    onSettingsIconClicked: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.primaryVariant)
            .drawBehind {
                val strokeWidth = 8f
                val x = size.width
                val y = size.height

                //top line
                drawLine(
                    color = Color.DarkGray,
                    start = Offset(0f, 0f), //(0,0) at top-left point of the box
                    end = Offset(x, 0f), //top-right point of the box
                    strokeWidth = strokeWidth
                )

                //bottom line
                drawLine(
                    color = Color.DarkGray,
                    start = Offset(0f, y),// bottom-left point of the box
                    end = Offset(x, y),// bottom-right point of the box
                    strokeWidth = strokeWidth
                )
            }
    ) {
        AnimatedVisibility(
            visible = menuIsVisible,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp, bottom = 18.dp)
            ) {
                Icon(Icons.Outlined.Replay, contentDescription = "reset", modifier = Modifier.size(36.dp).scale(scaleX = -1f, scaleY = 1f), tint = Color.White)
                Icon(Icons.Outlined.Casino, contentDescription = "dice", modifier = Modifier.size(36.dp), tint = Color.White)
                Icon(Icons.Outlined.FavoriteBorder, contentDescription = "startingLife", modifier = Modifier.size(36.dp), tint = Color.White)
                Icon(Icons.Outlined.Settings, contentDescription = "settings", modifier = Modifier.size(36.dp), tint = Color.White)
            }
        }
    }
}