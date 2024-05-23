package com.yan.demo.common.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.IOException;

/**
 * @Author: sixcolor
 * @Date: 2024-05-22 16:20
 * @Description:
 */
public class PdfPageUtil extends PdfPageEventHelper {

    private Font footerFont;

    public PdfPageUtil() {
        try {
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
            footerFont = new Font(bf, 10, Font.NORMAL);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        Phrase footer = new Phrase("Page " + document.getPageNumber(), footerFont);
        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                footer,
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.bottom() - 10, 0);
    }
}

