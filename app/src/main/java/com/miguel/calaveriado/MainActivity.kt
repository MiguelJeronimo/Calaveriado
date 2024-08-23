package com.miguel.calaveriado

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.tbouron.shakedetector.library.ShakeDetector
import com.miguel.calaveriado.ui.theme.CalaveriadoTheme
import com.miguel.calaveriado.utils.Sound
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private lateinit var soundPlayer: Sound
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sound = R.raw.skeleton_sound
        soundPlayer = Sound(sound, this@MainActivity)
        enableEdgeToEdge()
        setContent {
            var stateClick = remember {false}
            val imageState = remember{ mutableIntStateOf(R.drawable.baseline_play_arrow_24) }
            var shakeState = remember{ true }
            ShakeDetector.create(this) {
                //shakeState.value = !shakeState.value
                GlobalScope.launch {
                    if(shakeState){
                        stateClick = !stateClick
                        shakeState = !shakeState
                        imageState.intValue = R.drawable.baseline_stop_24
                        soundPlayer.start()
                        delay(1000)
                        imageState.intValue = R.drawable.baseline_play_arrow_24
                        soundPlayer.release()
                        soundPlayer.stop()
                        shakeState = !shakeState
                    }
                }
            }

            CalaveriadoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box (
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding)){
                        Column (Modifier.align(Alignment.Center)){
                            Image(painter = painterResource(id = R.drawable.skull), contentDescription = "Logo", Modifier.size(200.dp))
                            ButtonPlay(
                                Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .size(100.dp).padding(10.dp),
                                onClick = {
                                    stateClick = !stateClick
                                    GlobalScope.launch {
                                        when(stateClick){
                                            true->{
                                                imageState.intValue = R.drawable.baseline_stop_24
                                                soundPlayer.start()
                                                delay(1000)
                                                imageState.intValue = R.drawable.baseline_play_arrow_24
                                                soundPlayer.release()
                                            }
                                            else->{
                                                println("se detiene")
                                                soundPlayer.release()
                                                soundPlayer.stop()
                                                //shakeDetector.stopShakeDetector(baseContext)
                                                imageState.intValue = R.drawable.baseline_play_arrow_24
                                            }
                                        }
                                    }
                                },
                                image = imageState.intValue
                            )
                        }
                    }
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        println("Se reanudó")
        ShakeDetector.start()
    }

    override fun onStop() {
        super.onStop()
        println("Se detuvo")
        ShakeDetector.stop()
    }


    override fun onDestroy() {
        super.onDestroy()
        ShakeDetector.destroy()
        soundPlayer.release()
    }
}

@Composable
fun ButtonPlay(modifier: Modifier = Modifier, onClick: () -> Unit, image:Int) {
    IconButton(
        modifier = modifier,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = Color.LightGray,
            contentColor = Color.Red
        ),
        onClick = {
            onClick()
        }
    ){
        Image(painter = painterResource(id = image), contentDescription = "Play")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CalaveriadoTheme {
        Box (Modifier.fillMaxSize()){
            Column (Modifier.align(Alignment.Center)){
                Image(painter = painterResource(id = R.drawable.skull), contentDescription = "Logo")
                ButtonPlay(Modifier.align(Alignment.CenterHorizontally), onClick = {}, image = R.drawable.baseline_play_arrow_24)
            }
        }
    }
}