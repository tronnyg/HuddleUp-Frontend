package tech.fourge.huddleup_frontend.Ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import tech.fourge.huddleup_frontend.Helpers.UserHelper
import tech.fourge.huddleup_frontend.R
import tech.fourge.huddleup_frontend.Utils.CurrentUserUtil
import tech.fourge.huddleup_frontend.Ui.FragmentSettingsPage
import tech.fourge.huddleup_frontend.Utils.openIntent

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentProfilePage.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentProfilePage : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val functions = FirebaseFunctions.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile_page, container, false)

        // Set up the click listener for the Edit Profile button
        val editProfileButton: Button = view.findViewById(R.id.edit_profile_button)
        editProfileButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FragmentEditProfilePage())
                .addToBackStack(null)
                .commit()
        }
        view.findViewById<LinearLayout>(R.id.logout_item).setOnClickListener() {
            auth.signOut()
            openIntent(requireActivity(), MainActivity::class.java,null,true)
        }
        val editSettingsButton: LinearLayout = view.findViewById(R.id.settings_item)
        editSettingsButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FragmentSettingsPage())
                .addToBackStack(null)
                .commit()
        }
        view.findViewById<TextView>(R.id.profile_name).text = CurrentUserUtil.currentUser.firstname + " " + CurrentUserUtil.currentUser.lastname
        view.findViewById<TextView>(R.id.profile_username).text = CurrentUserUtil.currentUser.username
        view.findViewById<TextView>(R.id.profile_email).text = CurrentUserUtil.currentUser.email
        view.findViewById<TextView>(R.id.profile_phone).text = CurrentUserUtil.currentUser.phoneNumber

        return view
    }

        companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentProfilePage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentProfilePage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}