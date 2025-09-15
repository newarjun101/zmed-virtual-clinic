package com.medics.zmed.domain.repository

import com.medics.zmed.persistance.entity.AppointmentDao
import com.medics.zmed.persistance.entity.DoctorDao
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

interface AppointmentRepository {

    fun createAppointment(appointmentDao: AppointmentDao): Boolean
    fun isAppointmentAlreadyExit(appointmentDao: AppointmentDao): Boolean

    fun isDoctorAlreadyBooked(appointmentDao: AppointmentDao): Boolean
    fun isCustomerAlreadyBooked(appointmentDao: AppointmentDao): Boolean

    fun getAllAppointmentByUserId(userId : Long, pageNumber: Int,
                                  pageSize: Int) : Page<AppointmentDao>


}

@Repository
class AppointmentRepositoryImpl(private val repository: SpringAppointmentRepository) : AppointmentRepository {
    override fun createAppointment(appointmentDao: AppointmentDao): Boolean {
        return try {
            repository.save(appointmentDao)
            true
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw IllegalArgumentException(ex.message)
        }


    }

    override fun isAppointmentAlreadyExit(appointmentDao: AppointmentDao): Boolean {

        return repository.isAppointmentAlreadyExit(
            doctorId = appointmentDao.doctorId,
            userId = appointmentDao.userId,
            appointmentDao.appointmentTime,
            appointmentDao.appointmentDate
        )
    }

    override fun isDoctorAlreadyBooked(appointmentDao: AppointmentDao): Boolean {
        return repository.isDoctorAlreadyBooked(
            doctorId = appointmentDao.doctorId,
            appointmentDao.appointmentTime,
            appointmentDao.appointmentDate
        )
    }

    override fun isCustomerAlreadyBooked(appointmentDao: AppointmentDao): Boolean {
        return repository.isCustomerAlreadyBooked(
            userId = appointmentDao.userId,
            appointmentDao.appointmentTime,
            appointmentDao.appointmentDate
        )
    }

    override fun getAllAppointmentByUserId(userId : Long, pageNumber: Int,
                                           pageSize: Int): Page<AppointmentDao> {
        val pageable: Pageable = PageRequest.of(pageNumber, pageSize)
        return repository.getAllAppointmentByUserId(userId = userId,pageable)
    }


}


interface SpringAppointmentRepository : JpaRepository<AppointmentDao, Long> {

    @Query(
        """
    SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
    FROM AppointmentDao a
    WHERE (a.doctorId = :doctorId OR a.userId = :userId)
    AND a.appointmentTime = :appointTime
    AND a.appointmentDate = :appointDate
    """
    )
    fun isAppointmentAlreadyExit(
        @Param("doctorId") doctorId: Long,
        @Param("userId") userId: Long,
        @Param("appointTime") appointTime: String = "",
        @Param("appointDate") appointDate: String = ""
    ): Boolean



    @Query(
        """
    SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
    FROM AppointmentDao a
    WHERE a.doctorId = :doctorId 
    AND a.appointmentTime = :appointTime
    AND a.appointmentDate = :appointDate
    """
    )
    fun isDoctorAlreadyBooked(
        @Param("doctorId") doctorId: Long,
        @Param("appointTime") appointTime: String = "",
        @Param("appointDate") appointDate: String = ""
    ): Boolean

    @Query(
        """
    SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
    FROM AppointmentDao a
    WHERE a.userId = :userId
    AND a.appointmentTime = :appointTime
    AND a.appointmentDate = :appointDate
    """
    )
    fun isCustomerAlreadyBooked(
        @Param("userId") userId: Long,
        @Param("appointTime") appointTime: String = "",
        @Param("appointDate") appointDate: String = ""
    ): Boolean


    @Query(
        """
        SELECT a
        FROM AppointmentDao a
        WHERE a.userId = :userId 
        ORDER BY a.appointmentCreatedDate DESC
    """
    )
    fun getAllAppointmentByUserId(
        @Param("userId") userId: Long,
        pageable: Pageable
    ) :Page<AppointmentDao>
}

