package com.philexliveprojects.ordeist.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.philexliveprojects.ordeist.R
import com.philexliveprojects.ordeist.data.Order
import com.philexliveprojects.ordeist.ui.AppViewModelProvider

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onAddOrder: () -> Unit = {},
    onOrder: (Int) -> Unit = {},
    onProfile: () -> Unit = {}
) {
    val uiState by viewModel.orders.collectAsState()

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.app_name))
                },
                actions = {
                    IconButton(onProfile) {
                        Icon(Icons.Default.AccountCircle, contentDescription = null)
                    }
                }
            )
            LazyColumn {
                items(uiState.size) {
                    val order = uiState[it]
                    OrderItem(
                        order = order,
                        onClick = { onOrder(order.id) },
                        backgroundColor = if (it % 2 == 0) {
                            MaterialTheme.colorScheme.surfaceContainer
                        } else {
                            MaterialTheme.colorScheme.surface
                        }
                    )
                }
            }
        }

        FloatingActionButton(
            onAddOrder,
            Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.BottomEnd)
                .padding(dimensionResource(R.dimen.padding_large))
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
        }
    }
}

@Composable
fun OrderItem(
    order: Order,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface
) {

    Row(
        modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(onClick = onClick)
            .background(backgroundColor),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = order.category,
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_small))
                .weight(1f),
            style = MaterialTheme.typography.titleMedium
        )

        Column(Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = "№${order.id}",
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_small)),
                fontSize = 8.sp
            )

            Text(
                text = order.date,
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_small)),
                fontSize = 8.sp
            )
        }
    }
}
