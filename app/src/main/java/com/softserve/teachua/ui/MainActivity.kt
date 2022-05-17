package com.softserve.teachua.ui

import android.os.Bundle
import android.view.Menu
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.softserve.teachua.R
import com.softserve.teachua.app.baseImageUrl
import com.softserve.teachua.app.tools.Resource.Status.SUCCESS
import com.softserve.teachua.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.logged_in_user_nav_section.view.*
import kotlinx.android.synthetic.main.login_nav_section.view.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.android.synthetic.main.logged_in_user_nav_section.view.userPhoto as userPhoto1


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainActivityViewModel: MainActivityViewModel by viewModels()
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var logOutBtn: TextView
    private lateinit var enterToAccountBtn: TextView
    private lateinit var userLogo: ImageView

    private var roles = listOf("ВІДВІДУВАЧ", "АДМІНІСТРАТОР", "КЕРІВНИК")

    private lateinit var accountContainer: ViewFlipper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        initViews()
        initNavController()
        initDrawer()

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }


    private fun initViews() {
        toolbar = binding.appBarMain.tool.toolbar
        drawerLayout = binding.drawerLayout
        logOutBtn = binding.navView.getHeaderView(0).account_exit_btn
        enterToAccountBtn = binding.navView.getHeaderView(0).account_login_btn
        userLogo = binding.navView.getHeaderView(0).userPhoto
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_clubs, R.id.nav_challenges, R.id.nav_about_us),
            drawerLayout)
        navView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_content_main)
        accountContainer = navView.getHeaderView(0).account_container
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }


    override fun onSupportNavigateUp(): Boolean {
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

    private fun initDrawer() {
        mainActivityViewModel.loadUser()
        loadImagesToDrawer()
        //mainActivityViewModel.loadData()
//        mainActivityViewModel.loadUser(mainActivityViewModel.userToken.value.data?.token!!,
//            mainActivityViewModel.userToken.value.data?.id!!)

        lifecycleScope.launch {
            mainActivityViewModel.user.collectLatest { user ->
                when (user.status) {
                    SUCCESS -> {

                        // mainActivityViewModel.loadUser(userToken.data?.token!!, userToken.data.id)

                        logOutBtn.setOnClickListener {
                            mainActivityViewModel.logOut()

                        }
                        val userRole = binding.navView.getHeaderView(0).userRole
                        when (user.data?.roleName) {

                            "ROLE_USER" -> userRole.text = roles[0]
                            "ROLE_ADMIN" -> userRole.text = roles[1]
                            "ROLE_MANAGER" -> userRole.text = roles[2]
                        }
                        val userName = binding.navView.getHeaderView(0).userName
                        (user.data?.firstName + " " + user.data?.lastName).also {
                            userName.text = it
                        }
                        Glide.with(this@MainActivity)
                            .load(baseImageUrl + user.data?.urlLogo)
                            .optionalCircleCrop()
                            .into(userLogo)

                        showLoggedInView()
                    }

                    else -> {

                        enterToAccountBtn.setOnClickListener {
                            navController.navigate(R.id.nav_login)
                            drawerLayout.closeDrawer(navView)
                        }
                        showLoginView()
                    }
                }
            }


        }
    }


    private fun showLoginView() {
        accountContainer.displayedChild = 1
    }

    private fun showLoggedInView() {
        accountContainer.displayedChild = 0
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

    private fun setToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    fun showToolbar() {
        toolbar.visibility = VISIBLE
    }

    fun hideToolbar() {
        toolbar.visibility = GONE
    }


    fun openDrawer() {
        drawerLayout.openDrawer(navView)
    }

    fun changeLoginNavSection() {
        mainActivityViewModel.loadUser()
    }
}