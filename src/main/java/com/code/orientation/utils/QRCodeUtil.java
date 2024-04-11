package com.code.orientation.utils;

import com.code.orientation.constants.CodeEnum;
import com.code.orientation.exception.CustomException;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Base64;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 二维码工具类
 *
 * @author HeXin
 * @date 2024/04/09
 */
@Component
public class QRCodeUtil {

    private final RedisUtil redisUtil;

    private static final String CHARSET = "utf-8";

    // 二维码尺寸
    private static final int QRCODE_SIZE = 300;

    // LOGO宽度
    private static final int WIDTH = 100;

    // LOGO高度
    private static final int HEIGHT = 100;

    @Autowired
    public QRCodeUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    /**
     * 将传入的信息，编码成二维码
     *
     * @param content      内容
     * @param imgPath      IMG 路径
     * @param needCompress 需要压缩
     * @param outputStream 输出流
     */
    public void encode(String content, String imgPath, boolean needCompress, OutputStream outputStream) {
        BufferedImage image;
        image = createImage(content, imgPath, needCompress);
        try {
            ImageIO.write(image, "png", outputStream);
        } catch (IOException e) {
            throw new CustomException(e);
        }
    }

    /**
     * 生成二维码核心代码
     *
     * @param content      内容
     * @param imgPath      IMG 路径
     * @param needCompress 需要压缩
     * @return {@link BufferedImage}
     */
    private BufferedImage createImage(String content, String imgPath, boolean needCompress) {

        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        // 指定要使用的纠错程度，例如在二维码中。
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 指定字符编码
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        // 指定生成条形码时要使用的边距（以像素为单位）。
        hints.put(EncodeHintType.MARGIN, 1);

        // 生成一个二维位矩阵
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);
        } catch (WriterException e) {
            throw new CustomException(e);
        }

        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // true就是黑色，false就是白色
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        if (imgPath == null || imgPath.isEmpty()) {
            return image;
        }
        // 插入LOGO图片
        insertImage(image, imgPath, needCompress);
        return image;
    }

    /**
     * 插入 LOGO 图片
     *
     * @param source       源
     * @param imgPath      IMG 路径
     * @param needCompress 需要压缩
     */
    private void insertImage(BufferedImage source, String imgPath, boolean needCompress) {
        Image src;
        byte[] imageBytes;
        try {
            if (imgPath.startsWith("http") || imgPath.startsWith("https")) { // 网络图片
                // 从缓存中获取Logo
                BufferedImage image = parseImage(imgPath);
                if (image != null) {
                    // 如果缓存不为空，则直接读取缓存
                    src = image;
                } else {
                    src = ImageIO.read(new URL(imgPath));
                    // 将图片转换为byte数组
                    imageBytes = getImageBytes(src);
                    // 将图片加入缓存
                    cacheImage(imgPath, imageBytes);
                }
            } else { // 本地图片
                File file = new File(imgPath);
                if (!file.exists()) {
                    throw new CustomException(CodeEnum.FILE_NOT_FOUND);
                }
                src = ImageIO.read(file);
            }
        } catch (IOException e) {
            throw new CustomException(e);
        }

        int width = src.getWidth(null);
        int height = src.getHeight(null);

        // 压缩LOGO
        if (needCompress) {
            width = Math.min(width, WIDTH);
            height = Math.min(height, HEIGHT);

            // 创建此图像的缩放版本。
            src = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        }

        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (QRCODE_SIZE - width) / 2;
        int y = (QRCODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }


    /**
     * 获取图像byte数组
     * @param image 图像
     * @return {@link byte[]}
     */
    private byte[] getImageBytes(Image image) {
        try {
            BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = bufferedImage.createGraphics();
            graphics2D.drawImage(image, null, null);
            graphics2D.dispose();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new CustomException(e);
        }
    }

    /**
     * 缓存图片[仅供网络图片]
     * @param key 键
     * @param value 值
     */
    private void cacheImage(String key,byte[] value) {
        // 将byte数组转换为Base64编码的字符串
        String str = Base64.getEncoder().encodeToString(value);
        // 缓存 30 分钟
        redisUtil.set(key,str,1800L, TimeUnit.SECONDS);
    }

    /**
     * 解析缓存图片
     * @param key 键
     * @return {@link byte[]}
     */
    private BufferedImage parseImage(String key) {
        // 解析出缓存中的图片
        String str = redisUtil.getString(key);
        if(StringUtils.isBlank(str)) {
            return null;
        }
        // 将Base64编码的字符串解码为byte数组
        byte[] bytes = Base64.getDecoder().decode(str);
        try {
            return ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            throw new CustomException(e);
        }
    }

    /**
     * 解码，将二维码里的信息解码出来
     *
     * @param path 路径
     * @return {@link String}
     */
    public String decode(String path) {
        BufferedImage image;
        try {
            if (path.startsWith("http") || path.startsWith("https")) { // 网络图片
                URL url = new URL(path);
                image = ImageIO.read(url);
            } else { // 本地图片
                File file = new File(path);
                image = ImageIO.read(file);
            }
        } catch (IOException e) {
            throw new CustomException(e);
        }

        if (image == null) {
            throw new CustomException(CodeEnum.PARSING_FAILED);
        }

        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        try {
            result = new MultiFormatReader().decode(bitmap, hints);
        } catch (NotFoundException e) {
            throw new CustomException(e);
        }
        return result.getText();
    }


}

