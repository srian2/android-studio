package com.example.week3.respository


import com.example.shoppy.model.UserModel
import com.google.firebase.auth.FirebaseUser

interface UserRepository {


    fun login(email: String, password: String, callback: (Boolean, String) -> Unit)
    fun register(email: String, password: String, callback: (Boolean, String, String) -> Unit)

    fun forgetPassword(email: String, callback: (Boolean, String) -> Unit)



    fun addUserToDatabase(
        userId: String, userModel: UserModel,
        callback: (Boolean, String) -> Unit
    )

    fun logout(callback: (Boolean, String) -> Unit)

    fun editprofile(userId: String, data: MutableMap<String,Any>,
                    callback: (Boolean, String) -> Unit)

    fun getCurrentUser(): FirebaseUser?

    fun getUserfromdatabase(userID: String,callback: (UserModel?,Boolean, String) -> Unit)

    fun updateProfile(userId: String, data: MutableMap<String, String?>, callback: (Boolean, String) -> Unit)

}
