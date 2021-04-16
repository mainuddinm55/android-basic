package info.learncoding.androidbasiclearning.ui.post

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import info.learncoding.androidbasiclearning.R
import info.learncoding.androidbasiclearning.ui.UserViewModel

class PostFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var listView: ListView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userViewModel =
            ViewModelProvider(this).get(UserViewModel::class.java)
        return inflater.inflate(R.layout.fragment_slideshow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val userId = arguments?.getInt("userId")
        listView = view.findViewById(R.id.post_list_view)
        userViewModel.posts(userId).observe(viewLifecycleOwner, {
            Log.d("PostFragment", "onViewCreated: ${it}")
            Log.d("PostFragment", "onViewCreated: ${it.size}")
            if (it.isNotEmpty()) {
                val adapter =
                    ArrayAdapter(
                        requireContext(),
                        R.layout.item_user,
                        R.id.name_text_view,
                        it
                    )
                listView.adapter = adapter
            } else {
                userViewModel.fetchRemotePost(userId)
            }
        })
    }
}