package com.xiaofutest.controller;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.xiaofutest.model.LogisticsDTO;
import com.xiaofutest.model.request.PdfTableRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


/**
 * @author :cainiao
 * @date : 2024/10/28
 */
@RestController
@RequestMapping("/common")
public class CommonRequest {

    @GetMapping("/getPDFData")
    public ResponseEntity<Resource> getPDFData() throws IOException {
        String html = "";
        // 输出 PDF 文件的路径
        String dest = "/Users/fuxianan/Desktop/houduan/springTest/src/main/resources/output.pdf";
        // 替换为您字体文件的路径
        String fontPath = "/Users/fuxianan/Desktop/houduan/springTest/src/main/resources/SimSun.ttf";


        // 读取 HTML 文件内容
        html = new String(Files.readAllBytes(Paths.get("/Users/fuxianan/Desktop/houduan/springTest/src/main/resources/STSong-Light字体文件下载_百度搜索.html")), "UTF-8");
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdfDoc = new PdfDocument(writer);
        // 创建 ConverterProperties
        ConverterProperties properties = new ConverterProperties();

        // 创建并设置 FontProvider
        DefaultFontProvider fontProvider = new DefaultFontProvider();
        fontProvider.addFont(fontPath );
        properties.setFontProvider(fontProvider);

        // 将 HTML 转换为 PDF

        HtmlConverter.convertToPdf(html, pdfDoc, properties);
        pdfDoc.close();
        return  exportPdf(dest);
    }

    public  ResponseEntity<Resource>  exportPdf( String pdfFilePath){
        File pdfFile = new File(pdfFilePath);

        // 检查文件是否存在
        if (!pdfFile.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // 返回 PDF 文件作为响应
        Resource resource = new FileSystemResource(pdfFile);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + pdfFile.getName());

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfFile.length())
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @PostMapping("/setNovelList")
    public Boolean setNovelList(@RequestBody PdfTableRequest pdfTableRequest) throws IOException {
        String html = "/Users/fuxianan/Desktop/houduan/springTest/src/main/resources/STSong-Light字体文件下载_百度搜索.html";

        // 读取 HTML 文件
        String htmlContent = null;
        try {
            htmlContent = new String(Files.readAllBytes(Paths.get(html)), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document doc = Jsoup.parse(htmlContent);

        // 1. 查：查找所有的表格
        Elements tables = doc.select("table");


        // 2. 增：添加一行到第一个表格
        Element firstTable = tables.first();

        List <LogisticsDTO> logisticsDTOS ;
        if(pdfTableRequest == null || CollectionUtils.isEmpty(pdfTableRequest.getPdfTableList())){
            logisticsDTOS = new ArrayList<>(){{
                add( new LogisticsDTO(1,"平凡的世界","1986年","路遥","孙少安、孙少平",null));
                add( new LogisticsDTO(2,"繁花","2012年","金宇澄","沪生、阿宝、小毛",null));
            }};
        }else {
            logisticsDTOS = pdfTableRequest.getPdfTableList();
        }
        logisticsDTOS.stream().forEach(e->{
            Element newRow = firstTable.appendElement("tr");
            newRow.appendElement("td").text(e.getTitle());
            newRow.appendElement("td").text(e.getProtagonist());
            newRow.appendElement("td").text(e.getAuthor());
            newRow.appendElement("td").text(e.getYear());
        });
        Files.write(Paths.get(html), doc.outerHtml().getBytes("UTF-8"));
        return  true;
    }
}
