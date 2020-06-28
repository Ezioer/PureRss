package com.zxq.purerss.ui.type

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.zxq.purerss.databinding.FragmentTypeBinding
import com.zxq.purerss.utils.InjectorUtil

class FragmentType: Fragment() {
    private val viewM: TypeViewModel by viewModels {
        InjectorUtil.getTypeFactory(this)
    }
    private val args: FragmentTypeArgs by navArgs()
    private var type = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTypeBinding.inflate(inflater,container,false).apply {
            type = args.type
        }
        return binding.root
    }
}