package com.example.walkingdog_kotlin.Post

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.walkingdog_kotlin.GlideApp
import com.example.walkingdog_kotlin.Post.Model.DeletePostModel
import com.example.walkingdog_kotlin.Post.Model.FeedContent
import com.example.walkingdog_kotlin.Post.Model.RouteModel
import com.example.walkingdog_kotlin.R
import kotlinx.android.synthetic.main.feed_item.*
import kotlinx.android.synthetic.main.map_popup.view.*
import net.daum.mf.map.api.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedAdaptor(
    val context: Context,
    val feedList: ArrayList<FeedContent>,
    val itemClick: (FeedContent) -> Unit
) :
    RecyclerView.Adapter<FeedAdaptor.Holder>() {

    inner class Holder(itemView: View?, itemClick: (FeedContent) -> Unit) :
        RecyclerView.ViewHolder(itemView!!) {
        val profileImg = itemView?.findViewById<ImageView>(R.id.myfeed_profile_img)
        val userName = itemView?.findViewById<TextView>(R.id.myfeed_nickname)
        val date = itemView?.findViewById<TextView>(R.id.dateTv)
        val location = itemView?.findViewById<TextView>(R.id.myfeed_locate_text)
        val dogName = itemView?.findViewById<TextView>(R.id.dogNameTv)
        val feedImg = itemView?.findViewById<ImageView>(R.id.feed_main_img)
        val explain = itemView?.findViewById<TextView>(R.id.explain_text)
        val walkingTime = itemView?.findViewById<TextView>(R.id.walkingTimeTv)
        val walkingDistance = itemView?.findViewById<TextView>(R.id.walkingDistanceTv)
        val walkingCal = itemView?.findViewById<TextView>(R.id.walkingCalory)
        val likes = itemView?.findViewById<TextView>(R.id.likesTv)
        val mapIcon = itemView?.findViewById<ImageView>(R.id.map_icon)


        fun bind(feedContent: FeedContent, context: Context) {
            if (feedContent.profileImg != "") {
                GlideApp.with(context)
                    .load("${context.getString(R.string.server_url)}/${feedContent.profileImg}")
                    .apply(RequestOptions.centerInsideTransform().override(500,500))
                    .into(profileImg!!)
            } else {
                profileImg?.setImageResource(R.mipmap.ic_launcher)
            }

            userName?.text = feedContent.userName
            date?.text = feedContent.date.toString()
            location?.text = feedContent.location
            dogName?.text = feedContent.dogName
            if (feedContent.uploadImg.isNotEmpty()) {
                feedContent.uploadImg.forEach(fun(imageUrl) {
                    GlideApp.with(context)
                        .load("${context.getString(R.string.server_url)}/${imageUrl}")
                        .apply(RequestOptions.centerInsideTransform())
                        .into(feedImg!!)
                })
            } else {
                feedImg?.setImageResource(R.drawable.dog03)
            }

            explain?.text = feedContent.explain
            walkingTime?.text = feedContent.time.toString()
            walkingDistance?.text = feedContent.distance.toString()
            walkingCal?.text = feedContent.calory.toString()
            likes?.text = feedContent.likes.toString()

            itemView.setOnClickListener {
                itemClick(feedContent)

                mapIcon?.setOnClickListener {
                    val mDialogView = LayoutInflater.from(context).inflate(R.layout.map_popup, null)
                    val mBuilder = AlertDialog.Builder(context!!).setView(mDialogView)
                    var mapView: MapView? = null

                    mBuilder.setOnCancelListener(object : DialogInterface.OnCancelListener {
                        override fun onCancel(dialog: DialogInterface) {
                            mapView = null
                        }
                    })

                    val mAlertDialog = mBuilder.show()
                    mDialogView.map_popup_delete_btn.setOnClickListener {
                        mAlertDialog.dismiss()
                    }

                    val walkingId = feedContent.walkingId
                    val routeRetrofit = PostRetrofitCreators(context).PostRetrofitCreator()
                    routeRetrofit.getRoute(walkingId).enqueue(object : Callback<RouteModel> {
                        override fun onFailure(call: Call<RouteModel>, t: Throwable) {
                            Log.d("DEBUG", " Route Retrofit failed!!")
                            Log.d("DEBUG", t.message)
                        }

                        override fun onResponse(
                            call: Call<RouteModel>,
                            response: Response<RouteModel>
                        ) {
                            val routes = response.body()?.route
                            val toiletLoc = response.body()?.toiletLoc
                            mapView = MapView(context)
                            if (routes!!.size == 0) {
                                Toast.makeText(context, "산책 경로가 존재하지 않습니다.", Toast.LENGTH_LONG)
                                    .show()
                                mAlertDialog.dismiss()
                            }
                            val mapViewContainer: ViewGroup =
                                mDialogView?.findViewById<View>(R.id.map_layout) as ViewGroup
                            mapViewContainer.addView(mapView)

                            val polyline = MapPolyline()

                            polyline.lineColor = Color.argb(255, 103, 114, 241)

                            routes.forEach(fun(route) {
                                polyline.addPoint(
                                    MapPoint.mapPointWithGeoCoord(
                                        route.lat.toDouble(),
                                        route.lon.toDouble()
                                    )
                                )
                            })
                            mapView!!.addPolyline(polyline)
                            // 시작 지점 마커
                            val startMarker = MapPOIItem()
                            startMarker.itemName = ""
                            startMarker.mapPoint = polyline.getPoint(0)
                            startMarker.markerType = MapPOIItem.MarkerType.CustomImage
                            startMarker.customImageResourceId =
                                R.drawable.start_walking_icon
                            startMarker.setCustomImageAnchor(0.5f, 0.5f)
                            startMarker.isCustomImageAutoscale = false
                            startMarker.isShowCalloutBalloonOnTouch = false
                            mapView!!.addPOIItem(startMarker)

                            // 종료 지점 마커
                            val finishMarker = MapPOIItem()
                            finishMarker.itemName = ""
                            finishMarker.mapPoint = polyline.getPoint(polyline.pointCount - 1)
                            finishMarker.markerType = MapPOIItem.MarkerType.CustomImage
                            finishMarker.customImageResourceId =
                                R.drawable.labrador_icon
                            finishMarker.setCustomImageAnchor(0.5f, 0.5f)
                            finishMarker.isShowCalloutBalloonOnTouch = false
                            mapView!!.addPOIItem(finishMarker)

                            // 배변 활동 마커
                            val toiletMarker: MapPOIItem = MapPOIItem()
                            toiletMarker.itemName = ""
                            toiletMarker.isShowCalloutBalloonOnTouch = false
                            toiletMarker.markerType = MapPOIItem.MarkerType.CustomImage
                            toiletMarker.customImageResourceId =
                                R.drawable.toilet_activity
                            toiletMarker.setCustomImageAnchor(0.5f, 1.0f)
                            toiletLoc!!.forEach(fun(loc) {
                                toiletMarker.mapPoint = MapPoint.mapPointWithGeoCoord(
                                    loc.lat.toDouble(),
                                    loc.lon.toDouble()
                                )
                                mapView!!.addPOIItem(toiletMarker)
                            })

                            // 화면 중앙 정렬
                            val mapPointBounds = MapPointBounds(polyline.mapPoints)
                            mapView!!.moveCamera(
                                CameraUpdateFactory.newMapPointBounds(
                                    mapPointBounds,
                                    100
                                )
                            )


                        }
                    })
                }
            }
        }

        fun deletePost(postId: String) {
            val pref = context!!.getSharedPreferences("pref", Context.MODE_PRIVATE)
            val userToken = pref.getString("userToken", "")
            val deletePostRetrofit = PostRetrofitCreators(context).PostRetrofitCreator()
            deletePostRetrofit.deletePost(userToken!!, postId)
                .enqueue(object : Callback<DeletePostModel> {
                    override fun onFailure(call: Call<DeletePostModel>, t: Throwable) {
                        Log.d("DEBUG", " Delete Post Retrofit failed!!")
                        Log.d("DEBUG", t.message)
                    }

                    override fun onResponse(
                        call: Call<DeletePostModel>,
                        response: Response<DeletePostModel>
                    ) {
                        val error = response.body()?.error
                        Log.d("ERROR", "$error")
                    }
                })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.feed_item, parent, false)
        return Holder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return feedList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(feedList[position], context)
    }
}