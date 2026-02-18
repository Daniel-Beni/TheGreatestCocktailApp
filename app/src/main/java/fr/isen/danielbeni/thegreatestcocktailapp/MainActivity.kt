package fr.isen.danielbeni.thegreatestcocktailapp
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import fr.isen.danielbeni.thegreatestcocktailapp.ui.theme.TheGreatestCocktailAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TheGreatestCocktailAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
    override fun onStart() {
        super.onStart()
        // activity devient visible
    }

    override fun onResume() {
        super.onResume()
        //activity est interactive
    }

    override fun onPause() {
        super.onPause()
        // activity perd le focus
    }

    override fun onStop() {
        super.onStop()
        // activity n'est plus visible
    }

    override fun onRestart() {
        super.onRestart()
        // activity reprend depuis stopped
    }

    override fun onDestroy() {
        super.onDestroy()
        // activity est détruite
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    // alignement horizontal
    Row (modifier = modifier) {
            Text("Hello $name!")
            Text("Hello Isen")

    }
    // alignement vertical

    Column(modifier = modifier){
       Text("Hello $name!")
        Text("Hello Isen")
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

    TheGreatestCocktailAppTheme {
        Greeting("Android")
    }
}