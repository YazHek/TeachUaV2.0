package com.softserve.teachua

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.softserve.teachua.app.baseImageUrl
import com.softserve.teachua.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*

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
        loadImagesToDrawer()



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

        val topLevelDestinations = appBarConfiguration.topLevelDestinations
        Log.e("tag", topLevelDestinations.toString())
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

    private fun loadImagesToDrawer() {
        val headerView = navView.getHeaderView(0)
        val navigationImageInDrawer = headerView.navigation_image_in_drawer
        val navigationImageInDrawerBackground = headerView.navigation_image_in_drawer_back
        Glide.with(this)
            .load(baseImageUrl + "static/media/header-bg.4a858b95.png")
            .into(navigationImageInDrawerBackground)
        Glide.with(this)
            .load(baseImageUrl + "static/media/logo.22da8232.png")
            .into(navigationImageInDrawer)
    }

    private fun setToolbar(toolbar: Toolbar){
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
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