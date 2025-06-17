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
public class Experience extends Candidate {

    public static final int MAX_AGE = 60; // Tuổi tối đa cho ứng viên có kinh nghiệm
    public static final int MIN_AGE = 23; // Minimum age for experience candidates
    public static final int MIN_EXPERIENCE = 0; // Minimum experience required

    private int yearsOfExperience; // Years of experience
    private String professionalSkill; // Professional skills

    /**
     * Constructor for the Experience class.
     *
     * @param candidateId The candidate's ID.
     * @param firstName The candidate's first name.
     * @param lastName The candidate's last name.
     * @param birthDate The candidate's birth year.
     * @param address The candidate's address.
     * @param phone The candidate's phone number.
     * @param email The candidate's email.
     * @param candidateType The type of the candidate (0 for Experience).
     * @param yearsOfExperience The number of years of experience.
     * @param professionalSkill The professional skills of the candidate.
     */
    public Experience(String candidateId, String firstName, String lastName, int birthDate, String address,
            String phone, String email, int candidateType, int yearsOfExperience, String professionalSkill) {
        super(candidateId, firstName, lastName, birthDate, address, phone, email, candidateType);
        this.yearsOfExperience = yearsOfExperience;
        this.professionalSkill = professionalSkill;

        // Validate birth date inside constructor
        if (!isValidBirthDate(birthDate)) {
            throw new IllegalArgumentException("Invalid birth date for Experience candidate.");
        }
    }

    /**
     * Validates the birth year for Experience candidates.
     *
     * @param birthDate The candidate's birth year.
     * @return true if valid, otherwise false.
     */
    public static boolean isValidBirthDate(int birthDate) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int age = currentYear - birthDate;
        return age >= MIN_AGE && age <= MAX_AGE;
    }

    /**
     * Get the number of years of experience.
     *
     * @return Years of experience.
     */
    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    /**
     * Set the number of years of experience.
     *
     * @param yearsOfExperience Years of experience to set.
     */
    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    /**
     * Get the professional skill of the candidate.
     *
     * @return Professional skill.
     */
    public String getProfessionalSkill() {
        return professionalSkill;
    }

    /**
     * Set the professional skill of the candidate.
     *
     * @param professionalSkill Professional skill to set.
     */
    public void setProfessionalSkill(String professionalSkill) {
        this.professionalSkill = professionalSkill;
    }

    /**
     * Method to display detailed information for an experienced candidate. This
     * method overrides the printDetails method from the Candidate class to
     * include specific details for experienced candidates.
     *
     * @return A formatted string containing candidate details.
     */
    @Override
    public String toString() {
        return super.toString() + String.format(" | Experience: %d years | Skill: %s", yearsOfExperience, professionalSkill);
    }
}
