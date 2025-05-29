import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PinkLightPrimary = Color(0xFFF8BBD0)  // Rosa claro
private val PinkLightOnPrimary = Color(0xFF4A4A4A)  // Cinza escuro
private val PinkLightSecondary = Color(0xFFF48FB1)  // Rosa médio
private val PinkLightOnSecondary = Color(0xFFFFFFFF)  // Branco
private val PinkLightBackground = Color(0xFFFFF0F5)  // Rosa bem clarinho
private val PinkLightSurface = Color(0xFFFFEBEE)  // Fundo de cards
private val PinkLightSurfaceVariant = Color(0xFFFCE4EC)  // Fundo variante

internal val PinkLightColorScheme = lightColorScheme(
    primary = PinkLightPrimary,
    onPrimary = PinkLightOnPrimary,
    primaryContainer = PinkLightPrimary,
    onPrimaryContainer = PinkLightOnPrimary,
    secondary = PinkLightSecondary,
    onSecondary = PinkLightOnSecondary,
    secondaryContainer = PinkLightSecondary,
    onSecondaryContainer = PinkLightOnSecondary,
    background = PinkLightBackground,
    onBackground = Color.Black,
    surface = PinkLightSurface,
    onSurface = Color.Black,
    surfaceVariant = PinkLightSurfaceVariant,
    onSurfaceVariant = Color.Black
)

@Composable
fun SweetJoyGeladinhosTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = PinkLightColorScheme,
        typography = Typography(),
        shapes = Shapes(),
        content = content
    )
}
