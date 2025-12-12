package com.example.ecard.utils

import android.content.ContentValues
import android.content.Context
import android.provider.ContactsContract
import com.example.ecard.data.CardEntity

object ContactUtils {
    
    fun saveToContacts(context: Context, card: CardEntity): Boolean {
        return try {
            val values = ContentValues().apply {
                put(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                put(ContactsContract.RawContacts.ACCOUNT_NAME, null)
            }
            
            val uri = context.contentResolver.insert(ContactsContract.RawContacts.CONTENT_URI, values)
            val rawContactId = uri?.lastPathSegment?.toLongOrNull() ?: return false
            
            // Имя
            val nameValues = ContentValues().apply {
                put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
                put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, card.name)
            }
            context.contentResolver.insert(ContactsContract.Data.CONTENT_URI, nameValues)
            
            // Телефон
            card.phone?.let { phone ->
                val phoneValues = ContentValues().apply {
                    put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
                    put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone)
                    put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                }
                context.contentResolver.insert(ContactsContract.Data.CONTENT_URI, phoneValues)
            }
            
            // Email
            card.email?.let { email ->
                val emailValues = ContentValues().apply {
                    put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
                    put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    put(ContactsContract.CommonDataKinds.Email.DATA, email)
                    put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                }
                context.contentResolver.insert(ContactsContract.Data.CONTENT_URI, emailValues)
            }
            
            // Организация
            card.company?.let { company ->
                val orgValues = ContentValues().apply {
                    put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
                    put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                    put(ContactsContract.CommonDataKinds.Organization.COMPANY, company)
                }
                context.contentResolver.insert(ContactsContract.Data.CONTENT_URI, orgValues)
            }
            
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}

