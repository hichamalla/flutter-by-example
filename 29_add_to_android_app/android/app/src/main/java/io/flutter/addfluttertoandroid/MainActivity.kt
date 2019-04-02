package io.flutter.addfluttertoandroid

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import io.flutter.embedding.android.FlutterFragment
import io.flutter.embedding.engine.FlutterEngine
import kotlinx.android.synthetic.main.activity_main.*
import android.view.*
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.engine.FlutterShellArgs
import io.flutter.plugins.GeneratedPluginRegistrant


class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        // removeAllFragments()
        when (item.itemId) {
            R.id.navigation_home -> {
                addFragment(null, "I am an Android Fragment!!!")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                addFragment(null, "I am a second Android Fragment!!!")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                addFlutterFragment()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addFragment(null, "I am an Android Fragment!!!")

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_favorite -> {
            Log.i("onCreate", "Menu item selected")
            launchFlutterActivity()
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun addFragment(savedInstanceState: Bundle?, text: String) {
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container,
                    AndroidFragment.newInstance(text))
                .commit()
        }
    }


    private fun addFlutterFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container,
                MyFlutterFragment.newInstance())
            .commit()
    }

    private fun launchFlutterActivity() {
        startActivity(Intent(this, MyFlutterActivity::class.java))
    }
}


class AndroidFragment : Fragment() {

    companion object {
        fun newInstance(text: String): AndroidFragment {
            val fragment = AndroidFragment()
            val args = Bundle()
            args.putString("text", text)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        arguments?.getString("text").let {
            view.findViewById<TextView>(R.id.fragment_message).setText(it)
        }
        return view
    }
}

class MyFlutterFragment : FlutterFragment() {

    companion object {
        fun newInstance(): FlutterFragment {
            val fragment = MyFlutterFragment()
            fragment.arguments = FlutterFragment.createArgsBundle(null, null, null, null, null)
            return fragment
        }
    }

    override fun onCreateFlutterEngine(@NonNull context: Context): FlutterEngine {
        val app = context.applicationContext as MyApplication
        return app.engine
    }

    override fun retainFlutterIsolateAfterFragmentDestruction() : Boolean {
        return true
    }
}

class MyFlutterActivity : FlutterActivity() {

    override fun createFlutterFragment(): FlutterFragment {
        return MyFlutterFragment()
    }

}