/* RegSys.RegSys.java			0.01 03/02/2015
 *
 * This is code written for UCI Extension - I&C SCI_X460.10 WINTER 2015,UNEX,00113
 *
 * Team: Team A
 * Date: 03/02/2015
 * Class: UCI Extension - I&C SCI_X460.10 WINTER 2015,UNEX,00113
 * Assignment: Team Assignment
 *
 * Change Log
 * 03/02/15 - T. Fredericks - Initial source creation
 * 03/03/15 - D. Fuller - Modified structure of main() to create program flow loop
 * 03/05/15 - D. Fuller - Added menus and login user input and support for shutdown by system admin.
 * 03/05/15 - T. Fredericks - added comment "headers" for methods, modified login method to return RegSys.Student or null,
 *              renamed validateCourse() to isValidCourse(), changing register() to have one parameter (String courseId)
 *              broke out main() into series of methods, instantiated RegSys.RegSys regSys in main()
 * 03/05/15 - P. On - removed isStudentRegistered() and call, this function already exists in RegSys.Student class as isRegistered()
 * 				referenced RegSys.Student class function.  In main(), corrected "3" and "4" unregister/register calls.
 * 				Moved functionality of dispalyRegisterPrompt() (which had unregister steps) into displayUnregisterPrompt().
 * 				In main(), added capability to view all courses, and view all enrolled courses for student.
 * 03/06/15 - T. Fredericks - renaming isValidCourse() to getCourseById(), fixed course.enrollment() and course.openSeat()
 *                created new database files StudentDatabase.txt and CourseDatabase.txt (something weird with .csv files, can't figure it out)
 * 				  for testing:  using studentid=48474738 password=abcdefg, added boolean return value to loadDatabases and changing name to isDatabaseLoaded
 *				  renamed loadCourseDB and loadStudentDB to isCourseDBLoaded and isStudentDBLoaded
 * 03/07/15 - D. Fuller - Minor bug and program flow fixes.  Added more Registration menu.  Changed Course and Student database files from *.txt to *.csv.
 * 03/07/15 - T. Fredericks - modified isStudentDBLoaded to save regCourses, moved isDatabaseLoaded() before displayLogin,
 *                            added formatting to main's //VIEW ENROLLED COURSES, in isCourseDBLoaded added trim().
 *                            To test the app use studentid=48474738 password=abcdefg, displayRegisterPrompt() added 'x' to go back to main menu
 *							  added header for VIEW ENROLLED COURSES, replaced RegSys.Course with Course, displayUnregisterPrompt - removed userInput.close(),
 * 							  call userInput.close within main() where userInput was opened, added instance variable indexStudent to be used in updateArrayLists
 *							  added trim() to last tokens to get rid of \r in isStudentDBLoaded(), added more code to RegSys.unregister
 *	03/07/15 - D. Fuller - Added displayAvailableCourses to display all course catalog with column headers displayed every 10 courses.
 *	                       Added check on register return null meaning student did not choose a new course to register.  Return to main menu w/o error.
 *  03/08/15 - T. Fredericks - added SimpleDateFormat to isCourseDBLoaded and isStudentDBLoaded, removed DEBUG code, fixed refreshStudentDB:  added s.getPassword, removed indexStudent
 *  						   added header spacing with HEADER_SPACING, merged displayRegisterPrompt and displayUnregisterPrompt to getCourseIdInput,
 *  						   added pressToContinue so that user can see error message in register() before *very long* course list is displayed
 *  03/10/15 - P. On - Modified while(true) loop in main to remove bug, "}" was in wrong location and "INVALID MENU CHOICE." was always printed, even for valid choices.
 *	03/10/15 - T. Fredericks - access protected constant Course.COURSE_DATE_FORMAT, add first header in displayAvailableCourses
 *							   import java.util.Collections, added Collections.sort to displayCoursesOfStudent() and displayAvailableCourses()
 *  03/10/15 - D. Fuller - Added header for Unregister course.  Changed "Press any key to continue message" to "Press Enter to Continue" since any key did not work
 *  03/10/15 - T. Fredericks - added displayRegisterableCoursesOfStudent() which displays only courses that student is not registered and not full, which is called from getCourseIdInput()
 *  03/22/2015 - T. Fredericks - created constant COURSE_HEADER, displayCoursesOfStudent() formatting is consistent with displayAllAvailableCourses()
 *  							 added pressToContinue() to unregister() after displaying error message because error is displayed off screen due to displaying courses
 *								 create constant SUCCESSFULLY_SAVED_TO_DATABASE, register() and unregister() display SUCCESSFULLY_SAVED_TO_DATABASE
 *								 register() calls displayAvailableCourses() not displayRegisterableCourses()
 *  03/15/2015 - T. Fredericks - renamed displayAvailableCourses() to displayAllCourses(), renamed menu 1: "Display All Courses"
 */

