package com.example.walkingdog_kotlin.Walking

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.walkingdog_kotlin.Animal.AddPet
import com.example.walkingdog_kotlin.MainActivity
import com.example.walkingdog_kotlin.R
import com.example.walkingdog_kotlin.Walking.Model.CheckItem
import com.example.walkingdog_kotlin.Walking.Model.MyPetListModel
import com.example.walkingdog_kotlin.Walking.Model.WeatherAPIModel
import com.example.walkingdog_kotlin.Walking.Model.SelectDog
import kotlinx.android.synthetic.main.fragment_check.*
import kotlinx.android.synthetic.main.select_dog_popup.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckFragment : Fragment() {

    var checkItemList : ArrayList<CheckItem> = arrayListOf(
    )

    var selectDogList : ArrayList<SelectDog> = arrayListOf(
    )


    var location_weather: String? = ""        //현재 위치의 날씨를 저장할 변수
    var location_temp:String? = ""          //현재 위치의 온도를 저장할 변수
    var test: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_check, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = context!!.getSharedPreferences("pref", Context.MODE_PRIVATE)
        val edit = pref.edit()

        val userCheckList = pref.getString("checkList", "")
        Log.d("체크리스트", userCheckList)
        val checkList = userCheckList.split("//")
        checkList.forEach(fun(check) {
            if (check != "") {
                checkItemList.add(CheckItem(check))
            }
        })
        val checkItemAdapter = CheckListAdapter(context!!, checkItemList)
        checkListView.adapter = checkItemAdapter


        val lat: String? = pref.getString("latitude", "0")
        val lon: String? = pref.getString("longitude", "0")
        Log.d("location", lat+"  "+lon)
        val appid: String = getString(R.string.openwheather_api_key)

        var location_addressLocatlity:String? = pref.getString("addressLocality", "대한민국")//현재 위치의 도시를 저장

        // 날씨 받아오는 Retrofit
        val weatherRetrofit = WeatherRetrofitCreators().WeatherRetrofitCreator()
        weatherRetrofit.getWeatherInfo(lat!!, lon!!, appid).enqueue(object : Callback<WeatherAPIModel> {
            override fun onFailure(call: Call<WeatherAPIModel>, t: Throwable) {
                Log.d("TAG", "Weather Retrofit Failed!!")
                Log.d("TAG", t.message)
            }

            override fun onResponse(call: Call<WeatherAPIModel>,response: Response<WeatherAPIModel>) {
//                val fragment = fragmentManager!!.getFragment

                val weather = response.body()?.weather
                val main = response.body()?.main

                Log.d("TAG", weather!![0].main)

                Log.d("TAG", (main!!.temp-273).toString())

                location_weather = weather[0].main
                location_temp = (main.temp-273).toInt().toString()

                if (weather_tv == null) {
                    return
                }
                weather_tv.text = weather[0].main
                temporature_tv.text = location_temp
                location_tv.text = location_addressLocatlity

                when(weather_tv.text?.toString()) {
                    "Thunderstorm" -> {
                        weather_img.setImageResource(R.drawable.thunderstorm)
                        weather_tv.text = "낙뢰"
                    }
                    "Rain" -> {
                        weather_img.setImageResource(R.drawable.rain)
                        weather_tv.text ="비"
                    }
                    "Snow" -> {
                        weather_img.setImageResource(R.drawable.snow)
                        weather_tv.text ="눈"
                    }
                    "Clear" -> {
                        weather_img.setImageResource(R.drawable.clear)
                        weather_tv.text ="맑음"
                    }
                    "Clouds" -> {
                        weather_img.setImageResource(R.drawable.cloud)
                        weather_tv.text = "흐림"
                    }
                    else -> {
                        weather_img.setImageResource(R.drawable.mist)
                        weather_tv.text = "안개"
                    }
                }
            }
        })

        //체크리스트 목록을 삭제하는 함수
        sub_item_btn.setOnClickListener {

            /*----------커스텀 Dialog 띄우는 부분-------------*/
            val builder = AlertDialog.Builder(context)
            val dialogView = layoutInflater.inflate(R.layout.delete_checklist_popup, null)
            val dialogText = dialogView.findViewById<EditText>(R.id.delete_item_editText)
            val delete_check_btn = dialogView.findViewById<Button>(R.id.delete_check_btn)

            val popup = builder.setView(dialogView).show()
            /*-------------------------------------------*/

            var dIndex:Int = 9999   //더미 값

            delete_check_btn.setOnClickListener {
                var usl: String? = pref.getString("checkList", "")
                usl = usl!!.replace("//${dialogText.text}", "")
                val edit = pref.edit()
                edit.remove("checkList")
                edit.putString("checkList", usl)
                edit.apply()

                //삭제하려는 텍스트와 같은 텍스트를 갖는 요소의 인덱스를 찾고 저장
                for(i in 0 until checkItemList.size) {
                    if(dialogText.text.toString() == checkItemList[i].item) {
                        dIndex = i
                    }
                }

                //찾은 인덱스를 배열에서 삭제
                if(dIndex != 9999) {
                    checkItemList.removeAt(dIndex)
                }

                //변경된 값을 다시 리스트에 동기화
                checkItemAdapter.notifyDataSetChanged()

                //팝업창 닫기
                popup.dismiss()
            }
        }

        //체크리스트 목록을 추가하는 함수
        add_item_btn.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val dialogView = layoutInflater.inflate(R.layout.add_checklist_popup, null)
            val dialogText = dialogView.findViewById<EditText>(R.id.add_item_editText)
            val addCheckBtn = dialogView.findViewById<Button>(R.id.add_check_btn)

            val popup = builder.setView(dialogView).show()

            addCheckBtn.setOnClickListener {
                if(dialogText.text.isNotBlank() && dialogText.text.isNotEmpty()) {


                    val usl: String? = pref.getString("checkList", "")
                    val edit = pref.edit()
                    edit.putString("checkList", usl + "//" + dialogText.text.toString())
                    edit.apply()

                    checkItemList.add(
                        CheckItem(
                            dialogText.text.toString()
                        )
                    )
                }
                popup.dismiss()
            }

            checkItemAdapter.notifyDataSetChanged()
        }

        //산책측정 액티비티로 넘어가는 함수
        switch_btn_to_walking.setOnClickListener {
            val pref = context!!.getSharedPreferences("pref", Context.MODE_PRIVATE)
            val userToken = pref.getString("userToken", "")
            var myPetInfo = mutableMapOf<String, Double>()
            var readyToDeletePet = mutableMapOf<String, Double>()

            // 등록된 애견 정보 가져오기
            val myPetRetrofit = WalkingRetrofitCreators(context!!).WalkingRetrofitCreator()
            myPetRetrofit.getMyPet(userToken!!).enqueue(object : Callback<MyPetListModel> {
                override fun onFailure(call: Call<MyPetListModel>, t: Throwable) {
                    Log.d("DEBUG", "My Pet Retrofit failed!!")
                    Log.d("DEBUG", t.message)
                    Toast.makeText(context!!, "애견 목록 수신에 실패하였습니다. 네트워크를 확인해주세요.", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<MyPetListModel>, response: Response<MyPetListModel>) {
                    val error = response.body()?.error
                    val myPetList = response.body()?.myPetList
                    if (myPetList!!.size == 0) {
                        var intent = Intent(context, AddPet::class.java)
                        startActivity(intent)
                        Toast.makeText(context!!, "한 마리 이상의 애견을 체크해주세요.", Toast.LENGTH_LONG).show()
                        return
                    }
                    selectDogList.clear()
                    myPetList!!.forEach(fun(pet) {
                        selectDogList.add(SelectDog(pet.animalName))
                        myPetInfo[pet.animalName] = pet.weight
                        readyToDeletePet[pet.animalName] = pet.weight
                    })
                    // 애견 체크 페이지
                    val wDialogView = LayoutInflater.from(context).inflate(R.layout.select_dog_popup, null)
                    val wBuilder = androidx.appcompat.app.AlertDialog.Builder(context!!).setView(wDialogView)
                    val wAlertDialog = wBuilder.show()
                    val selectDogAdapter = SelectDogAdapter(context!!, selectDogList)

                    wDialogView.select_dog_listView.adapter = selectDogAdapter
                    wDialogView.selectDog_popup_delete_btn.setOnClickListener {
                        wAlertDialog.dismiss()
                    }

                    wDialogView.selectDog_popup_complete_btn.setOnClickListener {
                        val checkedName = selectDogAdapter.getCheckedName()
                        if (checkedName.size == 0) {
                            Toast.makeText(context!!, "한 마리 이상의 애견을 체크해주세요.", Toast.LENGTH_LONG).show()
                        } else {
                            checkedName.forEach(fun(name) {
                                readyToDeletePet.remove(name)
                            })
                            readyToDeletePet.keys.forEach(fun(name) {
                                myPetInfo.remove(name)
                            })

                            // 인텐트에 애견 정보 매달기
                            val animalName = arrayListOf<String>()
                            val animalWeight = arrayListOf<String>()
                            myPetInfo.forEach(fun(name, weight) {
                                animalName.add(name)
                                animalWeight.add("$weight")
                            })
                            val intent = Intent(context, WalkingActivity::class.java)
                            intent.putExtra("animalName", animalName)
                            intent.putExtra("animalWeight", animalWeight)
                            startActivity(intent)
                            wAlertDialog.dismiss()
                            (activity as MainActivity).finish()
                        }
                    }
                }
            })
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity!!.window.statusBarColor = (ContextCompat.getColor(context!!,
            R.color.green2
        ))



    }
}
