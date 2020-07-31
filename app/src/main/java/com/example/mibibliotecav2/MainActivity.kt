package com.example.mibibliotecav2

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mibibliotecav2.login.LoginActivity
import com.example.mibibliotecav2.model.remote.UsersRemote
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {
    private var urlfoto = ""

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        //Vesion completa
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.bibliotecaPersonalFragment,
                R.id.prestamosFragment,
                R.id.devolucionesFragment,
                R.id.recursosBibliograficosFragment,
                R.id.notificacionesBibliograficasFragment,
                R.id.configuracionGlobalFragment,
                R.id.califiquenosFragment,
                R.id.nuevolibroFragment,
                R.id.agregarprestamoFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val user: FirebaseUser? = mAuth.currentUser


        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("tablausuarios").child(user?.uid!!)
        val headerView = navView.getHeaderView(0)
        val navEmail = headerView.findViewById<TextView>(R.id.TV_email)
        val navPhoto = headerView.findViewById<ImageView>(R.id.IV_headerdrawer)

        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                var usuario = snapshot.getValue(UsersRemote::class.java)
                urlfoto = usuario?.urlPhoto!!
                if (urlfoto == "ok") {
                    Picasso.get().load(user.photoUrl).into(navPhoto)
                } else if (urlfoto != "") {
                    Picasso.get().load(urlfoto).into(navPhoto)
                }


            }
        }
        myRef.addValueEventListener(postListener)
        navEmail.text = user.email

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.MO_cerrar_sesion) {
            val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
            mAuth.signOut()

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


}