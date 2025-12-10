package com.example.tap2tell.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: user)

    @Query("SELECT * FROM user WHERE role = 'user'")
    suspend fun getAllUsers(): List<user>


    @Query("SELECT * FROM user WHERE email = :email LIMIT 1")
    suspend fun findByEmail(email: String): user?

    @Query("SELECT * FROM user WHERE role = 'pending_provider'")
    suspend fun getPendingProviders(): List<user>

    @Query("UPDATE user SET role = :newRole WHERE firebaseuid = :uid")
    suspend fun updateRole(uid: String, newRole: String)

    @Query("SELECT * FROM user WHERE firebaseuid = :uid LIMIT 1")
    suspend fun findByUid(uid: String): user?


    @Delete
    suspend fun deleteUser(user: user)
}
