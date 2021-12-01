package com.valeriyu.roomdao.user_messages_list

import com.valeriyu.roomdao.data.db.models.messages.MeesgesWithSendersAndReceiversNames

/*sealed class _UiModels {
    data class Message(
        var value: MeesgesWithSendersAndReceiversNames
    ) : _UiModels()

    data class Attachments(
        var attachList: List<com.valeriyu.roomdao.data.db.models.attachments.Attachments>
    ) : _UiModels()

}*/


sealed class MeesagesListItemsModels {
    data class Message(
        var value: MeesgesWithSendersAndReceiversNames
    ) : MeesagesListItemsModels()

    data class Attachments(
        var attachList: List<com.valeriyu.roomdao.data.db.models.attachments.Attachments>
    ) : MeesagesListItemsModels()

}
