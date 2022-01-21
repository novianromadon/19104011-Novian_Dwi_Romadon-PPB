package com.novian.sqlite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.novian.sqlite.databinding.ActivityMainBinding
import com.novian.sqlite.db.QuoteHelper
import com.novian.sqlite.helper.EXTRA_POSITION
import com.novian.sqlite.helper.EXTRA_QUOTE
import com.novian.sqlite.helper.REQUEST_ADD
import com.novian.sqlite.helper.REQUEST_UPDATE
import com.novian.sqlite.helper.RESULT_ADD
import com.novian.sqlite.helper.RESULT_DELETE
import com.novian.sqlite.helper.RESULT_UPDATE
import com.novian.sqlite.helper.mapCursorToArrayList
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var quoteHelper: QuoteHelper
    private lateinit var adapter: QuoteAdapter
    private val EXTRA_STATE = "EXTRA_STATE"
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Quotes"
        binding.rvQuotes.layoutManager = LinearLayoutManager(this)
        binding.rvQuotes.setHasFixedSize(true)
        adapter = QuoteAdapter(this)
        binding.rvQuotes.adapter = adapter

        binding.fabAdd.setOnClickListener { startActivity(Intent(this, QuoteAddUpdateActivity::class.java)) }

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this@MainActivity, QuoteAddUpdateActivity::class.java)
//            startActivityForResult(intent, REQUEST_ADD)
            startActivity(intent)
            Log.d("ActivityMain", "onCreate: Not Impl")
        }

        quoteHelper = QuoteHelper.getInstance(applicationContext)
        quoteHelper.open()
        if (savedInstanceState == null) {
            loadQuotes()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Quote>(EXTRA_STATE)
            if (list != null) {
                adapter.listQuotes = list
            }
        }
    }

    // Untuk membaca status hidupnya sebuah acvitivity
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listQuotes)
    }

    // Untuk melakukan request pemanggilan data
    private fun loadQuotes() {
        GlobalScope.launch(Dispatchers.Main) {
            progressbar.visibility = View.VISIBLE
            val cursor = quoteHelper.queryAll()
            var quotes = mapCursorToArrayList(cursor)
            progressbar.visibility = View.INVISIBLE
            if (quotes.size > 0) {
                adapter.listQuotes = quotes
            } else {
                adapter.listQuotes = ArrayList()
                showSnackbarMessage("Tidak ada data saat ini")
            }
        }
    }

    // Untuk menampilkan notifikasi
    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding.rvQuotes, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            when (requestCode) {
                REQUEST_ADD -> if (resultCode == RESULT_ADD) {
                    val quote = data.getParcelableExtra<Quote>(EXTRA_QUOTE) as Quote
                    adapter.addItem(quote)
                    binding.rvQuotes.smoothScrollToPosition(adapter.itemCount - 1)
                    showSnackbarMessage("Satu item berhasil ditambahkan")
                }
                REQUEST_UPDATE ->
                    when (resultCode) {
                        RESULT_UPDATE -> {
                            val quote = data.getParcelableExtra<Quote>(EXTRA_QUOTE) as Quote
                            val position = data.getIntExtra(EXTRA_POSITION, 0)
                            adapter.updateItem(position, quote)
                            binding.rvQuotes.smoothScrollToPosition(position)
                            showSnackbarMessage("Satu item berhasil diubah")
                        }
                        RESULT_DELETE -> {
                            val position = data.getIntExtra(EXTRA_POSITION, 0)
                            adapter.removeItem(position)
                            showSnackbarMessage("Satu item berhasil dihapus")
                        }
                    }
            }
        }
    }
}