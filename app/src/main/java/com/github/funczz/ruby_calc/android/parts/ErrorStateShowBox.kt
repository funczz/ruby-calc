package com.github.funczz.ruby_calc.android.parts

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.funczz.ruby_calc.android.R
import com.github.funczz.ruby_calc.android.ui.theme.RubyCalcTheme

@Composable
fun ErrorStateShowBox(
    throwable: Throwable?,
    paddingValues: PaddingValues = PaddingValues(),
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(paddingValues)
            .fillMaxSize()
            .horizontalScroll(rememberScrollState())
            .verticalScroll(rememberScrollState()),
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(
                value = throwable
                    ?.message
                    ?: "",
                onValueChange = {},
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.Start),
                readOnly = true,
                label = { Text(text = stringResource(id = R.string.system_error_message_label)) },
                singleLine = false,
                minLines = 1,
            )
            Spacer(modifier = modifier.height(4.dp))
            if (throwable is Throwable && throwable.cause is Throwable) {
                OutlinedTextField(
                    value = throwable
                        .cause
                        ?.message
                        ?: "",
                    onValueChange = {},
                    modifier = Modifier
                        .padding(20.dp)
                        .align(Alignment.Start),
                    readOnly = true,
                    label = { Text(text = stringResource(id = R.string.system_error_cause_label)) },
                    singleLine = false,
                    minLines = 1,
                )
                Spacer(modifier = modifier.height(4.dp))
            }
            OutlinedTextField(
                value = throwable
                    ?.stackTraceToString()
                    ?: "",
                onValueChange = {},
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.Start),
                readOnly = true,
                label = { Text(text = stringResource(id = R.string.system_error_stack_trace_label)) },
                singleLine = false,
                minLines = 1,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorStateShowBoxPreview() {
    val mockThrowable = Throwable(
        message = "throwable[1]: throw message",
        cause = Exception("ERROR!")
    )
    RubyCalcTheme {
        ErrorStateShowBox(mockThrowable)
    }
}
