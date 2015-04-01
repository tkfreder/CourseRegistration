/* Course.java			0.01 03/04/2015
 *
 * This is code written for UCI Extension - I&C SCI_X460.10 WINTER 2015,UNEX,00113
 *
 * Team: Team A
 * Date: 03/04/2015
 * Class: UCI Extension - I&C SCI_X460.10 WINTER 2015,UNEX,00113
 * Assignment: Team Assignment
 *
 * 03/04/2015 - Michael Morodomi - Initial file creation
 * 03/05/2015 - Perry On - fixed enrollment(), unenrollment(), openSeat()
 * 						   commented out isEnrollmentSuccessful(), isUnenrollmentSuccessful(), and isOpenSeat()
 * 						   updated toString() to remove unnecessary sets at beginning of function
 * 03/07/2015 - D. Fuller - Added Status OPEN or FULL to toString().
 * 03/07/2015 - T. Fredericks - modified toString() - replaced column names with tab delimiters, let caller handle printing of column names
 * 03/08/2015 - D. Fuller - modified toString for column formatting to improve legibility
 * 03/10/2015 - T. Fredericks - implement Comparable, created protected constant COURSE_DATE_FORMAT, which is used by RegSys
 * 03/13/2015 - D. Fuller - changed access modifer for course class from "protected" to "public" to be consistent with Student class.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;



public class Course implements Comparable<Course>{

    // Course DB File Information
    private String courseID = "";
    private String name     = "";
    private Date dateStart          = null;
    private Date dateEnd            = null;
    private String description      = "";
    private int enrollLimit         = 0;
    private int studentCount        = 0;
    protected static final String COURSE_DATE_FORMAT = "MM/dd/yyyy";

    private SimpleDateFormat dateFormat = new SimpleDateFormat(COURSE_DATE_FORMAT);


    // default Course constructor
    public Course() {

        // set course Information
        this.courseID     = "NO ID";
        this.name         = "NO NAME";
        this.dateStart    = new Date();
        this.dateEnd      = new Date();
        this.description  = "NO DESC";
        this.enrollLimit  = 0;
        this.studentCount = 0;

        String begin = "4/15/2015";  // default start date
        String end   = "5/15/2015";  // default end date

        try {
			this.dateStart = dateFormat.parse(begin);
			this.dateEnd   = dateFormat.parse(end);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }


    // default parameterized Course constructor
    public Course(String courseID,
    		      String name,
    		      Date dateStart,
    		      Date dateEnd,
    		      String description,
    		      int enrollLimit,
    		      int studentCount) {

        // set course Information
    	setCourseID(courseID);
    	setName(name);
        setStart(dateStart);
        setEnd(dateEnd);
        setDescription(description);
        setEnrollLimit(enrollLimit);
        setStudentCount(studentCount);
	}

    ////////////////////////////////////////////////
    // method for setter course ID
    ////////////////////////////////////////////////
    public void setCourseID(String courseID) {

        // set course ID
        this.courseID = courseID;
    }

    ////////////////////////////////////////////////
    // method for getter course ID
    ////////////////////////////////////////////////
    public String getCourseID() {

        // return course ID
        return this.courseID;
    }

    ////////////////////////////////////////////////
    // method for setter name
    ////////////////////////////////////////////////
    public void setName(String name) {

        // set name
        this.name = name;
    }

    ////////////////////////////////////////////////
    // method for getter name
    ////////////////////////////////////////////////
    public String getName() {

        // return name
        return this.name;
    }

    ////////////////////////////////////////////////
    // method for setter start date
    ////////////////////////////////////////////////
    public void setStart(Date date) {

        // set start date
        this.dateStart = date;
    }

    ////////////////////////////////////////////////
    // method for getter start date
    ////////////////////////////////////////////////
    public Date getStart() {

        // return start date
        return this.dateStart;
    }

    ////////////////////////////////////////////////
    // method for setter end date
    ////////////////////////////////////////////////
    public void setEnd(Date date) {

        // set end date
        this.dateEnd = date;
    }

    ////////////////////////////////////////////////
    // method for getter end date
    ////////////////////////////////////////////////
    public Date getEnd() {

        // return end date
        return this.dateEnd;
    }

    ////////////////////////////////////////////////
    // method for setter description
    ////////////////////////////////////////////////
    public void setDescription(String description) {

        // set description
        this.description = description;
    }

    ////////////////////////////////////////////////
    // method for getter description
    ////////////////////////////////////////////////
    public String getDescription() {

        // return description
        return this.description;
    }

    ////////////////////////////////////////////////
    // method for setter Enroll Limit
    ////////////////////////////////////////////////
    public void setEnrollLimit(int enrollLimit) {

        // set Enroll Limit
        this.enrollLimit = enrollLimit;
    }

    ////////////////////////////////////////////////
    // method for getter Enroll Limit
    ////////////////////////////////////////////////
    public int getEnrollLimit() {

        // return Enroll Limit
        return this.enrollLimit;
    }

    ////////////////////////////////////////////////
    // method for setter Student Count
    ////////////////////////////////////////////////
    public void setStudentCount(int studentCount) {

        // set Student Count
        this.studentCount = studentCount;
    }

    ////////////////////////////////////////////////
    // method for getter Student Count
    ////////////////////////////////////////////////
    public int getStudentCount() {

        // return Student Count
        return this.studentCount;
    }


    /////////////////////////////////////////////////////////////
    // method enrollment.  Returns True if space was available
    // and enrollment was successful
    /////////////////////////////////////////////////////////////
    public boolean enrollment() {
        if(openSeat()){
        	studentCount++;
        	return true;
        } else {
			return false;
        }
    }

    /////////////////////////////////////////////////////////////
    // method for unenrollment.  Returns True if unenrollment
    // was successful (i.e., course enrollments existed and not at zero)
    /////////////////////////////////////////////////////////////
    public boolean unenrollment() {
    	if(studentCount > 0){
    		studentCount--;
    		return true;
    	} else if(studentCount == 0){
    		return false;
    	} else{
			System.out.println("Warning from Course.java: studentCount less than 0, cannot unenroll");
			return false;
    	}
    }

    /////////////////////////////////////////////////////////////
    // method for openSeat.  Returns TRUE if a seat is
    // available for registration
    /////////////////////////////////////////////////////////////
    public boolean openSeat() {
        if(studentCount < enrollLimit){
        	return true;
        } else if(studentCount == enrollLimit){
        	return false;
        } else {
			System.out.println("Warning from Course.java: studentCount higher than enrollment limit");
			return false;
        }
    }

/*
    /////////////////////////////////////////////////////////////
    // method for isEnrollmentSuccess.  Returns TRUE if a
    // enrollment is successful
    /////////////////////////////////////////////////////////////
    private boolean isEnrollmentSuccess(String id) {

    	boolean result = false;

 		File courseDbFile = null;
		Scanner fileScanner;

		try {

			// create courseDbFile file object
			courseDbFile = new File("CourseDatabase.csv");
			fileScanner = new Scanner(courseDbFile);
			fileScanner.useDelimiter(",");

			while (fileScanner.hasNextLine()) {

				// read a line out of the CourseDatabase.csv file
				String line = fileScanner.nextLine();

				// split up the line based on the "," delimiter
				String[] info = line.split(",");

				// get course information
				// Course ID
				String courseId = info[0];

//				// Start Date
//				Date start = stringToDate(info[1]);
//
//				// End Date
//				Date end = stringToDate(info[2]);
//
//				// Name
//				String name = info[3];
//
//				// Description
//				String desc = info[4];

				// Enrollment Limit
				int enrollLimit = Integer.parseInt(info[5]);

				// Student Count Limit
				int studentCount = Integer.parseInt(info[6]);

				// search for the input ID
				if (courseId.equals(id)) {

					// if there is room in the class set result to true
					if (studentCount < enrollLimit) {
						result = true;
					}
				}
			}

			// close file scanner
			fileScanner.close();
		}
		catch (FileNotFoundException e) {

			// return false
			result = false;

			// print out stack trace
			e.printStackTrace();
		}

		return result;
    }


    /////////////////////////////////////////////////////////////
    // method for isUnenrollmentSuccess.  Returns TRUE if a
    // unenrollment is successful
    /////////////////////////////////////////////////////////////
    private boolean isUnenrollmentSuccess(String id) {

    	boolean result = false;

		File courseDbFile = null;
		Scanner fileScanner;

		try {

			// create courseDbFile file object
			courseDbFile = new File("CourseDatabase.csv");
			fileScanner = new Scanner(courseDbFile);
			fileScanner.useDelimiter(",");

			while (fileScanner.hasNextLine()) {

				// read a line out of the CourseDatabase.csv file
				String line = fileScanner.nextLine();

				// split up the line based on the "," delimiter
				String[] info = line.split(",");

				// get course information
				// Course ID
				String courseId = info[0];

//				// Start Date
//				Date start = stringToDate(info[1]);
//
//				// End Date
//				Date end = stringToDate(info[2]);
//
//				// Name
//				String name = info[3];
//
//				// Description
//				String desc = info[4];
//
//				// Enrollment Limit
//				int enrollLimit = Integer.parseInt(info[5]);

				// Student Count Limit
				int studentCount = Integer.parseInt(info[6]);

				// search for the input ID
				if (courseId.equals(id)) {

					// if there is at lease one student, then set result to true
					if (studentCount > 0) {
						result = true;
					}
				}
			}

			// close file scanner
			fileScanner.close();
		}
		catch (FileNotFoundException e) {

			// return false
			result = false;

			// print out stack trace
			e.printStackTrace();
		}

		return result;
    }


    /////////////////////////////////////////////////////////////
    // method for isOpenSeat.  Returns TRUE if a
    // seat is available
    /////////////////////////////////////////////////////////////
    private boolean isOpenSeat(String id) {

    	boolean result = false;

 		File courseDbFile = null;
		Scanner fileScanner;

		try {

			// create courseDbFile file object
			courseDbFile = new File("CourseDatabase.csv");
			fileScanner = new Scanner(courseDbFile);
			fileScanner.useDelimiter(",");

			while (fileScanner.hasNextLine()) {

				// read a line out of the CourseDatabase.csv file
				String line = fileScanner.nextLine();

				// split up the line based on the "," delimiter
				String[] info = line.split(",");

				// get course information
				// Course ID
				String courseId = info[0];

//				// Start Date
//				Date start = stringToDate(info[1]);
//
//				// End Date
//				Date end = stringToDate(info[2]);
//
//				// Name
//				String name = info[3];
//
//				// Description
//				String desc = info[4];

				// Enrollment Limit
				int enrollLimit = Integer.parseInt(info[5]);

				// Student Count Limit
				int studentCount = Integer.parseInt(info[6]);

				// search for the input ID
				if (courseId.equals(id)) {

					// if there is room in the class set result to true
					if (studentCount < enrollLimit) {
						result = true;
					}
				}
			}

			// close file scanner
			fileScanner.close();
		}
		catch (FileNotFoundException e) {

			// return false
			result = false;

			// print out stack trace
			e.printStackTrace();
		}

		return result;
    }
*/

	/////////////////////////////////////////////////////////////
	// method to convert string date to Date type
	/////////////////////////////////////////////////////////////
	private Date stringToDate(String date) {

		Date result = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
			result = dateFormat.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return result;
	}


    ////////////////////////////////////////////////
    // method that returns a String of all course
    // information.
    ////////////////////////////////////////////////
    public String toString() {
        String status;

        if(openSeat()) {
            status = "OPEN";
        }
        else {
            status = "FULL";
        }

        String padFormat = "%-" + String.valueOf(30 - getDescription().length()) + "s";


        String str = String.format("%-6s%-8s%-12s%-12s%-30s%-5s%-5s%s", getCourseID(), getName(),dateFormat.format(getStart())
                ,dateFormat.format(getEnd()),getDescription(),getStudentCount(),getEnrollLimit(),status);

        return str;
    }

    @Override
		 public int compareTo(Course c1) {
			 return name.compareTo(c1.getName());
    }
}