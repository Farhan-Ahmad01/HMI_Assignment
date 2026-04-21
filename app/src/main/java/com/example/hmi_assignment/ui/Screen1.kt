package com.example.hmi_assignment.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


private val Purple      = Color(0xFFB8A9D9)
private val PurpleLight = Color(0xFFD8CEF0)
private val Pink        = Color(0xFFE8A0A0)
private val PinkLight   = Color(0xFFF5C5C5)
private val Green       = Color(0xFF8DBFAD)
private val BgGray      = Color(0xFFF5F5F7)
private val CardWhite   = Color.White
private val TextDark    = Color(0xFF1A1A2E)
private val TextGray    = Color(0xFF9090A0)


@Composable
fun InsightsScreen() {
    Scaffold(
        bottomBar = { BottomNavBar() },
        containerColor = BgGray
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item { TopBar() }
            item { Spacer(Modifier.height(8.dp)) }
            item { SectionTitle("Stability Summary") }
            item { StabilitySummaryCard() }
            item { Spacer(Modifier.height(20.dp)) }
            item { SectionTitle("Cycle Trends") }
            item { CycleTrendsCard() }
            item { Spacer(Modifier.height(20.dp)) }
            item { SectionTitle("Body & Metabolic Trends") }
            item { WeightChartCard() }
            item { Spacer(Modifier.height(20.dp)) }
            item { SectionTitle("Body Signals") }
            item { SymptomTrendsCard() }
            item { Spacer(Modifier.height(20.dp)) }
            item { SectionTitle("Lifestyle Impact") }
            item { LifestyleImpactCard() }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

// Top App Bar
@Composable
private fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Two overlapping circles (logo)
        Box(Modifier.size(32.dp)) {
            Box(
                Modifier
                    .size(18.dp)
                    .offset(x = 0.dp, y = 4.dp)
                    .clip(CircleShape)
                    .background(Purple)
            )
            Box(
                Modifier
                    .size(18.dp)
                    .offset(x = 10.dp, y = 4.dp)
                    .clip(CircleShape)
                    .background(PurpleLight)
            )
        }
        Text(
            text = "Insights",
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = TextDark
        )
        Spacer(Modifier.size(32.dp)) // Balance the logo
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp,
        color = TextDark
    )
}

//  Stability Summary Card
@Composable
private fun StabilitySummaryCard() {
    InsightCard {
        Text(
            "Based on your recent logs and symptom patterns.",
            color = TextGray,
            fontSize = 13.sp
        )
        Spacer(Modifier.height(12.dp))
        Text("Stability Score", fontSize = 13.sp, color = TextGray)
        Text("78%", fontWeight = FontWeight.Bold, fontSize = 28.sp, color = TextDark)
        Spacer(Modifier.height(12.dp))

        // Stability chart (improving funnel shape drawn with Canvas)
        Box(
            Modifier
                .fillMaxWidth()
                .height(110.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // Shaded funnel area
                val path = Path().apply {
                    moveTo(0f, h * 0.15f)
                    lineTo(w, h * 0.55f)
                    lineTo(w, h * 0.85f)
                    lineTo(0f, h * 0.65f)
                    close()
                }
                drawPath(path, Brush.horizontalGradient(listOf(PurpleLight, Purple.copy(alpha = 0.4f))))

                // Center trend line
                drawLine(
                    color = Purple,
                    start = Offset(0f, h * 0.4f),
                    end   = Offset(w, h * 0.7f),
                    strokeWidth = 2.dp.toPx()
                )

                // Dot at Mar position (~60% across)
                val dotX = w * 0.62f
                val dotY = h * 0.57f
                drawCircle(color = Green, radius = 6.dp.toPx(), center = Offset(dotX, dotY))
                drawCircle(
                    color = Color.White,
                    radius = 3.dp.toPx(),
                    center = Offset(dotX, dotY)
                )
                // Dashed vertical line at dot
                var y = 0f
                while (y < dotY - 6.dp.toPx()) {
                    drawLine(TextGray, Offset(dotX, y), Offset(dotX, y + 4.dp.toPx()), 1.dp.toPx())
                    y += 8.dp.toPx()
                }
            }

            // "Stability Improving" tooltip
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(x = 30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(TextDark)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text("Stability\nImproving", color = Color.White, fontSize = 11.sp, textAlign = TextAlign.Center)
            }
        }

        // X-axis labels
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            listOf("Jan", "Feb", "Mar", "Apr").forEach {
                Text(it, fontSize = 11.sp, color = TextGray)
            }
        }
    }
}

