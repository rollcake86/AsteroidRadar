package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        val application = requireNotNull(this.activity).application
        val dataSource = AsteroidDatabase.getInstance(application).asteroidDatabaseDao

        setHasOptionsMenu(true)

        binding.root.findViewById<RecyclerView>(R.id.asteroid_recycler).apply {
            layoutManager = LinearLayoutManager(context)
            adapter =
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}

class AsteroidClick(val block: (Asteroid) -> Unit){

    fun onClick(asteroid: Asteroid) = block(asteroid)
}

class AsteroidListViewHolder(val viewDataBinding: ViewDataBinding)

class AsteroidListAdapter(val callback: AsteroidClick) : RecyclerView.Adapter<