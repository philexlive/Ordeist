package com.philexliveprojects.ordeist.ui.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.philexliveprojects.ordeist.R
import com.philexliveprojects.ordeist.ui.AppViewModelProvider

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun OrderScreen(
    onNavigateBack: () -> Unit = { },
    viewModel: OrderViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val order by viewModel.order.collectAsState()

    Column(
        modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TopAppBar(
            title = {
                Text(stringResource(R.string.app_name))
            },
            navigationIcon = {
                IconButton(onNavigateBack) {
                    Icon(
                        Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        )
        if(order != null) {
            InfoBox(order!!.category)

            InfoBox(order!!.clientName)

            if(!order!!.phoneNumber.isNullOrEmpty()) {
                InfoBox(order!!.phoneNumber!!)
            }

            if(!order!!.phoneNumber.isNullOrEmpty()) {
                InfoBox(order!!.email!!)
            }
        }

        Box(Modifier.weight(1f), contentAlignment = Alignment.BottomCenter) {
            Button(
                onClick = {
                    onNavigateBack()
                    viewModel.removeOrder()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(
                    text = stringResource(R.string.remove_order),
                    color = MaterialTheme.colorScheme.onError
                )
            }
        }
    }
}

@Composable
private fun InfoBox(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(R.dimen.corner_small)))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_large))
        )
    }
}
