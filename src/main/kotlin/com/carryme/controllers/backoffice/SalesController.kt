package com.carryme.controllers.backoffice

import com.carryme.controllers.BaseController
import com.carryme.dto.requests.DockRequestDto
import com.carryme.dto.requests.Mail
import com.carryme.dto.response.BaseResponse
import com.carryme.dto.response.SalesStatusRequestDto
import com.carryme.entities.Sales
import com.carryme.services.ISalesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.http.ResponseEntity

import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.util.StreamUtils

import org.springframework.web.bind.annotation.RequestMethod

import org.springframework.web.bind.annotation.RequestMapping
import java.io.IOException
import java.net.MalformedURLException

import org.springframework.core.io.UrlResource
import org.springframework.http.HttpHeaders
import java.nio.file.Path
import java.nio.file.Paths
import java.util.HashMap
import javax.servlet.http.HttpServletRequest


@RequestMapping("/backoffice/sales")
@RestController
@CrossOrigin(maxAge = 3600, origins = ["*"])
class SalesController(
    @Autowired
    val service: ISalesService
) : BaseController() {
    @RequestMapping("", method = [RequestMethod.GET])
    fun find(
        @RequestParam(value = "search") search: String,
        @RequestParam(value = "pageNumber") page: Int,
        @RequestParam(value = "sortOrder") sortOrder: String,
        @RequestParam(value = "sortField") sortField: String,
        @RequestParam(value = "pageSize") pageSize: Int,
    ): BaseResponse {
        val sort: Sort = if (sortOrder == "desc") Sort.by(sortField).descending() else Sort.by(sortField).ascending()
        val pgbl: Pageable = PageRequest.of(page, pageSize, sort)
        val pages: Page<Sales> = service.findAllByName("%$search%", pgbl)
        return successResponse(pages)!!
    }

    @RequestMapping("/{id}/submit-proof", method = [RequestMethod.POST, RequestMethod.PUT])
    fun submit(
        @PathVariable id: Long,
        @RequestParam("file") file: MultipartFile
    ): BaseResponse {
        return successResponse(service.submitProof(id, file))!!
    }

    @RequestMapping("/{id}", method = [RequestMethod.GET])
    fun get(
        @PathVariable("id") id: Long
    ): BaseResponse {
        return successResponse(service.detail(id))!!
    }

    @RequestMapping("/{id}/update-status", method = [RequestMethod.POST, RequestMethod.PUT])
    fun submit(
        @PathVariable id: Long,
        @RequestBody status: SalesStatusRequestDto
    ): BaseResponse {
        return successResponse(service.updateStatus(id, status.status!!))!!
    }

    @RequestMapping(
        value = ["/proof"],
        method = [RequestMethod.GET],
        produces = [MediaType.IMAGE_JPEG_VALUE]
    )
    @Throws(
        IOException::class
    )
    fun getImage(
            @RequestParam("path") pat: String,
            request: HttpServletRequest
    ): ResponseEntity<Resource?>? {
        val path: Path = Paths.get(pat).toAbsolutePath()

        var resource: Resource
        try {
            resource = UrlResource(path.toUri())
        } catch (e: MalformedURLException) {
            throw RuntimeException("Issue in reading the file", e)
        }

        var mimeType: String
        try {
            mimeType = request.getServletContext().getMimeType(resource.getFile().absolutePath)
        } catch (e: IOException) {
            mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE
        }
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(mimeType))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName="+resource.getFilename())
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName=" + resource.getFilename())
            .body(resource)

//        val imgFile = ClassPathResource(path)
//        val bytes: ByteArray = StreamUtils.copyToByteArray(imgFile.inputStream)
//        return ResponseEntity
//            .ok()
//            .contentType(MediaType.IMAGE_JPEG)
//            .body(bytes)
    }
}