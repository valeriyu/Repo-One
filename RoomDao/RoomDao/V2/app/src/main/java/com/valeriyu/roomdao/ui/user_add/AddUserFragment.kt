package com.valeriyu.roomdao

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.valeriyu.roomdao.data.db.models.users.User
import com.valeriyu.roomdao.ui.MessangerViewModel
import kotlinx.android.synthetic.main.fragment_user_add.*
import kotlinx.android.synthetic.main.view_toolbar.*

class AddUserFragment : Fragment(R.layout.fragment_user_add) {

    private val args: AddUserFragmentArgs by navArgs()
    private val viewModel by viewModels<MessangerViewModel>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initToolbar()
        bindViewModel()

        saveButton.setOnClickListener {
            saveUser()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        if (args.id > 0) {
            viewModel.getUserWhithProp(args.id)
        }
    }

    private fun initToolbar() {
        when (args.id) {
            0L -> toolbar.setTitle(R.string.user_add_title)
            else -> toolbar.setTitle("Изменить")
        }

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun bindViewModel() {
        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        viewModel.usersWithProp.observe(viewLifecycleOwner) {
            setExistingUserInfo(it[0])
        }

        viewModel.saveSuccessLiveData.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }


    private fun saveUser() {

        if (phoneTextField.editText?.text?.toString().orEmpty() == "") {
            Toast.makeText(
                requireContext(),
                "Не заполнены обязательные поля !!!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        var phone = phoneTextField.editText?.text?.toString().orEmpty()
        var password = passwordTextField.editText?.text?.toString().orEmpty().hashCode()

        var name = nameTextField.editText?.text?.toString().orEmpty()
        var avatar = avatarTextField.editText?.text?.toString().orEmpty()

        if (name.isEmpty()) {
            name = "User # " + phone
        }


        var uwp = User(
            id = args.id,
            phone = phone,
            password = password.hashCode(),
            name = name,
            avatar = avatar
        )

        viewModel.saveUserWithPropirties(uwp)
    }

    private fun setExistingUserInfo(uwp: User?) {
        if (uwp == null) return
        phoneTextField.editText?.setText(uwp.phone.orEmpty())
        nameTextField.editText?.setText(uwp.name.orEmpty())
        avatarTextField.editText?.setText(uwp.avatar.orEmpty())
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isRemoving) {
            requireActivity().hideKeyboardAndClearFocus()
        }
    }
}