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
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.exceptions.RealmMigrationNeededException;

public class Excel_sheet_access
{
    private static String TAG = "Excel_sheet_access";

    public static void readExcelFile(Context context, Uri uri,String BatchID,String Batch, String Subject)
    {
        ProgressDialog progress = new ProgressDialog(context);
        Realm realm;
        RealmList<Student> Student_list= new RealmList<Student>();
        Register register;
        Student student;
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(context).build();
        realm = Realm.getInstance(realmConfig);

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
                HSSFRow myRow = (HSSFRow) rowIter.next();

                Iterator tempIt = myRow.cellIterator();
                HSSFCell tempCell = (HSSFCell)tempIt.next();
                tempCell.setCellType(Cell.CELL_TYPE_STRING);

                if(tempCell.toString().length()<1)
                    break;

                Iterator cellIter = myRow.cellIterator();

                realm.beginTransaction();
                student=new Student();

                HSSFCell myCell = (HSSFCell) cellIter.next();
                myCell.setCellType(Cell.CELL_TYPE_STRING);
                student.setRoll_number(myCell.toString());

                myCell = (HSSFCell)cellIter.next();
                myCell.setCellType(Cell.CELL_TYPE_STRING);
                student.setStudent_name(myCell.toString());

                myCell = (HSSFCell)cellIter.next();
                myCell.setCellType(Cell.CELL_TYPE_STRING);
                student.setPhone_no(myCell.toString());

                myCell = (HSSFCell)cellIter.next();
                myCell.setCellType(Cell.CELL_TYPE_STRING);
                student.setMac_ID1(myCell.toString());

                if(cellIter.hasNext()) {
                    myCell = (HSSFCell) cellIter.next();
                    myCell.setCellType(Cell.CELL_TYPE_STRING);
                    if (myCell.toString().length() > 0)
                        student.setMac_ID2(myCell.toString());
                }

                realm.copyToRealmOrUpdate(student);
                realm.commitTransaction();
                Student_list.add(student);
            }
            Log.d(TAG,""+Student_list.size());
            Log.d(TAG,Student_list.get(0).getRoll_number());
            Log.d(TAG,Student_list.get(1).getRoll_number());
            Log.d(TAG,Student_list.get(2).getRoll_number());
            Log.d(TAG,Student_list.get(3).getRoll_number());
            realm.beginTransaction();
            register=new Register();
            register.setBatchID(BatchID);
            register.setBatch(Batch);
            register.setSubject(Subject);
            register.setStudents(Student_list);
            realm.copyToRealmOrUpdate(register);
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
