package com.example.ui.components

import android.graphics.Paint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.ui.theme.EmberAmber
import com.example.ui.theme.ParchmentLight
import com.example.ui.theme.SmokelessFlameBlue
import com.example.ui.theme.SolomonGold
import com.example.ui.theme.SolomonGoldBright
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class RitualNode(
    val id: Int,
    val name: String,
    val symbol: String,
    val angleDegrees: Float,
    val radiusRatio: Float,
    val color: Color
)

@Composable
fun SolomonCircleCanvas(
    activeNodes: Set<Int>,
    onNodeTapped: (Int) -> Unit,
    modifier: Modifier = Modifier,
    circleThemeColor: Color = SolomonGold
) {
    val infiniteTransition = rememberInfiniteTransition(label = "solomon_rotation")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 28000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "outer_ring_rotation"
    )

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val nodes = remember {
        listOf(
            RitualNode(1, "NAR (Fire)", "🜂", -90f, 0.72f, Color(0xFFFF3D00)),
            RitualNode(2, "MA' (Water)", "🜄", -30f, 0.72f, Color(0xFF00E5FF)),
            RitualNode(3, "HAWA' (Air)", "🜁", 30f, 0.72f, Color(0xFF00E676)),
            RitualNode(4, "ARD (Earth)", "🜃", 90f, 0.72f, Color(0xFFFFB300)),
            RitualNode(5, "ADAM (Clay)", "🜍", 150f, 0.72f, Color(0xFFAB47BC)),
            RitualNode(6, "RUH (Spirit)", "🜚", 210f, 0.72f, Color(0xFF7C4DFF))
        )
    }

    Box(
        modifier = modifier.size(320.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(activeNodes) {
                    detectTapGestures { tapOffset ->
                        val centerX = size.width / 2f
                        val centerY = size.height / 2f
                        val maxRadius = size.width.coerceAtMost(size.height) / 2f

                        for (node in nodes) {
                            val rad = Math.toRadians(node.angleDegrees.toDouble())
                            val r = maxRadius * node.radiusRatio
                            val nodeX = centerX + (r * cos(rad)).toFloat()
                            val nodeY = centerY + (r * sin(rad)).toFloat()

                            val dist = sqrt(
                                (tapOffset.x - nodeX) * (tapOffset.x - nodeX) +
                                    (tapOffset.y - nodeY) * (tapOffset.y - nodeY)
                            )
                            if (dist <= 36.dp.toPx()) {
                                onNodeTapped(node.id)
                                break
                            }
                        }
                    }
                }
        ) {
            val centerX = size.width / 2f
            val centerY = size.height / 2f
            val baseRadius = (size.width.coerceAtMost(size.height) / 2f) * 0.95f

            // Outer ethereal glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        circleThemeColor.copy(alpha = 0.22f * pulseScale),
                        SmokelessFlameBlue.copy(alpha = 0.08f),
                        Color.Transparent
                    ),
                    center = Offset(centerX, centerY),
                    radius = baseRadius * 1.08f
                ),
                radius = baseRadius * 1.08f,
                center = Offset(centerX, centerY)
            )

            // Outer Runic Ring
            drawCircle(
                color = circleThemeColor.copy(alpha = 0.85f),
                radius = baseRadius,
                center = Offset(centerX, centerY),
                style = Stroke(width = 2.5.dp.toPx())
            )

            // Second concentric ring
            drawCircle(
                color = circleThemeColor.copy(alpha = 0.45f),
                radius = baseRadius * 0.92f,
                center = Offset(centerX, centerY),
                style = Stroke(width = 1.2.dp.toPx())
            )

            // Celestial Radial Ticks with rotation
            val tickCount = 36
            for (i in 0 until tickCount) {
                val angleDeg = rotationAngle + (i * (360f / tickCount))
                val rad = Math.toRadians(angleDeg.toDouble())
                val innerR = if (i % 6 == 0) baseRadius * 0.91f else baseRadius * 0.94f
                val outerR = baseRadius
                val strokeW = if (i % 6 == 0) 2.dp.toPx() else 1.dp.toPx()

                val start = Offset(
                    (centerX + innerR * cos(rad)).toFloat(),
                    (centerY + innerR * sin(rad)).toFloat()
                )
                val end = Offset(
                    (centerX + outerR * cos(rad)).toFloat(),
                    (centerY + outerR * sin(rad)).toFloat()
                )
                drawLine(
                    color = circleThemeColor.copy(alpha = if (i % 6 == 0) 0.8f else 0.4f),
                    start = start,
                    end = end,
                    strokeWidth = strokeW
                )
            }

            // Draw Solomon Seal (Hexagram)
            val sealRadius = baseRadius * 0.72f
            drawHexagram(
                centerX = centerX,
                centerY = centerY,
                radius = sealRadius,
                color = circleThemeColor.copy(alpha = 0.5f),
                pulse = pulseScale
            )

            // Inner circle
            drawCircle(
                color = SmokelessFlameBlue.copy(alpha = 0.6f),
                radius = baseRadius * 0.40f * pulseScale,
                center = Offset(centerX, centerY),
                style = Stroke(width = 1.5.dp.toPx())
            )

            // Active connecting lines between active nodes
            val activeNodeList = nodes.filter { activeNodes.contains(it.id) }
            if (activeNodeList.size >= 2) {
                for (i in 0 until activeNodeList.size - 1) {
                    val n1 = activeNodeList[i]
                    val n2 = activeNodeList[i + 1]
                    val rad1 = Math.toRadians(n1.angleDegrees.toDouble())
                    val rad2 = Math.toRadians(n2.angleDegrees.toDouble())
                    val p1 = Offset(
                        (centerX + baseRadius * n1.radiusRatio * cos(rad1)).toFloat(),
                        (centerY + baseRadius * n1.radiusRatio * sin(rad1)).toFloat()
                    )
                    val p2 = Offset(
                        (centerX + baseRadius * n2.radiusRatio * cos(rad2)).toFloat(),
                        (centerY + baseRadius * n2.radiusRatio * sin(rad2)).toFloat()
                    )
                    drawLine(
                        brush = Brush.linearGradient(
                            listOf(SolomonGoldBright, SmokelessFlameBlue, SolomonGold)
                        ),
                        start = p1,
                        end = p2,
                        strokeWidth = 3.dp.toPx()
                    )
                }
            }

            // Draw the 6 Ritual Nodes
            for (node in nodes) {
                val isActive = activeNodes.contains(node.id)
                val rad = Math.toRadians(node.angleDegrees.toDouble())
                val nodeR = baseRadius * node.radiusRatio
                val nodePos = Offset(
                    (centerX + nodeR * cos(rad)).toFloat(),
                    (centerY + nodeR * sin(rad)).toFloat()
                )

                // Node outer glow if active
                if (isActive) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(
                                node.color.copy(alpha = 0.9f),
                                node.color.copy(alpha = 0.2f),
                                Color.Transparent
                            ),
                            center = nodePos,
                            radius = 28.dp.toPx()
                        ),
                        radius = 28.dp.toPx(),
                        center = nodePos
                    )
                }

                // Node disc
                drawCircle(
                    color = if (isActive) node.color else Color(0xFF1B1D2A),
                    radius = 16.dp.toPx(),
                    center = nodePos
                )
                drawCircle(
                    color = if (isActive) SolomonGoldBright else circleThemeColor.copy(alpha = 0.7f),
                    radius = 16.dp.toPx(),
                    center = nodePos,
                    style = Stroke(width = if (isActive) 2.5.dp.toPx() else 1.5.dp.toPx())
                )

                // Alchemical Symbol
                drawContext.canvas.nativeCanvas.apply {
                    val paint = Paint().apply {
                        color = (if (isActive) ParchmentLight else circleThemeColor).toArgb()
                        textSize = 34f
                        textAlign = Paint.Align.CENTER
                        isAntiAlias = true
                        isFakeBoldText = true
                    }
                    drawText(node.symbol, nodePos.x, nodePos.y + 12f, paint)
                }
            }

            // Center Seal of Solomon Sigil
            val centerPaint = Paint().apply {
                color = (if (activeNodes.size == 6) SolomonGoldBright else SolomonGold).toArgb()
                textSize = 52f
                textAlign = Paint.Align.CENTER
                isAntiAlias = true
                isFakeBoldText = true
            }
            drawContext.canvas.nativeCanvas.drawText("۞", centerX, centerY + 18f, centerPaint)
        }
    }
}

