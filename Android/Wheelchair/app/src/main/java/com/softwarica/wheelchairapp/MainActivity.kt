package com.softwarica.wheelchairapp

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.FlingAnimation
import androidx.dynamicanimation.animation.FloatPropertyCompat
import com.google.ar.sceneform.HitTestResult
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.Scene
import com.google.ar.sceneform.SceneView
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.robinhood.ticker.TickerUtils
import com.robinhood.ticker.TickerView
import io.ghyeok.stickyswitch.widget.StickySwitch
import java.lang.Math.toRadians
import java.util.*
import kotlin.math.cos
import kotlin.math.sin


class MainActivity() : AppCompatActivity(), Node.OnTouchListener {
    private lateinit var mDetector: GestureDetectorCompat
    private val quaternion = Quaternion()
    private val rotateVector = Vector3.up()
    private lateinit var speedTxt : TickerView;
    private lateinit var distanceTxt : TickerView;
    private lateinit var startbtn : StickySwitch;
    private lateinit var btnlay : LinearLayout


    lateinit var scene : Scene
     var wheelchair : Node = Node().apply {
         localRotation = getRotationQuaternion(1f)
        localPosition = Vector3(0f, -3f, -7f)
        localScale = Vector3(6f, 6f, 6f)
        name = "Wheelchair"

    }
   lateinit var sceneView : SceneView
    var lastDeltaYAxisAngle : Float = 0f;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sceneView = findViewById(R.id.sceneView)
        scene = sceneView.scene

        speedTxt = findViewById(R.id.speedtxt);
        speedTxt.setCharacterLists(TickerUtils.provideNumberList());

        speedTxt.text = "100 km/hr"

        distanceTxt = findViewById(R.id.distancetxt);
        distanceTxt.setCharacterLists(TickerUtils.provideNumberList());
        distanceTxt.text = "100 km"


        renderObj(Uri.parse("scene.sfb"))



        startbtn = findViewById(R.id.startbtn)
        startbtn.setOnClickListener {
            Log.d("TAG", "onCreate: " + startbtn.getDirection())
            if(startbtn.getDirection().toString().equals("RIGHT")){
            }else if(startbtn.getDirection().toString().equals("LEFT")){
            }

        }



        mDetector = GestureDetectorCompat(this, FlingGestureDetector())
        wheelchair.setOnTouchListener(this)
    }




    private fun renderObj(parse: Uri){
        ModelRenderable.builder()
                .setSource(this, parse)
                .build()
                .thenAccept{
                    addCardToScene(it)
                }
                .exceptionally {
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage(it.message)
                            .setTitle("Error!")
                    val dialog = builder.create()
                    dialog.show()
                    return@exceptionally null
                }
    }

    fun addCardToScene(modelRenderable: ModelRenderable) {
        with(wheelchair) {
            setParent(sceneView.scene)
            renderable = modelRenderable
        }
        modelRenderable?.let {
            scene.addChild(wheelchair)
        }
    }


    private fun addNodeToScene(model: ModelRenderable){
        model?.let {
            wheelchair
            scene.addChild(wheelchair)
        }
    }

    override fun onPause() {
        super.onPause()
        sceneView.pause()
    }

    override fun onResume() {
        super.onResume()
        sceneView.resume()
    }


    abstract class CardProperty(name: String) : FloatPropertyCompat<Node>(name)
    private val rotationProperty: CardProperty = object : CardProperty("rotation") {
        override fun setValue(card: Node, value: Float) {
            card.localRotation = getRotationQuaternion(value)
        }
        override fun getValue(card: Node): Float = card.localRotation.y
    }
    private var animation: FlingAnimation = FlingAnimation(wheelchair, rotationProperty).apply {
        friction = 20f
        minimumVisibleChange = DynamicAnimation.MIN_VISIBLE_CHANGE_ROTATION_DEGREES
    }



    private fun getRotationQuaternion(deltaYAxisAngle: Float): Quaternion {
        lastDeltaYAxisAngle = deltaYAxisAngle
        return quaternion.apply {
            val arc = toRadians(deltaYAxisAngle.toDouble())
            val axis = sin(arc / 2.0)
            x = (rotateVector.x * axis).toFloat()
            y = (rotateVector.y * axis).toFloat()
            z = (rotateVector.z * axis).toFloat()
            w = cos(arc / 2.0).toFloat()
            normalize()
        }
    }

    inner class FlingGestureDetector : GestureDetector.SimpleOnGestureListener() {

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            Log.d("TAG", "onDoubleTap: ")
            return true
        }

        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            val deltaX = -(distanceX / 200f) / 0.2f
            wheelchair.localRotation = getRotationQuaternion(lastDeltaYAxisAngle + deltaX)
            return true
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            Log.d("TAG", "onFling: " + Math.abs(velocityX))
            if (Math.abs(velocityX) > 500f) {
                val deltaVelocity = (velocityX / 200f) / 0.2f
                Log.d("TAG", "onFling: " + deltaVelocity)

                startAnimation(deltaVelocity)
            }
            return true
        }
    }
    private fun startAnimation(velocity: Float) {
        if (!animation.isRunning) {
            animation.setStartVelocity(velocity)
            animation.setStartValue(lastDeltaYAxisAngle)
            animation.start()
        }
    }



    override fun onTouch(p0: HitTestResult?, event: MotionEvent?): Boolean {
        mDetector.onTouchEvent(event)
        return true
    }


}