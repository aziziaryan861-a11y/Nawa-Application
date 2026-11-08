package com.nawa.app.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nawa.app.ui.screens.PlayerScreen
import com.nawa.app.ui.screens.QariScreen
import com.nawa.app.ui.screens.SplashScreen
import com.nawa.app.ui.screens.SurahScreen

@Composable
fun AppNav() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(
                onDone = {
                    navController.navigate("qari") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }
        composable("qari") {
            QariScreen(
                onQariSelected = { qariId ->
                    navController.navigate("surah/$qariId")
                }
            )
        }
        composable("surah/{qariId}") { backStackEntry ->
            val qariId = backStackEntry.arguments?.getString("qariId") ?: "basit"
            SurahScreen(
                qariId = qariId,
                onSurahSelected = { surahNumber ->
                    navController.navigate("player/$qariId/$surahNumber")
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable("player/{qariId}/{surahNo}") { backStackEntry ->
            val qariId = backStackEntry.arguments?.getString("qariId") ?: "basit"
            val surahNumber = backStackEntry.arguments?.getString("surahNo")?.toIntOrNull() ?: 36
            PlayerScreen(
                qariId = qariId,
                surahNumber = surahNumber,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
