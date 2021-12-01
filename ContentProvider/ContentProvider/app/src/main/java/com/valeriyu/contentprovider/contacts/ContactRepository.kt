package com.valeriyu.contentprovider.contacts

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import android.util.Patterns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.regex.Pattern

class ContactRepository(
    private val context: Context
) {
    private val phonePattern = Patterns.PHONE
    private val emailPattern = Patterns.EMAIL_ADDRESS // .matcher(emailAddress).matches()) true

    suspend fun deletePhone(contactId: Long, phone: String) {
        withContext(Dispatchers.IO) {
            context.contentResolver.delete(
                ContactsContract.Data.CONTENT_URI,
                ContactsContract.RawContacts.Data.RAW_CONTACT_ID + " = ? and " +
                        ContactsContract.RawContacts.Data.MIMETYPE + " = ? and " +
                        ContactsContract.CommonDataKinds.Phone.NUMBER + " = ? ",
                arrayOf(
                    contactId.toString(),
                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                    phone
                )
            )
        }
    }

    suspend fun deleteEmail(contactId: Long, email: String) {
        withContext(Dispatchers.IO) {
            context.contentResolver.delete(
                ContactsContract.Data.CONTENT_URI,
                ContactsContract.RawContacts.Data.RAW_CONTACT_ID + " = ? and " +
                        ContactsContract.RawContacts.Data.MIMETYPE + " = ? and " +
                        ContactsContract.CommonDataKinds.Email.ADDRESS + " = ? ",
                arrayOf(
                    contactId.toString(),
                    ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
                    email
                )
            )
        }
    }

    suspend fun deleteContact(contactId: Long) {
        withContext(Dispatchers.IO) {
            context.contentResolver.delete(
                ContactsContract.RawContacts.CONTENT_URI,
                "${ContactsContract.Data._ID} = ?",
                arrayOf(contactId.toString()),
            )
        }
    }

    suspend fun saveBody(contactId: Long, phone: String, email: String) {
        withContext(Dispatchers.IO) {
            if (phone.trim() != "") {
                if (phonePattern.matcher(phone).matches() && phone.trim() != "") {
                    saveContactPhone(contactId, phone)
                } else {
                    error("Неправильный телефон !!!")
                }
            }
        }

        withContext(Dispatchers.IO) {
            if (email.trim() != "") {
                if (emailPattern.matcher(email).matches()) {
                    saveContactEmail(contactId, email)
                } else {
                    error("Неправильный адрес эл. почты !!!")
                }
            }
        }
    }

    suspend fun saveContact(
        name: String,
        family_name: String,
        phone: String,
        email: String
    ) = withContext(Dispatchers.IO) {
        var contactId =0L
        try {
            if(name.isEmpty() || family_name.isEmpty() || phone.isEmpty() ){
                error("Заполнены не все обязательные поля !!!")
            }
            contactId = saveRawContact()
            saveContactName(contactId, name, family_name)
            saveBody(contactId, phone, email)
        }catch (t:Throwable){
            deleteContact(contactId)
            error(t.message as String)
        }
    }

    private fun saveRawContact(): Long {
        val uri = context.contentResolver.insert(
            ContactsContract.RawContacts.CONTENT_URI,
            ContentValues()
        )
        return uri?.lastPathSegment?.toLongOrNull() ?: error("cannot save raw contact")
    }

    private fun saveContactName(contactId: Long, name: String, family_name: String) {
        val contentValues = ContentValues().apply {
            put(ContactsContract.Data.RAW_CONTACT_ID, contactId)
            put(
                ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
            )
            put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
            if (family_name != "") {
                put(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, family_name)
            }
        }
        context.contentResolver.insert(ContactsContract.Data.CONTENT_URI, contentValues)
    }

    private fun saveContactPhone(contactId: Long, phone: String) {
        val contentValues = ContentValues().apply {
            put(ContactsContract.Data.RAW_CONTACT_ID, contactId)
            put(
                ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
            )
            put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone)
        }
        context.contentResolver.insert(ContactsContract.Data.CONTENT_URI, contentValues)
    }

    private fun saveContactEmail(contactId: Long, email: String) {
        val contentValues = ContentValues().apply {
            put(ContactsContract.Data.RAW_CONTACT_ID, contactId)
            put(
                ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE
            )
            put(ContactsContract.CommonDataKinds.Email.ADDRESS, email)
        }
        context.contentResolver.insert(ContactsContract.Data.CONTENT_URI, contentValues)
    }


    suspend fun getAllContacts(): List<Contact> {
        val list = mutableListOf<Contact>()
        withContext(Dispatchers.IO) {

            context.contentResolver.query(
                ContactsContract.Data.CONTENT_URI,
                null,
                /*   arrayOf(ContactsContract.Data.RAW_CONTACT_ID,
                       ContactsContract.Data.DISPLAY_NAME
                       , ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME*/
                //)

                //null,
                "${ContactsContract.Data.MIMETYPE} = ?",
                arrayOf("${ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE}"),

                null,
                null
            )?.use {
                if (it.moveToFirst().not()) {
                    return@use emptyList<Contact>()
                } else {
                    do {
                        list.add(
                            Contact(
                                id = it.getLong(
                                    it.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.RAW_CONTACT_ID)
                                ),
                                name = it.getString(
                                    it.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME)
                                ).orEmpty(),
                                famile_name = it.getString(
                                    it.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME)
                                ).orEmpty()
                            )
                        )
                    } while (it.moveToNext())
                }
            }
        }
        return list
    }


    suspend fun getContBody(contactId: Long): ContactBody {
        var phList: List<String> = emptyList()
        var mailList = emptyList<String>()
        withContext(Dispatchers.IO) {

            context.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
                "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                arrayOf(contactId.toString()),
                null
            )?.use {
                if (it.moveToFirst().not()) {
                    return@use emptyList<String>()
                } else {
                    do {
                        phList += it.getString(0)
                    } while (it.moveToNext())
                }
            }

            context.contentResolver.query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                arrayOf(ContactsContract.CommonDataKinds.Email.ADDRESS),
                "${ContactsContract.CommonDataKinds.Email.CONTACT_ID} = ?",
                arrayOf(contactId.toString()),
                null
            )?.use {
                if (it.moveToFirst().not()) {
                    return@use emptyList<String>()
                } else {
                    do {
                        mailList += it.getString(0)
                    } while (it.moveToNext())
                }
            }

        }

        return ContactBody(
            id = contactId,
            phones = phList,
            email = mailList
        )
    }
}