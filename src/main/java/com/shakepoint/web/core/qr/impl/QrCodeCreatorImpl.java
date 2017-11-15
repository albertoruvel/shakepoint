package com.shakepoint.web.core.qr.impl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.shakepoint.web.core.qr.QrCodeCreator;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.shakepoint.web.util.ShakeUtils;
import org.apache.log4j.Logger;

public class QrCodeCreatorImpl implements QrCodeCreator {
    private final Logger log = Logger.getLogger(getClass());

    private static final String CODE_FORMAT = "%s_%s_%s_%s"; //purchase_id_machine_id_product_id_qr_code_id

    @Override
    public String createQRCode(String purchaseId, String machineId, String productId, String qrCodeId) {
        String format = String.format(CODE_FORMAT, purchaseId, machineId,
                productId, qrCodeId);

        String path = ShakeUtils.QR_CODES_TMP_FOLDER + qrCodeId + ".png";
        File file = new File(path);

        try {
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix byteMatrix = qrCodeWriter.encode(format, BarcodeFormat.QR_CODE, 256, 256, hintMap);
            int width = byteMatrix.getWidth();
            BufferedImage image = new BufferedImage(width, width,
                    BufferedImage.TYPE_INT_RGB);
            image.createGraphics();
            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, width, width);
            graphics.setColor(Color.BLACK);

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < width; j++) {
                    if (byteMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }
            ImageIO.write(image, "png", file);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    @Override
    public String readQrCode(String qrCodeId) {
        String filePath = ShakeUtils.QR_CODES_RESOURCES_FOLDER + qrCodeId;
        Map hintMap = new HashMap();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        Result qrCodeResult = null;
        try {
            BufferedImage image = ImageIO.read(new FileInputStream(filePath));
            BufferedImageLuminanceSource bils = new BufferedImageLuminanceSource(image);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(bils));
            qrCodeResult = new MultiFormatReader().decode(binaryBitmap,
                    hintMap);
        } catch (IOException ex) {
            log.error("Could not read QR: " + ex.getMessage());
        } catch (NotFoundException nfex) {
            log.error("Could not read QR: " + nfex.getMessage());
        }
        return qrCodeResult.getText();
    }
}