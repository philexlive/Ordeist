package com.philexliveprojects.ordeist.ui.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
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
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onAddOrder: () -> Unit = {},
    onOrder: (Int) -> Unit = {},
    onProfile: () -> Unit = {},
    onAddCategory: () -> Unit = {}
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
        FloatingActionButtonMenu(
            modifier = Modifier.align(Alignment.BottomEnd),
            onAddOrder = onAddOrder,
            onAddCategory = onAddCategory
        )
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

@Composable
@Preview
fun FloatingActionButtonMenu(
    modifier: Modifier = Modifier,
    onAddOrder: () -> Unit = {},
    onAddCategory: () -> Unit = {}
) {
    var isFabExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.padding(dimensionResource(R.dimen.fab_menu_padding)),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.fab_menu_spacing))
    ) {
        AnimatedVisibility(
            visible = isFabExpanded,
            enter = scaleIn() + expandIn(expandFrom = Alignment.BottomEnd),
            exit = scaleOut() + shrinkHorizontally(shrinkTowards = Alignment.End)
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.fab_menu_item_spacing))
            ) {
                ExtendedFloatingActionButton(
                    onClick = {
                        isFabExpanded = !isFabExpanded
                        onAddOrder()
                    },
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.loweredElevation(),
                    containerColor = MaterialTheme.colorScheme.secondary
                ) {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = null
                    )

                    Text(stringResource(R.string.new_order))
                }

                ExtendedFloatingActionButton(
                    onClick = {
                        isFabExpanded = !isFabExpanded
                        onAddCategory()
                    },
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.loweredElevation(),
                    containerColor = MaterialTheme.colorScheme.secondary
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null
                    )

                    Text(stringResource(R.string.new_category))
                }
            }
        }

        AnimatedContent(
            targetState = isFabExpanded,
            transitionSpec = {
                scaleIn(transformOrigin = TransformOrigin(pivotFractionX = 0.8f, pivotFractionY = 0.2f)) togetherWith scaleOut()
            }
        ) {
            if (it) {
                SmallFloatingActionButton(
                    onClick = { isFabExpanded = false },
                    modifier = Modifier.padding(start = 24.dp, bottom = 24.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null
                    )
                }
            } else {
                FloatingActionButton(
                    onClick = { isFabExpanded = true },
                    shape = if (isFabExpanded) {
                        FloatingActionButtonDefaults.largeShape
                    } else {
                        FloatingActionButtonDefaults.smallShape
                    },
                    containerColor = if (isFabExpanded) {
                        MaterialTheme.colorScheme.tertiary
                    } else {
                        MaterialTheme.colorScheme.secondary
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    ) /*TODO Change content description*/
                }
            }
        }
    }
}

@Composable
@Preview
fun FabMenuPreview() {
    Box(Modifier.fillMaxSize()) {
        FloatingActionButtonMenu(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(dimensionResource(R.dimen.padding_large))
        )
    }
}