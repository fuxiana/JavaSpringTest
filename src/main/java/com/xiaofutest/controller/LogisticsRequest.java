package com.xiaofutest.controller;

import com.alibaba.fastjson2.JSON;
import com.xiaofutest.model.LogisticsDTO;
import com.xiaofutest.repository.BookDB;
import com.xiaofutest.unit.BaseResponse;
import com.xiaofutest.unit.ResultUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author :xs
 * @date : 2024/9/25
 */

@RestController
@RequestMapping("/book")
public class LogisticsRequest<IWorksheet> {

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
        URL xmlPath = getClass().getClassLoader().getResource("风险提醒清单Excel.xlsx");
        List<List<String>> lists = new ArrayList<>();
        try {
            InputStream is = new FileInputStream(URLDecoder.decode(xmlPath.getPath(),"UTF-8"));
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            // 获取sheet表
            Sheet sheet = workbook.getSheet("风险清单");
            for (Row row:sheet){
                List<String> objects = new ArrayList<>();
                for(Cell cell: row){
                    objects.add(cell.getStringCellValue());
                }
                lists.add(objects);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  lists;
    }
}