import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import java.util.Collections;

/**
 * RegSys.RegSys class
 *
 */

public class RegSys{

	//instance variables
	private static ArrayList<Course> courseList;
    private static ArrayList<Student> studentList;
    private static Scanner userInput;
    private static Student regStudent;
    private static RegSys regSys;
    private static final String courseFileName = "CourseDatabase.csv";
	private static final String studentFileName = "StudentDatabase.csv";
	private static final SimpleDateFormat formatter = new SimpleDateFormat(Course.COURSE_DATE_FORMAT);
	private static final String HEADER_SPACING = "\n";
	private static final String COURSE_HEADER = HEADER_SPACING + String.format("%-6s%-8s%-12s%-12s%-30s%-5s%-5s%s", "ID", "NAME","START DATE","END DATE","DESCRIPTION","ENR","CAP","STATUS") + "\n";
	private static final String SUCCESSFULLY_SAVED_TO_DATABASE = "(Saved to database file.)";

	/**
	 *  main
	 *
	 */
	public static void main(String args[]){

		regSys = new RegSys();


        //initialization
        courseList =  new ArrayList<Course>();
        studentList = new ArrayList<Student>();


        //ensure successful loading of databases before continuing
        if (!regSys.isDatabaseLoaded()){
			return;
		}

		try {
            userInput = new Scanner(System.in);

			while (true) {

                regSys.displayLogin();

				if (regStudent == null) {
					System.out.println(HEADER_SPACING + "INVALID LOGIN.");
				} else {
					// check if administrator login -- if so, immediately shutdown
					if (regStudent.getFirstName().equals("System") && regStudent.getLastName().equals("Administrator")) {
						// break out of loop and program ends
						System.out.println(HEADER_SPACING + "SHUTTING DOWN...GOODBYE!");
						userInput.close();
						System.exit(0);
					} else {
						System.out.println(HEADER_SPACING + "WELCOME " + regStudent.getFirstName() + " " + regStudent.getLastName() + "!");
					}
					while(true) {
						//DISPLAY MAIN MENU
						String choice = regSys.displayMenu();

						//VIEW ALL COURSES
						if (choice.equals("1")){
                            regSys.displayAllCourses();
						}

						//VIEW ENROLLED COURSES
						else if (choice.equals("2")){
							regSys.displayCoursesOfStudent();
						}
						//REGISTER
						else if (choice.equals("3")){
							String courseId;
							do{
								courseId = regSys.getCourseIdInput(true);
								if(courseId == null)
									break;
							}while(!regSys.register(courseId));
						}

						//UNREGISTER
						else if (choice.equals("4")){
							String courseId;
							do{
								courseId = regSys.getCourseIdInput(false);
								if(courseId == null)
									break;
							}while(!regSys.unregister(courseId));
						}

						//LOGOUT
						else if (choice.equals("5")) {
							System.out.println(HEADER_SPACING + "Logging out.  Have a nice day " + regStudent.getFirstName() + " " + regStudent.getLastName() + "!");
							break;
						}

						else{
							System.out.println("INVALID MENU CHOICE.");
						}
					}//while
				}//else
			}//while
		} catch (Exception e) {
			System.out.println("Unknown Exception " + e);
        }

        //close Scanner
		userInput.close();
    }//main


	/**
	 *  login
	 *
	 */
	 public Student login(String studentID, String password){

		for(Student student: studentList){
			if(student.getID().equals(studentID) && student.getPassword().equals(password)){
				return student;
			}
		}
		return null;
	}

	/**
	 *  displayLogin
	 *
	 */

