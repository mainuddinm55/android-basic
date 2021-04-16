package info.learncoding.androidbasiclearning.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import info.learncoding.androidbasiclearning.R
import info.learncoding.androidbasiclearning.ui.UserViewModel

class UserFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var listView: ListView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listView = view.findViewById(R.id.users_list_view)
        userViewModel.users.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                val adapter =
                    ArrayAdapter(
                        requireContext(),
                        R.layout.item_user,
                        R.id.name_text_view,
                        it
                    )
                listView.adapter = adapter
                listView.setOnItemClickListener { parent, view, position, id ->
                    val bundle = bundleOf(
                        pairs = arrayOf(
                            Pair(
                                "userId",
                                adapter.getItem(position)?.id ?: 1
                            )
                        )
                    )
                    findNavController().navigate(R.id.action_nav_user_to_nav_post, bundle)
                }
            } else {
                userViewModel.fetchRemoteUser()
            }
        })
    }
}