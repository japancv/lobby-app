package com.example.lobbyapp.view

import android.content.Context
import android.content.res.Configuration
import android.graphics.*
import android.graphics.drawable.VectorDrawable
import android.util.AttributeSet
import android.util.Log
import android.util.Size
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.lobbyapp.R
import com.google.mlkit.vision.face.Face

class FaceOverlay @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var previewWidth: Int = 0
    private var widthScaleFactor = 1.0f
    private var previewHeight: Int = 0
    private var heightScaleFactor = 1.0f
    private var orientation = Configuration.ORIENTATION_PORTRAIT
    private var mFaceRectBitmap: Bitmap? = null
    private var mSrc: Rect? = null

    private var faces = emptyArray<Face>()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.YELLOW
        style = Paint.Style.STROKE
        strokeWidth = 5.0f
    }

    fun setOrientation(orientation: Int) {
        this.orientation = orientation
    }

    fun setPreviewSize(size: Size) {
        // Need to swap width and height when in portrait, since camera's natural orientation is landscape.
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            previewWidth = size.width
            previewHeight = size.height
        } else {
            previewWidth = size.height
            previewHeight = size.width
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawOverlay(canvas)
    }

    fun setFaces(faceList: List<Face>) {
        faces = faceList.toTypedArray()
        Log.d("Box", faces.toString())
        postInvalidate()
    }

    private fun drawOverlay(canvas: Canvas) {
        widthScaleFactor = width.toFloat() / previewWidth
        heightScaleFactor = height.toFloat() / previewHeight

        for (face in faces) {
            drawFaceBorder(face, canvas)
        }
    }

    private fun drawFaceBorder(face: Face, canvas: Canvas) {
        val padding = 5
        val bounds = face.boundingBox
        val left = translateX(bounds.left.toFloat()).toInt() + padding
        val top = translateY(bounds.top.toFloat()).toInt() + padding * 3
        val right = translateX(bounds.right.toFloat()).toInt() + padding
        val bottom = translateY(bounds.bottom.toFloat()).toInt() + padding * 3

        val realFaceRect = Rect(
            left,
            top,
            right,
            bottom
        )

        val drawRect = Rect(
            realFaceRect.left - (0.08 * realFaceRect.width()).toInt(),
            realFaceRect.top - (0.08 * realFaceRect.height()).toInt(),
            realFaceRect.right + (0.08 * realFaceRect.width()).toInt(),
            realFaceRect.bottom + (0.08 * realFaceRect.height()).toInt()
        )

        if (mFaceRectBitmap == null) {
            mFaceRectBitmap = (ResourcesCompat.getDrawable(
                this.resources,
                R.drawable.bounding_box,
                null
            ) as VectorDrawable).toBitmap()
        }

        if (mSrc == null && mFaceRectBitmap != null) {
            mSrc = Rect(0, 0, mFaceRectBitmap!!.width, mFaceRectBitmap!!.height)
        }

        Log.d("Box", left.toString())
        Log.d("Box", top.toString())
        Log.d("Box", right.toString())
        Log.d("Box", bottom.toString())

        mFaceRectBitmap?.let { canvas.drawBitmap(it, mSrc, drawRect, null) }
    }

    private fun translateX(x: Float): Float = x * widthScaleFactor
    private fun translateY(y: Float): Float = y * heightScaleFactor

}