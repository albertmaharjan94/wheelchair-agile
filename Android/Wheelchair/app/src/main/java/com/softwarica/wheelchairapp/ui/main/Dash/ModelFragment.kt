package com.softwarica.wheelchairapp.ui.main.Dash

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GestureDetectorCompat
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.fragment.app.Fragment
import androidx.dynamicanimation.animation.FlingAnimation
import androidx.dynamicanimation.animation.FloatPropertyCompat
import androidx.lifecycle.ViewModelProvider
import com.google.ar.sceneform.HitTestResult
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.Scene
import com.google.ar.sceneform.SceneView
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.robinhood.ticker.TickerUtils
import com.robinhood.ticker.TickerView
import com.softwarica.wheelchairapp.TablViewModel
import com.softwarica.wheelchairapp.R
import com.softwarica.wheelchairapp.TabActivity
import com.softwarica.wheelchairapp.Utils.Constants
import com.softwarica.wheelchairapp.ui.main.Maps.MapViewModel
import io.github.controlwear.virtual.joystick.android.JoystickView
import java.lang.Exception
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ModelFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ModelFragment : Fragment(), Node.OnTouchListener  {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mDetector: GestureDetectorCompat
    private val quaternion = Quaternion()
    private val rotateVector = Vector3.up()
    private lateinit var speedTxt : TickerView;
    private lateinit var distanceTxt : TickerView;
    private lateinit var remotelay : LinearLayout

    private lateinit var jsFirst: JoystickView
    private lateinit var txtLog: TextView
    lateinit var mapViewModel: MapViewModel


    lateinit var scene : Scene
    var wheelchair : Node = Node().apply {
        localRotation = getRotationQuaternion(1f)
        localPosition = Vector3(0f, -3f, -7f)
        localScale = Vector3(176f, 176f, 176f)
        name = "Wheelchair"

    }
    lateinit var sceneView : SceneView
    var lastDeltaYAxisAngle : Float = 0f;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        activity?.let { it ->
//            val sharedViewModel = ViewModelProvider(it).get(TablViewModel::class.java)
//            sharedViewModel.getSpeed().observe(viewLifecycleOwner, { data ->
//                Log.d("Speed MODEL", data.toString())
//                speedTxt.text = data.toString()
//            })
//            sharedViewModel.getSerialData()!!.observe(viewLifecycleOwner, { data ->
//                Log.d("Frag", data.toString())
//                speedTxt.text = data[1]
//            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
                val view = inflater.inflate(R.layout.fragment_model, container, false)
        sceneView = view.findViewById(R.id.sceneView)
        scene = sceneView.scene

        speedTxt = view.findViewById(R.id.speedtxt);
        speedTxt.setCharacterLists(TickerUtils.provideNumberList());

        remotelay = view.findViewById(R.id.remotelay)

        speedTxt.text = "100 km/hr"

        distanceTxt = view.findViewById(R.id.distancetxt);
        distanceTxt.setCharacterLists(TickerUtils.provideNumberList());
        distanceTxt.text = "100 km"

        jsFirst = view.findViewById(R.id.jsFirst)
        txtLog = view.findViewById(R.id.txtLog)

        mapViewModel = ViewModelProvider(requireActivity()).get(MapViewModel::class.java)
        mapViewModel.getSpeed().observe(viewLifecycleOwner,{
            speedTxt.text = it.toString()
            Log.d("Speed MODEL", it.toString())
        })
        var sharedViewModel = ViewModelProvider(requireActivity()).get(TablViewModel::class.java)
        jsFirst.setOnMoveListener(JoystickView.OnMoveListener { angle, strength ->
            Log.d("Dpad angle", angle.toString())
//            val res = degreeToSpeed(angle, strength, safe_degree = 10)

            val ta = TabActivity
            try{
                if(strength > 40){
                    when (angle) {
                        in 0..180 -> {
                            ta._d_pad = 100
                            ta._reverse = 0
                            sharedViewModel.setReverse(false)
                        }
                        in 181..360 -> {
                            ta._d_pad = -100
                            ta._reverse = 1
                            sharedViewModel.setReverse(true)
                        }
                        else -> {
                            ta._reverse = 0
                            ta._d_pad = 0
                            sharedViewModel.setReverse(false)
                        }
                    }
                }else{
                    ta._d_pad = 0
                    sharedViewModel.setReverse(false)
                }

                val out = "${ta._key}#${ta._reverse}#${ta._speed_mode}#${ta._d_pad}\r\n"
                val ct = ta.connectedThread

                ct!!.write(out)
            }catch (e: Exception){
                ta._d_pad = 0
                print(e.stackTrace)
            }

            txtLog.text = "$angle $strength"
        })

        var mode = ""
        arguments?.getString(Constants.MODE)?.let {
            mode = it
        }

        if (mode == Constants.DOCK){
            sceneView.visibility = View.VISIBLE
            remotelay.visibility  =View.GONE
        }else if(mode == Constants.REMOTE){
            sceneView.visibility = View.GONE
            remotelay.visibility  =View.VISIBLE
        }

        renderObj(Uri.parse("wheelchair.sfb"))


        mDetector = GestureDetectorCompat(requireContext(), FlingGestureDetector())
        wheelchair.setOnTouchListener(this)
        // Inflate the layout for this fragment
        return view
    }


    private fun reMap(num: Float, from1: Float, to1: Float, from2: Float, to2: Float): Float {
        return (num - from1) / (to1 - from1) * (to2 - from2) + from2
    }

    private fun convertToStrength(
        num: Float,
        max: Float,
        sort: Int = 1,
        safe_degree: Int = 0
    ): Float {
        return if (sort == 1) {
            val res = reMap(num, 0F + safe_degree, 90F - safe_degree, max, 0F)
            if (res < 0) 0F else res
        } else {
            val res = reMap(num, 0F + safe_degree, 90F - safe_degree, 0F, max)
            if (res < 0) 0F else res

        }
    }

    private fun degreeToSpeed(
        degree: Int,
        strength: Int,
        safe_degree: Int = 5
    ): List<Int> {
        var leftSpeed = 0
        var rightSpeed = 0
        var str = strength
        when {
            degree in 0..180 + safe_degree || degree in 360 - safe_degree..360 -> {
                when {
                    degree in 90 - safe_degree..90 + safe_degree -> {
                        leftSpeed = str
                        rightSpeed = str
                    }
                    degree in 180 - safe_degree..180 + safe_degree -> {
                        leftSpeed = 0
                        rightSpeed = str
                    }
                    degree in 0..0 + safe_degree || degree in 360 - safe_degree..360 -> {
                        leftSpeed = str
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
                        rightSpeed = str
                    }
                }
            }
            else -> {
                when {
                    degree in 270 - safe_degree..270 + safe_degree -> {
                        leftSpeed = str
                        rightSpeed = str
                    }
                    degree < 270 -> {
                        leftSpeed = convertToStrength(
                            abs(degree - 270).toFloat(),
                            str.toFloat(),
                            safe_degree = safe_degree
                        ).roundToInt()
                        rightSpeed = str
                    }
                    else -> {
                        leftSpeed = str
                        rightSpeed = convertToStrength(
                            abs(degree - 360).toFloat(),
                            str.toFloat(),
                            0,
                            safe_degree = safe_degree
                        ).roundToInt()
                    }
                }

                leftSpeed = - abs(leftSpeed)
                rightSpeed = - abs(rightSpeed)
            }
        }
        return listOf<Int>(leftSpeed, rightSpeed)
    }

    private fun renderObj(parse: Uri){
        ModelRenderable.builder()
            .setSource(context, parse)
            .build()
            .thenAccept{
                addCardToScene(it)
            }
            .exceptionally {
                val builder = AlertDialog.Builder(requireContext())
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
            val arc = Math.toRadians(deltaYAxisAngle.toDouble())
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


    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int, mode : String): ModelFragment {
            return ModelFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                    putString(Constants.MODE, mode)
                }
            }
        }
    }


}