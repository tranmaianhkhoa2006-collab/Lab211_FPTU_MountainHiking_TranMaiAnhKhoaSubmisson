package Controller;

import Model.MenuHeaderType;
import Model.Mountain;
import Model.Student;
import Utils.Acceptable;
import Utils.FileIOHandler;
import Utils.Inputter;
import static Utils.Inputter.inputChoice;
import static Utils.Inputter.inputStringAndLoop;
import Utils.MenuContainer;
import Utils.ViewHandler;
import java.util.List;

/**
 *
 * @author admin
 */
public class Controller {
     private StudentManager studentList = new StudentManager();
     private MountainList mountainList = new MountainList();
     
     public Controller(){
      
     }
     
     public void processOption(int choice){
         ViewHandler.fakeClearScreen();
         
         switch(choice){
             case 0:
                exitProgram();
                break;
                 
             case 1:    
                 addStudent();
                 break;
                 
             case 2:
                 updateStudentInfo();
                 break;
                 
             case 3:
                 showAll();
                 break;
                 
             case 4:
                 deleteStudent();
                 break;
                 
             case 5:
                 searchByName();
                 break;
                 
             case 6:
                 filterDataByCampusCode();
                 break;
                 
             case 7:
                 statisticQuantityByLocation();
                 break;
             
             case 8:
                 displayListOfPeakInfo();
                 break;
             case 9:
                 saveData();
                 break; 
         }
         Inputter.inputString("Press enter to continue!");
         ViewHandler.fakeClearScreen();
     }
     
     public void exitProgram(){
         if(!studentList.isSaved()){
             ViewHandler.print("Do you want to save data before leaving?\n");
             ViewHandler.displayMenu(
                     MenuContainer.getInstance().createYesNoMenu().getMenu(),
                     MenuContainer.getHeader(MenuHeaderType.YES_NO_MENU_HEADER)); 
             
             int choice = Inputter.inputChoice("Input your choice: ", 0, 1);
             switch(choice){
                 case 0:
                     this.saveData();
                     return;
                 case 1:
                     return;
             }
             
         }
         
     }
     
     public void addStudent(){
         String id ;
         boolean isDuplicated;
         int count=0;
         do{
             
              if(++count>3){
                   ViewHandler.displayMenu(MenuContainer.getInstance().createYesNoMenu().getMenu(),MenuContainer.getHeader(MenuHeaderType.YES_NO_MENU_HEADER));
                   int choice = inputChoice("Do you want of continue?: ",0, 1);
                   if(choice == 0)
                       count =0;
                   else
                       return;
              }
             id = Inputter.inputStringAndLoop("Input student ID: ", Acceptable.STUDENT_ID).toUpperCase();
             
             if(id == null)
                 return;
             
             isDuplicated = studentList.containId(id);
             if(isDuplicated)
                 ViewHandler.printError("This id is already exist\n");    
         }
         while(isDuplicated);
        
         String name = inputStringAndLoop("Input student name (2 -> 20 characters): ", Acceptable.NAME_VALID);
         if(name == null)
                 return;
         
         String phoneNumber = inputStringAndLoop("Input student phone number: ", Acceptable.PHONE_VALID);
         if(phoneNumber == null)
                 return;
         
         boolean isViettelOrVina = Acceptable.isValid(phoneNumber, Acceptable.VIETTEL_VALID) ||  Acceptable.isValid(phoneNumber, Acceptable.VNPT_VALID);
         String email = inputStringAndLoop("Input student email (Domain name: @fpt): ",Acceptable.EMAIL_VALID);
         if(email == null)
                 return;
         
         
         String peakCode= Inputter.inputPeakCode(mountainList);
         if(peakCode==null)
             return;
         
         if(studentList.add(new Student(id, name, phoneNumber, email, peakCode, isViettelOrVina)))
             ViewHandler.print("Add student successfully!\n");
         }
     
    public void updateStudentInfo() {
        int choice =-1;
        while (true) {
            String searchId = Inputter.inputStringAndLoop("Input id to update: ", Acceptable.STUDENT_ID);
             
            do{
                Student foundStudent = studentList.searchByID(searchId);
                 if (foundStudent == null) {
                     ViewHandler.printError("There is no student with this id\n");
                    return;
                }
                
                 ViewHandler.fakeClearScreen();
                ViewHandler.print(ViewHandler.lineBreak(40)+foundStudent.toString()+ViewHandler.lineBreak(40));
                
                ViewHandler.displayMenu(MenuContainer.getInstance().createUpdateStudentMenu().getMenu(), MenuContainer.getHeader(MenuHeaderType.UPDATE_STUDENT_MENU_HEADER));
                choice = Inputter.inputChoice("Input your choice: ", 0, MenuContainer.getInstance().getNumberOfOptions() - 1);
                

               
                switch (choice) {
                    case 0:
                        return;
                    case 1:
                        this.updateStudentName(foundStudent);
                        break;
                    case 2:
                        this.updateStudentPeakCode(foundStudent);
                        break;
                    case 3:
                        this.updatePhoneNumber(foundStudent);
                        break;
                    case 4:
                        this.updateStudentEmail(foundStudent);
                        break;
                }
            }
            while(choice !=5);
        }
    }
     
