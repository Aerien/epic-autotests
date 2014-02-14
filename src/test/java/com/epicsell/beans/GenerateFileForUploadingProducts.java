package com.epicsell.beans;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 06.04.13
 */
public class GenerateFileForUploadingProducts {
    ArrayList<Product> products;
    String filePath;
    Product.Header invalidHeader;

    public GenerateFileForUploadingProducts(ArrayList<Product> products, String filePath) {
        this.products = products;
        this.filePath = filePath;
        run();
    }

    public GenerateFileForUploadingProducts(ArrayList<Product> products, String filePath, Product.Header invalidHeader) {
        this.products = products;
        this.filePath = filePath;
        this.invalidHeader = invalidHeader;
        run();
    }

    public void run() {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(filePath, true));
            out.write(ProductUploadingFileHeaders.fullValidHeasder + "\n");
            for (Product product : products) {
                out.write("\"" + (product.getCategory() == null ? "" : product.getCategory()) + "\";");
                out.write("\"" + (product.getName() == null ? "" : product.getName()) + "\";");
                if (invalidHeader != null && invalidHeader.equals(Product.Header.PRICE)) {
                    out.write("\"test\";");
                } else if (product.getPrice() == null) {
                    out.write("\"\";");
                } else {
                    out.write("\"" + product.getPrice() + "\";");
                }
                out.write("\"" + (product.getUrl() == null ? "" : product.getUrl()) + "\";");
                out.write("\"" + (product.getArticle() == null ? "" : product.getArticle()) + "\";");
                if (invalidHeader != null && invalidHeader.equals(Product.Header.ACTIVITY)) {
                    out.write("\"test\";");
                } else if (product.isActive() == null) {
                    out.write("\"\";");
                } else {
                    out.write("\"" + (product.isActive() ? "1" : "0") + "\";");
                }
                out.write("\"" + (product.getImage() == null ? "" : product.getImage()) + "\";");
                if (invalidHeader != null && invalidHeader.equals(Product.Header.QTY)) {
                    out.write("\"test\";");
                } else if (product.getQuantity() == null) {
                    out.write("\"\";");
                } else {
                    out.write("\"" + product.getQuantity() + "\";");
                }
                out.write("\"" + (product.getShortDescription() == null ? "" : product.getShortDescription()) + "\";");
                out.write("\"" + (product.getFullDescription() == null ? "" : product.getFullDescription()) + "\";");
                out.write("\"" + (product.getMetaTitle() == null ? "" : product.getMetaTitle()) + "\";");
                out.write("\"" + (product.getMetaKeywords() == null ? "" : product.getMetaKeywords()) + "\";");
                out.write("\"" + (product.getMetaDescription() == null ? "" : product.getMetaDescription()) + "\";\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
