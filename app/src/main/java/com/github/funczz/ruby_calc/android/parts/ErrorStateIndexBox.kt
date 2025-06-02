package com.github.funczz.ruby_calc.android.parts


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.funczz.ruby_calc.android.ui.theme.RubyCalcTheme

@Composable
fun ErrorStateIndexBox(
    throwableList: List<Throwable>,
    paddingValues: PaddingValues = PaddingValues(),
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit,
) {
    Box(
        modifier = modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        LazyColumn {
            items(throwableList.size) { index ->
                Surface(
                    modifier = modifier.clickable { onClick(index) },
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 16.dp),
                    ) {
                        Text(
                            text = throwableList[index].message ?: "",
                            style = MaterialTheme.typography.titleSmall,
                        )
                        Text(
                            text = throwableList[index].stackTraceToString(),
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 5,
                        )
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorStateIndexBoxPreview() {
    val mockThrowableList = (0..99).map {
        Throwable(
            "throwable[$it]: throw message"
        )
    }
    RubyCalcTheme {
        ErrorStateIndexBox(mockThrowableList) {}
    }
}
