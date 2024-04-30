package com.github.bvschaik.renderscriptcrash

import android.graphics.Bitmap
import android.os.Bundle
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.Type
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.drawToBitmap
import com.github.bvschaik.renderscriptcrash.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.invertButton.setOnClickListener {
            // YOLO on the main thread for testing purposes
            val inputBitmap = binding.inputImage.drawToBitmap()
            val output = invertImageRenderscript(inputBitmap)
            binding.outputImage.setImageBitmap(output)
        }
    }

    @Suppress("DEPRECATION")
    private fun invertImageRenderscript(input: Bitmap): Bitmap {
        val rs = RenderScript.create(this)
        val inputAllocation = Allocation.createFromBitmap(rs, input)
        val outputAllocation = Allocation.createTyped(rs, Type.createXY(rs, Element.RGBA_8888(rs), input.width, input.height))
        val invert = ScriptC_invert(rs)
        invert.forEach_invert(inputAllocation, outputAllocation)
        val outputBitmap = Bitmap.createBitmap(input.width, input.height, Bitmap.Config.ARGB_8888)
        outputAllocation.copyTo(outputBitmap)
        return outputBitmap
    }
}