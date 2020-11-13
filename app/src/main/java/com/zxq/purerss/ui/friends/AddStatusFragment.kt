package com.zxq.purerss.ui.friends

import android.app.Activity
import android.app.Service
import android.graphics.Rect
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform
import com.previewlibrary.GPreviewBuilder
import com.ypx.imagepicker.ImagePicker
import com.ypx.imagepicker.bean.MimeType
import com.ypx.imagepicker.bean.SelectMode
import com.ypx.imagepicker.data.OnImagePickCompleteListener
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.ImageItemInfo
import com.zxq.purerss.databinding.FragmentAddstatusBinding
import com.zxq.purerss.utils.InjectorUtil
import com.zxq.purerss.widget.GridItemDecoration
import kotlinx.android.synthetic.main.fragment_addstatus.*


class AddStatusFragment : Fragment(), SensorEventListener {
    private var isCanAddPic = true
    private var mAdapter: AddPicAdapter? = null
    private var mList: MutableList<ImageItemInfo> = mutableListOf()
    private val viewM: FriendsCircleViewModel by viewModels {
        InjectorUtil.getFriendsFactory(this)
    }
    private var binding: FragmentAddstatusBinding? = null

    private var mCurrentDegree = 0f
    private var sensorManager: SensorManager? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddstatusBinding.inflate(inflater, container, false).apply {
            sensorManager = activity?.getSystemService(Service.SENSOR_SERVICE) as SensorManager
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            toolbar.setOnMenuItemClickListener {
                if (it.itemId == R.id.send) {
                    //发布内容
                    sendStatus()
                }
                true
            }
            lifecycleOwner = this@AddStatusFragment
            mAdapter = AddPicAdapter()
            rvPic.addItemDecoration(
                GridItemDecoration.Builder(context!!).verSize(10).horSize(10).build()
            )
            rvPic.adapter = mAdapter
            mList?.add(ImageItemInfo("", null, ""))
            mAdapter?.setNewData(mList)
            mAdapter?.setOnItemClickListener { adapter, view, position ->
                //如果最后一个，并且当前可以添加图片，则添加图片，否则预览图片
                if (isCanAddPic && position == mAdapter!!.data.size - 1) {
                    addPic()
                } else {
                    computeBoundsBackward(rvPic, mList);//组成数据
                    GPreviewBuilder.from(context as Activity)
                        .setData(mList)
                        .setIsScale(true)
                        .setCurrentIndex(position)
                        .setType(GPreviewBuilder.IndicatorType.Dot)
                        .start() //启动
                }
            }
            viewM.sendStatus.observe(this@AddStatusFragment, Observer {
                if (it == 1) {
                    findNavController().navigateUp()
                }
            })
        }
        sharedElementEnterTransition = MaterialContainerTransform()
        return binding?.root
    }

    /**
     * 查找信息
     * @param list 图片集合
     */
    private fun computeBoundsBackward(rv: RecyclerView, list: List<ImageItemInfo>) {
        for (i in 0 until rv.childCount) {
            val itemView: View? = rv.getChildAt(i)
            val bounds = Rect()
            if (itemView != null) {
                val thumbView: ImageView = itemView.findViewById(R.id.iv_pic) as ImageView
                thumbView.getGlobalVisibleRect(bounds)
            }
            list[i].setBounds(bounds)
            list[i].uri = Uri.parse(list[i].getUrl())
        }
    }

    private fun addPic() {
        ImagePicker.withMulti(WeChatPresenter()) //指定presenter                                 //设置选择的最大数
            .setMaxCount(9) //设置列数
            .setColumnCount(4) //设置要加载的文件类型，可指定单一类型
            .mimeTypes(MimeType.ofAll()) //设置需要过滤掉加载的文件类型
            .filterMimeTypes(MimeType.GIF)
            .showCamera(true) //显示拍照
            .setPreview(true) //开启预览
            //大图预览时是否支持预览视频
            .setPreviewVideo(true) //设置视频单选
            .setVideoSinglePick(true) //设置图片和视频单一类型选择
            .setSinglePickImageOrVideoType(true) //当单选或者视频单选时，点击item直接回调，无需点击完成按钮
            .setSinglePickWithAutoComplete(false)
            .setOriginal(true) //显示原图
            //设置单选模，当maxCount==1时，可执行单选（下次选中会取消上一次选中）
            .setSelectMode(SelectMode.MODE_SINGLE) //设置视频可选取的最大时长
            .setMaxVideoDuration(2000L) //设置视频可选取的最小时长
            .setMinVideoDuration(60000L) //设置上一次操作的图片列表，下次选择时默认恢复上一次选择的状态
            .pick(activity, OnImagePickCompleteListener {
                //图片选择回调，主线程
                isCanAddPic = it.size < 9
                mList!!.clear()
                for (item in it) {
                    mList?.add(
                        ImageItemInfo(
                            item.cropUrl,
                            item.uri,
                            item.path
                        )
                    )
                }
                if (mList.size < 9) {
                    mList.add(
                        ImageItemInfo(
                            "",
                            null,
                            ""
                        )
                    )
                }
                mAdapter?.setNewData(mList)
                mAdapter?.notifyDataSetChanged()
            })
    }

    private fun sendStatus() {
        if (mAdapter!!.data.size >= 2 || !et_content.text.toString().isNullOrEmpty()) {
            viewM.insertOneStatus(et_content.text.toString(), mList)
        } else {
            Snackbar.make(cl_add, "不能发布空内容的状态", 800).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onResume() {
        super.onResume()
        sensorManager?.registerListener(
            this,
            sensorManager?.getDefaultSensor(Sensor.TYPE_ORIENTATION),
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ORIENTATION) {
            var degree = event?.values[0]
            val ra = RotateAnimation(
                mCurrentDegree, -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
            )
            // 设置动画的持续时间
            ra.setDuration(200)
            // 设置动画结束后的保留状态
            ra.setFillAfter(true)
            // 启动动画
            iv_compass.startAnimation(ra)
            mCurrentDegree = -degree
        }
    }
}