package ru.ok.itmo.hw.chats

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.ok.itmo.hw.R
import ru.ok.itmo.hw.authorization.AuthorizationFragment

class ChatsFragment : Fragment(R.layout.chats_screen) {

    private lateinit var viewModel : ChatsViewModel
    private lateinit var chatList : RecyclerView
    private lateinit var loadingView : ProgressBar
    private lateinit var errorTv : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this, ChatsViewModel.Factory(requireArguments().getString(AUTH_TOKEN_KEY)!!))
            .get(ChatsViewModel::class.java)
        activity?.setTitle(R.string.chats)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback = object : OnBackPressedCallback(
            true
        ) {
            override fun handleOnBackPressed() {
                viewModel.logout()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, AuthorizationFragment())
                    .commit()

            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllChatItems()
        chatList = view.findViewById(R.id.chats_view)
        loadingView = view.findViewById(R.id.loading_state)
        errorTv = view.findViewById(R.id.chats_loading_error)


        chatList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        viewModel.uiStateLiveData.observe(viewLifecycleOwner, this::render)

    }


    private fun render(uiState: ChatsUiState) {
        when(uiState) {
            is ChatsUiState.Success -> {
                errorTv.visibility = View.GONE
                loadingView.visibility = View.GONE
                chatList.visibility = View.VISIBLE
                chatList.adapter = ChatsAdapter(uiState.itemList)
                return
            }
            is ChatsUiState.Error -> {
                loadingView.visibility = View.GONE
                chatList.visibility = View.GONE
                errorTv.visibility = View.VISIBLE
                errorTv.text = uiState.throwable.message
                return
            }
            is ChatsUiState.Loading -> {
                chatList.visibility = View.GONE
                errorTv.visibility = View.GONE
                loadingView.visibility = View.VISIBLE
                return

            }
            is ChatsUiState.NoState -> {
                chatList.visibility = View.GONE
                errorTv.visibility = View.GONE
                loadingView.visibility = View.GONE
                return
            }
        }
    }



    companion object {
        const val AUTH_TOKEN_KEY = "AUTH"
    }
}