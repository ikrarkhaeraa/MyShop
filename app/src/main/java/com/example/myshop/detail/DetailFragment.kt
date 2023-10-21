package com.example.myshop.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.compose.rememberImagePainter
import com.example.core.SealedClass
import com.example.core.room.FavoriteEntity
import com.example.myshop.R
import kotlinx.coroutines.launch

class DetailFragment : Fragment() {

    private val detailViewModel: DetailViewModel by activityViewModels()
    private val args: DetailFragmentArgs by navArgs()
    private var productId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                DetailProductScreenViewModel()
            }
        }
    }

    @Composable
    fun DetailProductScreenViewModel() {
        productId = args.productId
        LaunchedEffect(key1 = Unit) {
            detailViewModel.getDetailProductData(productId)
        }
        val detailProductData =
            detailViewModel.productDetailData.collectAsStateWithLifecycle().value
        when (detailProductData) {
            is SealedClass.Init -> {

            }

            is SealedClass.Loading -> {
                ProgressBarDemo()
            }

            is SealedClass.Success -> {
                val images = detailProductData.data.images
                val title = detailProductData.data.title
                val desc = detailProductData.data.description
                val price = detailProductData.data.price
                val discountPercentage = detailProductData.data.discountPercentage
                val rating = detailProductData.data.rating
                val stock = detailProductData.data.stock
                val brand = detailProductData.data.brand
                val category = detailProductData.data.category
                val thumbnnail = detailProductData.data.thumbnail
                DetailProductScreen(
                    images,
                    title,
                    desc,
                    price,
                    discountPercentage,
                    rating,
                    stock,
                    brand,
                    category,
                    thumbnnail
                )
            }

            is SealedClass.Error -> {

            }

            else -> {}
        }
    }

    @Composable
    fun ProgressBarDemo() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(40.dp),
                color = colorResource(id = R.color.black)
            )
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun DetailProductScreen(
        images: List<String>,
        title: String,
        desc: String,
        price: Int,
        discountPercentage: Float,
        rating: Float,
        stock: Int,
        brand: String,
        category: String,
        thumbnail: String
    ) {

        var favorite: FavoriteEntity? = null
        var isImageChanged by rememberSaveable { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(key1 = Unit) {
            favorite = detailViewModel.getFavoriteForDetail(productId)
            if (productId == favorite?.id) {
                isImageChanged = true
            }
        }

//        lifecycleScope.launch {
//            favorite = model.getWishlistforDetail(productId)
//            if (productId == favorite?.productId) {
//                isImageChanged = true
//            }
//            Log.d("cekFavorite", favorite?.productId.toString())
//            Log.d("cekImageResource", isImageChanged.toString())
//        }

        var imageResource: Painter = if (!isImageChanged) {
            painterResource(id = R.drawable.baseline_favorite_border_24)
        } else {
            painterResource(id = R.drawable.baseline_favorite_24)
        }

        Scaffold { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
            ) {
                val pagerState = rememberPagerState()

                images.size.let {
                    Box {
                        HorizontalPager(
                            modifier = Modifier.fillMaxSize(),
                            pageCount = it,
                            state = pagerState
                        ) { pageIndex ->
                            val imageUrl = images[pageIndex]
                            Image(
                                painter = rememberImagePainter(
                                    data = imageUrl,
                                    builder = {
                                        crossfade(true)
                                    }
                                ),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(309.dp)
                            )
                        }

                        Row(
                            Modifier
                                .height(16.dp)
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            repeat(it) { iteration ->
                                val color =
                                    if (pagerState.currentPage == iteration) colorResource(id = androidx.appcompat.R.color.primary_material_light) else Color.LightGray
                                Box(
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .size(8.dp)

                                )
                            }
                        }
                    }
                }
                Column(Modifier.padding(8.dp)) {
                    Row {
                        Text(
                            text = title,
                            fontSize = 32.sp,
                            modifier = Modifier
                                .weight(1f),
                        )
                        Image(
                            painter = imageResource,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .clickable {
                                    coroutineScope.launch {
                                        val productFavorite =
                                            detailViewModel.getFavoriteForDetail(productId)
                                        if (productFavorite.toString() == "null") {
                                            isImageChanged = true
                                            detailViewModel.addFavorite(
                                                productId,
                                                title,
                                                desc,
                                                price,
                                                discountPercentage,
                                                rating,
                                                stock,
                                                brand,
                                                category,
                                                thumbnail
                                            )
                                            Toast
                                                .makeText(
                                                    requireContext(),
                                                    "Added to wishlist",
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        } else if (productFavorite?.id == productId) {
                                            detailViewModel.deleteFavorite(productId)
                                            isImageChanged = false
                                            Toast
                                                .makeText(
                                                    requireContext(),
                                                    "Remove from wishlist",
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        }
                                    }
                                }
                        )
                    }
                    Text(
                        text = desc,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Row(Modifier.padding(top = 4.dp)) {
                        Text(
                            text = "Price : ",
                            fontSize = 18.sp,
                            modifier = Modifier.padding(end = 2.dp)
                        )
                        Text(
                            text = price.toString(),
                            fontSize = 18.sp,
                        )
                    }
                    Row(Modifier.padding(top = 4.dp)) {
                        Text(
                            text = "Discount Percentage : ",
                            fontSize = 18.sp,
                            modifier = Modifier.padding(end = 2.dp)
                        )
                        Text(
                            text = discountPercentage.toString(),
                            fontSize = 18.sp,
                        )
                    }
                    Row(Modifier.padding(top = 4.dp)) {
                        Text(
                            text = "Rating : ",
                            fontSize = 18.sp,
                            modifier = Modifier.padding(end = 2.dp)
                        )
                        Text(
                            text = rating.toString(),
                            fontSize = 18.sp,
                        )
                    }
                    Row(Modifier.padding(top = 4.dp)) {
                        Text(
                            text = "Stock : ",
                            fontSize = 18.sp,
                            modifier = Modifier.padding(end = 2.dp)
                        )
                        Text(
                            text = stock.toString(),
                            fontSize = 18.sp,
                        )
                    }
                    Row(Modifier.padding(top = 4.dp)) {
                        Text(
                            text = "Brand : ",
                            fontSize = 18.sp,
                            modifier = Modifier.padding(end = 2.dp)
                        )
                        Text(
                            text = brand,
                            fontSize = 18.sp,
                        )
                    }
                    Row (Modifier.padding(top = 4.dp)) {
                        Text(
                            text = "Category : ",
                            fontSize = 18.sp,
                            modifier = Modifier.padding(end = 2.dp)
                        )
                        Text(
                            text = category,
                            fontSize = 18.sp,
                        )
                    }
                }
            }
        }
    }

    @Preview
    @Composable
    fun DetailProductPreview() {
        val sampleImages = listOf(
            "https://i.dummyjson.com/data/products/1/1.jpg",
            "https://i.dummyjson.com/data/products/1/2.jpg",
            "https://i.dummyjson.com/data/products/1/2.jpg"
        )
        val title = "Iphone"
        val desc = "This is description"
        val price = 30
        val discountPercentage = 2.1f
        val rating = 5.0f
        val stock = 90
        val brand = "Apple"
        val category = "Phone"
        val thumbnail = ""
        DetailProductScreen(
            sampleImages,
            title,
            desc,
            price,
            discountPercentage,
            rating,
            stock,
            brand,
            category,
            thumbnail
        )
    }
}