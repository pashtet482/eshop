package com.doc_byte.eshop.orders.service;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.doc_byte.eshop.orders.model.Orders;
import com.doc_byte.eshop.orders.model.OrderItems;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class ReceiptPdfGenerator {

    public byte[] generate(@NotNull Orders order, @NotNull List<OrderItems> items) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Чек заказа #" + order.getId()));
        document.add(new Paragraph("Дата: " + order.getCreatedAt()));
        document.add(new Paragraph("Статус: " + order.getStatus()));
        document.add(new Paragraph("Пользователь ID: " + order.getUser().getId()));
        document.add(new Paragraph("------------------------"));

        for (OrderItems item : items) {
            document.add(new Paragraph(
                    "Товар ID: " + item.getProduct().getId()
                            + " | Кол-во: " + item.getQuantity()
                            + " | Цена: " + item.getPriceAtPurchase()
            ));
        }

        document.add(new Paragraph("------------------------"));
        document.add(new Paragraph("Итого: " + order.getTotalPrice()));

        document.close();

        return out.toByteArray();
    }
}
