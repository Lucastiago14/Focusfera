package com.focusfera.app.ui.focus

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.focusfera.app.ui.focus.viewmodel.FocusTimerViewModel

@Composable
fun FocusTimerScreen(
    subjectName: String,
    focusMode: FocusMode,
    durationMinutes: Int,
    viewModel: FocusTimerViewModel,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val remainingMillis by
    viewModel.remainingMillis.collectAsStateWithLifecycle()

    val isRunning by
    viewModel.isRunning.collectAsStateWithLifecycle()

    val isFinished by
    viewModel.isFinished.collectAsStateWithLifecycle()

    val studiedMillis by
    viewModel.studiedMillis.collectAsStateWithLifecycle()

    var showExitDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(durationMinutes) {
        if (focusMode == FocusMode.TIMER) {
            viewModel.prepareTimer(durationMinutes)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = subjectName,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = when (focusMode) {
                FocusMode.TIMER ->
                    "Temporizador"

                FocusMode.POMODORO ->
                    "Pomodoro"

                FocusMode.STOPWATCH ->
                    "Cronômetro livre"
            },
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        if (focusMode == FocusMode.TIMER) {

            Text(
                text = formatTime(
                    remainingMillis
                ),
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = when {
                    isFinished ->
                        "Sessão finalizada"

                    isRunning ->
                        "Foco em andamento"

                    else ->
                        "Pronto para começar"
                },
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Tempo estudado: ${formatStudyTime(studiedMillis)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            if (!isRunning && !isFinished) {

                Button(
                    onClick = {
                        if (remainingMillis ==
                            durationMinutes * 60_000L
                        ) {
                            viewModel.startTimer()
                        } else {
                            viewModel.resumeTimer()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (
                            remainingMillis ==
                            durationMinutes * 60_000L
                        ) {
                            "Iniciar"
                        } else {
                            "Continuar"
                        }
                    )
                    if (
                        remainingMillis <
                        durationMinutes * 60_000L
                    ) {
                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        OutlinedButton(
                            onClick = {
                                viewModel.finishEarly()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Finalizar agora")
                        }
                    }
                }

            } else if (isRunning) {

                Button(
                    onClick = {
                        viewModel.pauseTimer()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Pausar")
                }
                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                OutlinedButton(
                    onClick = {
                        viewModel.finishEarly()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Finalizar agora")
                }
            }

            if (isFinished) {

                Button(
                    onClick = onExit,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Concluir")
                }
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            if (!isFinished) {

                OutlinedButton(
                    onClick = {
                        showExitDialog = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar sessão")
                }
            }

        } else {

            Text(
                text = "Este modo será implementado nas próximas etapas.",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            OutlinedButton(
                onClick = onExit
            ) {
                Text("Voltar")
            }
        }

        Spacer(
            modifier = Modifier.weight(1f)
        )
    }

    if (showExitDialog) {

        AlertDialog(
            onDismissRequest = {
                showExitDialog = false
            },
            title = {
                Text("Cancelar sessão?")
            },
            text = {
                Text(
                    "O tempo desta sessão ainda não será salvo."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetTimer(
                            durationMinutes
                        )

                        showExitDialog = false

                        onExit()
                    }
                ) {
                    Text("Cancelar sessão")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showExitDialog = false
                    }
                ) {
                    Text("Continuar estudando")
                }
            }
        )
    }
}

private fun formatTime(
    millis: Long
): String {

    val totalSeconds =
        (millis + 999L) / 1000L

    val minutes =
        totalSeconds / 60L

    val seconds =
        totalSeconds % 60L

    return "%02d:%02d".format(
        minutes,
        seconds
    )
}
private fun formatStudyTime(
    millis: Long
): String {

    val totalSeconds =
        millis / 1000L

    val minutes =
        totalSeconds / 60L

    val seconds =
        totalSeconds % 60L

    return "%02d:%02d".format(
        minutes,
        seconds
    )
}