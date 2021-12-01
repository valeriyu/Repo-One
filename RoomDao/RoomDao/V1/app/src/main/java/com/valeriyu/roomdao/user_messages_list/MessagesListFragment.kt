package com.valeriyu.roomdao.user_messages_list

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.valeriyu.roomdao.*
import com.valeriyu.roomdao.data.db.models.attachments.Attachments
import com.valeriyu.roomdao.data.db.models.attachments.MessagesWithAttachments
import kotlinx.android.synthetic.main.fragment_messages_list.*
import kotlinx.android.synthetic.main.view_toolbar.*
import kotlinx.coroutines.launch


class MessagesListFragment : Fragment(R.layout.fragment_messages_list) {
    private val viewModel by viewModels<MessangerViewModel>()
    private var msgAdapter: MessagesListAdapter? = null

    private val args: AddUserFragmentArgs by navArgs()


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initToolbar()
        initList()
        bindViewModel()

        addMsgButton.setOnClickListener {
            lifecycleScope.launch {
                var users = viewModel.getAllUsers()
                if (users.size < 2) {
                    return@launch
                }
                var rndId = users.map { it.id }.filter {
                    it != args.id
                }.random()

                when ((0..1).random()) {
                    0 -> viewModel.createMessage(
                        args.id,
                        rndId
                    )
                    1 -> viewModel.createMessage(
                        rndId,
                        args.id
                    )
                }
                viewModel.loadMessagesList(args.id)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //viewModel.loadMessagesList(args.id)
    }

    private fun initToolbar() {
        toolbar.setTitle(R.string.msg_list_title)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initList() {
        msgAdapter = MessagesListAdapter(
            {

                    viewModel.deleteMessageById(it as Long)
                    viewModel.loadMessagesList(args.id)
            },
            {

                    viewModel.deleteAttachment(it as Attachments)
                    viewModel.loadMessagesList(args.id)
               }
        )

        with(messagesList) {
            adapter = msgAdapter
            setHasFixedSize(true)
        }

        viewModel.loadMessagesList(args.id)
    }

    private fun bindViewModel() {
        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        viewModel.messagesWithAttachments.observe(viewLifecycleOwner) {
            loadList(it)
        }
    }

    private fun loadList(lst: List<MessagesWithAttachments>) {
        var uiList: List<MeesagesListItemsModels> = emptyList<MeesagesListItemsModels>()
        lst.map {
            uiList += MeesagesListItemsModels.Message(
                value = it.message
            )
            if (it.attachments.isNotEmpty()) {
                uiList += MeesagesListItemsModels.Attachments(
                    attachList = it.attachments
                )
            }
        }
        msgAdapter!!.items = uiList
    }

    override fun onDestroy() {
        msgAdapter = null
        super.onDestroy()
    }
}