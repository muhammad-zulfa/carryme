package com.carryme.services.impl

import com.carryme.dto.requests.RouteRequestDto
import com.carryme.entities.Routes
import com.carryme.repositories.DockRepository
import com.carryme.repositories.OperationRouteRepository
import com.carryme.repositories.RouteRepository
import com.carryme.services.IRouteService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class RouteService: IRouteService{

    @Autowired
    private lateinit var repository: RouteRepository
    @Autowired
    private lateinit var dockRepository: DockRepository
    @Autowired
    private lateinit var operationRouteRepository: OperationRouteRepository

    override fun findAllByName(routeOperation: Long, search: String, pgbl: Pageable): Page<Routes> {
        return repository.findAllByOperationRoutesIdAndNameLike(routeOperation,search,pgbl)
    }

    override fun submit(form: RouteRequestDto): Routes {
        var routes = Routes()
        if (form.id != null){
            routes = repository.findById(form.id!!).get().apply {
                this.origin = dockRepository.findById(form.origin!!).get()
                this.destination = dockRepository.findById(form.destination!!).get()
                this.eta = form.eta
                this.name = form.name
                this.price = form.price
                this.assuranceFee = form.assuranceFee
                this.retributionFee = form.retributionFee
                this.operationRoutes = operationRouteRepository.findById(form.operationRoutesId!!).get()
            }
        }else {
            routes = Routes().apply {
                this.origin = dockRepository.findById(form.origin!!).get()
                this.destination = dockRepository.findById(form.destination!!).get()
                this.eta = form.eta
                this.name = form.name
                this.price = form.price
                this.assuranceFee = form.assuranceFee
                this.retributionFee = form.retributionFee
                this.operationRoutes = operationRouteRepository.findById(form.operationRoutesId!!).get()
            }
        }
        return repository.save(routes)
    }

    override fun deleteAll(id: List<Long>) {
        repository.deleteAll(repository.findAllById(id))
    }

    override fun findAllByOperation(operation: Long): List<Routes> {
        return repository.findAllByOperationRoutesId(operation)
    }

    override fun findAll(pageable: Pageable?): Page<Routes>? {
        TODO("Not yet implemented")
    }

    override fun findAll(): MutableIterable<Routes>? {
        return repository.findAll()
    }

    override fun findById(id: Long): Routes {
        return repository.findById(id).get()
    }

    override fun save(entity: Routes): Routes {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long) {
        return repository.delete(repository.findById(id).get())
    }

}