package com.akhdan.myprofileapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import profileapp.shared.generated.resources.Res
import profileapp.shared.generated.resources.profile_pic
import org.jetbrains.compose.resources.painterResource

@Composable
fun App(
    viewModel: ProfileViewModel = viewModel { ProfileViewModel() }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SmoothDarkTheme(darkTheme = uiState.isDarkMode) {
        MyProfileScreen(
            uiState = uiState,
            onToggleDarkMode = { viewModel.toggleDarkMode() },
            onEditClick = { viewModel.setEditing(true) },
            onSaveProfile = { name, role, bio, email, phone, location ->
                viewModel.updateProfile(name, role, bio, email, phone, location)
            },
            onCancelEdit = { viewModel.setEditing(false) }
        )
    }
}

/**
 * Custom Smooth Dark Theme using animated color state transitions
 * providing buttery-smooth 400ms color interpolations when switching dark mode.
 */
@Composable
fun SmoothDarkTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val lightColors = lightColorScheme(
        primary = Color(0xFF1565C0),
        onPrimary = Color.White,
        primaryContainer = Color(0xFFE3F2FD),
        onPrimaryContainer = Color(0xFF0D47A1),
        secondary = Color(0xFF0288D1),
        onSecondary = Color.White,
        surface = Color.White,
        onSurface = Color(0xFF1C1B1F),
        surfaceVariant = Color(0xFFF1F5F9),
        onSurfaceVariant = Color(0xFF475569),
        background = Color(0xFFF8FAFC),
        onBackground = Color(0xFF0F172A),
        outline = Color(0xFF94A3B8)
    )

    val darkColors = darkColorScheme(
        primary = Color(0xFF90CAF9),
        onPrimary = Color(0xFF0D47A1),
        primaryContainer = Color(0xFF1E3A8A),
        onPrimaryContainer = Color(0xFFDBEAFE),
        secondary = Color(0xFF38BDF8),
        onSecondary = Color(0xFF0C4A6E),
        surface = Color(0xFF1E293B),
        onSurface = Color(0xFFF8FAFC),
        surfaceVariant = Color(0xFF334155),
        onSurfaceVariant = Color(0xFFCBD5E1),
        background = Color(0xFF0F172A),
        onBackground = Color(0xFFF8FAFC),
        outline = Color(0xFF64748B)
    )

    val targetColors = if (darkTheme) darkColors else lightColors
    val animationSpec = tween<Color>(durationMillis = 400, easing = FastOutSlowInEasing)

    @Composable
    fun animateColor(target: Color) = animateColorAsState(target, animationSpec, label = "themeColor").value

    val animatedColorScheme = targetColors.copy(
        primary = animateColor(targetColors.primary),
        onPrimary = animateColor(targetColors.onPrimary),
        primaryContainer = animateColor(targetColors.primaryContainer),
        onPrimaryContainer = animateColor(targetColors.onPrimaryContainer),
        secondary = animateColor(targetColors.secondary),
        onSecondary = animateColor(targetColors.onSecondary),
        surface = animateColor(targetColors.surface),
        onSurface = animateColor(targetColors.onSurface),
        surfaceVariant = animateColor(targetColors.surfaceVariant),
        onSurfaceVariant = animateColor(targetColors.onSurfaceVariant),
        background = animateColor(targetColors.background),
        onBackground = animateColor(targetColors.onBackground),
        outline = animateColor(targetColors.outline)
    )

    MaterialTheme(
        colorScheme = animatedColorScheme,
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileScreen(
    uiState: ProfileUiState,
    onToggleDarkMode: () -> Unit,
    onEditClick: () -> Unit,
    onSaveProfile: (name: String, role: String, bio: String, email: String, phone: String, location: String) -> Unit,
    onCancelEdit: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (uiState.isEditing) "Edit Profile" else "My Profile",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    // Dark Mode Toggle Switch / Button
                    IconButton(onClick = onToggleDarkMode) {
                        Icon(
                            imageVector = if (uiState.isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Toggle Dark Mode",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (!uiState.isEditing) {
                        IconButton(onClick = onEditClick) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedVisibility(
                visible = uiState.isEditing,
                enter = fadeIn(animationSpec = tween(300)),
                exit = fadeOut(animationSpec = tween(300))
            ) {
                EditProfileForm(
                    initialState = uiState,
                    onSave = onSaveProfile,
                    onCancel = onCancelEdit
                )
            }

            AnimatedVisibility(
                visible = !uiState.isEditing,
                enter = fadeIn(animationSpec = tween(300)),
                exit = fadeOut(animationSpec = tween(300))
            ) {
                ProfileDisplayView(
                    uiState = uiState,
                    onEditClick = onEditClick
                )
            }
        }
    }
}

@Composable
fun ProfileDisplayView(
    uiState: ProfileUiState,
    onEditClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileHeader(
            name = uiState.name,
            role = uiState.role
        )

        Spacer(modifier = Modifier.height(20.dp))

        ProfileCard(
            bio = uiState.bio
        )

        Spacer(modifier = Modifier.height(16.dp))

        ContactInfoCard(
            email = uiState.email,
            phone = uiState.phone,
            location = uiState.location
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onEditClick,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Edit Profile")
            }

            Button(
                onClick = { /* Connect action */ },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Connect")
            }
        }
    }
}

@Composable
fun EditProfileForm(
    initialState: ProfileUiState,
    onSave: (name: String, role: String, bio: String, email: String, phone: String, location: String) -> Unit,
    onCancel: () -> Unit
) {
    // State Hoisting: Local draft state for form fields
    var draftName by remember(initialState) { mutableStateOf(initialState.name) }
    var draftRole by remember(initialState) { mutableStateOf(initialState.role) }
    var draftBio by remember(initialState) { mutableStateOf(initialState.bio) }
    var draftEmail by remember(initialState) { mutableStateOf(initialState.email) }
    var draftPhone by remember(initialState) { mutableStateOf(initialState.phone) }
    var draftLocation by remember(initialState) { mutableStateOf(initialState.location) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Edit Profile Details",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        OutlinedTextField(
            value = draftName,
            onValueChange = { draftName = it },
            label = { Text("Full Name") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = draftRole,
            onValueChange = { draftRole = it },
            label = { Text("Role / Profession") },
            leadingIcon = { Icon(Icons.Default.Work, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = draftBio,
            onValueChange = { draftBio = it },
            label = { Text("Bio / About Me") },
            leadingIcon = { Icon(Icons.Default.Info, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            minLines = 3,
            maxLines = 5
        )

        OutlinedTextField(
            value = draftEmail,
            onValueChange = { draftEmail = it },
            label = { Text("Email Address") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = draftPhone,
            onValueChange = { draftPhone = it },
            label = { Text("Phone Number") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = draftLocation,
            onValueChange = { draftLocation = it },
            label = { Text("Location") },
            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Cancel, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Cancel")
            }

            Button(
                onClick = {
                    onSave(
                        draftName,
                        draftRole,
                        draftBio,
                        draftEmail,
                        draftPhone,
                        draftLocation
                    )
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Save")
            }
        }
    }
}

@Composable
fun ProfileHeader(name: String, role: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(Res.drawable.profile_pic),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = role,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ProfileCard(bio: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "About Me",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = bio,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun ContactInfoCard(
    email: String,
    phone: String,
    location: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Contact Details",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            InfoItem(icon = Icons.Default.Email, text = email)
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            InfoItem(icon = Icons.Default.Phone, text = phone)
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            InfoItem(icon = Icons.Default.LocationOn, text = location)
        }
    }
}

@Composable
fun InfoItem(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 15.sp
        )
    }
}
