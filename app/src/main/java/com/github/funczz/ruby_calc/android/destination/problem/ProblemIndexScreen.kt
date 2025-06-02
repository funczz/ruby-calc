package com.github.funczz.ruby_calc.android.destination.problem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.funczz.kotlin.notifier.Notifier
import com.github.funczz.ruby_calc.android.Presenter
import com.github.funczz.ruby_calc.android.R
import com.github.funczz.ruby_calc.android.UiState
import com.github.funczz.ruby_calc.android.parts.IndexOrderSelectionDropdownMenuBox
import com.github.funczz.ruby_calc.android.ui.theme.RubyCalcTheme
import com.github.funczz.ruby_calc.core.model.problem.ProblemIndex
import com.github.funczz.ruby_calc.core.model.problem.ProblemModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProblemIndexScreen(
    presenter: Presenter<UiState>,
    notifier: Notifier,
    navHostController: NavHostController,
    modifier: Modifier,

    ) {
    val uiState by presenter.getState()
    //val context = LocalContext.current
    //val owner = LocalLifecycleOwner.current

    val onIndexValue: (String) -> Unit = {
        ProblemUiCommand.index(
            value = it,
            orderColumn = uiState.problemIndex.orderColumn,
            orderBy = uiState.problemIndex.orderBy,
            limit = uiState.problemIndex.limit,
            notifier = notifier,
        )
    }
    val onCreate: () -> Unit = {
        ProblemUiCommand.create(
            presenter = presenter,
            notifier = notifier,
            navHostController = navHostController
        )
    }
    val onClick: (Long) -> Unit = {
        ProblemUiCommand.show(
            id = it,
            notifier = notifier,
            navHostController = navHostController
        )
    }

    onIndexValue(uiState.problemIndexSearchBoxUiState.indexValue.value.text)

    Scaffold(
        topBar = {
            TopAppBar(
                //検索テキストボックス
                title = {
                    OutlinedTextField(
                        value = uiState.problemIndexSearchBoxUiState.indexValue.value,
                        onValueChange = { v ->
                            onIndexValue(v.text)
                            uiState.problemIndexSearchBoxUiState.indexValue.onValueChange(value = v)
                        },
                        modifier = Modifier.size(TextFieldDefaults.MinWidth),
                        textStyle = MaterialTheme.typography.bodyMedium,
                        label = { Text(text = stringResource(id = R.string.problem_index_search_label)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = null,
                            )
                        },
                        singleLine = true,
                    )
                },
                //並べ替えドロップダウンメニュー
                navigationIcon = {
                    IndexOrderSelectionDropdownMenuBox(
                        value = { uiState.problemIndex.value },
                        column = { uiState.problemIndex.orderColumn },
                        orderBy = { uiState.problemIndex.orderBy },
                        limit = { uiState.problemIndex.limit },
                        action = { value, column, orderBy, limit, notifier ->
                            ProblemUiCommand.index(
                                value = value,
                                orderColumn = column,
                                orderBy = orderBy,
                                limit = limit,
                                notifier = notifier
                            )
                        },
                        notifier = notifier,
                    )
                },
                //新規作成ボタン
                actions = {
                    Button(
                        onClick = onCreate,
                        colors = ButtonDefaults.buttonColors(
                            contentColor = colorResource(id = R.color.bootstrap_light),
                            containerColor = colorResource(id = R.color.bootstrap_primary),
                            disabledContentColor = colorResource(id = R.color.bootstrap_light),
                            disabledContainerColor = colorResource(id = R.color.bootstrap_secondary),
                        )
                    ) {
                        Icon(
                            Icons.Filled.AddCircleOutline,
                            contentDescription = stringResource(id = R.string.add_content_description),
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(stringResource(id = R.string.add_content_description))
                    }
                }
            )
        }
    ) {
        LazyColumn(contentPadding = it) {
            items(uiState.problemIndex.problemList.size) { index ->
                val model = uiState.problemIndex.problemList[index]
                val updated = ProblemUiCommand.dateFormat(
                    instant = model.updatedAt,
                    pattern = stringResource(id = R.string.datetime_formatter_pattern)
                )
                val created = ProblemUiCommand.dateFormat(
                    instant = model.createdAt,
                    pattern = stringResource(id = R.string.datetime_formatter_pattern)
                )
                Surface(
                    modifier = modifier.clickable { onClick(model.id) },
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 16.dp),
                    ) {
                        Text(
                            text = model.name,
                            style = MaterialTheme.typography.titleSmall,
                        )
                        Text(
                            text = model.comment,
                            style = MaterialTheme.typography.bodySmall,
                        )
                        Spacer(modifier = modifier.height(4.dp))
                        Text(
                            text = "%s %s".format(
                                stringResource(id = R.string.update_at_content_format, updated),
                                stringResource(id = R.string.create_at_content_format, created),
                            ),
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .padding(end = 8.dp),
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Right,
                        )
                    }
                }
            }
        }
    }

    ProblemUiCommand.ConsumeUiEvent(presenter = presenter, context = LocalContext.current)
}

@Preview(showBackground = true)
@Composable
fun ProblemIndexScreenPreview() {
    RubyCalcTheme {
        ProblemIndexScreen(
            presenter = object : Presenter<UiState> {
                override fun getStateFlow(): StateFlow<UiState> {
                    TODO("Not yet implemented")
                }

                @Composable
                override fun getState(): State<UiState> {
                    val mockProblemIndexList = (0..99).map {
                        ProblemModel(
                            id = it.toLong(),
                            name = "hello world.",
                            programId = 0L,
                            comment = "Comment!",
                            createdAt = Instant.now(),
                            updatedAt = Instant.now(),
                        )
                    }
                    val uiState = UiState(
                        problemIndex = ProblemIndex(
                            problemList = mockProblemIndexList
                        )
                    )
                    return MutableStateFlow(uiState).asStateFlow().collectAsState()
                }

                override fun render(output: UiState) {
                    TODO("Not yet implemented")
                }
            },
            notifier = Notifier(),
            modifier = Modifier.fillMaxSize(),
            navHostController = rememberNavController()
        )
    }
}
