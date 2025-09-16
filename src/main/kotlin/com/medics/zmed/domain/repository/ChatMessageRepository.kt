package com.medics.zmed.domain.repository

import com.medics.zmed.persistance.entity.CategoryDao
import com.medics.zmed.persistance.entity.ChatMessageDao
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface ChatMessageRepository {


    fun findAllChatId(chatId : String,pageNumber : Int,pageSize : Int) : Page<ChatMessageDao>

    fun saveMessage(messageDao: ChatMessageDao) : Boolean
}


@Repository

class  ChatMessageRepositoryImpl(private val repository: SpringChatMessageRepository) : ChatMessageRepository {
    override fun findAllChatId(chatId: String,pageNumber : Int,pageSize : Int) : Page<ChatMessageDao> {

        val pageable = PageRequest.of(pageNumber, pageSize)
        return repository.findByChatId(chatId,pageable)
    }

    override fun saveMessage(messageDao: ChatMessageDao) : Boolean {

        try {
            repository.save(messageDao)
            return true
        } catch (error : Exception) {
            error.printStackTrace()
            throw IllegalArgumentException(error.message)
        }

    }

}


interface  SpringChatMessageRepository : JpaRepository<ChatMessageDao, Long> {
    fun findByChatId(chatId: String, pageable: Pageable): Page<ChatMessageDao>
}