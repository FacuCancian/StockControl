package com.example.stockcontrol.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.stockcontrol.model.Items
import com.example.stockcontrol.viewModel.ProductViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAddClick: () -> Unit,
    onItemClick: (String) -> Unit,
    onExit: () -> Unit,
    productViewModel: ProductViewModel

) {

    //var searchQuery by remember { mutableStateOf("") }
    val products by productViewModel.allProducts.observeAsState(emptyList())

    LaunchedEffect(Unit) {//should be up
        productViewModel.fetchProducts()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = onExit) {
                    Text("Salir", color = MaterialTheme.colorScheme.onPrimary)
                }
                Button(onClick = onAddClick) {
                    Text("AGREGAR", color = MaterialTheme.colorScheme.onPrimary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                Text(
                    "ID",
                    modifier = Modifier.weight(1f),
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "DESCRIPCIÓN",
                    modifier = Modifier.weight(2f),
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "PRECIO",
                    modifier = Modifier.weight(1f),
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "STOCK",
                    modifier = Modifier.weight(1f),
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            LazyColumn {
                items(products) { item ->
                    Card(modifier = Modifier.padding(6.dp).fillMaxWidth()) {
                        ItemRow(item, onItemClick = { selectedItem ->
                            onItemClick(selectedItem.id.toString())
                        })
                    }

                }
            }
        }
    }
}

@Composable
fun ItemRow(item: Items, onItemClick: (Items) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onItemClick(item) }
    ) {
        Text(
            item.id.toString(),
            modifier = Modifier.weight(1f),
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            item.description,
            modifier = Modifier.weight(2f),
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            item.price.toString(),
            modifier = Modifier.weight(1f),
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            item.stock.toString(),
            modifier = Modifier.weight(1f),
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
