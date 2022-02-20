package com.asd

import android.app.Activity
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.MediaController
import android.widget.VideoView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.asd.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.gowtham.library.utils.LogMessage
import com.gowtham.library.utils.TrimVideo
import java.io.File


class MainActivity : AppCompatActivity(), View.OnClickListener{

    private val tag = "MainActivity"
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var videoView: VideoView? = null
    private val mediaController: MediaController? = null

    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK &&
            result.data != null) {
            val uri = Uri.parse(TrimVideo.getTrimmedVideoPath(result.data))
            videoView?.setMediaController(mediaController)
            videoView?.setVideoURI(uri)
            videoView?.requestFocus()
            videoView?.start()

            videoView?.setOnPreparedListener { mediaPlayer: MediaPlayer? ->
                mediaController?.setAnchorView(videoView)
            }

            Log.d(tag, "Trimmed path:: $uri")
        }else
            LogMessage.v("videoTrimResultLauncher data is null")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        videoView = findViewById(R.id.video_view)
        TrimVideo.activity(Uri.fromFile(File("/storage/emulated/0/Download/video.mp4")).toString())
//        .setCompressOption(new CompressOption()) //empty constructor for default compress option
            .setHideSeekBar(true)
            .start(this,startForResult)    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}