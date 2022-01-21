package com.novian_19104011.broadcastreceiver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class SmsReceiverActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sms_receiver)

        val tv_from : TextView = findViewById(R.id.tv_from)
        val tv_message : TextView = findViewById(R.id.tv_message)
        val btn_close : Button = findViewById(R.id.btn_close)

        title = "Incoming Message"
        val senderNo = intent.getStringExtra(EXTRA_SMS_NO)
        val senderMessage = intent.getStringExtra(EXTRA_SMS_MESSAGE)
        tv_from.text = "Coming From"+": "+senderNo
        tv_message.text = senderMessage
        btn_close.setOnClickListener { finish() }
    }

    companion object {
        const val EXTRA_SMS_NO = "extra_sms_no"
        const val EXTRA_SMS_MESSAGE = "extra_sms_message" }
}