package cn.hiio.mall.cheap.common.utils

import org.apache.commons.io.FileUtils
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.*
import java.net.URLEncoder
import java.util.*
import javax.servlet.http.HttpServletResponse


class CSVFileUtil {
    companion object {
        /**
         * 创建CSV文件
         */
        @Throws(UnsupportedEncodingException::class)
        fun createCSV(head: Array<String>, dataList: List<List<String>>, fileName: String): File { // 表格头
            val headList = Arrays.asList<Any>(*head)
            var filePath = FileUtils.getTempDirectoryPath() + fileName
            var fileOutputStream = FileOutputStream(filePath)
//            var outputStreamWriter = OutputStreamWriter(fileOutputStream,"GB2312")

            //数据
            val csvFile: File? = null
            var csvWtriter: BufferedWriter? = null
            try { //            csvFile = new File(filePath + fileName);
                csvWtriter = BufferedWriter(OutputStreamWriter(fileOutputStream, "UTF-8"), 1024)
                // 写入文件头部
                writeRow(headList, csvWtriter)
                // 写入文件内容
                dataList.forEach {
                    writeRow(it, csvWtriter)
                }
                csvWtriter.flush()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    csvWtriter!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return File(filePath)
        }

        /**
         * 创建CSV文件
         */
        @Throws(UnsupportedEncodingException::class)
        fun createCSV(head: Array<String>, dataList: List<List<String>>, response: HttpServletResponse, fileName: String) { // 表格头
            val headList = Arrays.asList<Any>(*head)
            //数据
            val csvFile: File? = null
            var csvWtriter: BufferedWriter? = null
            try { //            csvFile = new File(filePath + fileName);
//            csvWtriter = new BufferedWriter(new OutputStreamWriter(new
// FileOutputStream(csvFile), "GB2312"), 1024);
//文件下载，使用如下代码
                response.contentType = "application/ms-txt.numberformat:@"
                response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"))
                val out = response.outputStream
                csvWtriter = BufferedWriter(OutputStreamWriter(out, "GB2312"), 1024)
// 写入文件头部
                writeRow(headList, csvWtriter)
                // 写入文件内容
                // 写入文件内容
                for (row in dataList) {
                    writeRow(row, csvWtriter)
                }
                csvWtriter.flush()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    csvWtriter!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        /**
         * 写一行数据
         * @param row 数据列表
         * @param csvWriter
         * @throws IOException
         */
        @Throws(IOException::class)
        private fun writeRow(row: List<Any>, csvWriter: BufferedWriter) {
            for (i in row.indices) {
                val data = row[i]
                val sb = StringBuffer()
                sb.append("\"").append(data)
                if (i == row.size - 1) {
                    sb.append("\"")
                } else {
                    sb.append("\",")
                }
                csvWriter.write(sb.toString())
            }
            csvWriter.newLine()
        }
//        excel
        /**
         * 新建Excel文件，New Workbook
         * @param excelType 可为null，Excel版本，可为2003（.xls）或者2007（.xlsx）,默认为2003版本
         * @param sheetName 新建表单名称
         * @param headList 表头List集合
         * @param dataList 数据List<List></List><集合>>(行<列>)
         * @param path 新建excel路径
         * @return
        </列></集合> */
        fun createWorkBook(excelType: String?, sheetName: String, headList: List<String>, dataList: List<List<String>>, path: String) {
            var wb: Workbook? = null
            /*创建文件*/if (excelType == null || excelType.endsWith("2003")) { /*操作Excel2003以前（包括2003）的版本，扩展名是.xls */
                wb = HSSFWorkbook()
            } else if (excelType.endsWith("2007")) { /*XSSFWorkbook:是操作Excel2007的版本，扩展名是.xlsx */
                wb = XSSFWorkbook()
            } else { //默认为2003版本
/*操作Excel2003以前（包括2003）的版本，扩展名是.xls */
                wb = HSSFWorkbook()
            }
            /*Excel文件创建完毕*/
            val createHelper = wb!!.creationHelper //创建帮助工具
            /*创建表单*/
            val sheet: Sheet = wb.createSheet(sheetName ?: "new sheet")
            // Note that sheet name is Excel must not exceed 31 characters（注意sheet的名字的长度不能超过31个字符，若是超过的话，会自动截取前31个字符）
// and must not contain any of the any of the following characters:（不能包含下列字符）
// 0x0000  0x0003  colon (:)  backslash (\)  asterisk (*)  question mark (?)  forward slash (/)  opening square bracket ([)  closing square bracket (])
/*若是包含的话，会报错。但有一个解决此问题的方法，
        就是调用WorkbookUtil的createSafeSheetName(String nameProposal)方法来创建sheet name,
        若是有如上特殊字符，它会自动用空字符来替换掉，自动过滤。*/
/*String safeName = WorkbookUtil.createSafeSheetName("[O'Brien's sales*?]"); // returns " O'Brien's sales   "过滤掉上面出现的不合法字符
        Sheet sheet3 = workbook.createSheet(safeName); //然后就创建成功了*/
/*表单创建完毕*/ //设置字体
            val headFont: Font = wb.createFont()
            headFont.setFontHeightInPoints(14.toShort())
            headFont.setFontName("Courier New")
            headFont.setItalic(false)
            headFont.setStrikeout(false)
            //设置头部单元格样式
            val headStyle = wb.createCellStyle()
            headStyle.borderBottom = BorderStyle.THICK //设置单元格线条
            headStyle.bottomBorderColor = IndexedColors.BLACK.getIndex() //设置单元格颜色
            headStyle.borderLeft = BorderStyle.THICK
            headStyle.leftBorderColor = IndexedColors.BLACK.getIndex()
            headStyle.borderRight = BorderStyle.THICK
            headStyle.rightBorderColor = IndexedColors.BLACK.getIndex()
            headStyle.borderTop = BorderStyle.THICK
            headStyle.topBorderColor = IndexedColors.BLACK.getIndex()
            headStyle.alignment = HorizontalAlignment.CENTER //设置水平对齐方式
            headStyle.verticalAlignment = VerticalAlignment.CENTER //设置垂直对齐方式
            //headStyle.setShrinkToFit(true);  //自动伸缩
            headStyle.setFont(headFont) //设置字体
            /*设置数据单元格格式*/
            val dataStyle = wb.createCellStyle()
            dataStyle.borderBottom = BorderStyle.THIN //设置单元格线条
            dataStyle.bottomBorderColor = IndexedColors.BLACK.getIndex() //设置单元格颜色
            dataStyle.borderLeft = BorderStyle.THIN
            dataStyle.leftBorderColor = IndexedColors.BLACK.getIndex()
            dataStyle.borderRight = BorderStyle.THIN
            dataStyle.rightBorderColor = IndexedColors.BLACK.getIndex()
            dataStyle.borderTop = BorderStyle.THIN
            dataStyle.topBorderColor = IndexedColors.BLACK.getIndex()
            dataStyle.alignment = HorizontalAlignment.LEFT //设置水平对齐方式
            dataStyle.verticalAlignment = VerticalAlignment.CENTER //设置垂直对齐方式
            //dataStyle.setShrinkToFit(true);  //自动伸缩
/*创建行Rows及单元格Cells*/
            val headRow: Row = sheet.createRow(0) //第一行为头
            for (i in headList.indices) { //遍历表头数据
                val cell: Cell = headRow.createCell(i) //创建单元格
                cell.setCellValue(createHelper.createRichTextString(headList[i])) //设置值
                cell.setCellStyle(headStyle) //设置样式
            }
            var rowIndex = 1 //当前行索引
            //创建Rows
            for (rowdata in dataList) { //遍历所有数据
                val row: Row = sheet.createRow(rowIndex++) //第一行为头
                for (j in rowdata.indices) { //编译每一行
                    val cell: Cell = row.createCell(j)
                    cell.setCellStyle(dataStyle)
                    cell.setCellValue(createHelper.createRichTextString(rowdata[j]))
                }
            }
            /*创建rows和cells完毕*/ /*设置列自动对齐*/for (i in headList.indices) {
                sheet.autoSizeColumn(i)
            }
            try {
                FileOutputStream(path).use { fileOut ->
                    //获取文件流
                    wb.write(fileOut) //将workbook写入文件流
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


}