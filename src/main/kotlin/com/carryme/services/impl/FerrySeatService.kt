package com.carryme.services.impl

import com.carryme.dto.requests.FerrySeatGenerateRequestDto
import com.carryme.dto.requests.FerrySeatRequestDto
import com.carryme.entities.FerrySeats
import com.carryme.repositories.FerryRepository
import com.carryme.repositories.FerrySeatRepository
import com.carryme.services.IFerrySeatService
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class FerrySeatService: IFerrySeatService{
    @Autowired
    lateinit var repository: FerrySeatRepository

    @Autowired
    lateinit var ferryRepository: FerryRepository
    override fun findAllByName(name: String, pageable: Pageable): Page<FerrySeats> {
        TODO("Not yet implemented")
    }

    override fun findAllByShipIdAndDeckNumber(shipId: Long, deck: Int?): Map<String,List<List<FerrySeats>>> {
        val resp : MutableMap<String,List<List<FerrySeats>>> = mutableMapOf()
        var seats: List<FerrySeats> = listOf()
        if (deck == null){
            seats =  repository.findAllByFerryIdOrderByDeckNumberAscSeatRowAscSeatCodeAsc(shipId)
        }else{
            seats = repository.findAllByDeckNumberAndFerryIdOrderByDeckNumberAscSeatRowAscSeatCodeAsc(deck, shipId)
        }
        val mergedRows: MutableList<List<FerrySeats>> = mutableListOf()
        val rows: MutableList<FerrySeats> = mutableListOf()
        var temp = ""
        seats.forEach {
            val deck = "deck${it.deckNumber}"
            if(temp == "") temp = deck
            else if(temp != deck){
                resp.put(temp,mergedRows)
                mergedRows.clear()
                temp = deck
            }
            rows.add(it)
            if(rows.size == 6){
                mergedRows.add(rows.toList())
                rows.clear()
                resp.put(deck,mergedRows)
            }
        }
        if(rows.size > 0){
            mergedRows.add(rows.toList())
            resp.put(temp,mergedRows)
        }
        return resp

    }


    override fun submit(shipId: Long, form: FerrySeatRequestDto): FerrySeats {
        val ferry = when(form.id){
            null -> FerrySeats()
            else -> repository.findById(form.id!!).get()
        }.apply {
            ferry = ferryRepository.findById(shipId).get()
        }
        BeanUtils.copyProperties(form,ferry)
        return repository.save(ferry)
    }

    override fun generate(ferryId: Long, form: FerrySeatGenerateRequestDto): List<FerrySeats> {
        val seats: MutableList<FerrySeats> = mutableListOf()
        var rowArray = arrayOf<String>("A","B","C","D","E","F","G","H","I","J","K","L","M",)
        var row = 0;
        val exists = repository.findAllByDeckNumberAndFerryIdOrderByDeckNumberAscSeatRowAscSeatCodeAsc(form.deckNumber,ferryId)
        repository.deleteAll(exists)
        var col = 1;
        for(x in 1..form.totalSeat){
            val seat = FerrySeats()
            seat.ferry = ferryRepository.findById(ferryId).get()
            seat.deckNumber = form.deckNumber
            seat.seatRow = rowArray[row]
            seat.seatCode = col
            seats.add( seat )
            col++
            if(x % 6 == 0){
                row++
                col = 1
            }
        }

        return repository.saveAll(seats).toList()
    }

    override fun update(id: Long, form: FerrySeatRequestDto): FerrySeats {
        val seat = repository.findById(id).get()
        BeanUtils.copyProperties(form,seat)
        return repository.save(seat)
    }

    override fun findAll(pageable: Pageable?): Page<FerrySeats>? {
        TODO("Not yet implemented")
    }

    override fun findAll(): MutableIterable<FerrySeats>? {
        TODO("Not yet implemented")
    }

    override fun findById(id: Long): FerrySeats {
        TODO("Not yet implemented")
    }

    override fun save(entity: FerrySeats): FerrySeats {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }


}