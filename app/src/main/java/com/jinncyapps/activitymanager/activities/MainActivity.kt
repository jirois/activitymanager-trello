package com.jinncyapps.activitymanager.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.jinncyapps.activitymanager.R
import com.jinncyapps.activitymanager.databinding.ActivityMainBinding
import com.jinncyapps.activitymanager.firebase.FirestoreClass
import com.jinncyapps.activitymanager.model.User

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        setupActionBar()

        FirestoreClass().loadUserData(this@MainActivity)

        binding.navView.setNavigationItemSelectedListener(this)

    }
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            // A double back press function is added in Base Activity.
            doubleBackToExit()
        }
    }

    private fun setupActionBar() {
        val toolbarApp = findViewById<Toolbar>(R.id.toolbar_main_activity)
        setSupportActionBar(toolbarApp)
        toolbarApp.setNavigationIcon(R.drawable.ic_menu)

        toolbarApp.setNavigationOnClickListener {
            toggleDrawer()
        }

    }

    private fun toggleDrawer() {
        val drawLayout:DrawerLayout = binding.drawerLayout
        if (drawLayout.isDrawerOpen(GravityCompat.START)){
            drawLayout.closeDrawer(GravityCompat.START)
        }else {
            drawLayout.openDrawer(GravityCompat.START)
        }
    }

    fun updateNavigationUserDetails(user: User){
        val headerView = binding.navView.getHeaderView(0)

        val navUserName = headerView.findViewById<TextView>(R.id.tv_username)

        navUserName.text = user.name

        val navUserImage = headerView.findViewById<ImageView>(R.id.iv_user_image)

        Glide
            .with(this@MainActivity)
            .load(user.image)
            .circleCrop()
            .placeholder(R.drawable.ic_image_holder)
            .into(navUserImage)

        Log.i("USER_IMG", user.image)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.nav_my_profile -> {
                startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
            }
            R.id.nav_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true

    }
}