package com.example.yarab

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.anastr.speedviewlib.SpeedView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class Preformsnvr : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preformsnvr)
        val ref = FirebaseDatabase.getInstance().getReference("Server").child("-N-ti1d2Yx-PX2JTPcX-")
        val CPU = findViewById<SpeedView>(R.id.CPU)
        val RAM = findViewById<SpeedView>(R.id.RAM)
        val TEMP = findViewById<SpeedView>(R.id.TEMP)
        CPU.withTremble = false
        RAM.withTremble = false
        TEMP.withTremble = false
        val CPUListener = object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post = dataSnapshot.getValue<String>()
                if (post != null) {
                    CPU.speedTo(post.toFloat(),1000)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        }
        val RAMListener = object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post = dataSnapshot.getValue<String>()
                if (post != null) {
                    RAM.speedTo(post.toFloat(),1000)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        }
        val TEMPListener = object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post = dataSnapshot.getValue<String>()
                if (post != null) {
                    TEMP.speedTo(post.toFloat(),1000)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        }


    ref.child("cpu").addValueEventListener(CPUListener)
    ref.child("mem").addValueEventListener(RAMListener)
    ref.child("temp").addValueEventListener(TEMPListener)

    }
}