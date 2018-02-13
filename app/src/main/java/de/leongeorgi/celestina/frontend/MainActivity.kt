package de.leongeorgi.celestina

import android.os.Bundle
import de.leongeorgi.celestina.frontend.MediaStoreActivity

class MainActivity : MediaStoreActivity() {
    override fun onMusicUpdated() {
        // TODO
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
