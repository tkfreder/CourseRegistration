/* Student.java			0.01 03/03/2015
 *
 * This is code written for UCI Extension - I&C SCI_X460.10 WINTER 2015,UNEX,00113
 *
 * Team: Team A
 * Date: 03/03/2015
 * Class: UCI Extension - I&C SCI_X460.10 WINTER 2015,UNEX,00113
 * Assignment: Team Assignment
 *
 * 03/03/2015 - Perry On - Initial file creation
 * 03/04/2015 - Perry On - Added getRegCourses()
 * 03/04/2015 - Perry On - Updated toString() to print out list instead of comma delimited courses
 * 03/07/2015 - Tina Fredericks - created DEBUG constant to turn on/off warning statements.  DEBUG = false, means let caller manage printing error messages.
 */

import java.util.ArrayList;

/**
 * This Student class implements a class for a Student
 * including student ID, first name, last name, password, and course(s).
 */
public class Student {

	private static final boolean DEBUG = false;

	//Instance Variables
	private String studentID;
	private String firstName;
	private String lastName;
	private String password;
	private ArrayList<String> regCourses;

	/**
	 * Constructor without any parameters.  Initializes all Student parameters to "" and creates
	 * an empty regCourses array list.
	 * All parameters initialized to "unknown" and ArrayList initialized.
	 */
	public Student(){
		studentID = "";
		firstName = "";
		lastName = "";
		password = "";
		regCourses = new ArrayList<String>();
	}

	/**
	 *  Overloaded constructor with primary parameters.  ArrayList is initialized.
	 *
	 *  @param	studentID	unique student ID assigned to each student
	 *  @param	lastName	last name of student
	 *  @param	firstName	first name of student
	 *  @param	password	student login password
	 */
	public Student(String sID, String fName, String lName, String pw){
		studentID = sID;
		firstName = fName;
		lastName = lName;
		password = pw;
		regCourses = new ArrayList<String>();
	}

	/**
	 *  Returns true if student has successfully been assigned student ID.  Otherwise, returns false.
	 *
	 *  @param	sID		unique student ID assigned to each student, must be non-blank value
	 */
	public boolean setID(String sID){
		if(sID.isEmpty()){
			if (DEBUG) System.out.println("Warning from Student.java: setting student ID to blank ID string.");
			return false;
		}
		else{
			studentID = sID;
			return true;
		}
	}

	/**
	 *  Returns true if student has successfully been assigned first name.  Otherwise, returns false.
	 *
	 *  @param	fName	first name of student, must be non-blank value
	 */
	public boolean setFirstName(String fName){
		if(fName.isEmpty()){
			if (DEBUG) System.out.println("Warning from Student.java: setting student first name to blank string.");
			return false;
		}
		else{
			firstName = fName;
			return true;
		}
	}

	/**
	 *  Returns true if student has successfully been assigned last name.  Otherwise, returns false.
	 *
	 *  @param	lName	last name of student, must be non-blank value
	 */
	public boolean setLastName(String lName){
		if(lName.isEmpty()){
			if (DEBUG) System.out.println("Warning from Student.java: setting student last name to blank string.");
			return false;
		}
		else{
			lastName = lName;
			return true;
		}
	}

	/**
	 *  Returns true if student has successfully set password.  Otherwise, returns false.
	 *
	 *  @param	pw		desired password for student, must be non-blank value
	 */
	public boolean setPassword(String pw){
		if(pw.isEmpty()){
			if (DEBUG) System.out.println("Warning from Student.java: setting student password to blank string.");
			return false;
		}
		else{
			password = pw;
			return true;
		}
	}

	/**
	 *  Returns String studentID.
	 *
	 */
	public String getID(){
		if(studentID.isEmpty()){
			if (DEBUG) System.out.println("Warning from Student.java: retrieved blank string for student ID.");
			return studentID;
		}
		else{
			return studentID;
		}
	}

	/**
	 *  Returns String firstName.
	 *
	 */
	public String getFirstName(){
		if(firstName.isEmpty()){
			if (DEBUG) System.out.println("Warning from Student.java: retrieved blank string for first name.");
			return firstName;
		}
		else{
			return firstName;
		}
	}

	/**
	 *  Returns String lastName.
	 *
	 */
	public String getLastName(){
		if(lastName.isEmpty()){
			if (DEBUG) System.out.println("Warning from Student.java: retrieved blank string for last name.");
			return lastName;
		}
		else{
			return lastName;
		}
	}

	/**
	 *  Returns String password.
	 *
	 */
	public String getPassword(){
		if(password.isEmpty()){
			if (DEBUG) System.out.println("Warning from Student.java: retrieved blank string for password.");
			return password;
		}
		else{
			return password;
		}
	}

	/**
	 *  Returns regCourses.
	 *
	 */
	public ArrayList<String> getRegCourses(){
		return regCourses;
	}

	/**
	 *  Returns true if student is already enrolled in course.  Otherwise, returns false.
	 *  Warning displayed if checked against blank course ID.
	 */
	public boolean isRegistered(String cID){
		if(cID.isEmpty()){
			if (DEBUG) System.out.println("Warning from Student.java: course ID verification is blank.");
		}

		for(String c: regCourses){
			if(c.equalsIgnoreCase(cID)){
				return true;
			}
		}
		return false;
	}

	/**
	 *  Register student for course indicated by courseID.  Returns true upon successful registration.
	 *  Otherwise, returns false.
	 *
	 */
	public boolean register(String cID){
		if(cID.isEmpty()){
			if (DEBUG) System.out.println("Warning from Student.java: attempting to register to blank courseID.");
			return false;
		}
		else{
			regCourses.add(cID);
			return true;
		}
	}

	/**
	 *  Unregister student for course indicated by courseID.  Returns true upon successful unregistration.
	 *  Otherwise, returns false.
	 *
	 */
	public boolean unregister(String cID){
		if(cID.isEmpty()){
			if (DEBUG) System.out.println("Warning from Student.java: attempting to unregister from blank courseID.");
			return false;
		}
		else{
			return regCourses.remove(cID);
		}
	}

	/**
	 *  Unregister student for course indicated by courseID.  Returns true upon successful unregistration.
	 *  Otherwise, returns false.
	 *
	 */
	public String toString(){
		String s = "";
		s = firstName + " " + lastName + " (ID " + studentID + ") is ";
		if(regCourses.isEmpty()){
			s += "not registered in any courses.";
			if (DEBUG) return s;
			else return null;
		}
		else{
			s += "registered in the following course(s): \n";
			for(int i = 0; i < regCourses.size(); i++){
				if(i < (regCourses.size() - 1)){
					s += regCourses.get(i) + "\n";
				}
				else{
					s += regCourses.get(i);
				}
			}
			return s;
		}
	}
}
