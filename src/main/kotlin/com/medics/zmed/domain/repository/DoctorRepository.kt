package com.medics.zmed.domain.repository

import com.medics.zmed.persistance.entity.DoctorDao
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

interface DoctorRepository {

    fun getPopularDoctors(pageNumber: Int, pageSize: Int): Page<DoctorDao>
    fun getAllDoctors(pageNumber: Int, pageSize: Int): Page<DoctorDao>
    fun saveDoctor(doctorDao: List<DoctorDao>): Boolean
    fun isDoctorValid(doctorId : Long?=null): Boolean

}

@Repository
class DoctorRepositoryImpl(private val repository: SpringDoctorRepository) : DoctorRepository {
    override fun getPopularDoctors(
        pageNumber: Int,
        pageSize: Int
    ): Page<DoctorDao> {
        val pageable: Pageable = PageRequest.of(pageNumber, pageSize)
        return repository.getPopularDoctor(minAppointments = 100L, minRating = 4.0,pageable)
    }

    override fun getAllDoctors(
        pageNumber: Int,
        pageSize: Int
    ): Page<DoctorDao> {
        val pageable: Pageable = PageRequest.of(pageNumber, pageSize)
        return repository.findAll(pageable)
    }

    override fun saveDoctor(doctorDao: List<DoctorDao>): Boolean {
        repository.saveAll(doctorDao)
        return true

    }

    override fun isDoctorValid(doctorId: Long?): Boolean {
        if(doctorId == null) {
            throw IllegalArgumentException("Doctor id cannot be null")
        }
        return  repository.existsById(doctorId)
    }




}


interface SpringDoctorRepository : JpaRepository<DoctorDao, Long> {

    @Query(
        """
        SELECT d 
        FROM DoctorDao d 
        WHERE d.totalAppointment >= :minAppointments 
          AND d.rating >= :minRating
        ORDER BY d.rating DESC
    """
    )
    fun getPopularDoctor(
        @Param("minAppointments") minAppointments: Long = 100,
        @Param("minRating") minRating: Double = 4.0,
        pageable: Pageable
    ) :Page<DoctorDao>

}