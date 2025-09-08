package com.example.stockcontrol.viewModel

import android.content.Intent
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockcontrol.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Estado que observará la UI
    private val _currentUser = mutableStateOf<FirebaseUser?>(auth.currentUser)
    val currentUser: State<FirebaseUser?> = _currentUser

    init {
        // Listener para cambios de autenticación
        auth.addAuthStateListener { firebaseAuth ->
            _currentUser.value = firebaseAuth.currentUser
        }
    }

    fun signUp(email: String, password: String, siguiente: () -> Unit, onError: (String) ->Unit){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                siguiente()
            } else {
                onError(task.exception?.message ?: "ERROR DESCONOCIDO")
            }
        }
    }

    fun logIn(email: String, password: String, siguiente: () -> Unit, onError: (String) ->Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                siguiente()
            } else {
                onError(task.exception?.message ?: "ERROR DESCONOCIDO")
            }
        }
    }

    fun signInWithGoogleCredential(credential: AuthCredential, home:() -> Unit){
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                home()
            }
        }
    }

    fun logOut() {
        auth.signOut()
    }
}
