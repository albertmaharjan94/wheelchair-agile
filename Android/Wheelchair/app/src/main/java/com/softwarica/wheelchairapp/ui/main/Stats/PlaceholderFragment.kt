package com.softwarica.wheelchairapp.ui.main.Stats

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.softwarica.wheelchairapp.R


/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment() {

    private var xAxisValue : List<String> =  ArrayList();
    private lateinit var pageViewModel: PageViewModel
    private var  radarEntries : MutableList<RadarEntry>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_tab, container, false)
        val radarChart : RadarChart = root.findViewById(R.id.chart);
        val radarDataSet : RadarDataSet = RadarDataSet(radarEntries, "");
        val radarData : RadarData = RadarData(radarDataSet)
        radarChart.data = radarData
        radarChart.getDescription().setEnabled(false);


        getEntries()

        val xAxis = radarChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
        xAxis.textSize = 10f
        xAxis.labelCount = xAxisValue.size
        xAxis.setCenterAxisLabels(true) //Set the label to center

        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValue)

        radarDataSet.setFillColor(Color.rgb(0, 0, 128));
        // Whether to fill the area solidly
        radarDataSet.setDrawFilled(true);



        radarDataSet.setValueTextColor(Color.BLACK);
        radarDataSet.setValueTextSize(10f);
        radarChart.setDrawWeb(true)
        radarChart.webColor = Color.BLACK
        return root
    }

    private fun getEntries() {
        radarEntries = ArrayList()

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
        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}