	public void displayLogin(){

        System.out.println(HEADER_SPACING +
        		"***********************************************************\n" +
                "*  Welcome to the Team-A Course Registration Application  *\n" +
                "*  (version 0.1)                                          *\n" +
                "***********************************************************\n");

        System.out.print("login: ");
		// get student id
		String sid = userInput.next();

		System.out.print("password: ");
		// get password
		String pwd = userInput.next();

		//save student object
		regStudent = login(sid, pwd);

	}

	/**
	 *  loadDatabases
	 *
	 */

	 public boolean isDatabaseLoaded(){

		 //DEBUG System.out.println("Loading Course and Student databases...");
		 if(isCourseDBLoaded() && isStudentDBLoaded())
		 	return true;
		 else
		 	return false;
	 }

	/**
	 *  displayMainMenu
	 *
	 */
	 public String displayMenu() {
		 while (true) {
			 System.out.print(HEADER_SPACING +
				 "***********************************\n" +
				 "*                                 *\n" +
				 "*  Course Registration Main Menu  *\n" +
				 "*                                 *\n" +
				 "***********************************\n" +
				 "\n" +
				 "Enter 1: Displays All Courses\n" +
				 "Enter 2: Displays Enrolled Course(s)\n" +
				 "Enter 3: To Register for a Course\n" +
				 "Enter 4: To Un-Register a Course\n" +
				 "Enter 5: Logout\n" +
				 "\n" +
				 "Enter 1, 2, 3, 4, or 5: ");

			 String choice = userInput.next();
			 if (choice.equals("1") || choice.equals("2") || choice.equals("3") || choice.equals("4") || choice.equals("5")) {
				 return choice;
			 } else {
				 System.out.println(HEADER_SPACING + "INVALID SELECTION!  TRY AGAIN!");
			 }
		 }
    }

	/**
	 *  getCourseIdInput
	 *
	 */
	 public String getCourseIdInput(boolean isRegister){

		 //REGISTER
		if (isRegister){
			System.out.println(HEADER_SPACING +
	        		 "*********************************\n" +
	                 "*                               *\n" +
	                 "*  Menu: Register for Course    *\n" +
	                 "*                               *\n" +
	                 "*********************************");

	         displayAllCourses();
		}
		// UNREGISTER
		else {
            System.out.println(HEADER_SPACING +
                    "*********************************\n" +
                    "*                               *\n" +
                    "*  Menu: Unregister for Course  *\n" +
                    "*                               *\n" +
                    "*********************************");

			displayCoursesOfStudent();
		}

		String registerString = isRegister ? "register" : "unregister";

		System.out.println(HEADER_SPACING + "Enter the Course ID to " + registerString + " OR 'X' to return to Main Menu: ");
		String courseId = userInput.next();

		if (courseId.toUpperCase().equals("X")) {
            return null;
        } else {
            return courseId;
        }
 	}

	 /**
	 *  pressToContinue
	 *
	 */

	 private void pressToContinue(){
		 System.out.println(HEADER_SPACING + "press [ENTER] key to continue...");
		 try{
            System.in.read();
		 }
        catch(Exception e){
        	e.printStackTrace();
        }

	 }


	/**
	 *  register
	 *
	 */

	public boolean register(String courseID){

		//check that user entered a valid course id
		Course course = getCourseById(courseID);
		if (course == null){
			System.out.println(HEADER_SPACING + "Invalid course id entered.");
			pressToContinue();
			return false;
		}

		//check if course is full
		if (!course.openSeat()){
			System.out.println(HEADER_SPACING + "Course is full.");
			pressToContinue();
			return false;
		}

		//check  if student is already registered to the course
		if(regStudent.isRegistered(courseID)){
			System.out.println(HEADER_SPACING + "You are already enrolled in this course.");
			pressToContinue();
			return false;
		}

		//register student to the course
		if (regStudent.register(courseID)){
			//increment enrollment count for the course
			if(course.enrollment()) {

				//rewrite student and course files by reading from studentList and courseList
				if(refreshStudentDB() && refreshCourseDB()){
					System.out.println(HEADER_SPACING + "REGISTRATION SUCCESSFUL " + SUCCESSFULLY_SAVED_TO_DATABASE);
					return true;
				}
			}
		}

		return false;
	}

