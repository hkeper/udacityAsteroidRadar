package com.udacity.asteroidradar.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidsFilter

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = AsteroidsAdapter(AsteroidsListener {
                asteroid ->  viewModel.onAsteroidClicked(asteroid)
        })
        binding.asteroidRecycler.adapter = adapter

        viewModel.asteroidsList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.navigateToDetails.observe(viewLifecycleOwner, Observer {asteroid ->
            asteroid?.let {
                this.findNavController().navigate(
                    MainFragmentDirections.actionShowDetail(asteroid))
                viewModel.onDetailsNavigated()
            }
        })

        binding.activityMainImageOfTheDay.setOnClickListener {
            if (viewModel.pictureOfDay.value?.mediaType=="video") {
                val openURL = Intent(Intent.ACTION_VIEW)
                val httpUri = Uri.parse(viewModel.pictureOfDay.value?.url)
                openURL.data = httpUri
                startActivity(openURL)
            }
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.updateFilter(
            when (item.itemId) {
                R.id.show_saved_menu -> AsteroidsFilter.SHOW_SAVED
                R.id.show_today_menu-> AsteroidsFilter.SHOW_TODAY
                R.id.show_week_menu-> AsteroidsFilter.SHOW_WEEK
                else -> AsteroidsFilter.SHOW_WEEK
            }
        )
        return true
    }

}
