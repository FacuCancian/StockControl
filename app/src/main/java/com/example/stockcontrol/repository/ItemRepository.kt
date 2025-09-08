package com.example.stockcontrol.repository

import android.content.ClipData.Item
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.stockcontrol.interfaces.ProductDao
import com.example.stockcontrol.model.Items
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class ItemRepository(private val productDao: ProductDao, private val firestore: FirebaseFirestore) {

    private val productCollection = firestore.collection("products")


    fun getProducts(): Flow<List<Items>> = productDao.getAllProducts()

    suspend fun insertProduct(product: Items){
        try{
            productCollection.document(product.id.toString()).set(product).await()//firebase
            productDao.insert(product)//room
        }catch(e : Exception){
            productDao.insert(product)
        }
    }
    suspend fun updateProduct(product: Items){
        try{
            productCollection.document(product.id.toString()).set(product).await()
            productDao.update(product)
        }catch(e : Exception){
            productDao.update(product)
        }
    }
    suspend fun deleteProduct(productId: Int) {
        try {
            productCollection.document(productId.toString()).delete().await()
            productDao.deleteById(productId)
        } catch (e: Exception) {
            productDao.deleteById(productId)
        }
    }
    fun getAllProducts(): Flow<List<Items>> {
        return productDao.getAllProducts()
    }
    suspend fun fetchProducts() {
        try {
            val snapshot = firestore.collection("products").get().await()
            val productList = snapshot.documents.mapNotNull { doc ->
                val idNumber = doc.getLong("id") ?: return@mapNotNull null
                val id = idNumber.toInt()
                val product = doc.toObject(Items::class.java)?.copy(id = id)
                product
            }

            if (productList.isNotEmpty()) {
                productDao.insertAll(productList)
                Log.d("room", "Productos insertados en Room")
            }
        } catch (e: Exception) {
            Log.e("ProductRepository", "Error al obtener productos de Firebase, usando Room", e)
        }
    }


}