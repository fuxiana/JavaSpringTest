package com.xiaofutest.model.request;


import com.xiaofutest.model.LogisticsDTO;

import java.util.List;

/**
 * @author :xs
 * @date : 2024/10/28
 */
public class PdfTableRequest {

    /**
     * PDF LIST上传
     */
    List<LogisticsDTO> pdfTableList;

    public List<LogisticsDTO> getPdfTableList() {
        return pdfTableList;
    }

    public void setPdfTableList(List<LogisticsDTO> pdfTableList) {
        this.pdfTableList = pdfTableList;
    }
}
