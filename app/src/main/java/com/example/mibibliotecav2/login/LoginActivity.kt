package com.example.mibibliotecav2.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mibibliotecav2.MainActivity
import com.example.mibibliotecav2.R
import com.example.mibibliotecav2.model.remote.UsersRemote
import com.example.mibibliotecav2.registro.RegisterActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import java.util.regex.Pattern


class LoginActivity : AppCompatActivity() {

    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 1234


    override fun onStart() {
        super.onStart()
        val user = mAuth.currentUser
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        configureGoogle()

        BT_google_gmail.setOnClickListener {
            signIn()
        }
        BT_ingresar_registro.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))

        }
        BT_ingresar_login.setOnClickListener {

            val correologin = ET_correo_login.text.toString()
            val contrasenalogin = ET_contrasena_login.text.toString()

            if (correologin.isEmpty() || contrasenalogin.isEmpty()) {
                Toast.makeText(this, "Por favor, llene los campos", Toast.LENGTH_SHORT).show()
            } else if (!isValidEmailId(correologin)) {
                Toast.makeText(this, "El correo ingresado no es válido", Toast.LENGTH_SHORT)
                    .show()
            } else {
                mAuth.signInWithEmailAndPassword(correologin, contrasenalogin)
                    .addOnCompleteListener(
                        this
                    ) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()

                        } else {
                            // If sign in fails, display a message to the user.
                            var errorfirebase: String = task.exception!!.message.toString()
                            when (errorfirebase) {
                                "There is no user record corresponding to this identifier. The user may have been deleted." ->
                                    Toast.makeText(this, "El usuario no existe", Toast.LENGTH_SHORT)
                                        .show()
                                "The password is invalid or the user does not have a password." ->
                                    Toast.makeText(
                                        this,
                                        "La clave es incorrecta",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                else -> Toast.makeText(this, errorfirebase, Toast.LENGTH_SHORT)
                                    .show()

                            }

                        }
                    }

            }


        }
    }

    private fun configureGoogle() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("Google SignIN", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("Google SignIN", "Google sign in failed", e)
                // ...
            }
        }

    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
                    val myRef: DatabaseReference = database.getReference("tablausuarios")
                    val user = mAuth.currentUser
                    val usuario = UsersRemote(
                        user?.uid,
                        user?.displayName!!,
                        user.email!!,
                        "",
                        "",
                        true,
                        "ok"
                    )
                    myRef.child(user.uid).setValue(usuario)





                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Autenticación Fallida", Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun isValidEmailId(email: String): Boolean {
        return Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }
}

