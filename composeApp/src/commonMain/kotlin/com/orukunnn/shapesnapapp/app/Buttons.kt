package com.orukunnn.shapesnapapp.app

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.woowla.compose.icon.collections.tabler.Tabler
import com.woowla.compose.icon.collections.tabler.tabler.Filled
import com.woowla.compose.icon.collections.tabler.tabler.filled.Heart

@Composable
fun ShapeSnapButton(
    imageVector: ImageVector,
    text: String,
    enabled: Boolean,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    OutlinedButton(
        enabled = enabled,
        onClick = onClick,
        colors = ButtonColors(
            containerColor = Color.Transparent,
            contentColor = color,
            disabledContainerColor = color,
            disabledContentColor = Color.White,
        ),
        modifier = modifier
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = Modifier.size(20.dp),
        )
        Spacer(Modifier.size(8.dp))
        Text(text = text)
    }
}

@Preview
@Composable
private fun ShapeSnapButtonPreview() {
    ShapeSnapButton(
        imageVector = Tabler.Filled.Heart,
        text = "Like",
        enabled = true,
        color = ShapeSnapColors.Accent,
        onClick = {},
    )
}