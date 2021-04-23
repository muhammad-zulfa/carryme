package com.carryme.services.impl

import com.carryme.dto.requests.RouteRequestDto
import com.carryme.entities.OperationRoutes
import com.carryme.entities.Routes
import com.carryme.repositories.DockRepository
import com.carryme.repositories.OperationRouteRepository
import com.carryme.repositories.RouteRepository
import com.carryme.services.IOperationRouteService
import com.carryme.services.IRouteService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class OperationRouteService: IOperationRouteService{

    @Autowired
    private lateinit var repository: OperationRouteRepository
    @Autowired
    private lateinit var dockRepository: DockRepository

    override fun findAllByName(search: String, pgbl: Pageable): Page<OperationRoutes> {
        return repository.findAllByNameLike(search,pgbl)
    }

    override fun submit(form: RouteRequestDto): OperationRoutes {
        var routes = OperationRoutes()
        if (form.id != null){
            routes = repository.findById(form.id!!).get().apply {
                this.origin = dockRepository.findById(form.origin!!).get()
                this.destination = dockRepository.findById(form.destination!!).get()
                this.eta = form.eta
                this.name = form.name
                this.price = form.price
            }
        }else {
            routes = OperationRoutes().apply {
                this.origin = dockRepository.findById(form.origin!!).get()
                this.destination = dockRepository.findById(form.destination!!).get()
                this.eta = form.eta
                this.name = form.name
                this.price = form.price
            }
        }
        return repository.save(routes)
    }

    override fun deleteAll(id: List<Long>) {
        repository.deleteAll(repository.findAllById(id))
    }

    override fun findAll(pageable: Pageable?): Page<OperationRoutes>? {
        TODO("Not yet implemented")
    }

    override fun findAll(): MutableIterable<OperationRoutes>? {
        return repository.findAll()
    }

    override fun findById(id: Long): OperationRoutes {
        return repository.findById(id).get()
    }

    override fun save(entity: OperationRoutes): OperationRoutes {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long) {
        return repository.delete(repository.findById(id).get())
    }

}