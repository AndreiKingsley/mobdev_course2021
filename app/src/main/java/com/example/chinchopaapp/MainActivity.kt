import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.chinchopaapp.MainViewModel
import com.example.chinchopaapp.R
import com.example.chinchopaapp.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewBinding by viewBinding(ActivityMainBinding::bind)

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToAuthorizationStatus()
    }

    private fun subscribeToAuthorizationStatus() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isAuthorizedFlow.collect {
                    showSuitableNavigationFlow(it)
                }
            }
        }
    }

    // This method have to be idempotent. Do not override restored backstack.
    private fun showSuitableNavigationFlow(isAuthorized: Boolean) {
        val navController = findNavController(R.id.mainActivityNavigationHost)
        when (isAuthorized) {
            true -> {
                if (navController.backQueue.any { it.destination.id == R.id.registered_user_nav_graph}) {
                    return
                }
                navController.navigate(R.id.action_registeredUserNavGraph)
            }
            false -> {
                if (navController.backQueue.any { it.destination.id == R.id.guest_nav_graph}) {
                    return
                }
                navController.navigate(R.id.action_guestNavGraph)
            }
        }
    }
}

/*
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    val viewModel: MainViewModel by viewModels()
    private val viewBinding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       /* supportFragmentManager.beginTransaction().replace(
            R.id.fragmentContainerView,

        )

        */
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
        val userAdapter = setupRecyclerView()
        val userRecyclerView = findViewById<View>(R.id.recyclerView).also {
            it.isVisible = false
        }
        val progressBar = findViewById<View>(R.id.progressBar).also {
            it.isVisible = true
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect {
                    when(it) {
                        is MainViewModel.ViewState.Data -> {
                            userAdapter.userList = it.users
                            userAdapter.notifyDataSetChanged()
                            progressBar.isVisible = false
                            userRecyclerView.isVisible = true
                        }
                        is MainViewModel.ViewState.Loading -> {
                            userAdapter.userList = listOf()
                            progressBar.isVisible = true
                            userRecyclerView.isVisible = false
                        }
                    }
                }
            }
        }



         */

    }


    private fun setupRecyclerView(): UserAdapter {
        val usersRecyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val userAdapter = UserAdapter()
        usersRecyclerView.adapter = userAdapter
        return userAdapter
    }


}*/