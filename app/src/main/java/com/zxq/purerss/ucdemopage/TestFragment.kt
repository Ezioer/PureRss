package com.zxq.purerss.ucdemopage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zxq.purerss.R
import com.zxq.purerss.ui.wxboom.ImMsgInfo
import com.zxq.purerss.ui.wxboom.WxBoomAdapter
import kotlinx.android.synthetic.main.fragment_test.*

/**
 *  created by xiaoqing.zhou
 *  on 2021/10/14
 *  fun
 */
class TestFragment : Fragment() {
    companion object {
        fun newInstance(): TestFragment {
            return TestFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_test, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var list = mutableListOf<ImMsgInfo>()
        list.add(ImMsgInfo(0, "\uD83D\uDCA9"))
        list.add(ImMsgInfo(0, "\uD83D\uDCA9"))
        list.add(ImMsgInfo(0, "\uD83D\uDCA9"))
        list.add(ImMsgInfo(1, "\uD83D\uDCA3"))
        list.add(ImMsgInfo(1, "\uD83D\uDCA3"))
        list.add(ImMsgInfo(1, "\uD83D\uDCA3"))
        list.add(ImMsgInfo(1, "\uD83D\uDCA3"))
        list.add(ImMsgInfo(1, "\uD83D\uDCA3"))
        list.add(ImMsgInfo(1, "\uD83D\uDCA3"))
        list.add(ImMsgInfo(1, "\uD83D\uDCA3"))
        list.add(ImMsgInfo(1, "\uD83D\uDCA3"))
        list.add(ImMsgInfo(1, "\uD83D\uDCA3"))
        list.add(ImMsgInfo(1, "\uD83D\uDCA3"))
        list.add(ImMsgInfo(1, "\uD83D\uDCA3"))
        list.add(ImMsgInfo(1, "\uD83D\uDCA3"))
        list.add(ImMsgInfo(1, "\uD83D\uDCA3"))
        list.add(ImMsgInfo(1, "\uD83D\uDCA3"))
        list.add(ImMsgInfo(1, "\uD83D\uDCA3"))
        list.add(ImMsgInfo(1, "\uD83D\uDCA3"))
        list.add(ImMsgInfo(1, "\uD83D\uDCA3"))
        list.add(ImMsgInfo(1, "\uD83D\uDCA3"))
        list.add(ImMsgInfo(1, "\uD83D\uDCA3"))
        val adapter = WxBoomAdapter()
        rv_test.adapter = adapter
        adapter.addData(list)
    }
}