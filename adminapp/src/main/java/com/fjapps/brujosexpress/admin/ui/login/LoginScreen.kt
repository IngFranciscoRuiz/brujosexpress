package com.fjapps.brujosexpress.admin.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.fjapps.brujosexpress.admin.data.di.rememberLoginViewModel
import com.fjapps.brujosexpress.admin.data.di.StoreManager
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    val vm = rememberLoginViewModel()
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val error = vm.error.collectAsState().value
    val loading = vm.loading.collectAsState().value

    Scaffold(topBar = { TopAppBar(title = { Text("Iniciar sesión") }) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            error?.let { Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp)) }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    // Firebase Auth login (reemplaza dummy)
                    if (email.isBlank() || password.isBlank()) return@Button
                    vm.loading.value = true
                    vm.error.value = null
                    Firebase.auth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            val user = Firebase.auth.currentUser
                            if (user != null) {
                                user.getIdToken(true)
                                    .addOnSuccessListener { tokenResult ->
                                        val claims = tokenResult.claims
                                        val storeIdClaim = claims["storeId"] as? String
                                        // Guarda el storeId del dueño para que los repos apunten a su tienda
                                        if (!storeIdClaim.isNullOrBlank()) {
                                            StoreManager.save(context, storeIdClaim)
                                        }
                                        vm.loading.value = false
                                        onLoginSuccess()
                                    }
                                    .addOnFailureListener { e ->
                                        vm.loading.value = false
                                        vm.error.value = e.message
                                    }
                            } else {
                                vm.loading.value = false
                                onLoginSuccess()
                            }
                        }
                        .addOnFailureListener { e ->
                            vm.loading.value = false
                            vm.error.value = e.message
                        }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (loading) {
                    Text("Entrando...", color = Color.White)
                } else {
                    Text("Entrar", color = Color.White)
                }
            }
        }
    }
}