@Composable
private fun CycleTrendsCard() {
    InsightCard {
        val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun")
        val days   = listOf(28, 30, 28, 32, 28, 28)

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            months.forEachIndexed { i, month ->
                CycleBar(month = month, totalDays = days[i])
            }
        }
    }
}

@Composable
private fun CycleBar(month: String, totalDays: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "$totalDays",
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextDark
        )
        Spacer(Modifier.height(4.dp))
        // Bar made of stacked colored segments
        Box(
            modifier = Modifier
                .width(30.dp)
                .height(90.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            Column(Modifier.fillMaxSize()) {
                Box(Modifier.weight(0.25f).fillMaxWidth().background(Purple))
                Box(Modifier.weight(0.45f).fillMaxWidth().background(PurpleLight))
                Box(Modifier.weight(0.15f).fillMaxWidth().background(Pink))
                Box(Modifier.weight(0.15f).fillMaxWidth().background(PinkLight))
            }
            // Cycle icon overlay
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(26.dp)
                    .offset(y = 18.dp)
                    .background(Green.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                Text("↻", color = Color.White, fontSize = 12.sp)
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(month, fontSize = 10.sp, color = TextGray)
    }
}

@Composable
private fun WeightChartCard() {
    var selectedTab by remember { mutableStateOf(0) }

    InsightCard {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text("Your weight", fontWeight = FontWeight.SemiBold, color = TextDark, fontSize = 14.sp)
                Text("in kg", color = TextGray, fontSize = 12.sp)
            }
            // Monthly / Weekly toggle
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFEEEEEE))
                    .padding(3.dp)
            ) {
                listOf("Monthly", "Weekly").forEachIndexed { idx, label ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(17.dp))
                            .background(if (selectedTab == idx) TextDark else Color.Transparent)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                            .then(
                                Modifier.noRippleClickable { selectedTab = idx }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            label,
                            fontSize = 12.sp,
                            color = if (selectedTab == idx) Color.White else TextGray
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Weight line chart
        val weightPoints = listOf(35f, 38f, 45f, 55f, 68f, 72f, 65f, 58f, 52f)
        Box(Modifier.fillMaxWidth().height(130.dp)) {
            Canvas(Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                val minW = weightPoints.min()
                val maxW = weightPoints.max()
                val xStep = w / (weightPoints.size - 1)

                fun xOf(i: Int) = i * xStep
                fun yOf(v: Float) = h - ((v - minW) / (maxW - minW)) * (h * 0.85f) - h * 0.05f

                // Filled area below line
                val fillPath = Path().apply {
                    moveTo(xOf(0), yOf(weightPoints[0]))
                    weightPoints.forEachIndexed { i, v ->
                        if (i > 0) lineTo(xOf(i), yOf(v))
                    }
                    lineTo(xOf(weightPoints.lastIndex), h)
                    lineTo(xOf(0), h)
                    close()
                }
                drawPath(
                    fillPath,
                    Brush.verticalGradient(listOf(Pink.copy(alpha = 0.5f), PinkLight.copy(alpha = 0.05f)))
                )

                // Line
                val linePath = Path().apply {
                    moveTo(xOf(0), yOf(weightPoints[0]))
                    weightPoints.forEachIndexed { i, v ->
                        if (i > 0) lineTo(xOf(i), yOf(v))
                    }
                }
                drawPath(linePath, Pink, style = Stroke(2.dp.toPx(), cap = StrokeCap.Round))

                // Dots
                weightPoints.forEachIndexed { i, v ->
                    drawCircle(Pink, radius = 3.5.dp.toPx(), center = Offset(xOf(i), yOf(v)))
                    drawCircle(Color.White, radius = 2.dp.toPx(), center = Offset(xOf(i), yOf(v)))
                }

                // Y-axis labels (drawn natively)
                listOf(75f, 50f, 25f).forEach { label ->
                    val ly = h - ((label - minW) / (maxW - minW)) * (h * 0.85f) - h * 0.05f
                    drawLine(TextGray.copy(alpha = 0.2f), Offset(0f, ly), Offset(w, ly), 1.dp.toPx())
                }
            }

            // Y labels overlay
            Column(
                Modifier.align(Alignment.CenterStart).fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("75", "50", "25").forEach {
                    Text(it, fontSize = 10.sp, color = TextGray)
                }
            }
        }

        // X-axis labels
        Row(Modifier.fillMaxWidth().padding(start = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            listOf("Jan", "Feb", "Mar", "Apr", "May").forEach {
                Text(it, fontSize = 10.sp, color = TextGray)
            }
        }
    }
}
@Composable
private fun SymptomTrendsCard() {
    InsightCard {
        Text("Symptom Trends", fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = TextDark)
        Text("Compared to last cycle", fontSize = 12.sp, color = TextGray)
        Spacer(Modifier.height(16.dp))

        // 4 segments — 3 labelled + 1 filler to complete the full circle
        val segments = listOf(
            Triple("Mood",     0.30f, Purple),
            Triple("Bloating", 0.31f, PurpleLight),
            Triple("Fatigue",  0.21f, Pink),
            Triple("",         0.18f, Green.copy(alpha = 0.45f))  // unlabelled filler
        )

        Box(
            Modifier
                .fillMaxWidth()
                .height(220.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(Modifier.size(180.dp)) {
                val strokeWidth = 38.dp.toPx()
                val radius = (size.minDimension / 2f) - strokeWidth / 2f
                var startAngle = -90f

                segments.forEach { (_, sweep, color) ->
                    val sweepAngle = sweep * 360f
                    drawArc(
                        color = color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle - 2f,
                        useCenter = false,
                        style = Stroke(strokeWidth, cap = StrokeCap.Round),
                        topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f),
                        size = Size(radius * 2f, radius * 2f)
                    )
                    startAngle += sweepAngle
                }
            }

            // Labels at fixed positions that match the screenshot
            Box(Modifier.size(240.dp)) {
                // 30% Mood — left side
                Text(
                    "30%\nMood",
                    modifier = Modifier.align(Alignment.CenterStart),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDark,
                    textAlign = TextAlign.Center
                )
                // 31% Bloating — right side
                Text(
                    "31%\nBloating",
                    modifier = Modifier.align(Alignment.CenterEnd),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDark,
                    textAlign = TextAlign.Center
                )
                // 21% Fatigue — bottom center
                Text(
                    "21%\nFatigue",
                    modifier = Modifier.align(Alignment.BottomCenter),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDark,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun BoxScope.DonutLabel(
    text: String,
    alignment: Alignment,
    paddingBottom: Dp,
    paddingStart: Dp
) {
    Text(
        text = text,
        modifier = Modifier
            .align(alignment)
            .padding(bottom = paddingBottom, start = paddingStart),
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        color = TextDark,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun LifestyleImpactCard() {
    InsightCard {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Correlation Strength",
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = TextDark
            )
            Text("4 months ▾", fontSize = 12.sp, color = TextGray)
        }

        Spacer(Modifier.height(14.dp))

        val rows = listOf(
            Triple("Sleep",    8, Purple),
            Triple("Hydrate",  6, Pink),
            Triple("Caffeine", 5, Green),
            Triple("Exercise", 4, Pink)
        )

        rows.forEach { (label, filled, color) ->
            LifestyleRow(label, filled, color)
            Spacer(Modifier.height(10.dp))
        }
    }
}

@Composable
private fun LifestyleRow(label: String, filledCount: Int, color: Color) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            modifier = Modifier.width(70.dp),
            fontSize = 13.sp,
            color = TextDark
        )
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            repeat(8) { i ->
                Box(
                    modifier = Modifier
                        .size(width = 26.dp, height = 22.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(if (i < filledCount) color else Color(0xFFEEEEEE))
                )
            }
        }
    }
}

@Composable
private fun BottomNavBar() {
    NavigationBar(containerColor = Color.White, tonalElevation = 4.dp) {
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home", fontSize = 11.sp) }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.DateRange, contentDescription = "Track") },
            label = { Text("Track", fontSize = 11.sp) }
        )
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Default.BarChart, contentDescription = "Insights") },
            label = { Text("Insights", fontSize = 11.sp) }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
            label = { Text("", fontSize = 11.sp) }
        )
    }
}

@Composable
private fun InsightCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            content()
        }
    }
}

@Composable
private fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier =
    this.then(
        Modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            onClick = onClick
        )
    )