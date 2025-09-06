package com.fjapps.brujosexpress.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjapps.brujosexpress.data.models.CartItem
import com.fjapps.brujosexpress.data.models.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {
    
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()
    
    private val _total = MutableStateFlow(0.0)
    val total: StateFlow<Double> = _total.asStateFlow()
    
    fun addToCart(product: Product) {
        viewModelScope.launch {
            val currentItems = _cartItems.value.toMutableList()
            val existingItem = currentItems.find { it.product.id == product.id }
            
            if (existingItem != null) {
                val index = currentItems.indexOf(existingItem)
                currentItems[index] = existingItem.copy(quantity = existingItem.quantity + 1)
            } else {
                currentItems.add(CartItem(product, 1))
            }
            
            _cartItems.value = currentItems
            calculateTotal()
        }
    }
    
    fun removeFromCart(productId: String) {
        viewModelScope.launch {
            val currentItems = _cartItems.value.toMutableList()
            currentItems.removeAll { it.product.id == productId }
            _cartItems.value = currentItems
            calculateTotal()
        }
    }
    
    fun updateQuantity(productId: String, newQuantity: Int) {
        viewModelScope.launch {
            if (newQuantity <= 0) {
                removeFromCart(productId)
                return@launch
            }
            
            val currentItems = _cartItems.value.toMutableList()
            val index = currentItems.indexOfFirst { it.product.id == productId }
            
            if (index != -1) {
                currentItems[index] = currentItems[index].copy(quantity = newQuantity)
                _cartItems.value = currentItems
                calculateTotal()
            }
        }
    }
    
    fun clearCart() {
        viewModelScope.launch {
            _cartItems.value = emptyList()
            _total.value = 0.0
        }
    }
    
    private fun calculateTotal() {
        val total = _cartItems.value.sumOf { it.product.price * it.quantity }
        _total.value = total
    }
    
    fun getCartItemCount(): Int {
        return _cartItems.value.sumOf { it.quantity }
    }
}

