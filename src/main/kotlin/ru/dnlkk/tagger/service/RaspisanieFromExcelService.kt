package ru.dnlkk.tagger.service

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.util.ResourceUtils
import ru.dnlkk.tagger.entity.Group
import ru.dnlkk.tagger.entity.LessonTime
import ru.dnlkk.tagger.util.parseLessonString
import java.io.FileInputStream
import java.io.InputStream
import java.time.DayOfWeek

@Service
class RaspisanieFromExcelService(private val lessonService: LessonService) {

    fun readExcelFromFile(resource: Resource): List<List<String>> {
        val inputStream: InputStream = resource.inputStream
        return readExcel(inputStream)
    }

    private fun readExcel(inputStream: InputStream): List<List<String>> {
        val workbook: Workbook = XSSFWorkbook(inputStream)
        val sheet: Sheet = workbook.getSheetAt(0)

        val groups = Array(sheet.getRow(0).count() - 2) { Group() }

        val result = mutableListOf<List<String>>()
        for (i in 0..2) {
            val row = sheet.getRow(i)
            var course = 1
            var group = "1"
            var podgroup = 1
            var direction = "Направление \"Информационные системы и технологии\""
            for (cellIdx in 0 until row.count() - 2) {
                val cell = row.getCell(cellIdx + 2)
                val cellValue = getCellValue(cell)
                when (i) {
                    0 -> {
                        if (cellValue != "") {
                            val newCourse = cellValue.split(" ")[0].toInt()
                            if (newCourse != course) {
                                course = newCourse
                            }
                        }
                        groups[cellIdx].course = course
                    }

                    1 -> {
                        if (cellValue != "") {
                            val newGroup = cellValue.split(" ")[0]
                            if (newGroup != group) {
                                group = newGroup
                                podgroup = 1
                            }
                        }
                        groups[cellIdx].groupId = group
                        groups[cellIdx].subgroup = podgroup
                        podgroup++
                    }

                    2 -> {
                        if (cellValue != "") {
                            val newDirection = cellValue
                            if (newDirection != direction) {
                                direction = newDirection
                            }
                        }
                    }
                }
            }
        }

        var counter = 0
        var dayOfWeek = 1
            for (i in 4 until sheet.physicalNumberOfRows) {
            if (counter == 16) {
                counter = 0
                dayOfWeek++
                continue
            }

            val row = sheet.getRow(i)
            for (cellIdx in 0 until sheet.getRow(0).physicalNumberOfCells - 2) {
                val cell = row.getCell(cellIdx + 2)
                val cellValue = if (cell != null) {
                    if (isMergedCell(cell)) {
                        val firstCell = getFirstCellFromMergedRegion(sheet, cell)
                        getCellValue(firstCell)
                    } else {
                        getCellValue(cell)
                    }
                } else {
                    "Военная подготовка"
                }

                val (subject, teacher, location) = parseLessonString(cellValue)
                if (subject.isNotBlank()) {
                    lessonService.saveLessonWithStudy(
                        name = subject,
                        lessonTime = LessonTime.values()[Math.floorDiv(counter, 2)],
                        week = Math.floorMod(counter, 2),
                        auditoriumId = location,
                        lectorFio = teacher,
                        course = groups[cellIdx].course,
                        groupId = groups[cellIdx].groupId,
                        subgroup = groups[cellIdx].subgroup,
                        dayOfWeek = DayOfWeek.of(dayOfWeek),
                    )
                }
            }
            counter++
        }
        workbook.close()
        return result
    }

    private fun getCellValue(cell: Cell?): String {
        if (cell == null) {
            return "Военная подготовка"
        }
        return when (cell.cellType) {
            CellType.STRING -> cell.stringCellValue
            CellType.NUMERIC -> cell.numericCellValue.toString()
            CellType.BOOLEAN -> cell.booleanCellValue.toString()
            else -> ""
        }
    }

    fun isMergedCell(cell: Cell): Boolean {
        val sheet = cell.sheet
        val mergedRegions = sheet.numMergedRegions
        for (i in 0 until mergedRegions) {
            val mergedRegion = sheet.getMergedRegion(i)
            if (cell.rowIndex in mergedRegion.firstRow..mergedRegion.lastRow &&
                cell.columnIndex in mergedRegion.firstColumn..mergedRegion.lastColumn
            ) {
                return true
            }
        }
        return false
    }

    fun getFirstCellFromMergedRegion(sheet: Sheet, cell: Cell): Cell {
        val mergedRegions = sheet.numMergedRegions
        for (i in 0 until mergedRegions) {
            val mergedRegion = sheet.getMergedRegion(i)
            if (cell.rowIndex in mergedRegion.firstRow..mergedRegion.lastRow &&
                cell.columnIndex in mergedRegion.firstColumn..mergedRegion.lastColumn
            ) {
                return sheet.getRow(mergedRegion.firstRow).getCell(mergedRegion.firstColumn)
            }
        }
        throw IllegalStateException("Cell is not part of a merged region")
    }

}