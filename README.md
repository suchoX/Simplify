# Simplify

Download the apk from [HERE](https://github.com/suchoX/Simplify/blob/master/Simplify.apk "Simplify APK")

##Automatic Bluetooth Attendance

Marking attendance in schools/colleges are still done on physical registers which is time consuming and takes a lot of time to manually copy data from the register to a excel sheet. This process also makes getting personalized data like individual attendance percentage impossible.

Simplify aims to make this process simpler and with minimal physical work. Following are the features-
* Import Student details directly from excel sheets
* Export detailed student to excel sheet for the whole date range or by selecting date range
* Choose to manually mark students or scans students using Bluetooth (Using the MAC id's for each student)
* View indivdual student data like attendance percentage and days of absence/presence
* Graphically compare attendance percentage of each student
* Use of [Realm](https://realm.io/ "Realm") Database and [Scand Database Browser](http://scand.com/products/realmbrowser/ "Scand") to edit the RAW database 

####ScreenShots
<img src="https://github.com/suchoX/Simplify/blob/master/Images/Register_list.png" width="275" height="427"> <img src="https://github.com/suchoX/Simplify/blob/master/Images/Class_Details.png" width="275" height="427"> <img src="https://github.com/suchoX/Simplify/blob/master/Images/Student_details.png" width="275" height="427"> 
<img src="https://github.com/suchoX/Simplify/blob/master/Images/Student_attendance.png" width="275" height="427"> <img src="https://github.com/suchoX/Simplify/blob/master/Images/Attendance_graph.png" width="275" height="427"> <img src="https://github.com/suchoX/Simplify/blob/master/Images/Date_range.png" width="275" height="427">

###Instructions
* The Excel sheets used for importing **Must be in .XLS(Word 2003 format) ONLY, NOT .XLSX.** The format of the import excel sheet should be-
<img src="https://github.com/suchoX/Simplify/blob/master/Images/Import_Excel_Sheet.PNG">

  * **NO HEADINGS**
  * The columns are respectively **Roll Number (Primary Key)** , **Name** , **Phone Number** , **MAC ID 1** , **MAC ID 2** (For Bluetooth Attendance)
  * Two students **CANNOT** have the same RollNumber for the whole app, since Students are put in the same table, not different table for each class.
  * If you don't want MAC ID's, please fill both the columns with random data rather than keeping it empty

* The exported Excel sheet has the following format - 
<img src="https://github.com/suchoX/Simplify/blob/master/Images/Export_Excel_Sheet.PNG">
  
  * The values in the excel sheet depicts the **Attendance Value** for that entry



##Wi-Fi Quiz

The Wi-Fi quiz section will have the following features- 

1. The question setter can import questions into the questions database from an excel file containing questions. 

2. Teacher and Students have to be connected to the same Wi-Fi network before the test. 

3. The questions will be sent and displayed to the applicants in an MCQ format. 

4. Selection of questions will be in a random fashion from a question pool containing a set of preloaded questions. 