     public void updateStudentName(Student foundStudent){
         String name = inputStringAndLoop("Input student name: ", Acceptable.NAME_VALID);
         if(name == null)
             return;
         
         studentList.update(foundStudent.setName(name));
         
     }
     
     public void updateStudentPeakCode(Student foundStudent){
          String peakCode= Inputter.inputPeakCode(mountainList);
         if(peakCode == null)
             return;
         
         studentList.update(foundStudent.setPeakCode(peakCode));
         
     }
     
     public void updatePhoneNumber(Student foundStudent){
         String phoneNumber = inputStringAndLoop("Input phone number: ", Acceptable.PHONE_VALID);
         if(phoneNumber == null)
             return;
         
         studentList.update(foundStudent.setPhoneNumber(phoneNumber));
     }
     
     public void updateStudentEmail(Student foundStudent){
         String email = inputStringAndLoop("Input email: ",Acceptable.EMAIL_VALID);
          if(email == null)
             return;
         
         studentList.update(foundStudent.setEmail(email));
         
     }
     
     public void showAll(){
         studentList.showAll();
     
     }
     
     public void deleteStudent(){
         String deletedID = Inputter.inputStringAndLoop("Input student id to delete: ",Acceptable.STUDENT_ID);
         
         if(deletedID == null)
             return;
         
         Student foundStudent = studentList.searchByID(deletedID);
         if(foundStudent==null){
             ViewHandler.printError("There is no student with this id\n");
             return;
         }
         
         ViewHandler.print(
                 ViewHandler.lineBreak(MenuContainer.HEADER_WIDTH)+
                 foundStudent.toString()+
                 ViewHandler.lineBreak(MenuContainer.HEADER_WIDTH));
         
         ViewHandler.print("Are you sure? ");
         ViewHandler.displayMenu(MenuContainer.getInstance().createYesNoMenu().getMenu(),
                 MenuContainer.getHeader(MenuHeaderType.YES_NO_MENU_HEADER)
                 );
         
         int choice = Inputter.inputChoice("Your choice: ", 0, 1);
         if(choice == 0){
             if(studentList.delete(deletedID))
                 ViewHandler.print("Deleted successfully!\n");
             else
                 ViewHandler.print("Deleted unsuccessfully!\n");
         }
         else {
             ViewHandler.print("You denied to delete!\n");
         }
         
     }
     
     public void searchByName(){
         String name = inputStringAndLoop("Input name to search: ",Acceptable.NAME_VALID);
         
         if(name == null)
             return;
         
         List<Student> nameSearchList = studentList.searchByName(name);
         if(nameSearchList.size()==0){
             ViewHandler.printError("There is no student with the similar name\n");
             return;
         }
         ViewHandler.showStudentList(nameSearchList);
         
     }
     
     public void filterDataByCampusCode(){
         String campusCode = inputStringAndLoop("Input campus code [SE|CE|QE|HE|DE]: ", Acceptable.CAMPUS_CODE_VALID).toUpperCase();
         
         if(campusCode == null)
             return;
         
         List<Student> filterList = studentList.filterByCampusCode(campusCode);
         if(filterList.size()==0){
             ViewHandler.printError("There is no student in this campus that join mountain hiking!\n");
             return;
         }
       ViewHandler.print(StudentManager.TABLE_HEADER);
       for(Student student: filterList){
           String rowOfStudentInfo = 
                   ViewHandler.attributeOfStudentList(
                              student.getStudentID(), 
                              student.getName(),
                              student.getEmail(),
                              student.getPhoneNumber(), 
                              student.getPeakCode(),
                              ViewHandler.getVietnamMoneyFormat(student.getTuitionFee())
                   );
           
           ViewHandler.print(rowOfStudentInfo);
           ViewHandler.print(ViewHandler.lineBreak(rowOfStudentInfo.length()));  
       }
     }
     
   
     public void statisticQuantityByLocation(){
         StatisticList statistics = StatisticList.getInstance();
         List<Student> listOfStudent = studentList.getAllStudentList();
         if(listOfStudent.size()==0){
             ViewHandler.print("There is no registration yet!\n");
             return;
         }
             
             
             statistics.statisticalize(listOfStudent).show();
         ViewHandler.print("Note: The remaining mountain(s) that are/is not display, has its number of students is Zero\n");
     }
     
     public void displayListOfPeakInfo(){
         ViewHandler.print(ViewHandler.lineBreak(150));
         for(Mountain mountain: mountainList.getListOfMountain()){
              ViewHandler.print(mountain.toString()+ViewHandler.lineBreak(150));
         }
        
     }
     
     public void saveData(){
          FileIOHandler<Student> stuđentFileIOHandler = new FileIOHandler<>();
          stuđentFileIOHandler.writeFileObject(studentList.getPathFile(), studentList.getAllStudentList());
          studentList.setSaveStatus();
          ViewHandler.print("Save successfully!\n");
     }
     
     
     
}
