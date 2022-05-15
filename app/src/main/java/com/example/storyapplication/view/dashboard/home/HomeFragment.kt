package com.example.storyapplication.view.dashboard.home

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapplication.R
import com.example.storyapplication.ViewModelFactory
import com.example.storyapplication.databinding.HomeFragmentBinding

class HomeFragment : Fragment() {

    private lateinit var factory: ViewModelFactory
    private val viewModel : HomeViewModel by activityViewModels{factory}
    private var _homebinding: HomeFragmentBinding? = null
    private val binding get() = _homebinding!!
    private var hideNavView = false
    private lateinit var adapter : ListStoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _homebinding = HomeFragmentBinding.inflate(layoutInflater,container,false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)
        factory = ViewModelFactory.getInstance(requireActivity())
        binding.refreshLayout.isRefreshing = true
        binding.refreshLayout.setOnRefreshListener {
            adapter.refresh()
        }
        fetchUserStories()
        initAction()
        val navView = requireActivity().findViewById<View>(R.id.nav_view)
        if (navView != null) {
            hideShowBottomNavigation(navView)
        }


    }
    private fun hideShowBottomNavigation(navView: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.rvStory.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                val height = (navView.height + 32).toFloat()

                if (!hideNavView && scrollY > oldScrollY) {
                    hideNavView = true
                    ObjectAnimator.ofFloat(navView, "translationY", 0f, height).apply {
                        duration = 200
                        start()
                    }
                }

                if (hideNavView && scrollY < oldScrollY) {
                    hideNavView = false
                    ObjectAnimator.ofFloat(navView, "translationY", height, 0f).apply {
                        duration = 200
                        start()
                    }
                }
            }
        }
    }

    private fun fetchUserStories() {

        viewModel.getUserToken().observe(viewLifecycleOwner) {
            binding.refreshLayout.isRefreshing = true
            viewModel.getUserStories(it)
            initRecycler()
            Log.e("Home", "Token: $it")

        }
    }
    private fun initRecycler(){
        binding.rvStory.layoutManager = LinearLayoutManager(activity)
        adapter = ListStoryAdapter()
        viewModel.userStories.observe(viewLifecycleOwner){
            binding.refreshLayout.isRefreshing = false
            adapter.submitData(lifecycle,it)
        }
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateListStoryAdapter { adapter.retry() }
        )

    }
    private fun initAction(){
        binding.toolbar.apply {
            inflateMenu(R.menu.nav_setting)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.navigation_setting -> {
                        findNavController().navigate(R.id.action_navigation_home_to_settingFragment)
                        true

                    }
                    else -> false
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _homebinding = null
        adapter.submitData(lifecycle, PagingData.empty())
    }

}