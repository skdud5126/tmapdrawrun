//package com.example.tmapdrawrun
//// 기본 tmap 지도
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import com.skt.tmap.TMapView
//import com.example.tmapdrawrun.databinding.ActivityMainBinding
//
//class MainActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityMainBinding  // ✅ lateinit으로 선언
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivityMainBinding.inflate(layoutInflater)  // ✅ 바인딩 초기화
//        setContentView(binding.root)
//
//        initTMapView()  // ✅ TMapView 초기화 함수 호출
//    }
//
//    private fun initTMapView() {
//        val tMapView = TMapView(this)
//        binding.tmapViewContainer.addView(tMapView)  // ✅ 바인딩 사용
//
//        tMapView.setSKTMapApiKey("api_key")
//        tMapView.setOnMapReadyListener {
//            // TODO: 맵 로딩 완료 후 구현
//        }
//    }
//}

//package com.example.tmapdrawrun
//
//import android.Manifest
//import android.content.pm.PackageManager
//import android.location.Location
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import com.skt.tmap.TMapGpsManager
//import com.skt.tmap.TMapView
//import com.example.tmapdrawrun.databinding.ActivityMainBinding
//
//class MainActivity : AppCompatActivity(), TMapGpsManager.TMapLocationCallback {
//
//    private lateinit var binding: ActivityMainBinding
//    private lateinit var tMapView: TMapView
//    private lateinit var tMapGps: TMapGpsManager
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        initTMapView()
//        requestLocationPermission() // ✅ 위치 권한 요청
//    }
//
//    private fun initTMapView() {
//        tMapView = TMapView(this)
//        binding.tmapViewContainer.addView(tMapView)
//
//        tMapView.setSKTMapApiKey("api_key")
//
//        tMapView.setOnMapReadyListener {
//            enableGPS()
//        }
//    }
//
//    private fun enableGPS() {
//        tMapGps = TMapGpsManager(this).apply {
//            minTime = 1000 // 1초마다 업데이트
//            minDistance = 5f // 5미터 이동 시 업데이트
//            setProvider(TMapGpsManager.GPS_PROVIDER) // ✅ 최신 SDK에서 setProvider() 사용
//        }
//
//        tMapGps.setLocationCallback(this) // ✅ 위치 업데이트 콜백 설정
//        tMapGps.openGps() // ✅ GPS 활성화
//
//        val lastKnownLocation = tMapGps.location // ✅ 최신 SDK에서 위치 가져오기
//        lastKnownLocation?.let {
//            tMapView.setCenterPoint(it.longitude, it.latitude)
//            tMapView.setZoomLevel(15)
//        }
//    }
//
//    // ✅ 현재 위치가 변경될 때마다 호출됨
//    override fun onLocationChange(location: Location) {
//        tMapView.setCenterPoint(location.longitude, location.latitude)
//    }
//
//    private fun requestLocationPermission() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(
//                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
//            )
//        }
//    }
//}



