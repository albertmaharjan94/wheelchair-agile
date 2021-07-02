package com.softwarica.wheelchairapp.ui.main.User

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.softwarica.wheelchairapp.R
import com.softwarica.wheelchairapp.network.api.ServiceBuilder

class AccountsFragment : Fragment() {

    private lateinit var root : View
    private lateinit var fullname : TextView
    private lateinit var address : TextView
    private lateinit var contact : TextView
    private lateinit var distance : TextView
    private lateinit var speed : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_accounts, container, false)

        init()
        setProfile()

        return root
    }

    private fun setProfile(){
        fullname.text = ServiceBuilder.logged_user?.name
        address.text = ServiceBuilder.logged_user?.address
        contact.text = ServiceBuilder.logged_user?.contact
        distance.text = ServiceBuilder.logged_user?.distance.toString()
        speed.text = ServiceBuilder.logged_user?.speed.toString()
    }

    private fun init(){
        fullname = root.findViewById(R.id.fullname)
        address = root.findViewById(R.id.address)
        contact = root.findViewById(R.id.number)
        distance = root.findViewById(R.id.tvdistance)
        speed = root.findViewById(R.id.tvspeed)
    }

    companion object {

        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int): AccountsFragment {
            return AccountsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}