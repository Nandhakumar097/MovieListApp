package com.example.movielist.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.movielist.data.Constant
import com.example.movielist.data.ProductionCompany
import com.example.movielist.ui.theme.Blue
import com.example.movielist.ui.theme.MovieListTheme
import com.example.movielist.ui.theme.Yellow
import com.example.movielist.viewmodel.MovieViewModel

@Composable
fun MovieInfoScreen(
        moviesViewModel: MovieViewModel
) {

    MovieListTheme {
        val movieInfo = moviesViewModel.infoState.value
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = Constant().BACKDROP_URL + movieInfo.backdrop_path,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = movieInfo.title,
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                )
                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        modifier = Modifier,
                        text = movieInfo.release_date.take(4),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        modifier = Modifier,
                        text = ".",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        modifier = Modifier,
                        text = "${movieInfo.runtime} min",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))



                Row(
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp)
                        .height(250.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {

                    Card(
                        modifier = Modifier,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        ),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        AsyncImage(
                            model = Constant().BACKDROP_URL + movieInfo.poster_path,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxHeight()
                                .shadow(10.dp)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 10.dp)
                            .weight(1f)
                            .fillMaxHeight()
                    ) {

                        Spacer(modifier = Modifier.height(10.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            items(movieInfo.genres.size) { position ->
                                Card(
                                    modifier = Modifier,
                                    shape = MaterialTheme.shapes.medium,
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.background,
                                    ),
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                                ) {
                                    Text(
                                        modifier = Modifier.padding(
                                            start = 10.dp,
                                            end = 10.dp,
                                            top = 5.dp,
                                            bottom = 5.dp
                                        ),
                                        text = movieInfo.genres[position].name,
                                        style = MaterialTheme.typography.bodyMedium,
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = movieInfo.original_title,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = movieInfo.overview,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }

                }
                Spacer(modifier = Modifier.height(15.dp))

                Row(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Star, contentDescription = null,
                        tint = Yellow
                    )
                    Text(
                        modifier = Modifier.padding(start = 3.dp),
                        text = movieInfo.vote_average.toString().take(3),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        modifier = Modifier,
                        text = "/10",
                        style = MaterialTheme.typography.labelSmall,
                    )
                    Text(
                        modifier = Modifier.padding(start = 5.dp),
                        text = "${movieInfo.vote_count}(votes)",
                        style = MaterialTheme.typography.labelSmall,
                    )
                    Icon(
                        Icons.Default.StarBorder, contentDescription = null,
                        tint = Blue,
                        modifier = Modifier.padding(start = 15.dp)
                    )
                    Text(
                        modifier = Modifier.padding(start = 3.dp),
                        text = "Rate",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)
                        .align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(movieInfo.spoken_languages.size) { position ->
                        Card(
                            modifier = Modifier,
                            shape = MaterialTheme.shapes.medium,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.background,
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                        ) {
                            Text(
                                text = movieInfo.spoken_languages[position].name,
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp),
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)
                        .align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(movieInfo.production_companies.size) { position ->
                        ProductionCompany((movieInfo.production_companies[position]))
                    }
                }
            }
        }
    }

}

@Composable
fun ProductionCompany(productionCompany: ProductionCompany) {
    if (productionCompany.logo_path != null && productionCompany.logo_path.endsWith(".png")) {
        Column(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            AsyncImage(
                model = Constant().LOGO_URL + productionCompany.logo_path,
                contentDescription = null,
                modifier = Modifier
                    .height(50.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = productionCompany.name,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(15.dp))

        }
    }

}