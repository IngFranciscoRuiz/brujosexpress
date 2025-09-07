package com.fjapps.brujosexpress.admin.data.repository

import com.fjapps.brujosexpress.admin.ui.model.AdminOrder
import com.fjapps.brujosexpress.admin.ui.model.AdminProduct
import com.fjapps.brujosexpress.admin.ui.model.AdminStore
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID

private const val COLLECTION_STORES = "stores"
private const val COLLECTION_PRODUCTS = "products"
private const val COLLECTION_ORDERS = "orders"

class FirestoreProductsRepository(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val storeId: String
) : ProductsRepository {
    override val products: Flow<List<AdminProduct>> = callbackFlow {
        val ref = firestore.collection(COLLECTION_STORES).document(storeId).collection(COLLECTION_PRODUCTS)
        val listener = ref.addSnapshotListener { snap, err ->
            if (err != null) {
                trySend(emptyList())
                return@addSnapshotListener
            }
            val items = snap?.documents?.mapNotNull { doc ->
                try {
                    val data = doc.data ?: return@mapNotNull null
                    AdminProduct(
                        id = doc.id,
                        name = data["name"] as? String ?: "",
                        priceCents = (data["priceCents"] as? Number)?.toInt() ?: 0,
                        imageUrl = data["imageUrl"] as? String,
                        category = data["category"] as? String ?: "",
                        available = data["available"] as? Boolean ?: true,
                        featured = data["featured"] as? Boolean ?: false
                    )
                } catch (_: Exception) { null }
            } ?: emptyList()
            trySend(items)
        }
        awaitClose { listener.remove() }
    }

    override suspend fun upsert(product: AdminProduct) {
        val id = if (product.id.isBlank()) UUID.randomUUID().toString() else product.id
        val ref = firestore.collection(COLLECTION_STORES).document(storeId).collection(COLLECTION_PRODUCTS).document(id)
        val data = mapOf(
            "name" to product.name,
            "priceCents" to product.priceCents,
            "imageUrl" to product.imageUrl,
            "category" to product.category,
            "available" to product.available,
            "featured" to product.featured
        )
        ref.set(data).await()
    }

    override suspend fun duplicate(productId: String) {
        val ref = firestore.collection(COLLECTION_STORES).document(storeId).collection(COLLECTION_PRODUCTS).document(productId)
        val snap = ref.get().await()
        if (snap.exists()) {
            val data = snap.data ?: return
            val name = (data["name"] as? String ?: "") + " (Copia)"
            val newId = UUID.randomUUID().toString()
            firestore.collection(COLLECTION_STORES).document(storeId)
                .collection(COLLECTION_PRODUCTS).document(newId)
                .set(data.toMutableMap().apply { this["name"] = name }).await()
        }
    }

    override suspend fun delete(productId: String) {
        firestore.collection(COLLECTION_STORES).document(storeId)
            .collection(COLLECTION_PRODUCTS).document(productId).delete().await()
    }
}

class FirestoreStoreRepository(
    private val firestore: FirebaseFirestore,
    private val storeId: String
) : StoreRepository {
    override val store: Flow<AdminStore> = callbackFlow {
        val ref = firestore.collection(COLLECTION_STORES).document(storeId)
        val listener = ref.addSnapshotListener { snap, _ ->
            val data = snap?.data
            val store = if (data != null) AdminStore(
                id = snap.id,
                name = data["name"] as? String ?: "",
                logoUrl = data["logoUrl"] as? String,
                phone = data["phone"] as? String,
                deliveryFeeCents = (data["deliveryFeeCents"] as? Number)?.toInt() ?: 0,
                isOpen = data["isOpen"] as? Boolean ?: false
            ) else AdminStore(storeId, "", null, null, 0, false)
            trySend(store)
        }
        awaitClose { listener.remove() }
    }

    override suspend fun updateStore(store: AdminStore) {
        val ref = firestore.collection(COLLECTION_STORES).document(storeId)
        val data = mapOf(
            "name" to store.name,
            "logoUrl" to store.logoUrl,
            "phone" to store.phone,
            "deliveryFeeCents" to store.deliveryFeeCents,
            "isOpen" to store.isOpen
        )
        ref.set(data).await()
    }
}

class FirestoreOrdersRepository(
    private val firestore: FirebaseFirestore,
    private val storeId: String
) : OrdersRepository {
    override val orders: Flow<List<AdminOrder>> = callbackFlow {
        val ref = firestore.collection(COLLECTION_ORDERS).whereEqualTo("storeId", storeId)
        val listener = ref.addSnapshotListener { snap, _ ->
            val items = snap?.documents?.mapNotNull { doc ->
                val d = doc.data ?: return@mapNotNull null
                try {
                    AdminOrder(
                        id = doc.id,
                        customer = d["customer"] as? String ?: "",
                        totalCents = (d["totalCents"] as? Number)?.toInt() ?: 0,
                        status = d["status"] as? String ?: "Recibido",
                        timestamp = (d["timestamp"] as? Number)?.toLong() ?: System.currentTimeMillis()
                    )
                } catch (_: Exception) { null }
            } ?: emptyList()
            trySend(items)
        }
        awaitClose { listener.remove() }
    }

    override suspend fun updateStatus(orderId: String, status: String) {
        firestore.collection(COLLECTION_ORDERS).document(orderId)
            .update("status", status).await()
    }
}


