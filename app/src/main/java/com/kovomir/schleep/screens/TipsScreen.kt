package com.kovomir.schleep.screens

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
import com.kovomir.schleep.R
import com.kovomir.schleep.utils.Article
import com.kovomir.schleep.utils.ArticleViewModel
import kotlinx.coroutines.launch

val article1 = Article(
    title = "Ideální prostředí pro spánek",
    text = "Světlo, teplota a hluk jsou zásadní faktory prostředí, které mají kritický vliv na váš noční odpočinek. Ideální prostředí pro spánek by mělo být temné, chladné a tiché. Dle expertů je nejoptimálnější teplota vzduchu v rozmezí od 15,5 do 19,5 °C. " +
            "Svojí roli hraje také kvalita vzduchu, například vysoká hladina oxidu uhličitého nebo alergenů, může mít negativní účinky. Temnější prostředí pak podporuje tvorbu melatoninu, který lidem pomáhá usnout, zatímco vyvarování se všech rušivých zvukových vjemů předchází přerušovanému spánku.\n\n" +
            "Otevřením okna před spaním je v našich podnebních podmínkách možné efektivně regulovat teplotu a kvalitu vzduchu v ložnici. Pro blokování světelného znečistění se nabízí například použití zatemňovacích závěsů, žaluzií a v poslední řadě může pomoci maska na oči. Přílišný hluk lze pak řešit" +
            " ku příkladu odhlučněním oken či užitím špuntů do uší. Odstraněním elektronických zařízení z místnosti může člověk snížit nechtěný zvukový i světelný ruch.",
    imageId = R.drawable.bedroom
)

val article2 = Article(
    title = "Pravidelná spánková rutina",
    text = "Pravidelná spánková rutina je klíčem k dosažení zdravého a obnovujícího spánku. Udržování konzistentních časů spaní a budění pomáhá stabilizovat biologické hodiny těla, což vede k optimálním spánkovým cyklům a stabilním hormonálním hladinám. To zajišťuje, že se probudíme v" +
            " ideální fázi spánku, cítíme se svěží a máme lepší kognitivní funkce. Tím nejenže podporujeme kvalitní spánek, ale také usnadňujeme udržování dostatečné délky spánku." +
            " Pravidelná spánková rutina by měla být prioritou pro zachování zdraví a pohody.",
    imageId = R.drawable.alarm_clock
)

val article3 = Article(
    title = "Fyzická aktivita a jídelníček",
    text = "Pravidelná fyzická aktivita s sebou přináší mnoho pozitivních účinků pro celý organismus. Spánek nevyjímaje. Je však třeba mít se na pozoru, jelikož kritické je její načasování. Člověk by se neměl vystavovat náročné fyzické zátěži několik hodin před ulehnutím.\n\n" +
            "Svou roli hraje i správná strava. Těžká jídla pozdě večer mohou způsobit nepokojný spánek. Místo toho je vhodné večer upřednostnit menší porce zdravých potravin s vysokým obsahem bílkovin a minimem cukru." +
            " Alkohol a látky se stimulačními účinky, jako jsou nikotin či kofein, dokážou velmi výrazně zhoršit kvalitu spánku. Kofein prodlužuje dobu, ve které je jedinec schopen usnout, zatímco alkohol je spojován s neklidným spaním." +
            " Pro optimální kvalitu spánku bychom se měli snažit těmto substancím úplně vyvarovat, zejména v pozdějších částech dne. V podobě kávy či čaje by kofein měl být konzumován pouze v ranních hodinách.",
    imageId = R.drawable.coffee
)

val article4 = Article(
    title = "Optimální délka spánku",
    text = "Optimální délka spánku se mění v závislosti na věku jedince. Světová zdravotnická organizace (WHO) doporučuje dospělým jedincům spát 7 až 8 hodin denně. Pravidelný spánek kratší než 6 hodin, nebo delší než 10 hodin, je považován za nezdravý.\n" +
            "\n" +
            "U novorozenců (0-3 měsíce) je doporučeno spát 14-17 hodin denně, což je nezbytné pro jejich růst a vývoj. U kojenců (4-11 měsíců) by měl spánek trvat 12-16 hodin denně, což zahrnuje krátké spánkové cykly během dne.\n" +
            "\n" +
            "Děti (1-2 roky) by měly spát 11-14 hodin denně, zatímco batolata (3-4 roky) by měla spát 10-13 hodin. Pro školní věk (5-12 let) se doporučuje 9-11 hodin spánku denně, což pomáhá vývoji a učení. U dospívajících (13-18 let) je optimální délka spánku 8-10 hodin, což podporuje fyzický a duševní rozvoj v tomto důležitém období. \n" +
            "\n" +
            "Důležité je si uvědomit, že potřeba spánu je individuální. V průběhu života se navíc mohou objevovat různé faktory, které mohou ovlivnit kvalitu a délku spánku. To může zahrnovat změny ve spánkových vzorcích, zvýšenou potřebu nočních návštěv koupelny či spánkové apnoe. Je proto důležité naslouchat svému tělu a reagovat na jeho signály. Pokud osoba trpí problémy se spánkem, měla by se poradit se svým lékařem.",
    imageId = R.drawable.clock_drawing
)

val article5 = Article(
    title = "Spánkový dluh",
    text = "Pokaždé když spíte kratší dobu, než by ste měli, narůstá váš spánkový dluh. Zvětšuje se kumulativně, což znamená, že se každý nedostatek přičte na váš celkový dluh. Pokud tedy týden budete spát pouze 6 místo 7 hodin, tak se váš celkový dluh zvětší o 7 hodin. Chronický spánkový dluh, který se hromadí po měsíce a roky, může mít závažné důsledky. Mezi ně se řadí zvýšené riziko vzniku zdravotních problémů, včetně diabetu.\n" +
            "\n" +
            "Ačkoli se můžete pokusit dohnat ztracený spánek, nejde o úplnou náhradu. Podle výzkumu jsou třeba čtyři dny na plné zotavení z jedné hodiny ztraceného spánku. Spánek navíc může odstranit únavu a snížit hladinu stresových hormonů v těle, ale výzkumy naznačují, že chronický spánkový dluh způsobuje problémy, které lze jen těžko zvrátit. Prevence spánkového dluhu je klíčová pro udržení zdravého spánku a dobrého zdraví.",
    imageId = R.drawable.sleep_debt
)

val articles = listOf(article1, article2, article3, article4, article5)

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
    val selectedArticle = viewModel.selectedArticle
    var expanded by remember {
        mutableStateOf(false)
    }
    val scrollState = rememberScrollState()
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
                        text = "Vyberte článek",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                } else {
                    Text(
                        text = selectedArticle.title,
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
                            color = MaterialTheme.colorScheme.primary,
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
        contentScale = ContentScale.Fit,
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
