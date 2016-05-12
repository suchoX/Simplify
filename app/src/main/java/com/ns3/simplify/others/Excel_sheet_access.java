package com.ns3.simplify.others;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.ns3.simplify.realm.DateRegister;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.exceptions.RealmMigrationNeededException;

public class Excel_sheet_access
{
    private static String TAG = "Excel_sheet_access";
    public static void readExcelFile(Context context, Uri uri,String BatchID,String Subject,String SubjectCode,int Batch, int Semester,String Stream,String Section,String Group)
    {
        ProgressDialog progress = new ProgressDialog(context);
        Realm realm;
        RealmList<Student> Student_list= new RealmList<Student>();
        RealmList<DateRegister> Record = new RealmList<DateRegister>();
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
                student.setMac_ID1(myCell.toString().toUpperCase());

                if(cellIter.hasNext()) {
                    myCell = (HSSFCell) cellIter.next();
                    myCell.setCellType(Cell.CELL_TYPE_STRING);
                    if (myCell.toString().length() > 0)
                        student.setMac_ID2(myCell.toString().toUpperCase());
                }

                realm.copyToRealmOrUpdate(student);
                realm.commitTransaction();
                Student_list.add(student);
            }
            Log.d(TAG,BatchID);
            realm.beginTransaction();
            register=new Register();
            register.setBatchID(BatchID);
            register.setSubject(Subject);
            register.setSubjectCode(SubjectCode);
            register.setBatch(Batch);
            register.setSemester(Semester);
            register.setStream(Stream);
            register.setSection(Section);
            register.setGroup(Group);
            register.setStudents(Student_list);
            register.setRecord(Record);
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

