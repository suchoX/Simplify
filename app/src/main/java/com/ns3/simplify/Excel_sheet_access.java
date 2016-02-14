package com.ns3.simplify;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.ns3.simplify.realm.Register;
import com.ns3.simplify.realm.Student;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.exceptions.RealmMigrationNeededException;

public class Excel_sheet_access
{
    public static void readExcelFile(Context context, Uri uri,String Batch)
    {
        ProgressDialog progress = new ProgressDialog(context);
        Realm realm;
        RealmList<Student> Student_list= new RealmList<Student>();
        Register register;
        realm = Realm.getInstance(context);
        try
        {

            progress.setTitle("Setting Up");
            progress.setMessage("Please wait while we set up this class");
            progress.setCancelable(false);
            progress.show();
            // Creating Input Stream
            File file = new File(uri.getPath());
            FileInputStream myInput = new FileInputStream(file);

            // Create a POIFSFileSystem object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);

            /** We now need something to iterate through the cells.**/
            Iterator rowIter = mySheet.rowIterator();

            if(rowIter==null)
                Toast.makeText(context,"Empty File",Toast.LENGTH_SHORT).show();

            while(rowIter.hasNext())
            {
                int count=0;
                realm.beginTransaction();

                Student student =realm.createObject(Student.class);

                HSSFRow myRow = (HSSFRow) rowIter.next();
                Iterator cellIter = myRow.cellIterator();
                while(cellIter.hasNext())
                {
                    HSSFCell myCell = (HSSFCell) cellIter.next();
                    myCell.setCellType(Cell.CELL_TYPE_STRING);
                    //Log.d("TAG", "Cell Value: " + myCell.toString());
                    //Toast.makeText(context, "cell Value: " + myCell.toString(), Toast.LENGTH_SHORT).show();
                    switch(count)
                    {
                        case 0:
                            student.setRoll_number(myCell.toString());
                            //Log.d("rgf",student.getRoll_number());
                            break;
                        case 1:
                            student.setStudent_name(myCell.toString());
                            break;
                        case 2:
                            student.setPhone_no(myCell.toString());
                            break;
                        case 3:
                            student.setMac_ID1(myCell.toString());
                            break;
                        case 4:
                            student.setMac_ID2(myCell.toString());
                            break;
                    }
                    count++;
                }
                Student_list.add(student);  //Creating a list of Student in this batch
                realm.commitTransaction();
            }
            realm.beginTransaction();
            register=realm.createObject(Register.class);
            register.setBatch(Batch);
            register.setStudents(Student_list);
            realm.commitTransaction();
        }
        catch (Exception e){e.printStackTrace(); }
        finally {
            progress.dismiss();
            realm.close();
        }
        return;
    }

    private static boolean saveExcelFile(Context context, String fileName)
    {
        boolean success = false;

        //New Workbook
        Workbook wb = new HSSFWorkbook();

        Cell c = null;

        //Cell style for header row
        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.LIME.index);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        //New Sheet
        Sheet sheet1 = null;
        sheet1 = wb.createSheet("myOrder");

        // Generate column headings
        Row row = sheet1.createRow(0);

        c = row.createCell(0);
        c.setCellValue("Item Number");
        c.setCellStyle(cs);

        c = row.createCell(1);
        c.setCellValue("Quantity");
        c.setCellStyle(cs);

        c = row.createCell(2);
        c.setCellValue("Price");
        c.setCellStyle(cs);

        sheet1.setColumnWidth(0, (15 * 500));
        sheet1.setColumnWidth(1, (15 * 500));
        sheet1.setColumnWidth(2, (15 * 500));

        // Create a path where we will place our List of objects on external storage
        File file = new File(context.getExternalFilesDir(null), fileName);
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            wb.write(os);
            Log.w("FileUtils", "Writing file" + file);
            success = true;
        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + file, e);
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
            }
        }
        return success;
    }
}
