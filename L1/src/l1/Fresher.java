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
public class Fresher extends Candidate {

    public static final int MAX_AGE = 60; // Maximum age for experienced candidates
    public static final int MIN_AGE = 22; // Minimum age for fresher candidates

    private int graduationYear; // Graduation year
    private String graduationRank; // Graduation rank
    private String education; // Education institution name

    /**
     * Constructor for the Fresher class.
     *
     * @param candidateId The candidate's ID.
     * @param firstName The candidate's first name.
     * @param lastName The candidate's last name.
     * @param birthDate The candidate's birth year.
     * @param address The candidate's address.
     * @param phone The candidate's phone number.
     * @param email The candidate's email.
     * @param candidateType The type of the candidate (1 for Fresher).
     * @param graduationYear The year of graduation.
     * @param graduationRank The graduation rank (e.g., Excellent, Good).
     * @param education The name of the university or educational institution.
     */
    public Fresher(String candidateId, String firstName, String lastName, int birthDate, String address,
            String phone, String email, int candidateType, int graduationYear, String graduationRank, String education) {
        super(candidateId, firstName, lastName, birthDate, address, phone, email, candidateType);
        this.graduationYear = graduationYear;
        this.graduationRank = graduationRank;
        this.education = education;
        // Validate birth date during construction
        if (!isValidBirthDate(birthDate)) {
            throw new IllegalArgumentException("Invalid birth date for Fresher candidate.");
        }
    }

    /**
     * Validates birth year for Fresher candidates (must be at least 22 years
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
     * Validates if the input string is a valid integer.
     *
     * @param input the input string
     * @return true if the input is a valid integer, otherwise false
     */
    public boolean isValidInteger(String input) {
        if (input == null) {
            return false; // Null input is not valid
        }
        try {
            Integer.parseInt(input); // Attempt to parse the input as an integer
            return true; // Parsing succeeded, input is valid
        } catch (NumberFormatException e) {
            return false; // Parsing failed, input is not a valid integer
        }
    }

    /**
     * Get the graduation year.
     *
     * @return Graduation year.
     */
    public int getGraduationYear() {
        return graduationYear; // Return the graduation year
    }

    /**
     * Set the graduation year.
     *
     * @param graduationYear Graduation year to set.
     */
    public void setGraduationYear(int graduationYear) {
        this.graduationYear = graduationYear; // Set the graduation year
    }

    /**
     * Get the graduation rank.
     *
     * @return Graduation rank.
     */
    public String getGraduationRank() {
        return graduationRank; // Return the graduation rank
    }

    /**
     * Set the graduation rank.
     *
     * @param graduationRank Graduation rank to set.
     */
    public void setGraduationRank(String graduationRank) {
        this.graduationRank = graduationRank; // Set the graduation rank
    }

    /**
     * Get the name of the university.
     *
     * @return University name.
     */
    public String getEducation() {
        return education; // Return the name of the university
    }

    /**
     * Set the name of the university.
     *
     * @param education University name to set.
     */
    public void setEducation(String education) {
        this.education = education; // Set the name of the university
    }

    /**
     * Overrides the toString method to display detailed information for a
     * Fresher candidate. This method extends the Candidate class's toString
     * method by adding specific details for Freshers, including graduation
     * year, graduation rank, and university name.
     *
     * @return A formatted string containing the Fresher's details.
     */
    @Override
    public String toString() {
        return super.toString() + String.format(" | Graduation Year: %d | Rank: %s | Education: %s",
                graduationYear, graduationRank, education);
    }
}
