package com.focusfera.app.ui.focus

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.focusfera.app.data.local.entity.SubjectEntity
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@Composable
fun FocusSetupScreen(
    subjects: List<SubjectEntity>,
    onContinue: (
        subjectId: Long,
        focusMode: FocusMode,
        durationMinutes: Int
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSubject by remember {
        mutableStateOf<SubjectEntity?>(null)
    }

    var selectedMode by remember {
        mutableStateOf<FocusMode?>(null)
    }

    var durationMinutes by remember {
        mutableIntStateOf(50)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = "Preparar foco",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        Text(
            text = "Escolha o que você vai estudar e como deseja focar.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        Text(
            text = "Matéria",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        subjects.forEach { subject ->

            val isSelected =
                selectedSubject?.id == subject.id

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .then(
                        if (isSelected) {
                            Modifier.border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(12.dp)
                            )
                        } else {
                            Modifier
                        }
                    )
                    .clickable {
                        selectedSubject = subject
                    },
                colors = CardDefaults.cardColors(
                    containerColor =
                        if (isSelected) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        }
                )
            ) {
                Text(
                    text = subject.name,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = "Modo de foco",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        FocusModeButton(
            text = "Temporizador",
            selected = selectedMode == FocusMode.TIMER,
            onClick = {
                selectedMode = FocusMode.TIMER
            }
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        FocusModeButton(
            text = "Pomodoro",
            selected = selectedMode == FocusMode.POMODORO,
            onClick = {
                selectedMode = FocusMode.POMODORO
            }
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        FocusModeButton(
            text = "Cronômetro livre",
            selected = selectedMode == FocusMode.STOPWATCH,
            onClick = {
                selectedMode = FocusMode.STOPWATCH
            }
        )

        if (selectedMode == FocusMode.TIMER) {

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Text(
                text = "Tempo de foco",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    25,
                    50,
                    60
                ).forEach { minutes ->

                    if (durationMinutes == minutes) {
                        Button(
                            onClick = {
                                durationMinutes = minutes
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("$minutes min")
                        }
                    } else {
                        OutlinedButton(
                            onClick = {
                                durationMinutes = minutes
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("$minutes min")
                        }
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = {
                        if (durationMinutes > 5) {
                            durationMinutes -= 5
                        }
                    }
                ) {
                    Text("- 5")
                }

                Text(
                    text = "$durationMinutes minutos",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                OutlinedButton(
                    onClick = {
                        if (durationMinutes < 240) {
                            durationMinutes += 5
                        }
                    }
                ) {
                    Text("+ 5")
                }
            }
        }

        Spacer(
            modifier = Modifier.weight(1f)
        )

        Button(
            onClick = {
                val subject = selectedSubject
                    ?: return@Button

                val mode = selectedMode
                    ?: return@Button

                onContinue(
                    subject.id,
                    mode,
                    durationMinutes
                )
            },
            enabled =
                selectedSubject != null &&
                        selectedMode != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Continuar")
        }
    }
}

@Composable
private fun FocusModeButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    if (selected) {
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text)
        }
    }
}