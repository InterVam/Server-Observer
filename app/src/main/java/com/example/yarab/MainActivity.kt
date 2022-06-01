package com.example.yarab

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.github.anastr.speedviewlib.SpeedView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlin.math.min


class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val uptime = findViewById<TextView>(R.id.uptime)
        val downtimee = findViewById<TextView>(R.id.downtime2)
        val cconnected = findViewById<TextView>(R.id.cconnected)
        val servercheck = findViewById<TextView>(R.id.Servercheck)
        val packetloss = findViewById<TextView>(R.id.packetloss)
        val packetsent = findViewById<TextView>(R.id.packetsent)
        val preformancebutt = findViewById<Button>(R.id.preformancebutt)
        val Downbutt = findViewById<Button>(R.id.fakedown)
        val packetrcvd = findViewById<TextView>(R.id.packetrcvd)
        val speedometer = findViewById<SpeedView>(R.id.speedView)
        val speednum = findViewById<TextView>(R.id.speednum)
        val downtime = findViewById<Chronometer>(R.id.simpleChronometer)
        speedometer.withTremble = false

        val ref = FirebaseDatabase.getInstance().getReference("Server").child("-N-ti1d2Yx-PX2JTPcX-")
        ref.get().addOnCompleteListener { task->if(task.isSuccessful){
            val snapshot = task.result
            val value = snapshot.child("avgTime").getValue(String::class.java)
            speednum.text= "$value MS"
            if (value != null) {
                speedometer.speedTo(value.toFloat(),1000)
            }
            }


        }
        val passtxtListener = object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post = dataSnapshot.getValue<String>()

                    speednum.text="$post MS"

                    if (post != null) {
                        speedometer.speedTo(post.toFloat(),1000)
                    }


            }

            override fun onCancelled(error: DatabaseError) {
            }
        }


        ref.child("avgTime").addValueEventListener(passtxtListener)

        var id = 0
        createNotificationChannel()
        val downtimeListener = object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post = dataSnapshot.getValue<Int>()
                if (post == 1){
                    id += 1
                    servercheck.text = "Server is DOWN"
                    servercheck.setTextColor(Color.rgb(255,0,0))
                    downtime.base = SystemClock.elapsedRealtime()
                    downtime.start()
                    NotificationManagerCompat.from(applicationContext).notify(id,builder.build())
                }
                else if(post == 0){
                    id += 1
                    servercheck.text = "Server is UP"
                    servercheck.setTextColor(Color.rgb(0,255,0))
                    downtime.stop()
                    downtime.base = SystemClock.elapsedRealtime()
                    NotificationManagerCompat.from(applicationContext).notify(id,builder2.build())
                }


            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        val uptimeListener = object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post = dataSnapshot.getValue<Int>()

                val hours = post?.div(3600);
                val minutes = (post?.rem(3600))?.div(60);
                val seconds = post?.rem(60);
                uptime.text = hours.toString() +":" + minutes.toString() +":"+ seconds.toString()
                }




            override fun onCancelled(error: DatabaseError) {
            }
        }
        val packetlossListener = object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post = dataSnapshot.getValue<Int>()

                packetloss.text = post.toString() + " %"
            }




            override fun onCancelled(error: DatabaseError) {
            }
        }
        val packetsentListener = object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post = dataSnapshot.getValue<Int>()

                packetsent.text = post.toString()
            }




            override fun onCancelled(error: DatabaseError) {
            }
        }
        val packetrcvdListener = object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post = dataSnapshot.getValue<Int>()

                packetrcvd.text = post.toString()
            }




            override fun onCancelled(error: DatabaseError) {
            }
        }
        val downtime2Listener = object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post = dataSnapshot.getValue<String>()

                downtimee.text = post
            }




            override fun onCancelled(error: DatabaseError) {
            }
        }
        val cconnectedListener = object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post = dataSnapshot.getValue<String>()

                cconnected.text = post
            }




            override fun onCancelled(error: DatabaseError) {
            }
        }
        ref.child("dflag").addValueEventListener(downtimeListener)
        ref.child("uptime").addValueEventListener(uptimeListener)
        ref.child("loss").addValueEventListener(packetlossListener)
        ref.child("sent").addValueEventListener(packetsentListener)
        ref.child("recv").addValueEventListener(packetrcvdListener)
        ref.child("downtime").addValueEventListener(downtime2Listener)
        ref.child("clients").addValueEventListener(cconnectedListener)
        Downbutt.setOnClickListener {
            ref.child("simPkt").setValue(1)
            Toast.makeText(this,"Simulating Packet Loss",Toast.LENGTH_LONG).show()
        }
        preformancebutt.setOnClickListener {
            startActivity(Intent(this,Preformsnvr::class.java))
        }

    }
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Serverdown"
            val descriptionText = "detects if server is down"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("e", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    var builder = NotificationCompat.Builder(this, "e")
        .setSmallIcon(R.drawable.noti)
        .setContentTitle("Beware!!")
        .setContentText("The Server is DOWN")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
    var builder2 = NotificationCompat.Builder(this, "e")
        .setSmallIcon(R.drawable.noti)
        .setContentTitle("Beware!!")
        .setContentText("The Server is UP")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
}