//package com.example.tmapdrawrun
//
//import android.Manifest
//import android.content.pm.PackageManager
//import android.location.Location
//import android.os.Bundle
//import android.util.Log
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import com.example.tmapdrawrun.databinding.ActivityMainBinding
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationServices
//import com.skt.tmap.TMapPoint
//import com.skt.tmap.TMapView
//import com.skt.tmap.overlay.TMapMarkerItem
//
//class MainActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityMainBinding
//    private lateinit var tMapView: TMapView
//    private lateinit var fusedLocationClient: FusedLocationProviderClient
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        initTMapView()
//
//        // FusedLocationProviderClient 초기화
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//
//        // 위치 권한 요청 및 현재 위치 가져오기
//        requestLocationPermission()
//    }
//
//    private fun initTMapView() {
//        tMapView = TMapView(this)
//        binding.tmapViewContainer.addView(tMapView)
//
//        tMapView.setSKTMapApiKey("api_key")
//
//        // ✅ TMap이 완전히 로드된 후에만 위치 요청 실행
//        tMapView.setOnMapReadyListener {
//            Log.d("TMap", "TMap 로딩 완료")
//            getCurrentLocation()  // ✅ 여기서 위치 요청 실행
//        }
//    }
//
//    private fun requestLocationPermission() {
//        val locationPermissionRequest = registerForActivityResult(
//            ActivityResultContracts.RequestMultiplePermissions()
//        ) { permissions ->
//            val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
//            if (granted) {
//                Log.d("Permission", "위치 권한 허용됨")
//                getCurrentLocation()
//            } else {
//                Log.e("Permission", "위치 권한이 거부되었습니다.")
//            }
//        }
//
//        // 권한이 없으면 요청
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            locationPermissionRequest.launch(
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
//            )
//        } else {
//            getCurrentLocation()
//        }
//    }
//
//    private fun getCurrentLocation() {
//        // ✅ TMapView가 초기화되지 않으면 실행하지 않음
//        if (!::tMapView.isInitialized) {
//            Log.e("TMap", "TMapView가 아직 초기화되지 않았습니다.")
//            return
//        }
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Log.e("Location", "위치 권한 없음")
//            return
//        }
//
//        fusedLocationClient.lastLocation
//            .addOnSuccessListener { location: Location? ->
//                if (location != null) {
//                    val latitude = location.latitude
//                    val longitude = location.longitude
//                    Log.d("Location", "현재 위치: 위도=$latitude, 경도=$longitude")
//
//                    addMarker(latitude, longitude)  // ✅ 위치가 있을 때만 실행
//                } else {
//                    Log.e("Location", "위치를 가져올 수 없습니다. GPS 활성화 확인 필요")
//                }
//            }
//            .addOnFailureListener { e ->
//                Log.e("Location", "위치 가져오기 실패: ${e.message}")
//            }
//    }
//
//    private fun addMarker(latitude: Double, longitude: Double) {
//        // ✅ TMapView가 null이면 실행하지 않음
//        if (!::tMapView.isInitialized) {
//            Log.e("TMap", "TMapView가 초기화되지 않았습니다. 마커 추가 중단.")
//            return
//        }
//
//        val tMapPoint = TMapPoint(latitude, longitude)
//        val markerItem = TMapMarkerItem()
//
//        markerItem.tMapPoint = tMapPoint
//        markerItem.name = "현재 위치"
//        markerItem.setCanShowCallout(true)
//
//        // ✅ TMapView가 정상적으로 초기화된 후 마커 추가
//        tMapView.addTMapMarkerItem("myLocation", markerItem)
//
//        // ✅ 현재 위치로 지도 이동
//        tMapView.setCenterPoint(longitude, latitude)
//    }
//}

package com.example.tmapdrawrun

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.skt.tmap.TMapView
import com.example.tmapdrawrun.databinding.ActivityMainBinding
import android.location.LocationListener
import android.location.LocationManager
import android.content.Context
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var tMapView: TMapView
    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTMapView()
        requestLocationUpdates()
    }

    private fun initTMapView() {
        tMapView = TMapView(this)
        binding.tmapViewContainer.addView(tMapView)

        tMapView.setSKTMapApiKey("api_key")
        tMapView.setOnMapReadyListener {
            // 맵이 로딩된 후 현재 위치를 업데이트
            requestLocationUpdates()
        }
    }

    private fun requestLocationUpdates() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                1001
            )
            return
        }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000L, // 1초마다 업데이트
            1f, // 1미터 이동 시 업데이트
            object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    updateMapLocation(location.latitude, location.longitude)
                }

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            }
        )
    }

    private fun updateMapLocation(latitude: Double, longitude: Double) {
        tMapView.setLocationPoint(longitude, latitude) // 현재 위치를 지도에 설정
        tMapView.setCenterPoint(longitude, latitude) // 지도를 현재 위치로 이동
        Toast.makeText(this, "현재 위치: $latitude, $longitude", Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates()
            } else {
                Toast.makeText(this, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


