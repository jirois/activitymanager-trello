package com.jinncyapps.activitymanager.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.jinncyapps.activitymanager.R
import com.jinncyapps.activitymanager.databinding.ActivityLoginBinding
import com.jinncyapps.activitymanager.firebase.FirestoreClass
import com.jinncyapps.activitymanager.model.User

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setupActionBar()

        binding.btnSignIn.setOnClickListener {
            signInUser()
        }

    }

    private fun signInUser() {

        val email: String = binding.etEmail.text.toString().trim { it <= ' ' }
        val password: String = binding.etPassword.text.toString().trim { it <= ' ' }


        if (validateForm(email, password)) {
            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->

                        // Hide the progress dialog
                        hideProgressDialog()

                        // If the registration is successfully done
                        if (task.isSuccessful) {

                            // Firebase registered user
                           FirestoreClass().signInUser(this@LoginActivity)

                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Email/password not correct",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })

        }
    }


        private fun setupActionBar() {

            setSupportActionBar(binding.toolbarLoginActivity)

            val actionBar = supportActionBar
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true)
                actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
            }

            binding.toolbarLoginActivity.setNavigationOnClickListener { onBackPressed() }
        }

    private fun validateForm( email: String, password: String): Boolean{
        return when {
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please enter an email")
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter a valid password")
                false
            }
            else -> {
                true
            }

        }
    }

    fun signInSuccess(user: User) {
        hideProgressDialog()
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }
}