package com.fjapps.brujosexpress.data.repository

import com.fjapps.brujosexpress.data.models.Order
import kotlinx.coroutines.delay

class PaymentRepository {
    
    /**
     * Procesa el pago del pedido
     * En una implementación futura, aquí se conectaría con Stripe o Mercado Pago
     */
    suspend fun processPayment(order: Order): PaymentResult {
        // Simular procesamiento de pago
        delay(2000)
        
        return PaymentResult.Success(
            transactionId = "TXN_${System.currentTimeMillis()}",
            amount = order.total,
            paymentMethod = "Tarjeta de Crédito"
        )
    }
    
    /**
     * Obtiene métodos de pago disponibles
     */
    fun getAvailablePaymentMethods(): List<PaymentMethod> {
        return listOf(
            PaymentMethod.CREDIT_CARD,
            PaymentMethod.DEBIT_CARD,
            PaymentMethod.CASH,
            PaymentMethod.DIGITAL_WALLET
        )
    }
    
    /**
     * Valida la información de la tarjeta
     */
    fun validateCardInfo(cardNumber: String, expiryDate: String, cvv: String): Boolean {
        return cardNumber.length == 16 && 
               expiryDate.matches(Regex("\\d{2}/\\d{2}")) && 
               cvv.length == 3
    }
}

sealed class PaymentResult {
    data class Success(
        val transactionId: String,
        val amount: Double,
        val paymentMethod: String
    ) : PaymentResult()
    
    data class Error(
        val message: String,
        val errorCode: String? = null
    ) : PaymentResult()
}

enum class PaymentMethod(val displayName: String, val icon: String) {
    CREDIT_CARD("Tarjeta de Crédito", "💳"),
    DEBIT_CARD("Tarjeta de Débito", "💳"),
    CASH("Efectivo", "💵"),
    DIGITAL_WALLET("Billetera Digital", "📱")
}

