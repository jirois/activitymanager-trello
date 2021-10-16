package com.jinncyapps.activitymanager.firebase

import android.provider.SyncStateContract
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.jinncyapps.activitymanager.activities.LoginActivity
import com.jinncyapps.activitymanager.activities.SignUp
import com.jinncyapps.activitymanager.model.User
import com.jinncyapps.activitymanager.utils.*


class FirestoreClass {

    private val mFirestore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUp,  userInfo: User){
        mFirestore.collection(Constants.USERS)
            .document(getCurentUserID())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisterSuccess()
            }

            .addOnFailureListener{e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting loggedIn user details",
                    e
                )

            }
    }
    fun signInUser(activity: LoginActivity){
        mFirestore.collection(Constants.USERS)
            .document(getCurentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.e(
                    activity.javaClass.simpleName, document.toString()
                )
                val loggedUser = document.toObject(User::class.java)
                if (loggedUser != null)
                    activity.signInSuccess(loggedUser)

            }
            .addOnFailureListener{e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting loggedIn user details",
                    e
                )

            }
    }

    fun getCurentUserID():String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentID: String = ""
        if (currentUser != null) {
            currentID = currentUser.uid
        }
        return currentID
    }
}