	/**
	 *  displayCoursesOfStudent
	 *
	 */
	 public void displayCoursesOfStudent(){
		 System.out.println(HEADER_SPACING +
				 "*********************************\n" +
				 "*                               *\n" +
				 "*  Your enrolled courses        *\n" +
				 "*                               *\n" +
				 "*********************************");
		if (regStudent.getRegCourses().size() == 0)
					System.out.println(HEADER_SPACING + "You are not registered to any courses.");
		else{
			System.out.println(COURSE_HEADER);

			//sort courses by name
			ArrayList<String> regCourseIDs = regStudent.getRegCourses();

			//save ArrayList as Course objects
			ArrayList<Course> regCourses = new ArrayList<Course>();
			for ( String s : regCourseIDs )
			 	regCourses.add(regSys.getCourseById(s));

			//sort by course name
			Collections.sort(regCourses);

			//display courses
			for (Course c : regCourses){
				if (c != null)
					System.out.println(c);
			}
		}

	 }

	/**
	 *  unregister
	 *
	 */
	public boolean unregister(String courseID){
		Course course = getCourseById(courseID);

		//check if this is a valid course
		if(course == null){
			System.out.println(HEADER_SPACING + "ENTERED AN INVALID COURSE ID.");
			pressToContinue();
			return false;
		}

		//check if student is already registered to this course
		else if (!regStudent.isRegistered(courseID)){
			System.out.println(HEADER_SPACING + "YOU ARE NOT REGISTERED TO THIS COURSE.");
			pressToContinue();
			return false;
		}


		//register student to this course
		else if (regStudent.unregister(courseID)) {

			//decrement student count for course
			if(course.unenrollment()){
				//updateArrayLists(course);

				//rewrite database files based on array lists
				if (refreshStudentDB() && refreshCourseDB()){
					System.out.println(HEADER_SPACING + "REMOVING REGISTRATION SUCCESSFUL " + SUCCESSFULLY_SAVED_TO_DATABASE);
					return true;
				}
			}
		}
		System.out.println(HEADER_SPACING + "Failed to unregister from course.");
		pressToContinue();

		return false;
	}

	/**
	 *  getIndexByCourseId
	 *
	 */
	public int getIndexByCourseId(String courseID){
		for(int i = 0; i < courseList.size(); i++){
			if(courseList.get(i).getCourseID().equals(courseID)){
				return i;
			}
		}
		return -1;
	}

	/**
	 *  getCourseById
	 *
	 */
	public Course getCourseById(String courseID){
		for(Course c : courseList){
			if(c.getCourseID().equals(courseID)){
				return c;
			}
		}
		return null;
	}

	/**
	 *  isCourseDBLoaded
	 *
	 */
	public boolean isCourseDBLoaded(){

		try {
			Scanner fileScanner = new Scanner(new File(courseFileName));
			fileScanner.useDelimiter("\n");

			//read from file if it's not empty
			while(fileScanner.hasNext()){
				String str = fileScanner.next();
				String[] tokens = str.split(",");

				Date dateStart = new Date();
				Date dateEnd = new Date();
				try{
					dateStart = formatter.parse(tokens[1]);
					dateEnd = formatter.parse(tokens[2]);
				}catch (ParseException pe){
                    System.out.println(HEADER_SPACING + "EXCEPTION 1");
					pe.printStackTrace();
				}

				try{
					courseList.add(
						new Course(tokens[0],tokens[3],dateStart,dateEnd,tokens[4],Integer.parseInt(tokens[5]), Integer.parseInt(tokens[6].trim())));
				}catch (NumberFormatException nfe){
                    System.out.println(HEADER_SPACING + "EXCEPTION 2");
					nfe.printStackTrace();
				}

			}
			fileScanner.close();

			return true;
		} catch (FileNotFoundException e) {
			System.out.println(HEADER_SPACING + "The file is not found");
		} catch (InputMismatchException e) {
			System.out.println(HEADER_SPACING + "Reading the wrong input type");
		}
		return false;
	}

