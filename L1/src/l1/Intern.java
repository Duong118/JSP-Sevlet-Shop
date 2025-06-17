/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package l1;

import java.util.Calendar;

/**
 * L01 - Create a Java console program to manage Candidates of company.
 *
 * @author DUONGNCCE180374
 */
public class Intern extends Candidate {

    public static final int MAX_AGE = 60; // Maximum age for experienced candidates
    public static final int MIN_AGE = 21; // Minimum age for intern candidates

    private String major; // Candidate's major
    private String semester; // Candidate's current semester
    private String universityName; // Name of the university

    /**
     * Constructor for the Intern class.
     *
     * @param candidateId The candidate's ID.
     * @param firstName The candidate's first name.
     * @param lastName The candidate's last name.
     * @param birthDate The candidate's birth year.
     * @param address The candidate's address.
     * @param phone The candidate's phone number.
     * @param email The candidate's email.
     * @param candidateType The type of the candidate (2 for Intern).
     * @param major The candidate's major.
     * @param semester The candidate's current semester.
     * @param universityName The name of the university the candidate is
     * attending.
     */
    public Intern(String candidateId, String firstName, String lastName, int birthDate, String address,
            String phone, String email, int candidateType, String major, String semester, String universityName) {
        super(candidateId, firstName, lastName, birthDate, address, phone, email, candidateType);
        this.major = major;
        this.semester = semester;
        this.universityName = universityName;

        // Validate birth date during construction
        if (!isValidBirthDate(birthDate)) {
            throw new IllegalArgumentException("Invalid birth date for Intern candidate.");
        }
    }



    /**
     * Validates birth year for Intern candidates (must be at least 21 years
     * old).
     *
     * @param birthDate candidate's birth year
     * @return true if valid, otherwise false
     */
    public static boolean isValidBirthDate(int birthDate) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int age = currentYear - birthDate;
        return age >= MIN_AGE && age <= MAX_AGE;
    }

    /**
     * Get the major of the intern.
     *
     * @return Major.
     */
    public String getMajor() {
        return major; // Return the major of the intern
    }

    /**
     * Set the major of the intern.
     *
     * @param major Major to set.
     */
    public void setMajor(String major) {
        this.major = major; // Set the major of the intern
    }

    /**
     * Get the current semester of the intern.
     *
     * @return Semester.
     */
    public String getSemester() {
        return semester; // Return the current semester of the intern
    }

    /**
     * Set the current semester of the intern.
     *
     * @param semester Semester to set.
     */
    public void setSemester(String semester) {
        this.semester = semester; // Set the current semester of the intern
    }

    /**
     * Get the name of the university.
     *
     * @return University name.
     */
    public String getUniversityName() {
        return universityName; // Return the name of the university
    }

    /**
     * Set the name of the university.
     *
     * @param universityName University name to set.
     */
    public void setUniversityName(String universityName) {
        this.universityName = universityName; // Set the name of the university
    }

    /**
     * Overrides the toString method to display detailed information for an
     * intern candidate. This method extends the Candidate class's toString
     * method by adding specific details related to interns.
     *
     * @return A formatted string containing the intern's details, including
     * major, semester, and university name.
     */
    @Override
    public String toString() {
        return super.toString() + String.format(" | Major: %s | Semester: %s | University: %s",
                major, semester, universityName);
    }
}
