package info.learncoding.androidbasiclearning.ui.home

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import info.learncoding.androidbasiclearning.R
import info.learncoding.androidbasiclearning.data.model.Post
import info.learncoding.androidbasiclearning.data.model.User
import info.learncoding.androidbasiclearning.data.network.ApiResponse
import info.learncoding.androidbasiclearning.ui.UserViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var titleEditText: EditText
    private lateinit var bodyEditText: EditText
    private lateinit var userSpinner: Spinner
    private lateinit var saveBtn: Button
    private lateinit var radioGroup: RadioGroup
    private var adapter: ArrayAdapter<User>? = null
    private var user: User? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userViewModel =
            ViewModelProvider(this).get(UserViewModel::class.java)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        radioGroup = view.findViewById(R.id.radio_group)
        titleEditText = view.findViewById(R.id.title_edit_text)
        bodyEditText = view.findViewById(R.id.desc_edit_text)
        userSpinner = view.findViewById(R.id.users_spinner)
        saveBtn = view.findViewById(R.id.save_btn)

        userViewModel.users.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                adapter =
                    ArrayAdapter(
                        requireContext(),
                        R.layout.item_user,
                        R.id.name_text_view,
                        it
                    )
                userSpinner.adapter = adapter
            } else {
                userViewModel.fetchRemoteUser()
            }
        })

        userSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                user = adapter?.getItem(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.yes_radio_btn) {
                titleEditText.visibility = View.VISIBLE
                bodyEditText.visibility = View.VISIBLE
                saveBtn.visibility = View.VISIBLE
                userSpinner.visibility = View.VISIBLE
            } else if (checkedId == R.id.no_radio_btn) {
                titleEditText.visibility = View.GONE
                bodyEditText.visibility = View.GONE
                saveBtn.visibility = View.GONE
                userSpinner.visibility = View.GONE
            }
        }
        saveBtn.setOnClickListener {
            if (TextUtils.isEmpty(titleEditText.text)) {
                Toast.makeText(context, "Please write title", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(bodyEditText.text)) {
                Toast.makeText(context, "Please write body", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (user == null) {
                Toast.makeText(context, "Please select user", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            userViewModel.post(
                Post(
                    user?.id ?: 0,
                    0,
                    titleEditText.text.toString(),
                    bodyEditText.text.toString()
                )
            ).observe(viewLifecycleOwner, {
                if (it is ApiResponse.Success) {
                    Toast.makeText(context, "Post Successful", Toast.LENGTH_SHORT).show()
                    titleEditText.text = null
                    bodyEditText.text = null
                } else if (it is ApiResponse.Error) {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            })

        }
    }
}