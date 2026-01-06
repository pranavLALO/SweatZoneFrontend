package com.example.sweatzone

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// Data model for a blog post
data class BlogPost(
    val title: String,
    val description: String,
    val imageRes: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitnessBlogScreen(navController: NavController) {
    // Sample Data based on your image
    val blogPosts = listOf(
        BlogPost(
            "Overcoming Workout Plateaus",
            "Discover strategies to break through training plateaus and continue making progress.",
            R.drawable.chestimg // Replace with your actual image resource
        ),
        BlogPost(
            "Dealing with Muscle Soreness",
            "Learn effective tips and remedies for managing post-workout muscle soreness.",
            R.drawable.chestimg
        ),
        BlogPost(
            "The Importance of Rest Days",
            "Understand why rest and recovery are crucial for muscle growth and overall fitness.",
            R.drawable.chestimg
        ),
        BlogPost(
            "Nutrition for Muscle Gain",
            "A guide to essential dietary principles for maximizing muscle hypertrophy.",
            R.drawable.chestimg
        ),
        BlogPost(
            "Preventing Common Workout Injuries",
            "Key practices and techniques to keep you safe and injury-free during training.",
            R.drawable.chestimg
        )
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Fitness Issues",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFF1F2937) // Dark Slate
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF1F2937)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 10.dp, bottom = 30.dp)
        ) {
            items(blogPosts) { post ->
                BlogListItem(post, onClick = {
                    if (post.title == "Overcoming Workout Plateaus") {
                        navController.navigate("overcoming_plateaus")
                    }
                    else if (post.title == "Dealing with Muscle Soreness") {
                        navController.navigate("muscle_soreness")
                    }
                    else if (post.title == "The Importance of Rest Days") {
                        navController.navigate("importance_of_rest")
                    }
                    // 👇 FIXED: Removed the extra space at the end
                    else if (post.title == "Nutrition for Muscle Gain") {
                        navController.navigate("nutrition_muscle_gain")
                    }
                    else if (post.title == "Preventing Common Workout Injuries") {
                        navController.navigate("preventing_injuries")
                    }
                })

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 16.dp),
                    thickness = 0.5.dp,
                    color = Color(0xFFE5E7EB)
                )
            }

        }
    }
}

@Composable
fun BlogListItem(post: BlogPost, onClick: () -> Unit) { // Added onClick parameter
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick) // Uses the passed click action

            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Text Content
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = post.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = post.description,
                fontSize = 13.sp,
                color = Color(0xFF6B7280), // Muted Gray
                lineHeight = 18.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Image Thumbnail
        Image(
            painter = painterResource(id = post.imageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(width = 80.dp, height = 60.dp)
                .clip(RoundedCornerShape(12.dp))
        )
    }
}
