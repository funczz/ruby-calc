package com.github.funczz.ruby_calc.android.parts


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.funczz.kotlin.notifier.Notifier
import com.github.funczz.ruby_calc.android.R
import com.github.funczz.ruby_calc.core.vo.Column
import com.github.funczz.ruby_calc.core.vo.OrderBy

@Composable
fun IndexOrderSelectionDropdownMenuBox(
    value: () -> String,
    column: () -> Column,
    orderBy: () -> OrderBy,
    limit: () -> Int,
    action: (String, Column, OrderBy, Int, Notifier) -> Unit,
    notifier: Notifier,
) {

    var expanded by remember { mutableStateOf(false) }

    val valueResult = value()
    val columnResult = column()
    val orderByResult = orderBy()
    val limitResult = limit()

    val onSelect: (() -> Unit) -> Unit = {
        it()
        expanded = false
    }
    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopStart)
    ) {
        IconButton(
            onClick = {
                expanded = true
            }
        ) {
            Icon(
                Icons.Filled.Menu,
                contentDescription = stringResource(id = R.string.menu_content_description),
            )
        }
        DropdownMenu(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp)),
            expanded = expanded,
            onDismissRequest = { onSelect {} }
        ) {
            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.sort_by_name_content_description)) },
                onClick = {
                    onSelect {
                        action(
                            valueResult,
                            Column.NAME,
                            orderByResult,
                            limitResult,
                            notifier,
                        )
                    }
                },
                trailingIcon = {
                    if (columnResult == Column.NAME) Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null
                    )
                },
                enabled = columnResult != Column.NAME
            )
            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.sort_by_update_at_content_description)) },
                onClick = {
                    onSelect {
                        action(
                            valueResult,
                            Column.UPDATED_AT,
                            orderByResult,
                            limitResult,
                            notifier,
                        )
                    }
                },
                trailingIcon = {
                    if (columnResult == Column.UPDATED_AT) Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null
                    )
                },
                enabled = columnResult != Column.UPDATED_AT
            )
            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.sort_by_create_at_content_description)) },
                onClick = {
                    onSelect {
                        action(
                            valueResult,
                            Column.CREATED_AT,
                            orderByResult,
                            limitResult,
                            notifier,
                        )
                    }
                },
                trailingIcon = {
                    if (columnResult == Column.CREATED_AT) Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null
                    )
                },
                enabled = columnResult != Column.CREATED_AT

            )
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.sort_order_by_asc_content_description)) },
                onClick = {
                    onSelect {
                        action(
                            valueResult,
                            columnResult,
                            OrderBy.ASC,
                            limitResult,
                            notifier,
                        )
                    }
                },
                trailingIcon = {
                    if (orderByResult == OrderBy.ASC) Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null
                    )
                },
                enabled = orderByResult != OrderBy.ASC
            )
            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.sort_order_by_desc_content_description)) },
                onClick = {
                    onSelect {
                        action(
                            valueResult,
                            columnResult,
                            OrderBy.DESC,
                            limitResult,
                            notifier,
                        )
                    }
                },
                trailingIcon = {
                    if (orderByResult == OrderBy.DESC) Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null
                    )
                },
                enabled = orderByResult != OrderBy.DESC
            )
        }
    }
}
