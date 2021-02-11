package com.carryme.services.impl

import com.carryme.dto.requests.DockRequestDto
import com.carryme.dto.requests.FerrySeatRequestDto
import com.carryme.dto.requests.RouteRequestDto
import com.carryme.entities.Docks
import com.carryme.entities.FerrySeats
import com.carryme.entities.Routes
import com.carryme.repositories.DockRepository
import com.carryme.repositories.FerrySeatRepository
import com.carryme.repositories.RouteRepository
import com.carryme.services.IFerryDetailService
import com.carryme.services.IRouteService
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.router

@Service
class RouteService: IRouteService{

    private lateinit var repository: RouteRepository
    private lateinit var dockRepository: DockRepository

    override fun findAllByName(search: String, pgbl: Pageable): Page<Routes> {
        return repository.findAllByNameLike(search,pgbl)
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
            }
        }else {
            routes = Routes().apply {
                this.origin = dockRepository.findById(form.origin!!).get()
                this.destination = dockRepository.findById(form.destination!!).get()
                this.eta = form.eta
                this.name = form.name
                this.price = form.price
            }
        }
        return repository.save(routes)
    }

    override fun findAll(pageable: Pageable?): Page<Routes>? {
        TODO("Not yet implemented")
    }

    override fun findAll(): MutableIterable<Routes>? {
        TODO("Not yet implemented")
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