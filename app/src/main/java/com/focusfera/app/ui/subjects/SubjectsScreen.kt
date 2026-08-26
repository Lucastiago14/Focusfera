package com.focusfera.app.ui.subjects

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.focusfera.app.data.local.entity.SubjectEntity
import com.focusfera.app.ui.subjects.viewmodel.SubjectsViewModel

@Composable
fun SubjectsScreen(
    viewModel: SubjectsViewModel,
    modifier: Modifier = Modifier
) {
    val subjects by viewModel.subjects.collectAsStateWithLifecycle()

    var showAddDialog by remember {
        mutableStateOf(false)
    }

    var subjectToEdit by remember {
        mutableStateOf<SubjectEntity?>(null)
    }

    var subjectToDelete by remember {
        mutableStateOf<SubjectEntity?>(null)
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "Matérias",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Organize seu tempo por matéria.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            if (subjects.isEmpty()) {
                EmptySubjectsContent()
            } else {
                SubjectsList(
                    subjects = subjects,
                    onEdit = { subject ->
                        subjectToEdit = subject
                    },
                    onDelete = { subject ->
                        subjectToDelete = subject
                    }
                )
            }
        }

        FloatingActionButton(
            onClick = {
                showAddDialog = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Text("+")
        }
    }

    if (showAddDialog) {
        SubjectFormDialog(
            title = "Nova matéria",
            initialName = "",
            initialColor = null,
            confirmText = "Salvar",
            onDismiss = {
                showAddDialog = false
            },
            onConfirm = { name, color ->
                viewModel.addSubject(
                    name = name,
                    color = color
                )

                showAddDialog = false
            }
        )
    }

    subjectToEdit?.let { subject ->
        SubjectFormDialog(
            title = "Editar matéria",
            initialName = subject.name,
            initialColor = subject.color,
            confirmText = "Salvar",
            onDismiss = {
                subjectToEdit = null
            },
            onConfirm = { name, color ->
                val trimmedName = name.trim()

                if (trimmedName.isNotBlank()) {
                    viewModel.updateSubject(
                        subject.copy(
                            name = trimmedName,
                            color = color
                        )
                    )
                }

                subjectToEdit = null
            }
        )
    }

    subjectToDelete?.let { subject ->
        DeleteSubjectDialog(
            subject = subject,
            onDismiss = {
                subjectToDelete = null
            },
            onConfirm = {
                viewModel.deleteSubject(subject)
                subjectToDelete = null
            }
        )
    }
}

@Composable
private fun EmptySubjectsContent() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Nenhuma matéria cadastrada.",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Crie sua primeira matéria para começar a acompanhar seu tempo de estudo.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SubjectsList(
    subjects: List<SubjectEntity>,
    onEdit: (SubjectEntity) -> Unit,
    onDelete: (SubjectEntity) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = subjects,
            key = { subject ->
                subject.id
            }
        ) { subject ->
            SubjectCard(
                subject = subject,
                onEdit = {
                    onEdit(subject)
                },
                onDelete = {
                    onDelete(subject)
                }
            )
        }
    }
}

@Composable
private fun SubjectCard(
    subject: SubjectEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var menuExpanded by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 18.dp,
                    top = 10.dp,
                    end = 6.dp,
                    bottom = 10.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(
                        Color(subject.color.toInt())
                    )
            )

            Spacer(
                modifier = Modifier.size(14.dp)
            )

            Text(
                text = subject.name,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )

            Box {
                IconButton(
                    onClick = {
                        menuExpanded = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Opções da matéria"
                    )
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = {
                        menuExpanded = false
                    }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text("Editar")
                        },
                        onClick = {
                            menuExpanded = false
                            onEdit()
                        }
                    )

                    DropdownMenuItem(
                        text = {
                            Text("Excluir")
                        },
                        onClick = {
                            menuExpanded = false
                            onDelete()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SubjectFormDialog(
    title: String,
    initialName: String,
    initialColor: Long?,
    confirmText: String,
    onDismiss: () -> Unit,
    onConfirm: (
        name: String,
        color: Long
    ) -> Unit
) {
    val colors = listOf(
        Color(0xFF536DFE),
        Color(0xFF43A047),
        Color(0xFFE53935),
        Color(0xFFFFA000),
        Color(0xFF8E24AA),
        Color(0xFF00897B)
    )

    var subjectName by remember(initialName) {
        mutableStateOf(initialName)
    }

    var selectedColor by remember(initialColor) {
        mutableLongStateOf(
            initialColor
                ?: colors.first().toArgb().toLong()
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(title)
        },
        text = {
            Column {
                OutlinedTextField(
                    value = subjectName,
                    onValueChange = {
                        subjectName = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Nome")
                    },
                    singleLine = true
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Text(
                    text = "Cor",
                    style = MaterialTheme.typography.titleSmall
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    colors.forEach { color ->

                        val colorValue =
                            color.toArgb().toLong()

                        val isSelected =
                            selectedColor == colorValue

                        Box(
                            modifier = Modifier
                                .size(
                                    if (isSelected) 40.dp
                                    else 32.dp
                                )
                                .clip(CircleShape)
                                .background(color)
                                .clickable {
                                    selectedColor = colorValue
                                }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        subjectName,
                        selectedColor
                    )
                },
                enabled = subjectName.isNotBlank()
            ) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancelar")
            }
        },
        shape = RoundedCornerShape(24.dp)
    )
}

@Composable
private fun DeleteSubjectDialog(
    subject: SubjectEntity,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Excluir matéria?")
        },
        text = {
            Text(
                text = "Tem certeza que deseja excluir \"${subject.name}\"?"
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm
            ) {
                Text("Excluir")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancelar")
            }
        }
    )
}