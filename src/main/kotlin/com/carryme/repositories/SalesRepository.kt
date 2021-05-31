package com.carryme.repositories

import com.carryme.entities.FerryDetail
import com.carryme.entities.Sales
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.math.BigInteger
import java.util.*

interface SalesRepository: CrudRepository<Sales,Long> {
    fun findAllByStatusAndPaymentProofIsNotNull(ferryId: String): List<Sales>
    fun findAllByStatusLikeAndPaymentProofIsNotNull(name: String, pgbl: Pageable): Page<Sales>
    fun findAllByStatusLikeAndStatusNotOrderByCreatedAtDescStatusDesc(name: String, excStatus: String, pgbl: Pageable): Page<Sales>
    fun findAllByStatusInAndPaymentExpiredLessThanEqual(status: List<String>, exp: Date): List<Sales>
    @Query(nativeQuery = true,
        value = "select DATE(created_at), count(created_at) from sales group by DATE(created_at),status having status = 'finish' and DATE(created_at) > (current_date - interval '7 days')")
    fun findCountDataPerDay(): List<Map<String,BigInteger>>
    @Query(nativeQuery = true,
        value = "select DATE(created_at), sum(total_price) from sales group by DATE(created_at),status having status = 'finish' and DATE(created_at) > (current_date - interval '7 days')")
    fun findIncomeDataPerDay(): List<Map<String,BigInteger>>

    @Query("select s from Sales s where s.status = ?1 and s.createdAt between ?2 and ?3 ")
    fun findAllByStatusAndCreatedAtGreaterThanAndCreatedAtLessThan(status: String, start: Date, end: Date): List<Sales>
}