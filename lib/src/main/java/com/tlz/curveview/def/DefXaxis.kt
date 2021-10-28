package com.tlz.curveview.def

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import com.tlz.curveview.Xaxis
import android.R.attr.top
import android.R.attr.bottom



/**
 * 默认X轴.
 * 无X轴.
 * Created by Tomlezen.
 * Data: 2018/10/22.
 * Time: 14:25.
 */
class DefXaxis internal constructor(builder: XaxisBuilder) : Xaxis() {

  /** 文字画笔. */
  private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)

  /** 已测量的高度. */
  private var measureHeight = 0
  /** X轴偏移. */
  private var textOffsetX = 0
  /** 是否需要重新计算. */
  private var isNeedCalculate = true

  /** 文字大小. */
  var textSize: Float = builder.textSize
    set(value) {
      field = value
      isNeedCalculate = true
      curveView.refresh()
    }
  /** 文字颜色. */
  var textColor = builder.textColor
    set(value) {
      field = value
      curveView.refresh()
    }
  /** 左边距. */
  var paddingLeft = builder.paddingLeft
    set(value) {
      field = value
      isNeedCalculate = true
      curveView.refresh()
    }
  /** 右边距. */
  var paddingRight = builder.paddingRight
    set(value) {
      field = value
      isNeedCalculate = true
      curveView.refresh()
    }
  /** 上边距. */
  var paddingTop = builder.paddingTop
    set(value) {
      field = value
      isNeedCalculate = true
      curveView.refresh()
    }
  /** 下边距. */
  var paddingBot = builder.paddingBot
    set(value) {
      field = value
      isNeedCalculate = true
      curveView.refresh()
    }

  /** Y轴item. */
  override var items: Array<Any> = builder.items
    set(value) {
      field = value
      isNeedCalculate = true
      curveView.refresh()
    }

  override fun getMeasureHeight(): Int =
          if (isNeedCalculate) calculateHeight().also { measureHeight = it } else measureHeight

  override fun onDraw(cvs: Canvas, left: Int, top: Int, right: Int, bottom: Int) {
    if (items.isNotEmpty()) {
      textPaint.textSize = textSize
      textPaint.color = textColor
      textPaint.textAlign = Paint.Align.RIGHT
      val drawnWidth = right - left - paddingLeft - paddingRight
      val itemCount = items.size
      val eachWidth = drawnWidth.toFloat() / (itemCount - 1)
      val startX = left + paddingLeft
      val drawnR = top + paddingBot.toFloat()
      items.forEachIndexed { index, item ->
        // 绘制文字
        cvs.drawText(item.toString(), startX + eachWidth * index + textOffsetX, drawnR, textPaint)
      }
    }
  }

  /**
   * 计算宽度.
   * @return Int
   */
  private fun calculateHeight(): Int {
    textPaint.textSize = textSize
    var height = 0f
    val fontMetrics = textPaint.getFontMetrics()
    val height1 = fontMetrics.descent - fontMetrics.ascent
    val height2 = fontMetrics.bottom - fontMetrics.top
    height = if (height1 > height2) height1 else height2
    textOffsetX = textPaint.textOffsetX() + 2
    return (paddingTop + paddingBot + height).toInt()
  }

}