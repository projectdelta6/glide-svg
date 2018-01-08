package ch.qoqa.glide.svg

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.PictureDrawable
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.resource.SimpleResource
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder
import com.caverock.androidsvg.SVG

class SvgDrawableTranscoder(private val context: Context) : ResourceTranscoder<SVG, Drawable> {
    override fun transcode(toTranscode: Resource<SVG>, options: Options): Resource<Drawable> {
        val svg = toTranscode.get()
        val width = svg.documentWidth.toInt().takeIf { it > 0 }
                ?: (svg.documentViewBox.right - svg.documentViewBox.left).toInt()
        val height = svg.documentHeight.toInt().takeIf { it > 0 }
                ?: (svg.documentViewBox.bottom - svg.documentViewBox.top).toInt()

        val picture = svg.renderToPicture(width, height)
        val drawable = PictureDrawable(picture)

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawPicture(drawable.picture)

        return SimpleResource(BitmapDrawable(context.resources, bitmap))
    }
}
