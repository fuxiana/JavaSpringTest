package com.xiaofutest.controller;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.io.resource.Resource;
import com.xiaofutest.Config.token.PassToken;
import com.xiaofutest.model.LogisticsDTO;
import com.xiaofutest.repository.BookDB;
import com.xiaofutest.unit.BaseResponse;
import com.xiaofutest.unit.ErrorCode;
import com.xiaofutest.unit.ResultUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author :xs
 * @date : 2024/9/25
 */

@RestController
@RequestMapping("/book")
public class LogisticsRequest<IWorksheet> {
    // 目标文件路径
    private static final Resource EXCEL_PATH = new ClassPathResource("汇丰4.14-4.20纯销.xlsx");

    @GetMapping("/list")
    public BaseResponse<List<LogisticsDTO>> list()  {
        List<LogisticsDTO> logisticsDTOS = new ArrayList<>();
        logisticsDTOS = BookDB.query();
        return ResultUtils.success(logisticsDTOS);
    }

    @PostMapping("/submit")
    public BaseResponse<Boolean>  submit(HttpServletRequest request, @RequestBody LogisticsDTO logisticsDTO ){
        if(logisticsDTO.getId() == null){
            return  ResultUtils.success(BookDB.add(logisticsDTO));
        }else {
            return  ResultUtils.success(BookDB.edit(logisticsDTO));
        }
    }

    @GetMapping("/getExcel")
    public List<List<String>> getExcel(HttpServletRequest request){
        String filePath = "/Users/fuxianan/Desktop/后端项目/spring-root-test/src/main/resources/风险提醒清单Excel.xlsx";
        String sheetName = "风险清单";
        String targetSupplier = "供应商【八匹马新能源科技有限公司】";
        int targetColumnIndex = 5; // F列（处置建议）

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet不存在: " + sheetName);
            }

            // 遍历数据行（假设数据从第6行开始）
            for (int rowIndex = 5; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;

                // 匹配供应商名称
                Cell supplierCell = row.getCell(0);
                if (supplierCell == null) continue;
                String supplierValue = supplierCell.getStringCellValue().trim();

                if (supplierValue.equals(targetSupplier)) {
                    Cell adviceCell = row.getCell(targetColumnIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    adviceCell.setCellValue("1");
                    System.out.println("成功修改行：" + (rowIndex + 1));
                }
            }

            // 保存修改
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
                System.out.println("文件已更新至: " + filePath);
            }

        } catch (Exception e) {
            System.err.println("操作失败，请检查路径或文件权限:");
            e.printStackTrace();
        }
        return  null;
    }

    @PostMapping("/generateExcel")
    public ResponseEntity<org.springframework.core.io.Resource> generateExcel(@RequestBody LogisticsDTO logisticsDTO) {
        try (Workbook workbook = new XSSFWorkbook()) {
            // 1. 创建 Sheet 和表头
            Sheet sheet = workbook.createSheet("纯销数据");
            createHeaderAndData(sheet, logisticsDTO.getResultData());

            // 2. 确保目标目录存在
            File file = new File(EXCEL_PATH.getUrl().getFile());
            file.getParentFile().mkdirs();

            // 3. 写入文件
            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);

                // 创建文件资源对象
                org.springframework.core.io.Resource resource = new FileSystemResource(file);


                // 设置响应头
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
                headers.setContentLength(file.length());
                headers.setContentDisposition(ContentDisposition.attachment()
                        .filename(resource.getFilename(), StandardCharsets.UTF_8)
                        .build());

                // 返回文件和响应头
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(resource);
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }
    // 创建表头并填充数据
    private void createHeaderAndData(Sheet sheet, Map<String, Map<String, String>> data) {
        // 收集所有药品名称
        Set<String> drugs = new TreeSet<>();
        data.values().forEach(map -> drugs.addAll(map.keySet()));
        List<String> drugList = new ArrayList<>(drugs);

        // 创建表头行
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("药店名称");
        for (int i = 0; i < drugList.size(); i++) {
            headerRow.createCell(i + 1).setCellValue(drugList.get(i));
        }

        // 填充数据行
        int rowNum = 1;
        for (Map.Entry<String, Map<String, String>> entry : data.entrySet()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(entry.getKey()); // 药店名称
            Map<String, String> drugData = entry.getValue();

            for (int i = 0; i < drugList.size(); i++) {
                String drug = drugList.get(i);
                String value = drugData.getOrDefault(drug, "0");
                row.createCell(i + 1).setCellValue(value);
            }
        }

        // 自动调整列宽
        for (int i = 0; i <= drugList.size(); i++) {
            sheet.autoSizeColumn(i);
        }
    }
}
