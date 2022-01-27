package com.tracec.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.widget.Toast;

import com.tracec.data.ExcelData;

public class ExcelUtil {

    public static String root = Environment.getExternalStorageDirectory()
            .getPath();

    public static void writeExcel(Context context, List<ExcelData> exportData,
                                  String fileName) throws Exception {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)&&getAvailableStorage()>1000000) {
            Toast.makeText(context, "SD card unavailable", Toast.LENGTH_LONG).show();
            return;
        }
        String[] title = { "Date", "P_ID", "P_Name", "P_DOB","Closing casino times","Staying times per each time","Warning times" };
        File file;
        File dir = new File(context.getExternalFilesDir(null).getPath());
        file = new File(dir, fileName + ".xls");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        WritableWorkbook wwb;
        OutputStream os = new FileOutputStream(file);
        wwb = Workbook.createWorkbook(os);
        WritableSheet sheet = wwb.createSheet("report", 0);
        Label label;
        for (int i = 0; i < title.length; i++) {
            label = new Label(i, 0, title[i], getHeader());
            sheet.addCell(label);
        }

        for (int i = 0; i < exportData.size(); i++) {
            ExcelData data = exportData.get(i);

            Label date = new Label(0, i + 1, data.getDate());
            Label pid = new Label(1, i + 1, data.getPid());
            Label pname = new Label(2,i+1,data.getPname());
            Label pdob = new Label(3, i + 1, data.getPdob());
            Label ctimes = new Label(4,i+1,data.getClosingtimes());
            Label stime = new Label(5,i+1,data.getStaytime());
            Label wtimes = new Label(6,i+1,data.getWarningtimes());

            sheet.addCell(date);
            sheet.addCell(pid);
            sheet.addCell(pname);
            sheet.addCell(pdob);
            sheet.addCell(ctimes);
            sheet.addCell(stime);
            sheet.addCell(wtimes);
        }

        wwb.write();
        wwb.close();
        Toast.makeText(context, "Export Success", Toast.LENGTH_LONG).show();
    }

    public static WritableCellFormat getHeader() {
        WritableFont font = new WritableFont(WritableFont.TIMES, 10,
                WritableFont.BOLD);
        try {
            font.setColour(Colour.BLUE);
        } catch (WriteException e1) {
            e1.printStackTrace();
        }
        WritableCellFormat format = new WritableCellFormat(font);
        try {
            format.setAlignment(jxl.format.Alignment.CENTRE);
            format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return format;
    }

    private static long getAvailableStorage() {

        StatFs statFs = new StatFs(root);
        long blockSize = statFs.getBlockSize();
        long availableBlocks = statFs.getAvailableBlocks();
        long availableSize = blockSize * availableBlocks;
        return availableSize;
    }
}
