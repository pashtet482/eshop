package com.doc_byte.eshop.orders.service;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.doc_byte.eshop.orders.model.Orders;
import com.doc_byte.eshop.orders.model.OrderItems;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Component
public class ReceiptPdfGenerator {

    public byte[] generate(@NotNull Orders order, @NotNull List<OrderItems> items) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            String fontPath = "src/main/resources/fonts/arial.ttf";
            PdfFont font = PdfFontFactory.createFont(fontPath);
            document.setFont(font);

            Paragraph header = new Paragraph("Чек заказа #" + order.getId())
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(15f);
            document.add(header);

            ZoneId zoneId = ZoneId.of("Europe/Moscow");
            String formattedDate = order.getCreatedAt()
                    .atZone(zoneId)
                    .format(DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm", Locale.of("ru", "RU")));

            Paragraph orderInfo = new Paragraph()
                    .add("Дата: " + formattedDate + "\n")
                    .add("Статус: " + translatePending(order.getStatus().toString()) + "\n")
                    .add("Пользователь: " + order.getUser().getUsername())
                    .setFontSize(12)
                    .setMarginBottom(15f);
            document.add(orderInfo);

            float[] columnWidths = {250F, 80F, 80F};
            Table table = new Table(columnWidths);
            table.setWidth(UnitValue.createPercentValue(100));

            table.addHeaderCell(new Cell().add(new Paragraph("Товар").setBold()).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addHeaderCell(new Cell().add(new Paragraph("Количество").setBold()).setBackgroundColor(ColorConstants.LIGHT_GRAY).setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Цена, руб").setBold()).setBackgroundColor(ColorConstants.LIGHT_GRAY).setTextAlignment(TextAlignment.RIGHT));

            for (OrderItems item : items) {
                table.addCell(new Cell().add(new Paragraph(item.getProduct().getName())));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(item.getQuantity()))).setTextAlignment(TextAlignment.CENTER));
                String priceStr = String.format("%.2f", item.getPriceAtPurchase());
                table.addCell(new Cell().add(new Paragraph(priceStr)).setTextAlignment(TextAlignment.RIGHT));
            }

            document.add(table);

            document.add(new Paragraph("\n"));

            String totalPriceStr = String.format("%.2f", order.getTotalPrice());
            Paragraph total = new Paragraph("Итого: " + totalPriceStr + " руб")
                    .setFontSize(14)
                    .setBold()
                    .setTextAlignment(TextAlignment.RIGHT);
            document.add(total);

            document.close();
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Ошибка генерации PDF", e);
        }
    }

    @Contract(pure = true)
    private @NotNull String translatePending(@NotNull String status){
        return switch (status) {
            case "SHIPPED" ->
                    "Отправлен";
            case "DELIVERED" ->
                    "Доставлен";
            case "CANCELLED" ->
                    "Отменен";
            default ->
                    "Оформлен";
        };
    }
}

