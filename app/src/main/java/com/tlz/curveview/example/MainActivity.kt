package com.tlz.curveview.example

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.tlz.curveview.def.DefCurveRender
import com.tlz.curveview.def.DefData
import com.tlz.curveview.def.setupByDef
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

  private lateinit var curveRender: DefCurveRender<Data>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    // 初始化曲线为默认样式 动态添加模式
    curve_view.setupByDef<Data> {
      yaxis {
        items = Array(6) {
          it * 20
        }
      }

      curveRender {
        maxShownDataPoint = 40

        baseline = Data(60f)

        curveColor = Color.GREEN

        hintRectBg = Color.RED
        hintTextColor = Color.WHITE
        hintTextFormat = { data ->
          "${data.value}\n${data.time.toDate()}"
        }

        this.smoothMoveDuration = 1000L

        xaxisSpace = 5
        xaxisStartPoint = 0

        onDataLongPressed = { data ->
          Toast.makeText(this@MainActivity, "长按数据:${data.value}", Toast.LENGTH_LONG).show()
        }
      }
    }

    var isStop = true

    btn_start.setOnClickListener {
      if (!isStop) return@setOnClickListener
      isStop = false
      (curve_view.curveRender<Data>() as? DefCurveRender<Data>)?.start()
      Thread {
        var i=0
        while (!isStop) {
          Thread.sleep(2000)
          i++
          runOnUiThread {
            if (!isStop) {
              if (i > 10 && i < 20){
                curve_view.dataset<Data>()?.appendData(Data(Random().nextInt(100).toFloat(), isShow = false))
              }else{
                curve_view.dataset<Data>()?.appendData(Data(Random().nextInt(100).toFloat()))
              }
            }
          }
        }
      }.start()
    }

    btn_stop.setOnClickListener {
      isStop = true
      (curve_view.curveRender<Data>() as? DefCurveRender<Data>)?.stop()
    }

    btn_null.setOnClickListener {
      startActivity(Intent(this@MainActivity, BezerActivity::class.java))
    }


    // 初始化曲线为默认样式 静态模式
    curve_view_idle.setupByDef<Data> {
      yaxis {
        items = Array(5) {
          String.format("%.1f", it * 0.2)
        }
      }

      /**
       * 固定坐标轴
       */
//      xaxis {
//        items = Array(5) {
//          String.format("%d", it * 2)
//        }
//      }

      curveRender = curveRender {
        isIdleMode = true

        baseline = Data(0.5f)
        baselineColor = Color.RED
        baselineThickness = 4f

        curveColor = Color.GREEN

        shadowColor = Color.BLUE
        shadowDirection = DefCurveRender.Direction.DOWN
        shadowThickness = 0.5f

        //动态X坐标，间隔5个点一个x坐标
        xaxisSpace = 10
        xaxisStartPoint = 3

        //放缩时决定放大倍数，越小（3~50）放大倍数越大
        maxShownDataPoint = 10

        hintRectBg = Color.RED
        hintTextColor = Color.WHITE
        hintTextFormat = { data ->
          "值:${data.value}\n时间:${data.time.toDate()}"
        }

        markerIcon = resources.getDrawable(R.mipmap.ic_marker_gray).toBitmap()
//                markNumTextColor = Color.BLACK
        onMarkClicked = { d ->
          Toast.makeText(this@MainActivity, "标记被点击啦:${d.value}", Toast.LENGTH_LONG).show()
        }
      }
    }

    curve_view_idle.dataset<Data>()?.setData(List(50) {
//      Data(Random().nextInt(100).toFloat())
      if (it >=10 && it<20){
        Data(0.5f,isShow = false)
      }else{
        Data(Random().nextFloat())
      }
    })

    btn_scale.setOnClickListener {
      curveRender.scale { points ->
        //动态设置x轴的数量
        curveRender.xaxisSpace = points / 5
      }
    }

    btn_zoom.setOnClickListener {
      curveRender.zoom { points ->
        //动态设置x轴的数量
        curveRender.xaxisSpace=points/5
      }
    }

    btn_change.setOnClickListener { _ ->
      curve_view_idle.dataset<Data>()?.setData(List(100) {
        if (it>40){
          Data(0.5f,isShow = false)
        }else{
          Data(Random().nextFloat())
        }
      })
    }
  }

  class Data(val value: Float, val time: Long = System.currentTimeMillis(),val isShow:Boolean = true, private val alert: String = "131") : DefData {

    override val yScale: Float
      get() = if (value <= 1) value else value/100

    override val mark: String
      get() = alert

    override val isShown: Boolean
      get() = isShow

    override val xValue: String
      get() = SimpleDateFormat("HH:mm:ss").format(Date())
  }

  private val dateFormat = SimpleDateFormat("hh:mm:ss", Locale.CHINA)

  private fun Long.toDate() = dateFormat.format(Date(this))

  fun Drawable.toBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, if (opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565)
    val canvas = Canvas(bitmap)
    setBounds(0, 0, intrinsicWidth, intrinsicHeight)
    draw(canvas)
    return bitmap
  }
}
