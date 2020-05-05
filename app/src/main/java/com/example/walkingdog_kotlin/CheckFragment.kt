package com.example.walkingdog_kotlin

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.fragment_check.*

class CheckFragment : Fragment() {

    var checkItemList : ArrayList<CheckItem> = arrayListOf(
        CheckItem("목줄"),
        CheckItem("식수"),
        CheckItem("입마개"),
        CheckItem("배변봉투")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_check, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity!!.window.statusBarColor = (ContextCompat.getColor(context!!, R.color.mainBlue))

        val checkItemAdapter = CheckListAdapter(context!!, checkItemList)
        checkListView.adapter = checkItemAdapter

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
            val add_check_btn = dialogView.findViewById<Button>(R.id.add_check_btn)

            val popup = builder.setView(dialogView).show()

            add_check_btn.setOnClickListener {
                if(dialogText.text.isNotBlank() && dialogText.text.isNotEmpty())
                    checkItemList.add(CheckItem(dialogText.text.toString()))

                popup.dismiss()
            }

            checkItemAdapter.notifyDataSetChanged()
        }


        //산책측정 액티비티로 넘어가는 함수
        switch_btn_to_walking.setOnClickListener {
            var intent = Intent(context, WalkingActivity::class.java)
            startActivity(intent)
        }


    }
}
