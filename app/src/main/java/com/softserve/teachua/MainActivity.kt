package com.softserve.teachua

import android.os.Bundle
import android.view.Menu
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.softserve.teachua.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration : AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        initViews()
        initNavController()





        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    private fun initViews(){
        toolbar = binding.appBarMain.tool.toolbar
        drawerLayout = binding.drawerLayout
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_clubs, R.id.nav_challenges, R.id.nav_about_us),
            drawerLayout)
        navView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_content_main)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun initNavController() {
        setToolbar(toolbar)
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        //findViewById<Toolbar>(R.id.toolbar).setupWithNavController(navController, appBarConfiguration)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    fun setToolbar(toolbar: Toolbar){
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    fun resetDefaultToolbar(){
        setSupportActionBar(toolbar)
        showToolbar()
    }

    fun showToolbar(){
        toolbar.visibility = VISIBLE
    }

    fun hideToolbar(){
        toolbar.visibility = GONE
    }


    fun openDrawer() {
        drawerLayout.openDrawer(navView)
    }

    override fun onPause() {
        super.onPause()
        println("paused")
    }

    override fun onResume() {
        super.onResume()
        println("resssumed")
    }



}