    public static void readExcelFile(Context context, Uri uri,String batchID)
    {
        ProgressDialog progress = new ProgressDialog(context);
        Realm realm;
        Student student;
        RealmList<Student> Student_list= new RealmList<Student>();
        Register register;
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
                student.setMac_ID1(myCell.toString().toUpperCase());

                if(cellIter.hasNext()) {
                    myCell = (HSSFCell) cellIter.next();
                    myCell.setCellType(Cell.CELL_TYPE_STRING);
                    if (myCell.toString().length() > 0)
                        student.setMac_ID2(myCell.toString().toUpperCase());
                }
                realm.copyToRealmOrUpdate(student);
                realm.commitTransaction();

                register = realm.where(Register.class).equalTo("BatchID",batchID).findFirst();
                Student_list = register.getStudents();
                Log.d("hdfb",""+Student_list.size());

                if(!Student_list.contains(student))
                {
                    realm.beginTransaction();
                    register.getStudents().add(student);
                    realm.commitTransaction();
                }
            }
        }
        catch (Exception e){e.printStackTrace(); }
        finally {
            progress.dismiss();
            realm.close();
        }
        return;
    }

    public static boolean saveExcelFile(Context context, String fileName, String batchID)
    {
        Realm realm;
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(context).build();
        realm = Realm.getInstance(realmConfig);
        ProgressDialog progress = new ProgressDialog(context);
        boolean success = false;

        RealmList<DateRegister> record = new RealmList<DateRegister>();
        RealmResults<Student> studentList;
        Register register;

        try
        {
            progress.setTitle("Exporting");
            progress.setMessage("Please wait while we are creating then excel Sheet");
            progress.setCancelable(false);
            progress.show();

            register = realm.where(Register.class).equalTo("BatchID",batchID).findFirst();
            record = register.getRecord();
            studentList = register.getStudents().sort("Roll_number");

            //New Workbook
            Workbook wb = new HSSFWorkbook();

            Cell c = null;

            //New Sheet
            Sheet sheet1 = null;
            sheet1 = wb.createSheet("Attendance");

            addFirstRow(sheet1,c,record);
            addSecondRow(sheet1,c,record);
            addStudentData(sheet1,c,record,studentList);
            // Create a path where we will place our List of objects on external storage
            File file = new File(context.getExternalFilesDir(null), fileName);
            FileOutputStream os = null;

            try {
                os = new FileOutputStream(file);
                wb.write(os);
                Log.w("FileUtils", "Writing file" + file);
                success = true;

                final File f = file;
                final Context cx = context;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("File Exported in direcory-\n\n"+file+"\n\nView it?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                Intent intent = new Intent();
                                intent.setAction(android.content.Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.fromFile(f),"application/vnd.ms-excel");
                                cx.startActivity(intent);

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id)
                            {

                            }
                        }).create();
                AlertDialog alert = builder.create();
                alert.show();

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
        }
        catch (Exception e){e.printStackTrace(); }
        finally {
            progress.dismiss();
            realm.close();
        }
        return success;
    }


    public static boolean saveExcelFile(Context context, String fileName, String batchID,Date fromDate, Date toDate)
    {
        Realm realm;
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(context).build();
        realm = Realm.getInstance(realmConfig);
        ProgressDialog progress = new ProgressDialog(context);
        boolean success = false;

        RealmList<DateRegister> temp = new RealmList<DateRegister>();
        RealmList<DateRegister> record = new RealmList<DateRegister>();
        RealmResults<Student> studentList;
        Register register;

        try
        {
            progress.setTitle("Exporting");
            progress.setMessage("Please wait while we are creating then excel Sheet");
            progress.setCancelable(false);
            progress.show();

            register = realm.where(Register.class).equalTo("BatchID",batchID).findFirst();
            temp = register.getRecord();
            studentList = register.getStudents().sort("Roll_number");

            Date presentDate;

            for(int i=0 ; i<temp.size() ; i++) {
                presentDate = new Date(temp.get(i).getDateToday().getYear()+1900,temp.get(i).getDateToday().getMonth(),temp.get(i).getDateToday().getDate());
                if (presentDate.compareTo(fromDate) >= 0 && presentDate.compareTo(toDate) <= 0)
                    record.add(temp.get(i));
            }

            //New Workbook
            Workbook wb = new HSSFWorkbook();

            Cell c = null;

            //New Sheet
            Sheet sheet1 = null;
            sheet1 = wb.createSheet("Attendance");

            addFirstRow(sheet1,c,record);
            addSecondRow(sheet1,c,record);
            addStudentData(sheet1,c,record,studentList);
            // Create a path where we will place our List of objects on external storage
            File file = new File(context.getExternalFilesDir(null), fileName);
            FileOutputStream os = null;

            try {
                os = new FileOutputStream(file);
                wb.write(os);
                Log.w("FileUtils", "Writing file" + file);
                success = true;

                final File f = file;
                final Context cx = context;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("File Exported in direcory-\n\n"+file+"\n\nView it?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                Intent intent = new Intent();
                                intent.setAction(android.content.Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.fromFile(f),"application/vnd.ms-excel");
                                cx.startActivity(intent);

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id)
                            {

                            }
                        }).create();
                AlertDialog alert = builder.create();
                alert.show();

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
        }
        catch (Exception e){e.printStackTrace(); }
        finally {
            progress.dismiss();
            realm.close();
        }
        return success;
    }

    static void addFirstRow(Sheet sheet1,Cell c,RealmList<DateRegister> record)
    {
        int j;
        Row row = sheet1.createRow(0);

        c = row.createCell(0);
        c.setCellValue("Sl.No");

        c = row.createCell(1);
        c.setCellValue("Roll No");

        c = row.createCell(2);
        c.setCellValue("Name");

        // Adding Dates
        for(j=3 ; j<record.size()+3 ; j++)
        {
            c = row.createCell(j);
            c.setCellValue(""+record.get(j-3).getDateToday().getDate()+"/"+(record.get(j-3).getDateToday().getMonth()+1)+"/"+(record.get(j-3).getDateToday().getYear()+1900));
        }

        c = row.createCell(record.size()+3);
        c.setCellValue("Total");

        c = row.createCell(record.size()+4);
        c.setCellValue("%");
    }

    static void addSecondRow(Sheet sheet1,Cell c,RealmList<DateRegister> record)
    {
        int j,value=0;
        Row row = sheet1.createRow(1);
        c = row.createCell(0);
        c.setCellValue("");

        c = row.createCell(1);
        c.setCellValue("No. of Classes");

        c = row.createCell(2);
        c.setCellValue("");

        for(j=3 ; j<record.size()+3 ; j++)
        {
            c = row.createCell(j);
            c.setCellValue(record.get(j-3).getValue());
            value+=record.get(j-3).getValue();
        }
        c = row.createCell(record.size()+3);
        c.setCellValue(value);

        c = row.createCell(record.size()+4);
        c.setCellValue("");
    }

    static void addStudentData(Sheet sheet1,Cell c,RealmList<DateRegister> record,RealmResults<Student> studentList)
    {
        int i,j;
        int value=0,present=0;
        for(i=0 ; i<studentList.size() ; i++)
        {
            value=0;
            present=0;
            Row row = sheet1.createRow(i+2);

            c = row.createCell(0);
            c.setCellValue(i+1);

            c = row.createCell(1);
            c.setCellValue(studentList.get(i).getRoll_number());

            c = row.createCell(2);
            c.setCellValue(studentList.get(i).getStudent_name());

            for(j=3 ; j<record.size()+3 ; j++)
            {
                value+=record.get(j-3).getValue();
                c = row.createCell(j);
                if(record.get(j-3).getStudentPresent().contains(studentList.get(i))) {
                    c.setCellValue(record.get(j - 3).getValue());
                    present+=record.get(j - 3).getValue();
                }
                else
                    c.setCellValue("");
            }
            c = row.createCell(record.size()+3);
            c.setCellValue(present);

            c = row.createCell(record.size()+4);
            c.setCellValue((present*100)/value);
        }
    }


}
