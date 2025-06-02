package com.github.funczz.ruby_calc.android.event

import android.content.Context
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.github.funczz.ruby_calc.android.R
import com.github.funczz.ruby_calc.android.getAssets
import com.github.funczz.ruby_calc.android.ui.theme.RubyCalcTheme
import java.util.Optional

data class LicenseDialogUiEvent(
    val assetsDirectory: String,
    val confirmAction: () -> Unit,
    val modifier: Modifier = Modifier,
) : UiEvent {

    override var isDone: Boolean = false

    private var licenseTextList: Optional<List<String>> = Optional.empty()

    @Composable
    fun Show(context: Context) {
        if (isDone) return

        if (!licenseTextList.isPresent) {
            val list = mutableListOf<Pair<String, String>>()
            context.assets.getAssets(fileName = assetsDirectory).useWithFileName { (name, stream) ->
                list.add((name to stream.reader(Charsets.UTF_8).readText()))
            }
            licenseTextList = Optional.of(
                //context.assets.getAssets(assetsDirectory).fileTree().map { it.fileName }
                list.sortedBy { it.first }.map { it.second }
            )
        }

        val onClick: () -> Unit = {
            isDone = true
            confirmAction()
        }

        LicenseDialog(
            licenseList = licenseTextList.get(),
            modifier = modifier,
        ) { onClick() }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicenseDialog(
    licenseList: List<String>,
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
                            Text(text = stringResource(id = R.string.license_dialog_title))
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
                Box(
                    modifier = modifier
                        .padding(it)
                        .fillMaxSize()
                        .horizontalScroll(rememberScrollState()),
                ) {
                    LazyColumn(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start,
                    ) {
                        items(licenseList.size) { index ->
                            Text(
                                text = licenseList[index],
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                fontFamily = FontFamily.Monospace,
                                textAlign = TextAlign.Start,
                                softWrap = false,
                            )
                            //HorizontalDivider(
                            //    thickness = 1.dp,
                            //    color = MaterialTheme.colorScheme.primary
                            //)

                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LicenseDialogPreview() {
    val mockLicenseList = (0..99).map {
        "License $it"
    }
    RubyCalcTheme {
        LicenseDialog(mockLicenseList) {}
    }
}
