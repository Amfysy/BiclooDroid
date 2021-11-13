package com.amfysy.bicloodroid.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.amfysy.bicloodroid.databinding.MainFragmentBinding
import com.mapbox.maps.Style

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var mainFragmentBinding: MainFragmentBinding
    private var viewModel: MainViewModel = MainViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.mainFragmentBinding = MainFragmentBinding.inflate(inflater)
        return mainFragmentBinding.apply {
            this.datacontext = viewModel
            this.mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS)
        }.root
    }
}