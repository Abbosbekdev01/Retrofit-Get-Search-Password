package uz.abbosbek.retrofitlesson_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView.OnQueryTextListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import uz.abbosbek.retrofitlesson_2.adapters.ProductAdapter
import uz.abbosbek.retrofitlesson_2.databinding.ActivityMainBinding
import uz.abbosbek.retrofitlesson_2.retrofit.AuthRequest
import uz.abbosbek.retrofitlesson_2.retrofit.MainApi
import uz.abbosbek.retrofitlesson_2.retrofit.User

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.title = "Hello "

        adapter = ProductAdapter()
        binding.rv.adapter = adapter

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dummyjson.com").client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val mainApi = retrofit.create(MainApi::class.java)


        var user: User? = null
        CoroutineScope(Dispatchers.IO).launch {
            user = mainApi.auth(
                AuthRequest(
                    "kminchelle",
                    "0lelplR"
                )
            )
            runOnUiThread {
                supportActionBar?.title = user?.firstName
            }
        }

        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                CoroutineScope(Dispatchers.IO).launch {
                    val list = newText?.let { mainApi.getProductsByNameAuth(user?.token ?: "", it) }

                    runOnUiThread {
                        binding.apply {
                            adapter.submitList(list?.products)
                        }
                    }
                }
                return true
            }
        })

//        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
//
//            //todo: <-onQueryTextSubmit-> bu funksiya search bosilgandan keyin ishlaydi
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return true
//            }
//
//            //todo: <-onQueryTextChange-> bu funksiya searchga yozish boshlangan payitdan qidirishni boshlaydi
//            override fun onQueryTextChange(text: String?): Boolean {
//                CoroutineScope(Dispatchers.IO).launch {
//                    val list = text?.let { mainApi.getProductsByName(it) }
//
//                    runOnUiThread {
//                        binding.apply {
//                            adapter.submitList(list?.products)
//                        }
//                    }
//                }
//                return true
//            }
//        })
    }
}