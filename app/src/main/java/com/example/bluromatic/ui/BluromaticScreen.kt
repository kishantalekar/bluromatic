package com.example.bluromatic.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bluromatic.R
import com.example.bluromatic.data.BlurAmount
import com.example.bluromatic.data.BlurAmountData

@Composable
fun BluromaticScreen(blurViewModel: BlurViewModel = viewModel(factory = BlurViewModel.Factory)) {
    val uiState by blurViewModel.blurUiState.collectAsStateWithLifecycle()
    val layoutDirection = LocalLayoutDirection.current
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(
                start = WindowInsets.safeDrawing
                    .asPaddingValues()
                    .calculateStartPadding(layoutDirection),
                end = WindowInsets.safeDrawing
                    .asPaddingValues()
                    .calculateEndPadding(layoutDirection)
            )
    ) {
        BluromaticScreenContent(
            blurUiState = uiState,
            blurAmountOptions = blurViewModel.blurAmount,
            applyBlur = blurViewModel::applyBlur,
            cancelWork = {},
            modifier = Modifier
                .padding(10.dp)
                .verticalScroll(
                    rememberScrollState()
                )
        )
    }
}

@Composable
fun BluromaticScreenContent(
    blurUiState: BlurUiState,
    blurAmountOptions: List<BlurAmount>,
    applyBlur: (Int) -> Unit,
    cancelWork: () -> Unit,
    modifier: Modifier = Modifier
) {

    var selectedValue by rememberSaveable { mutableStateOf(1) }
    Column(modifier=modifier) {
        Image(
            painter = painterResource(id = R.drawable.android_cupcake),
            contentDescription = stringResource(
                id = R.string.description_image
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            contentScale = ContentScale.Fit
        )
        BlurAmountContent(
            selectedValue = selectedValue,
            blurAmounts = blurAmountOptions,

            modifier = Modifier.fillMaxWidth(),
            onSelectedValueChange = { selectedValue = it }
        )

        BlurActions(
            blurUiState = blurUiState,
            onStartClick = { applyBlur(selectedValue) },
            onSeeFileClick = {},
            onCancelClick = { cancelWork() },
            modifier = Modifier.fillMaxWidth()
        )

    }
}

@Composable
private fun BlurActions(
    blurUiState: BlurUiState,
    onStartClick: () -> Unit,
    onSeeFileClick: (String) -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onStartClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.start))
        }
    }
}

@Composable
fun BlurAmountContent(
    selectedValue: Int,
    blurAmounts: List<BlurAmount>,
    modifier: Modifier = Modifier,
    onSelectedValueChange: (Int) -> Unit
) {
    Column(modifier = modifier.selectableGroup()) {
        Text(
            text = stringResource(R.string.blur_title),
            style = MaterialTheme.typography.headlineSmall
        )

        blurAmounts.forEach { amount ->
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        role = Role.RadioButton,
                        selected = (selectedValue == amount.blurAmount),
                        onClick = { onSelectedValueChange(amount.blurAmount) }
                    )
                    .size(48.dp)) {
                RadioButton(
                    selected = (selectedValue == amount.blurAmount),
                    onClick = null,
                    modifier=Modifier.size(48.dp),
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.primary
                    )
                )
                Text(stringResource(amount.blurAmountRes))
            }
        }
    }
}