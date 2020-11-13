package com.zxq.purerss.ui.chooserss

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.table.goods
import com.zxq.purerss.databinding.FragmentChooserssBinding
import com.zxq.purerss.listener.OscillatingScrollListener
import com.zxq.purerss.listener.smoothScrollToPositionWithSpeed
import com.zxq.purerss.utils.InjectorUtil

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/19
 *  fun
 */
class ChooseRssFragment: Fragment() {
    val viewM: SaveDBRssViewModel by viewModels {
        InjectorUtil.getRssFeedFactory(this)
    }
    private var binding: FragmentChooserssBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChooserssBinding.inflate(inflater, container, false).apply {

            viewM.insertComplete.observe(this@ChooseRssFragment, Observer {
                if (it) {
                    findNavController().popBackStack()
                }
            })
            chooseGrid.apply {
                adapter = ChooseRssAdapter(context).apply {
                    submitList(goods.reversed())
                }
                smoothScrollToPositionWithSpeed(goods.size)
                addOnScrollListener(
                    OscillatingScrollListener(resources.getDimensionPixelSize(R.dimen.grid_2))
                )
            }
            fab.setOnClickListener {
                viewM.insertRss((chooseGrid.adapter as ChooseRssAdapter).getSelectList())
            }
        }
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}