package com.carryme.entities

import java.util.*
import javax.persistence.*
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.isSupertypeOf
import kotlin.reflect.full.memberProperties

@MappedSuperclass
abstract class BaseEntity(@Id
                          @GeneratedValue(strategy = GenerationType.AUTO)
                          private val id: Long = -1L) {

    @Column(name = "created_by",nullable = true)
    var createdBy: String? = null

    @Column(name = "updated_by",nullable = true)
    val updatedBy: String? = null

    @Column(name = "created_at",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    var createdAt: Date? = null

    @Column(name = "updated_at",nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    var updatedAt: Date? = null

    /**
     * Sets createdAt before insert
     */
    @PrePersist
    open fun setCreationDate() {
        createdAt = Date()
    }

    /**
     * Sets updatedAt before update
     */
    @PreUpdate
    open fun setChangeDate() {
        updatedAt = Date()
    }

    fun <T : Any, R : Any> T.copyPropsFrom(fromObject: R, vararg props: KProperty<*>) {
        // only consider mutable properties
        val mutableProps = this::class.memberProperties.filterIsInstance<KMutableProperty<*>>()
        // if source list is provided use that otherwise use all available properties
        val sourceProps = if (props.isEmpty()) fromObject::class.memberProperties else props.toList()
        // copy all matching
        mutableProps.forEach { targetProp ->
            sourceProps.find {
                // make sure properties have same name and compatible types
                it.name == targetProp.name && targetProp.returnType.isSupertypeOf(it.returnType)
            }?.let { matchingProp ->
                targetProp.setter.call(this, matchingProp.getter.call(fromObject))
            }
        }
    }
}