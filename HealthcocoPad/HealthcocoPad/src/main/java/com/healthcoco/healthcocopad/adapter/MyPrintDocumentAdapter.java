package com.healthcoco.healthcocopad.adapter;

import android.annotation.TargetApi;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;


import com.healthcoco.healthcocopad.HealthCocoActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by neha on 29/08/16.
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class MyPrintDocumentAdapter extends PrintDocumentAdapter {
    private static String PATH_TO_PRINT_FILE = "";
    private HealthCocoActivity mActivity;
    private int pageHeight;
    private int pageWidth;
    public PdfDocument myPdfDocument;
    public int totalpages = 4;

    public MyPrintDocumentAdapter(HealthCocoActivity mActivity, String filePath) {
        this.mActivity = mActivity;
        this.PATH_TO_PRINT_FILE = filePath;
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes,
                         PrintAttributes newAttributes,
                         CancellationSignal cancellationSignal,
                         LayoutResultCallback callback,
                         Bundle metadata) {
        if (cancellationSignal.isCanceled()) {
            callback.onLayoutCancelled();
            return;
        }

//        int pages = computePageCount(newAttributes);

        PrintDocumentInfo pdi = new PrintDocumentInfo.Builder("Name of file").setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT).build();

        callback.onLayoutFinished(pdi, true);
    }


    @Override
    public void onWrite(final PageRange[] pageRanges,
                        final ParcelFileDescriptor destination,
                        final CancellationSignal cancellationSignal,
                        final WriteResultCallback callback) {
        InputStream input = null;
        OutputStream output = null;

        try {

            input = new FileInputStream(new File(PATH_TO_PRINT_FILE));
            output = new FileOutputStream(destination.getFileDescriptor());

            byte[] buf = new byte[1024];
            int bytesRead;

            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }

            callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});

        } catch (FileNotFoundException ee) {
            //Catch exception
        } catch (Exception e) {
            //Catch exception
        } finally {
            try {
                input.close();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    private int computePageCount(PrintAttributes printAttributes) {
//        int itemsPerPage = 4; // default item count for portrait mode
//
//        PrintAttributes.MediaSize pageSize = printAttributes.getMediaSize();
//        if (!pageSize.isPortrait()) {
//            // Six items per page in landscape orientation
//            itemsPerPage = 6;
//        }
//
//        // Determine number of print items
//        int printItemCount = getPrintItemCount();
//
//        return (int) Math.ceil(printItemCount / itemsPerPage);
//    }


}