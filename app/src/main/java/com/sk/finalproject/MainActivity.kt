package com.sk.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.billingclient.api.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.sk.finalproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var btnBuy: Button
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        //Storing the button Id
        btnBuy = findViewById(R.id.btn_buynow)

        val skuList = ArrayList<String>()
        skuList.add("android.test.purchased")

        val purchasesUpdatedListener = PurchasesUpdatedListener{
            billingResult, purchaseList ->
        }

        //Initializing the Billing client
        var billingClient = BillingClient.newBuilder(this)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases().build()
        //Defining the on click method for the buy now button.
        btnBuy.setOnClickListener{
            //Inserting to the database
            val firstName = findViewById<EditText>(R.id.firstName).text.toString()
            val lastName = findViewById<EditText>(R.id.lastName).text.toString()
            val age = findViewById<EditText>(R.id.age).text.toString()
            val contactDetails = findViewById<EditText>(R.id.contactDetails).text.toString()
            val uName = findViewById<EditText>(R.id.userName).text.toString()

            database = FirebaseDatabase.getInstance().getReference("Users")
            val User = UserData(firstName, lastName, age, contactDetails)
            database.child(uName).setValue(User)

            findViewById<EditText>(R.id.firstName).text.clear()
            findViewById<EditText>(R.id.lastName).text.clear()
            findViewById<EditText>(R.id.lastName).text.clear()
            findViewById<EditText>(R.id.lastName).text.clear()
            findViewById<EditText>(R.id.lastName).text.clear()

            billingClient.startConnection(object: BillingClientStateListener {
                override fun onBillingServiceDisconnected() {
                    TODO("Not yet implemented")
                }

                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        val params = SkuDetailsParams.newBuilder()
                        params.setSkusList(skuList)
                            .setType(BillingClient.SkuType.INAPP)

                        billingClient.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->

                            for (skuDetail in skuDetailsList!!) {
                                val flowPurchase = BillingFlowParams.newBuilder()
                                    .setSkuDetails(skuDetail)
                                    .build()

                                val responseCode =
                                    billingClient.launchBillingFlow(this@MainActivity,
                                        flowPurchase).responseCode
                            }
                        }
                    }
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.blueToothAv) {
            val intent = Intent(
                this, BluetoothActivity::class.java)
            startActivity (intent)
        }
        return true

    }
}