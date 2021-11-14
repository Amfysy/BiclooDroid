package com.amfysy.bicloodroid.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.Observable
import androidx.databinding.ObservableList
import androidx.fragment.app.Fragment
import com.amfysy.bicloodroid.R
import com.amfysy.bicloodroid.databinding.MainFragmentBinding
import com.amfysy.bicloodroid.ui.shared.BiclooRecordViewModel
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.extension.style.layers.properties.generated.TextAnchor
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager

class MainFragment : Fragment() {

    private var annotationManager: PointAnnotationManager? = null
    private var mainFragmentBinding: MainFragmentBinding? = null
    private var viewModel: MainViewModel = MainViewModel()
    private val pinToRecordsMap = mutableMapOf<Long, BiclooRecordViewModel>()

    private var selectedPinId: Long? = null

    init {
        viewModel.biclooRecords.addOnListChangedCallback(object : ObservableList.OnListChangedCallback<ObservableList<BiclooRecordViewModel>>() {
            override fun onChanged(sender: ObservableList<BiclooRecordViewModel>?) = drawPins()
            override fun onItemRangeChanged(sender: ObservableList<BiclooRecordViewModel>?, positionStart: Int, itemCount: Int) = drawPins()
            override fun onItemRangeInserted(sender: ObservableList<BiclooRecordViewModel>?, positionStart: Int, itemCount: Int) = drawPins()
            override fun onItemRangeMoved(sender: ObservableList<BiclooRecordViewModel>?, fromPosition: Int, toPosition: Int, itemCount: Int) = drawPins()
            override fun onItemRangeRemoved(sender: ObservableList<BiclooRecordViewModel>?, positionStart: Int, itemCount: Int) = drawPins()
        })
        viewModel.selectedRecord.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (redrawSelectedPin()) return
            }
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

    private fun redrawSelectedPin(): Boolean {
        val manager = annotationManager ?: return true
        val oldSelectedRecord = selectedPinId?.let { pinToRecordsMap[it] }
        val newSelectedRecord = viewModel.selectedRecord.get()
        if (oldSelectedRecord == newSelectedRecord) {
            return true
        }

        selectedPinId?.let { id -> manager.annotations.firstOrNull { it.id == id } }?.run {
            manager.delete(this)
            pinToRecordsMap.remove(selectedPinId)
        }
        oldSelectedRecord?.run { drawBiclooPin(this, manager) }

        pinToRecordsMap.entries.firstOrNull { it.value == newSelectedRecord }?.key?.run {
            manager.annotations.firstOrNull { it.id == this }?.let { manager.delete(it) }
            pinToRecordsMap.remove(this)
        }
        newSelectedRecord?.run {
            selectedPinId = drawBiclooPin(this, manager)
        }
        return false
    }

    private fun drawPins() {
        val mapview = this.mainFragmentBinding?.mapView ?: return
        val pointAnnotationManager = this.annotationManager ?: mapview.annotations.createPointAnnotationManager(mapview)
            .also { manager ->
                this.annotationManager = manager
                manager.addClickListener { annotation ->
                    pinToRecordsMap[annotation.id]?.let(viewModel::pinClicked)
                    true
                }
            }

        pointAnnotationManager.deleteAll()
        pinToRecordsMap.clear()

        try {
            viewModel.biclooRecords.forEach {
                drawBiclooPin(it, pointAnnotationManager)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun drawBiclooPin(biclooRecordViewModel: BiclooRecordViewModel, pointAnnotationManager: PointAnnotationManager): Long? {
        val icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_bike_pin, null)
            ?.toBitmap(120, 120) ?: return null

        val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
            .withPoint(Point.fromLngLat(biclooRecordViewModel.biclooRecord.longitude, biclooRecordViewModel.biclooRecord.latitude))
            .withIconImage(icon)
            .withIconAnchor(IconAnchor.BOTTOM).let {
                if (biclooRecordViewModel == viewModel.selectedRecord.get()) {
                    it.withTextField(
                        """${biclooRecordViewModel.biclooRecord.stationName} 
                        ${
                            resources.getQuantityString(
                                R.plurals.n_available_places,
                                biclooRecordViewModel.biclooRecord.availablePlaces,
                                biclooRecordViewModel.biclooRecord.availablePlaces
                            )
                        } """
                    ).withTextAnchor(TextAnchor.BOTTOM)
                        .withTextOffset(listOf(0.0, -3.0))
                        .withTextHaloColor("rgba(255, 255, 255, 100)")
                        .withTextHaloWidth(100.0)
                } else {
                    it
                }
            }

        return pointAnnotationManager.create(pointAnnotationOptions).let {
            pinToRecordsMap[it.id] = biclooRecordViewModel
            it.id
        }
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}