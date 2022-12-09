package compose.notezz.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import compose.notezz.R

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AboutScreen (){

    Scaffold(topBar = {
        TopAppBar(
            modifier = Modifier.fillMaxWidth(), backgroundColor = Color.DarkGray
        ) {

            Icon(
                modifier = Modifier.padding(start = 10.dp),
                tint = Color.Cyan,
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "logo"
            )
            //  Icon(imageVector = Icons.Default.MoreVert, contentDescription = "more")
        }
    }) {}

    var focus = LocalFocusManager.current
    Column(
        modifier = Modifier
            .clickable(
                MutableInteractionSource(),
                indication = null,
                onClick = { focus.clearFocus() })
            .fillMaxWidth()
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
        verticalArrangement = Arrangement.Center
    ) {



    }
}
