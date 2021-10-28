package com.tlz.curveview.example;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Copyright (C) quhao All Rights Reserved <blakequ@gmail.com>
 * <p>
 * Licensed under the Plusub License, Version 1.0 (the "License");
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * author  : quhao <blakequ@gmail.com>
 * date     : 2019/8/16 10:14
 * last modify author :
 * version : 1.0
 * description:
 */
public class BezerView extends View {

    int mViewWidth = 0;
    int mViewHeight = 0;
    int mWidth = 0;
    int mHeight = 0;
    float r = 0;
    RectF rectF;
    Paint mPaint = new Paint();
    Path mPath = new Path();
    XYValue start = new XYValue(100, 100);
    XYValue end = new XYValue(100, 200);
    XYValue control1 = new XYValue(100, 300);
    ArrayList<XYValue> array = new ArrayList<>();

    public BezerView(Context context) {
        super(context);
    }

    public BezerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BezerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
        mWidth = mViewWidth - getPaddingLeft() - getPaddingRight();
        mHeight = mViewHeight - getPaddingTop() - getPaddingBottom();
        r = Math.min(mWidth,mHeight)*0.4f;
        rectF = new RectF(-r,-r,r,r);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);

//        System.out.println("-----111");
//        ArrayList<XYValue> array2 = new ArrayList<>();
//        mPath.rewind();
//        mPath.moveTo(300,300);
//        mPath.lineTo(500, 300);
//        canvas.drawPath(mPath,mPaint);

        System.out.println("-----1");
        float step = 0.05f;
        mPath.moveTo(100, 300);
        mPath.cubicTo(400, 200, 700, 300, 600, 400);
        PathMeasure measure = new PathMeasure(mPath, false);
        float len = measure.getLength();
        float[] point = new float[2];
        for (float t = 0; t <= 1; t += step) {
            float dis = t * len;
            measure.getPosTan(dis, point, null);
            canvas.drawPoint(point[0], point[1], mPaint);
        }
    }

//    private void mydraw(Canvas canvas) {
//        Paint mPaint = new Paint();
//        Path mPath = new Path();
//        mPaint.setStrokeWidth(5);
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setColor(Color.GREEN);
//        for (int i=0; i<array.size(); i++) {
//            XYValue value = array.get(i);
//            mPath.moveTo(value.x, value.y);
//            mPath.lineTo(value.x, 300);
//            canvas.drawPath(mPath, mPaint);
//        }
//    }

//    private void drawBottom(Canvas canvas) {
//        Paint mPaint = new Paint();
//        Path mPath = new Path();
//        mPaint.setStrokeWidth(10);
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setColor(Color.BLUE);
//        mPath.moveTo(300,300);
//        mPath.lineTo(500, 300);
//        canvas.drawPath(mPath,mPaint);
//    }

    class XYValue{
        float x;
        float y;
        XYValue(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}
