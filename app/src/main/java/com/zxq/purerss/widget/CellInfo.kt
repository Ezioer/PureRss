package com.zxq.purerss.widget

class CellInfo(
    //当前细胞的id
    var id: Int,
    //当前细胞的左右上下坐标，通过坐标来确定细胞位置
    var left: Int,
    var top: Int,
    var right: Int,
    var bottom: Int,
    var status: Int,//0死亡，1是存活
    var originStatus: Int,//0死亡，1存活
    //与细胞相邻的8个细胞
    var roundCell: MutableList<CellInfo?>?
)