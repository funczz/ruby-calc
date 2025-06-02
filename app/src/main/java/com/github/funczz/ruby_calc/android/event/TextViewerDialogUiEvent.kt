package com.github.funczz.ruby_calc.android.event

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.github.funczz.ruby_calc.android.R
import com.github.funczz.ruby_calc.android.ui.theme.RubyCalcTheme

data class TextViewerDialogUiEvent(
    val title: String,
    val body: String,
    val confirmAction: () -> Unit,
    val modifier: Modifier = Modifier,
) : UiEvent {

    override var isDone: Boolean = false

    @Composable
    fun Show() {
        if (isDone) return

        val onClick: () -> Unit = {
            isDone = true
            confirmAction()
        }

        TextViewerDialog(
            title = title,
            body = body,
            modifier = modifier,
        ) { onClick() }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextViewerDialog(
    title: String,
    body: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {

    Dialog(
        onDismissRequest = onClick,
    ) {
        Surface(
            modifier = modifier,
            shape = AlertDialogDefaults.shape,
            color = AlertDialogDefaults.containerColor,
            tonalElevation = AlertDialogDefaults.TonalElevation,
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            if (title.isNotBlank()) {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.titleSmall,
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = onClick) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack, //Icons.Default.ArrowBack,
                                    contentDescription = stringResource(id = R.string.back_content_description)
                                )
                            }
                        },
                        actions = {},
                    )
                }
            ) {
                Column(
                    modifier = modifier
                        .padding(it)
                        .fillMaxSize()
                        .horizontalScroll(rememberScrollState())
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    OutlinedTextField(
                        value = body,
                        onValueChange = {},
                        modifier = Modifier
                            .padding(20.dp)
                            .align(Alignment.Start),
                        readOnly = true,
                        singleLine = false,
                        minLines = 1,
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TextViewerDialogPreview() {
    val mockTitle = "jruby/lib/bigdecimal_math.rb"
    val mockBody = """
        |#encoding:utf-8
        |require 'java'
        |
        |java_import 'ch.obermuhlner.math.big.BigDecimalMath'
        |java_import 'java.lang.Math'
        |java_import 'java.math.BigDecimal'
        |java_import 'java.math.MathContext'
        |java_import 'java.math.RoundingMode'
        |java_import 'java.text.DecimalFormat'
        |
        |class BigDecimal
        |  method_hash = {
        |    :* => :multiply,
        |    :- => :subtract,
        |    :+ => :add
        |  }
        |
        |  method_hash.each_pair do |bigdecimal_method, bd_method|
        |    define_method(bigdecimal_method.to_sym) do |other|
        |      send(bd_method, other)
        |    end
        |  end
        |.
        |.
        |.
    """.trimMargin("|")
    RubyCalcTheme {
        TextViewerDialog(title = mockTitle, body = mockBody) {}
    }
}
