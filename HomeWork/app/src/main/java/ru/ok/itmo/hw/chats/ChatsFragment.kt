package ru.ok.itmo.hw.chats

import android.content.Context
import android.os.Bundle
import android.view.View
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
        chatList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        viewModel.uiStateLiveData.observe(viewLifecycleOwner, this::render)




    }


    private fun render(uiState: ChatsUiState) {
        when(uiState) {
            is ChatsUiState.Success -> {
                chatList.adapter = ChatsAdapter(uiState.itemList)
            }
            is ChatsUiState.Error -> {
                return
            }
            is ChatsUiState.Loading -> {}
            is ChatsUiState.NoState -> {}
        }
    }


    override fun onDetach() {
        viewModel.logout()
        super.onDetach()
    }

    companion object {
        const val AUTH_TOKEN_KEY = "AUTH"
    }
}