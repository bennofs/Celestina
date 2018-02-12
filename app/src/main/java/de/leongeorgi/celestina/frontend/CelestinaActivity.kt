package de.leongeorgi.celestina.frontend

import android.support.v7.app.AppCompatActivity
import de.leongeorgi.celestina.Celestina

/**
 * @author leon
 */
abstract class CelestinaActivity : AppCompatActivity() {
    val celestina
        get() = application as Celestina
}