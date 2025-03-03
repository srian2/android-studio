package com.example.shoppy.respository

import com.example.shoppy.model.UserModel
import com.example.week3.respository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserRepositoryImp : UserRepository {
    private  var auth : FirebaseAuth = FirebaseAuth.getInstance()

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val reference = database.reference.child("users")

    override fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, "Login Successful")
            } else {
                // If the login fails, get the error message
                val errorMessage = task.exception?.message ?: "An error occurred"
                callback(false, errorMessage)
            }
        }
    }






    override fun register(
        email: String,
        password: String,
        callback: (Boolean, String, String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
          if(it.isSuccessful){
                        callback(true, "Registration successful", auth.currentUser?.uid.toString())
                    } else {
                        callback(false, it.exception?.message.toString(),"")
                    }
                }
            }



    override fun forgetPassword(email: String, callback: (Boolean, String) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Password reset email sent successfully")
                } else {
                    // If there's an error, return the error message
                    val errorMessage = task.exception?.message ?: "Failed to send password reset email"
                    callback(false, errorMessage)
                }
            }
    }

    override fun addUserToDatabase(
        userId: String,
        userModel: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        var userId = auth.currentUser?.uid
        reference.child(userId.toString()).setValue(userModel)
            .addOnCompleteListener {
            if(it.isSuccessful){
                callback(true,"Registration Successfull")
            }else{
                callback(false,it.exception?.message.toString())
            }
        }
    }

    override fun logout(callback: (Boolean, String) -> Unit) {

        try{
            auth.signOut()
            callback(true,"Logout Success")

        }catch (e:Exception){
            callback(false,e.message.toString())
        }
    }


    override fun editprofile(
        userId: String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    ) {

        reference.child(userId).updateChildren(data).addOnCompleteListener{
            if(it.isSuccessful){
                callback(true,"Profile edited")
            }else{
                callback(false,it.exception?.message.toString())
            }
        }

}


    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override fun getUserfromdatabase(
        userID: String,
        callback: (UserModel?, Boolean, String) -> Unit
    ) {
        reference.child(userID).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    var usermodel = snapshot.getValue(UserModel::class.java)
                    callback(usermodel,true,"data Fetched successfully")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null,false,error.message)
            }
        })
    }


    override fun updateProfile(
        userId: String,
        data: MutableMap<String, String?>,
        callback: (Boolean, String) -> Unit
    ) {
        // Reference the user's node in the database
        reference.child(userId).updateChildren(data as Map<String, Any>).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, "Profile updated successfully")
            } else {
                // If there's an error, return the error message
                val errorMessage = task.exception?.message ?: "Error updating profile"
                callback(false, errorMessage)
            }
        }
    }

}