	/**
	 *  isStudentDBLoaded
	 *
	 */
	public boolean isStudentDBLoaded(){
		try {
			Scanner fileScanner = new Scanner(new File(studentFileName));
			fileScanner.useDelimiter("\n");

			//read from file if it's not empty
			while(fileScanner.hasNext()){
				String str = fileScanner.next();

				String[] tokens = str.split(",");
				Student student = new Student(tokens[0],tokens[1],tokens[2],tokens[3].trim());

				//save registered courses for student, if any
				if (tokens.length > 4){
					for (int i = 4; i < tokens.length; i++){
						if(tokens[i] != null){
							student.register(tokens[i].trim());
						}
					}
				}
				studentList.add(student);

			}
			fileScanner.close();

			return true;
		} catch (FileNotFoundException e) {
			System.out.println(HEADER_SPACING + "The file is not found");
		} catch (InputMismatchException e) {
			System.out.println(HEADER_SPACING + "Readng thes wrong input type");
		}

		return false;
	}

	/**
	 *  rewrites course.csv from contents of courseList
	 *
	 */
	public boolean refreshCourseDB(){

		try{
			FileWriter fileWriter = new FileWriter(courseFileName);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			//write comma-delimited Person details
			for (Course c : courseList){
				/* The order of arguments must be consistent with order the columns in CourseDatabase file.
				 Note:  the order is different that Course constructor parameters */
				bufferedWriter.write(c.getCourseID() + "," + /* COURSE ID */
				formatter.format(c.getStart()) + "," + /* START DATE */
				formatter.format(c.getEnd()) + "," + /* END DATE */
				c.getName() + "," + /* COURSE NAME */
				c.getDescription() + "," + /* DESCRIPTION */
				c.getEnrollLimit() + "," + /* ENROLLMENT LIMIT */
				c.getStudentCount()); /* STUDENT COUNT */
				//write new line
				bufferedWriter.newLine();
			}

			bufferedWriter.close();
			return true;

		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}

		return false;
	}

	/**
	 *  rewrites student.csv from contents of studentList
	 *
	 */
	public boolean refreshStudentDB(){

		try{
			FileWriter fileWriter = new FileWriter(studentFileName);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			//write comma-delimited Person details
			for (Student s : studentList){
				bufferedWriter.write(s.getID() + "," +
				s.getFirstName() + "," +
				s.getLastName() + "," +
				s.getPassword());

				//if student has registered courses, add them
				if (s.getRegCourses().size() != 0){
					for(String courseId : s.getRegCourses()){
						bufferedWriter.write("," + courseId);
					}
				}
				//write new line
				bufferedWriter.newLine();
			}

			bufferedWriter.close();
			return true;

			}catch(FileNotFoundException e){
				e.printStackTrace();
			}catch(IOException ioe){
				ioe.printStackTrace();
		}

		return false;
	}

	/**
	 *  displayRegisterableCoursesOfStudent
	 *
	 */

	public void displayRegisterableCoursesOfStudent(){
		ArrayList<String> studentCourseIds = regStudent.getRegCourses();
		ArrayList<Course> registerableCourses = new ArrayList<Course>();

		for (Course c : courseList){
			//save the course that student is not registered and is not full
			if(!studentCourseIds.contains(c.getCourseID()) && c.openSeat()){
				registerableCourses.add(c);
			}
		}

		//if the arraylist empty, no courses to display
		if (registerableCourses.size() == 0)
			System.out.println("There are no courses that has an open seat and that you are not registered.");
		else{
			//sort registerable courses by name
			Collections.sort(registerableCourses);

			//header names
			System.out.println(COURSE_HEADER);

			//display registerable courses
			for (Course c: registerableCourses){
				System.out.println(c);
			}
		}


	}

	/**
	 *  displayAllCourses
	 *
	 */
    public void displayAllCourses() {
		int courseCnt = 0;

		System.out.println(HEADER_SPACING + "COURSE LIST:");

		//sort courses by name
		Collections.sort(courseList);

		for(Course course : courseList) {
			courseCnt++;
			//display first header
			if (courseCnt == 1)
				System.out.println(COURSE_HEADER);
			if((courseCnt % 10) == 0) {
				System.out.println(COURSE_HEADER);
			}
			System.out.println(course);
		}

    }

}

