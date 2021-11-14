package com.amfysy.bicloodroid.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.ObservableList
import androidx.fragment.app.Fragment
import com.amfysy.bicloodroid.R
import com.amfysy.bicloodroid.databinding.MainFragmentBinding
import com.amfysy.bicloodroid.ui.shared.BiclooRecordViewModel
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager

class MainFragment : Fragment() {

    private var annotationManager: PointAnnotationManager? = null
    private var mainFragmentBinding: MainFragmentBinding? = null
    private var viewModel: MainViewModel = MainViewModel()

    init {
        viewModel.biclooRecords.addOnListChangedCallback(object : ObservableList.OnListChangedCallback<ObservableList<BiclooRecordViewModel>>() {
            override fun onChanged(sender: ObservableList<BiclooRecordViewModel>?) = drawPins()
            override fun onItemRangeChanged(sender: ObservableList<BiclooRecordViewModel>?, positionStart: Int, itemCount: Int) = drawPins()
            override fun onItemRangeInserted(sender: ObservableList<BiclooRecordViewModel>?, positionStart: Int, itemCount: Int) = drawPins()
            override fun onItemRangeMoved(sender: ObservableList<BiclooRecordViewModel>?, fromPosition: Int, toPosition: Int, itemCount: Int) = drawPins()
            override fun onItemRangeRemoved(sender: ObservableList<BiclooRecordViewModel>?, positionStart: Int, itemCount: Int) = drawPins()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return MainFragmentBinding.inflate(inflater).apply {
            mainFragmentBinding = this
            this.datacontext = viewModel
            this.mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS)
        }.root
    }

    private fun drawPins() {
        val mapview = this.mainFragmentBinding?.mapView ?: return
        val pointAnnotationManager = this.annotationManager ?: mapview.annotations.createPointAnnotationManager(mapview)
            .also { this.annotationManager = it }

        pointAnnotationManager.deleteAll()
        try {
            viewModel.biclooRecords.forEach {
                if (drawBiclooPin(it, pointAnnotationManager)) return
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun drawBiclooPin(biclooRecordViewModel: BiclooRecordViewModel, pointAnnotationManager: PointAnnotationManager): Boolean {
        val icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_bike_pin, null)
            ?.toBitmap(120, 120) ?: return true

        val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
            .withPoint(Point.fromLngLat(biclooRecordViewModel.biclooRecord.longitude, biclooRecordViewModel.biclooRecord.latitude))
            .withIconImage(icon)
            .withIconAnchor(IconAnchor.BOTTOM)

        pointAnnotationManager.create(pointAnnotationOptions)
        return false
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}