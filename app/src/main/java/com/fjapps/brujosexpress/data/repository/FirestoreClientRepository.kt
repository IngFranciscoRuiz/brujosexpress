package com.fjapps.brujosexpress.data.repository

import com.fjapps.brujosexpress.data.models.Product
import com.fjapps.brujosexpress.data.models.Store
import com.fjapps.brujosexpress.ui.models.FeaturedUi
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

private const val COLLECTION_STORES = "stores"
private const val COLLECTION_PRODUCTS = "products"

class FirestoreClientRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun getOpenStores(): List<Store> {
        val snap = db.collection(COLLECTION_STORES).whereEqualTo("isOpen", true).get().await()
        return snap.documents.map { d ->
            Store(
                id = d.id,
                name = d.getString("name") ?: "",
                logoUrl = d.getString("logoUrl"),
                isOpen = d.getBoolean("isOpen") ?: false,
                deliveryFeeCents = (d.getLong("deliveryFeeCents") ?: 0L).toInt()
            )
        }
    }

    suspend fun getProductsForStore(storeId: String): List<Product> {
        val snap = db.collection(COLLECTION_STORES).document(storeId)
            .collection(COLLECTION_PRODUCTS).whereEqualTo("available", true).get().await()
        return snap.documents.map { d ->
            Product(
                id = d.id,
                storeId = storeId,
                name = d.getString("name") ?: "",
                price = ((d.getLong("priceCents") ?: 0L).toDouble() / 100.0),
                imageUrl = d.getString("imageUrl") ?: "",
                description = "",
                category = d.getString("category") ?: "Otros",
                stock = 0,
                type = com.fjapps.brujosexpress.data.models.ProductType.GROCERY
            )
        }
    }

    suspend fun getFeatured(limit: Int = 10): List<FeaturedUi> {
        // Robust fallback without collectionGroup: recorre tiendas abiertas y toma destacados
        val storesSnap = db.collection(COLLECTION_STORES).whereEqualTo("isOpen", true).get().await()
        val result = mutableListOf<FeaturedUi>()
        for (s in storesSnap.documents) {
            if (result.size >= limit) break
            val store = Store(
                id = s.id,
                name = s.getString("name") ?: "",
                logoUrl = s.getString("logoUrl"),
                isOpen = true,
                deliveryFeeCents = (s.getLong("deliveryFeeCents") ?: 0L).toInt()
            )
            val q = db.collection(COLLECTION_STORES).document(store.id)
                .collection(COLLECTION_PRODUCTS)
                .whereEqualTo("featured", true)
                .whereEqualTo("available", true)
                .limit((limit - result.size).toLong())
                .get().await()
            q.documents.forEach { d ->
                val name = d.getString("name") ?: ""
                val imageUrl = d.getString("imageUrl") ?: (store.logoUrl ?: "")
                val fee = store.deliveryFeeCents / 100
                result.add(
                    FeaturedUi(
                        id = d.id,
                        name = name,
                        imageUrl = imageUrl,
                        rating = 4.5,
                        etaMinutes = 25,
                        deliveryFee = fee,
                        discountPercent = null,
                        storeId = store.id
                    )
                )
            }
        }
        return result
    }

    suspend fun getProductsByCategory(category: String, limit: Int = 50): List<Product> {
        val snap = db.collectionGroup(COLLECTION_PRODUCTS)
            .whereEqualTo("category", category)
            .whereEqualTo("available", true)
            .limit(limit.toLong())
            .get()
            .await()
        return snap.documents.mapNotNull { d ->
            val storeId = d.reference.parent.parent?.id ?: return@mapNotNull null
            Product(
                id = d.id,
                storeId = storeId,
                name = d.getString("name") ?: "",
                price = ((d.getLong("priceCents") ?: 0L).toDouble() / 100.0),
                imageUrl = d.getString("imageUrl") ?: "",
                description = "",
                category = d.getString("category") ?: "",
                stock = 0,
                type = com.fjapps.brujosexpress.data.models.ProductType.GROCERY
            )
        }
    }
}


