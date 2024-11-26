package com.example.main_map.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.main_map.R
import com.example.main_map.databinding.FragmentMainMapBinding
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingRouter
import com.yandex.mapkit.directions.driving.DrivingRouterType
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PolylineMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.network.NetworkError

class MainMapFragment : Fragment() {
    private var _mBinding: FragmentMainMapBinding? = null
    private val mBinding get() = _mBinding!!
    val vm: MainViewModel by viewModels()

    private lateinit var mapView: MapView
    private lateinit var map: Map

    private val inputListener = object : InputListener {
        override fun onMapTap(map: Map, point: Point) = Unit

        override fun onMapLongTap(map: Map, point: Point) {
            routePoints = routePoints + point
        }
    }

    private val drivingRouteListener = object : DrivingSession.DrivingRouteListener {
        override fun onDrivingRoutes(drivingRoutes: MutableList<DrivingRoute>) {
            routes = drivingRoutes
        }

        override fun onDrivingRoutesError(error: Error) {
            when (error) {
                is NetworkError -> requireContext().showToast("Routes request error due network issues")
                else -> requireContext().showToast("Routes request unknown error")
            }
        }
    }

    private var routePoints = emptyList<Point>()
        set(value) {
            field = value
            onRoutePointsUpdate()
        }

    private var routes = emptyList<DrivingRoute>()
        set(value) {
            field = value
            onRoutesUpdated()
        }

    private lateinit var drivingRouter: DrivingRouter
    private var drivingSession: DrivingSession? = null
    private lateinit var placemarksCollection: MapObjectCollection
    private lateinit var routesCollection: MapObjectCollection




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(this.context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentMainMapBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initStartSettings()

        setPin(Point(25.196141, 55.278543))

    }

    private fun initStartSettings() {
        mapView = mBinding.mapview
        map = mapView.mapWindow.map
        map.addInputListener(inputListener)

        placemarksCollection = map.mapObjects.addCollection()
        routesCollection = map.mapObjects.addCollection()

        drivingRouter =
            DirectionsFactory.getInstance().createDrivingRouter(DrivingRouterType.COMBINED)

        mBinding.clearBtn.setOnClickListener {
            routePoints = emptyList()
        }

        val map = mapView.mapWindow.map
        map.move(START_POSITION)

    }

    private fun setPin(point: Point) {
        val imageProvider = ImageProvider.fromResource(context, R.drawable.pin)

        val placemark = mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = point
            setIcon(imageProvider)
        }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        _mBinding = null
        super.onDestroyView()
    }

    private fun onRoutePointsUpdate() {
        placemarksCollection.clear()

        if (routePoints.isEmpty()) {
            drivingSession?.cancel()
            routes = emptyList()
            return
        }

        val imageProvider = ImageProvider.fromResource(context, R.drawable.pin)

        routePoints.forEach {
            placemarksCollection.addPlacemark().apply {
                geometry = it
                setIcon(imageProvider, IconStyle().apply {
                    scale = 0.5f
                    zIndex = 20f
                })
            }
        }

        if (routePoints.size < 2) return

        val requestPoints = buildList {
            add(RequestPoint(routePoints.first(),  RequestPointType.WAYPOINT, null, null))
            addAll(
                routePoints.subList(1, routePoints.size - 1)
                    .map { RequestPoint(it,  RequestPointType.VIAPOINT, null, null) }
            )
            add(RequestPoint(routePoints.last(),  RequestPointType.WAYPOINT, null, null))
        }

        val drivingOptions = DrivingOptions()
        val vehicleOptions = VehicleOptions()

        drivingSession = drivingRouter.requestRoutes(
            requestPoints,
            drivingOptions,
            vehicleOptions,
            drivingRouteListener
        )
    }

    private fun onRoutesUpdated() {
        routesCollection.clear()

        if (routes.isEmpty()) return

        routes.forEachIndexed { index ,route ->
            routesCollection.addPolyline(route.geometry).apply {
                if (index == 0) styleMainRoute() else styleAlternativeRoute()
            }
        }

    }

    private fun PolylineMapObject.styleMainRoute() {
        zIndex = 10f
        setStrokeColor(ContextCompat.getColor(requireContext(), R.color.gray))
        strokeWidth = 5f
        outlineColor = ContextCompat.getColor(requireContext(), R.color.black)
        outlineWidth = 3f
    }

    private fun PolylineMapObject.styleAlternativeRoute() {
        zIndex = 5f
        setStrokeColor(ContextCompat.getColor(requireContext(), R.color.light_blue))
        strokeWidth = 4f
        outlineColor = ContextCompat.getColor(requireContext(), R.color.black)
        outlineWidth = 2f
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MainMapFragment()

        private val START_POSITION = CameraPosition(Point(59.941282, 30.308046), 13.0f, 0f, 0f)
    }


    fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()
    }

}

