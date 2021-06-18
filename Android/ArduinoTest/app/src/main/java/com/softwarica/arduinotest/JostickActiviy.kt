package com.softwarica.arduinotest

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.github.controlwear.virtual.joystick.android.JoystickView
import io.github.controlwear.virtual.joystick.android.JoystickView.OnMoveListener
import kotlin.math.abs
import kotlin.math.roundToInt

class JostickActiviy : AppCompatActivity() {
    private lateinit var jsFirst: JoystickView
    private lateinit var txtLog: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jostick_activiy)

        jsFirst = findViewById(R.id.jsFirst)
        txtLog = findViewById(R.id.txtLog)
        jsFirst.setOnMoveListener(OnMoveListener { angle, strength ->
            val res = degreeToSpeed(angle, strength, safe_degree = 10, safe_strength = 15)
            Log.d("Test", res.toString())
            txtLog.text = "$res"
        })
    }

    private fun reMap(num: Float, from1: Float, to1: Float, from2: Float, to2: Float): Float {
        return (num - from1) / (to1 - from1) * (to2 - from2) + from2
    }

    private fun convertToStrength(
        num: Float,
        max: Float,
        sort: Int = 1,
        safe_degree: Int = 0,
        safe_strength: Int = 15
    ): Float {
        return if (sort == 1) {
            val res = reMap(num, 0F + safe_degree, 90F, max, 0F - safe_strength)
            if (res < 0) 0F else res
        } else {
            val res = reMap(num, 0F + safe_degree, 90F, 0F, max)
            if (res < 0) 0F else res

        }
    }

    private fun convertToDefaultStrength(
        num: Int,
        max: Int,
        sort: Int = 1,
        safe_strength: Int = 15
    ): Int {
        return reMap(num.toFloat(), 0F + safe_strength, max.toFloat(), 0F, max.toFloat()).toInt()
    }

    private fun degreeToSpeed(
        degree: Int,
        strength: Int,
        safe_degree: Int = 5,
        safe_strength: Int = 15
    ): List<Int> {
        var leftState = 1
        var rightState = 1
        var leftSpeed = 0
        var rightSpeed = 0
        var str = strength
        when {
            str <= safe_strength -> {
                leftState = 1
                leftSpeed = 0
                rightState = 1
                rightSpeed = 0
            }
            degree in 0..180 + safe_degree || degree in 360 - safe_degree..360 -> {
                when {
                    degree in 90 - safe_degree..90 + safe_degree -> {
                        leftSpeed =
                            convertToDefaultStrength(str, 100, safe_strength = safe_strength)
                        rightSpeed =
                            convertToDefaultStrength(str, 100, safe_strength = safe_strength)
                    }
                    degree in 180 - safe_degree..180 + safe_degree -> {
                        leftSpeed = 0
                        rightSpeed =
                            convertToDefaultStrength(str, 100, safe_strength = safe_strength)
                    }
                    degree in 0..0 + safe_degree || degree in 360 - safe_degree..360 -> {
                        leftSpeed =
                            convertToDefaultStrength(str, 100, safe_strength = safe_strength)
                        rightSpeed = 0
                    }
                    degree < 90 -> {
                        leftSpeed = str
                        rightSpeed = convertToStrength(
                            abs(degree - 90).toFloat(),
                            str.toFloat(),
                            safe_degree = safe_degree
                        ).roundToInt()
                    }
                    else -> {
                        leftSpeed = convertToStrength(
                            abs(degree - 180).toFloat(),
                            str.toFloat(),
                            0,
                            safe_degree = safe_degree
                        ).roundToInt()
                        rightSpeed =
                            convertToDefaultStrength(str, 100, safe_strength = safe_strength)
                    }
                }
            }
            else -> {
                leftState = 0
                rightState = 0
                when {
                    degree in 270 - safe_degree..270 + safe_degree -> {
                        leftSpeed =
                            convertToDefaultStrength(str, 100, safe_strength = safe_strength)
                        rightSpeed =
                            convertToDefaultStrength(str, 100, safe_strength = safe_strength)
                    }
                    degree < 270 -> {
                        leftSpeed = convertToStrength(
                            abs(degree - 270).toFloat(),
                            str.toFloat(),
                            safe_degree = safe_degree
                        ).roundToInt()
                        rightSpeed =
                            convertToDefaultStrength(str, 100, safe_strength = safe_strength)
                    }
                    else -> {
                        leftSpeed =
                            convertToDefaultStrength(str, 100, safe_strength = safe_strength)
                        rightSpeed = convertToStrength(
                            abs(degree - 360).toFloat(),
                            str.toFloat(),
                            0,
                            safe_degree = safe_degree
                        ).roundToInt()
                    }
                }
            }
        }
        return listOf<Int>(leftState, leftSpeed, rightState, rightSpeed)
    }
}