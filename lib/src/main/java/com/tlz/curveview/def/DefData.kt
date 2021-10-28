package com.tlz.curveview.def

/**
 * Created by Tomlezen.
 * Data: 2018/10/22.
 * Time: 15:56.
 */
interface DefData {

  /** 获取Y轴比例(值为0-1). */
  val yScale: Float

  /** 对应X轴坐标值 */
  val xValue: String

  /** 标记. */
  val mark: String

  /** 是否显示该点 */
  val isShown: Boolean
}