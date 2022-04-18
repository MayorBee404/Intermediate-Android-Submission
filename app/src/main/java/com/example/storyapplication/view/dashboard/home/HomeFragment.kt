package com.example.storyapplication.view.dashboard.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapplication.R
import com.example.storyapplication.ViewModelFactory
import com.example.storyapplication.databinding.ActivityDashboardBinding.inflate
import com.example.storyapplication.databinding.HomeFragmentBinding

class HomeFragment : Fragment() {

    private lateinit var factory: ViewModelFactory
    private val viewModel : HomeViewModel by activityViewModels{factory}
    private var _homebinding: HomeFragmentBinding? = null
    private val binding get() = _homebinding!!
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
        binding.refreshLayout.setOnRefreshListener {
            fetchUserStories()
        }
        fetchUserStories()
        initRecycler()


    }

    private fun fetchUserStories() {

        viewModel.getUserToken().observe(viewLifecycleOwner) {
            binding.refreshLayout.isRefreshing = true
            viewModel.getUserStories(it)
            Log.e("Home", "Token: $it")

        }
    }
    private fun initRecycler(){
        binding.rvStory.layoutManager = LinearLayoutManager(activity)
        adapter = ListStoryAdapter()
        viewModel.userStories.observe(viewLifecycleOwner){
            binding.refreshLayout.isRefreshing = false
            adapter.setData(it)
        }
        binding.rvStory.adapter = adapter

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
    }

}