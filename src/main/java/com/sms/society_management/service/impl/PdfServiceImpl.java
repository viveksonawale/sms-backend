package com.sms.society_management.service.impl;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.sms.society_management.entity.Receipt;
import com.sms.society_management.exception.ResourceNotFoundException;
import com.sms.society_management.repository.ReceiptRepository;
import com.sms.society_management.service.PdfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class PdfServiceImpl implements PdfService {

    private static final DateTimeFormatter MONTH_YEAR_FMT = DateTimeFormatter.ofPattern("MMMM yyyy");
    private static final DateTimeFormatter DATE_FMT       = DateTimeFormatter.ofPattern("dd MMMM yyyy");
    private static final DateTimeFormatter DT_FMT         = DateTimeFormatter.ofPattern("dd MMM yyyy | hh:mm a");

    private final ReceiptRepository receiptRepository;
    private final TemplateEngine templateEngine;

    @Override
    @Transactional(readOnly = true)
    public byte[] generateReceiptPdf(Long receiptId) {
        log.info("Generating PDF for receipt id={}", receiptId);
        Receipt receipt = receiptRepository.findById(receiptId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Receipt not found with id: " + receiptId));

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            
            // 1. Prepare Thymeleaf Context
            Context context = new Context();
            context.setVariable("receiptNumber", receipt.getReceiptNumber());
            context.setVariable("generatedAt", receipt.getGeneratedAt().format(DT_FMT));
            
            context.setVariable("ownerName", receipt.getMaintenance().getOwner().getName());
            context.setVariable("flatNumber", receipt.getMaintenance().getOwner().getFlatNumber());
            
            String period = receipt.getMaintenance().getFromDate().format(MONTH_YEAR_FMT)
                    + " \u2794 " // Thick Right arrow
                    + receipt.getMaintenance().getToDate().format(MONTH_YEAR_FMT);
            context.setVariable("paymentPeriod", period);
            context.setVariable("numberOfMonths", receipt.getMaintenance().getNumberOfMonths());
            
            context.setVariable("amount", receipt.getMaintenance().getAmount().toPlainString());
            
            String paymentDate = receipt.getMaintenance().getPaymentDate() != null
                    ? receipt.getMaintenance().getPaymentDate().format(DATE_FMT)
                    : "-";
            context.setVariable("paymentDate", paymentDate);
            context.setVariable("transactionId", receipt.getMaintenance().getTransactionId());
            
            // Load logo as Base64 for the template
            context.setVariable("base64Logo", getBase64Logo());

            // 2. Render HTML
            String html = templateEngine.process("receipt", context);

            // 3. Convert HTML to PDF
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, "/");

            // Register custom fonts to support Rupee, Arrow and Marathi
            registerFont(builder, "static/fonts/NotoSans.ttf", "Noto Sans");
            registerFont(builder, "static/fonts/NotoSansDevanagari.ttf", "Noto Sans Devanagari");

            builder.toStream(baos);
            builder.run();

            log.info("PDF generated successfully for receipt id={}", receiptId);
            return baos.toByteArray();

        } catch (Exception ex) {
            log.error("Failed to generate PDF for receipt id={}: {}", receiptId, ex.getMessage(), ex);
            throw new RuntimeException("PDF generation failed for receipt id: " + receiptId, ex);
        }
    }

    private void registerFont(PdfRendererBuilder builder, String resourcePath, String fontFamily) {
        try {
            org.springframework.core.io.ClassPathResource resource = new org.springframework.core.io.ClassPathResource(resourcePath);
            java.io.File fontFile = java.io.File.createTempFile(fontFamily.replace(" ", ""), ".ttf");
            fontFile.deleteOnExit();
            try (java.io.InputStream is = resource.getInputStream();
                 java.io.FileOutputStream fos = new java.io.FileOutputStream(fontFile)) {
                is.transferTo(fos);
            }
            builder.useFont(fontFile, fontFamily);
        } catch (Exception e) {
            log.warn("Failed to load font {}: {}", fontFamily, e.getMessage());
        }
    }

    private String getBase64Logo() {
        try {
            ClassPathResource resource = new ClassPathResource("static/images/shri.png");
            try (InputStream is = resource.getInputStream()) {
                byte[] bytes = is.readAllBytes();
                return "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
            }
        } catch (Exception e) {
            log.warn("Failed to load logo image: {}", e.getMessage());
            // Fallback empty image or handle appropriately
            return "";
        }
    }
}
