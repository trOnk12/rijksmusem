package com.example.maxdoroassigment.presentation

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.maxdoroassigment.R
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject


class MainActivity : FragmentActivity(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

//
//    lateinit var recyclerView: RecyclerView
//

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }
//    lateinit var itemAdapter: ArtListAdapter
//    lateinit var adapter: ConcatAdapter
//
//    lateinit var swipeToRefresh: SwipeRefreshLayout


//
//        recyclerView = findViewById(R.id.artList)
//
//        itemAdapter = ArtListAdapter(ArtComparator)
//
//        adapter = itemAdapter.withLoadStateHeaderAndFooter(
//            header = ArtListLoadStateAdapter(),
//            footer = ArtListLoadStateAdapter()
//        )
//
//        recyclerView.layoutManager =
//            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        recyclerView.adapter = adapter
//
//
//        swipeToRefresh = findViewById(R.id.swiperefresh)
//
//        swipeToRefresh.setOnRefreshListener {
//            itemAdapter.refresh()
//
//            swipeToRefresh.isRefreshing = false
//        }
//
//        viewModel.artListItemPagingData.observe(this, {
//            Log.d("TEST", "data $it")
//            itemAdapter.submitData(lifecycle, it)
//        })
}