private fun DrawScope.drawHexagram(
    centerX: Float,
    centerY: Float,
    radius: Float,
    color: Color,
    pulse: Float
) {
    val r = radius * pulse
    // Upward Triangle
    val pathUp = Path().apply {
        val a1 = Math.toRadians(-90.0)
        val a2 = Math.toRadians(30.0)
        val a3 = Math.toRadians(150.0)
        moveTo((centerX + r * cos(a1)).toFloat(), (centerY + r * sin(a1)).toFloat())
        lineTo((centerX + r * cos(a2)).toFloat(), (centerY + r * sin(a2)).toFloat())
        lineTo((centerX + r * cos(a3)).toFloat(), (centerY + r * sin(a3)).toFloat())
        close()
    }
    drawPath(pathUp, color = color, style = Stroke(width = 1.8.dp.toPx()))

    // Downward Triangle
    val pathDown = Path().apply {
        val a1 = Math.toRadians(90.0)
        val a2 = Math.toRadians(210.0)
        val a3 = Math.toRadians(330.0)
        moveTo((centerX + r * cos(a1)).toFloat(), (centerY + r * sin(a1)).toFloat())
        lineTo((centerX + r * cos(a2)).toFloat(), (centerY + r * sin(a2)).toFloat())
        lineTo((centerX + r * cos(a3)).toFloat(), (centerY + r * sin(a3)).toFloat())
        close()
    }
    drawPath(pathDown, color = color, style = Stroke(width = 1.8.dp.toPx()))
}
