package com.example.myshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.example.myshop.databinding.ActivityMainBinding
import com.example.myshop.detail.DetailFragmentArgs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val navHostFragment: NavHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.nhf_main) as NavHostFragment
    }
    private val navController by lazy {
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }

    fun goToDetail(productId: Int) {
        navController.navigate(
            R.id.store_to_detailCompose,
            DetailFragmentArgs(productId).toBundle(),
            navOptions = null
        )
    }

}