package com.carryme.services.impl

import com.carryme.dto.response.DashboardDTO
import com.carryme.entities.TicketSales
import com.carryme.repositories.SalesRepository
import com.carryme.repositories.TicketSalesRepository
import com.carryme.repositories.UserRepository
import com.carryme.services.ITicketSalesService
import org.apache.poi.ss.usermodel.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.math.BigInteger

import org.apache.poi.xssf.usermodel.XSSFWorkbook

import org.apache.poi.xssf.usermodel.XSSFFont
import org.apache.poi.ss.usermodel.Cell

import org.apache.poi.ss.usermodel.CellStyle
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


@Service
class TicketSalesService(
    @Autowired
    val salesRepository: SalesRepository,
    @Autowired
    val userRepository: UserRepository,
    @Autowired
    val repository: TicketSalesRepository
): ITicketSalesService{
    override fun getDataCount(): DashboardDTO {

        val countSales = salesRepository.findCountDataPerDay()
        val incomeSales = salesRepository.findIncomeDataPerDay()
        val newUsers = userRepository.findNewDataPerDay()
        val dateAxis = mutableListOf<String>()
        val countSalesAxis = mutableListOf<BigInteger>()
        val incomeSalesAxis = mutableListOf<BigInteger>()
        val newUsersAxis = mutableListOf<BigInteger>()
        countSales.forEach {
            dateAxis.add(it.get("date").toString())
            countSalesAxis.add(it.get("count")!!)
        }
        incomeSales.forEach {
            incomeSalesAxis.add(BigInteger.valueOf((it.get("sum")!!.toInt() / 1000).toLong()))
        }
        newUsers.forEach {
            newUsersAxis.add(it.get("count")!!)
        }

        return DashboardDTO(countSalesAxis, incomeSalesAxis, newUsersAxis, dateAxis)
    }

    override fun getReport(dateStart: String, dateEnd: String): String {
        val workbook: Workbook = XSSFWorkbook()

        val sheet: Sheet = workbook.createSheet("Sales")
        sheet.setColumnWidth(0, 6000)
        sheet.setColumnWidth(1, 4000)
        val formatDate = SimpleDateFormat("yyyy-MM-dd")
        val start = formatDate.parse(dateStart)
        val end = formatDate.parse(dateEnd)
        val data = repository.findReport("finish",start,end)
        //header
        val header: Row = sheet.createRow(0)

        val headerStyle = workbook.createCellStyle()
        headerStyle.fillForegroundColor = IndexedColors.LIGHT_BLUE.getIndex()
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND)

        val font = (workbook as XSSFWorkbook).createFont()
        font.fontName = "Arial"
        font.fontHeightInPoints = 16.toShort()
        font.bold = true
        headerStyle.setFont(font)

        var headerCell: Cell = header.createCell(0)
        headerCell.setCellValue("Customer Name")
        headerCell.cellStyle = headerStyle

        headerCell = header.createCell(1)
        headerCell.setCellValue("Customer NIK")
        headerCell.cellStyle = headerStyle

        headerCell = header.createCell(2)
        headerCell.setCellValue("Customer Phone")
        headerCell.cellStyle = headerStyle

        headerCell = header.createCell(3)
        headerCell.setCellValue("Customer Emergency Number")
        headerCell.cellStyle = headerStyle

        headerCell = header.createCell(4)
        headerCell.setCellValue("Customer Type")
        headerCell.cellStyle = headerStyle

        headerCell = header.createCell(5)
        headerCell.setCellValue("Travel Route")
        headerCell.cellStyle = headerStyle

        headerCell = header.createCell(6)
        headerCell.setCellValue("Ticket Price")
        headerCell.cellStyle = headerStyle

        headerCell = header.createCell(7)
        headerCell.setCellValue("Peron Retribution")
        headerCell.cellStyle = headerStyle

        headerCell = header.createCell(8)
        headerCell.setCellValue("Assurance Fee")
        headerCell.cellStyle = headerStyle

        headerCell = header.createCell(9)
        headerCell.setCellValue("Ship Name")
        headerCell.cellStyle = headerStyle

        headerCell = header.createCell(10)
        headerCell.setCellValue("Departure Time")
        headerCell.cellStyle = headerStyle

        // content
        val style = workbook.createCellStyle()
        style.wrapText = true
        var rows = 1
        val formatDateR = SimpleDateFormat("yyyy-MM-dd HH:mm")
        data.forEach {
            val row = sheet.createRow(rows)
            var cell = row.createCell(0)
            cell.setCellValue(it.user!!.fullname)
            cell.cellStyle = style

            cell = row.createCell(1)
            cell.setCellValue(it.user!!.nik)
            cell.cellStyle = style

            cell = row.createCell(2)
            cell.setCellValue(it.user!!.phone)
            cell.cellStyle = style

            cell = row.createCell(3)
            cell.setCellValue(it.user!!.phoneEmerg)
            cell.cellStyle = style

            cell = row.createCell(4)
            cell.setCellValue(it.user!!.paxType)
            cell.cellStyle = style

            cell = row.createCell(5)
            cell.setCellValue("${it.ticket!!.routes!!.origin!!.name} to ${it.ticket!!.routes!!.destination!!.name}")
            cell.cellStyle = style

            cell = row.createCell(6)
            cell.setCellValue(it.ticket!!.routes!!.price.toString())
            cell.cellStyle = style

            cell = row.createCell(7)
            cell.setCellValue(it.ticket!!.routes!!.retributionFee.toString())
            cell.cellStyle = style

            cell = row.createCell(8)
            cell.setCellValue(it.ticket!!.routes!!.assuranceFee.toString())
            cell.cellStyle = style

            cell = row.createCell(9)
            cell.setCellValue(it.ticket!!.ferry!!.name)
            cell.cellStyle = style

            cell = row.createCell(10)
            cell.setCellValue(formatDateR.format(it.ticket!!.departure))
            cell.cellStyle = style

            rows += 1
        }


        //write to file
        val currDir = File(".")
        val path: String = currDir.getAbsolutePath()
        val fileLocation = path.substring(0, path.length - 1) + "temp.xlsx"

        val outputStream = FileOutputStream(fileLocation)
        workbook.write(outputStream)
        workbook.close()

        return fileLocation
    }

    override fun checkedIn(id: Long): TicketSales {
        val data = repository.findById(id)
        if(data.isPresent){
            val ticket = data.get()
            ticket.checkedIn = true
            return ticket
        }
        return data.get()
    }

    override fun findAll(pageable: Pageable?): Page<TicketSales>? {
        TODO("Not yet implemented")
    }

    override fun findAll(): MutableIterable<TicketSales>? {
        TODO("Not yet implemented")
    }

    override fun findById(id: Long): TicketSales {
        return repository.findById(id).get()
    }

    override fun save(entity: TicketSales): TicketSales {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }

}