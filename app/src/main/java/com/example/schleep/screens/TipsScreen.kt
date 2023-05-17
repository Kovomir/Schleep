package com.example.schleep.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.schleep.R
import com.example.schleep.components.Article
import com.example.schleep.components.ArticleViewModel
import kotlinx.coroutines.launch

val article1 = Article(
    title = "Ideální prostředí pro spánek",
    text = "Ideální prostředí pro spánek by mělo být temné, chladné a tiché. Dle expertů je nejoptimálnější teplota vzduchu v rozmezí od 15,5 do 19,5 °C. (Zwarensteyn, 2022) Svojí roli hraje také kvalita vzduchu, například vysoká hladina oxidu uhličitého nebo alergenů, může mít negativní účinky. Temnější prostředí pak podporuje tvorbu melatoninu, který lidem pomáhá usnout, zatímco vyvarování se všech rušivých zvukových vjemů předchází přerušovanému spánku. (Zwarensteyn, 2022)\n" +
            "Otevřením okna před spaním je v našich podnebních podmínkách možné efektivně regulovat teplotu a kvalitu vzduchu v ložnici. Pro blokování světelného znečistění se nabízí například použití zatemňovacích závěsů, žaluzií a v poslední řadě může pomoci maska na oči. Přílišný hluk lze pak řešit ku příkladu odhlučněním oken či užitím špuntů do uší. Odstraněním elektronických zařízení z místnosti může člověk snížit nechtěný zvukový i světelný ruch.\n",
    imageId = R.drawable.images
)

val article2 = Article(
    title = "Spánková rutina",
    text = "",
    imageId = R.drawable.images
)

val articles = listOf(article1, article2)

val articlesViewModel = ArticleViewModel(articles)

@Composable
fun TipsScreen() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Tipy",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
            ArticleListScreen(viewModel = articlesViewModel)
        }
    }
}

@Composable
fun ArticleListScreen(viewModel: ArticleViewModel) {
    val articles = viewModel.articles
    var selectedArticle = viewModel.selectedArticle
    var expanded by remember {
        mutableStateOf(false)
    }
    var scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(all = 5.dp)
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center

        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = MaterialTheme.shapes.large
                    )
                    .align(Alignment.Center)
                    .fillMaxWidth(0.95f)
            ) {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Výběr článku")
                }
                if (selectedArticle == null) {
                    Text(
                        text = "Vyberte článek}",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                } else {
                    Text(
                        text = "${selectedArticle.title}",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth(0.85f),
                    offset = DpOffset(0.dp, 2.dp)
                ) {
                    articles.forEachIndexed { index, article ->
                        DropdownMenuItem(
                            text = { Text(text = article.title) },
                            onClick = {
                                viewModel.selectArticle(article)
                                coroutineScope.launch {
                                    scrollState.animateScrollTo(0)
                                }
                                expanded = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        if (selectedArticle != null) {
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
                shape = MaterialTheme.shapes.large
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(PaddingValues(all = 10.dp))
                        .verticalScroll(scrollState),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        DisplayImageFromResources(resourceId = selectedArticle.imageId)
                        Text(
                            text = selectedArticle.title,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = selectedArticle.text,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Justify
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun DisplayImageFromResources(resourceId: Int) {
    val painter: Painter = painterResource(id = resourceId)
    Image(
        painter = painter, contentDescription = "Image from resources",
        contentScale = ContentScale.Crop,
        alignment = Alignment.Center,
        modifier = Modifier
            .padding(PaddingValues(all = 15.dp))
            .shadow(
                elevation = 10.dp, MaterialTheme.shapes.medium,
                clip = true,
                ambientColor = DefaultShadowColor,
                spotColor = DefaultShadowColor
            )
    )
}
