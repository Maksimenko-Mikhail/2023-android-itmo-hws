package ru.ok.itmo.hw.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import ru.ok.itmo.hw.chats.ChatsFragment
import ru.ok.itmo.hw.R


class AuthorizationFragment : Fragment(R.layout.authorisation_screen) {

    private lateinit var loginEt : EditText
    private lateinit var passwordEt : EditText
    private lateinit var enterBtn : Button
    private lateinit var errorTv : TextView

    private val viewModel: AuthorizationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        activity?.setTitle(R.string.autorization)
        return rootView
    }

    private fun render(uiState: LoginUiState) {
        when (uiState) {
            is LoginUiState.Data -> {
                errorTv.visibility = View.INVISIBLE
                val bundle = bundleOf(ChatsFragment.AUTH_TOKEN_KEY to uiState.result)
                parentFragmentManager.commit {
                    replace(R.id.container, ChatsFragment::class.java, bundle)
                }
            }

            is LoginUiState.Error -> {
                errorTv.visibility = View.VISIBLE
                errorTv.text = uiState.throwable.toString()
            }
            is LoginUiState.NoState -> {}
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        loginEt = view.findViewById(R.id.login_edit)
        passwordEt = view.findViewById(R.id.password_edit)
        enterBtn = view.findViewById(R.id.enter_btn)
        errorTv = view.findViewById(R.id.login_error_tv)

        loginEt.doOnTextChanged { text, _, _, _ -> enterBtn.isEnabled = text?.isEmpty() == false && passwordEt.text.isNotEmpty() }
        passwordEt.doOnTextChanged { text, _, _, _ -> enterBtn.isEnabled = text?.isEmpty() == false && loginEt.text.isNotEmpty() }



        enterBtn.setOnClickListener {
//            viewModel.login(loginEt.text.toString(), passwordEt.text.toString())
            viewModel.login("a", "SiTCiAXXX")
        }

        viewModel.uiStateLiveData.observe(viewLifecycleOwner, this::render)
//        enterBtn.performClick()
    }
}