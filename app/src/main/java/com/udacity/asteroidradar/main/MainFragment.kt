package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.AsteroidItemBinding
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import timber.log.Timber

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private var asteroidListAdapter: AsteroidListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel.asteroidList.observe(viewLifecycleOwner, Observer { asteroids ->
//            asteroids?.apply {
//                asteroidListAdapter?.asteroids = asteroids
//            }
//        })
        viewModel.asteroidListForDB.observe(viewLifecycleOwner) { asteroids ->
            Timber.i("asteroids count ${asteroids.size}")
            asteroids?.apply {
                asteroidListAdapter?.asteroids = asteroids
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        asteroidListAdapter = AsteroidListAdapter(AsteroidClick {
            this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
        })

        viewModel.todayImage.observe(viewLifecycleOwner) {
            Picasso.with(context).load(it.url)
                .into(binding.root.findViewById<ImageView>(R.id.activity_main_image_of_the_day))
        }
        setHasOptionsMenu(true)
        binding.root.findViewById<RecyclerView>(R.id.asteroid_recycler).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = asteroidListAdapter
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

class AsteroidClick(val block: (Asteroid) -> Unit) {

    fun onClick(asteroid: Asteroid) = block(asteroid)
}

class AsteroidListViewHolder(val viewDataBinding: AsteroidItemBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.asteroid_item
    }
}

class AsteroidListAdapter(val callback: AsteroidClick) :
    RecyclerView.Adapter<AsteroidListViewHolder>() {

    var asteroids: List<Asteroid> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidListViewHolder {
        val withDataBinding: AsteroidItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            AsteroidListViewHolder.LAYOUT,
            parent,
            false
        )
        return AsteroidListViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: AsteroidListViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.asteroid = asteroids[position]
            it.asteroidCallback = callback
        }
    }

    override fun getItemCount(): Int {
        return asteroids.size
    }
}