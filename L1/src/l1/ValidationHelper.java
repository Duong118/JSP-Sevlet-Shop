/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package l1;

import java.util.Calendar;
import java.util.List;

/**
 * L01 - Create a Java console program to manage Candidates of company.
 *
 * @author DUONGNCCE180374
 */
public class ValidationHelper {

    private List<Candidate> candidates; // A list to store candidate objects for validation purposes.

    /**
     * Checks if the input string is a valid integer.
     *
     * @param input the input string
     * @return true if the input is a valid integer, otherwise false
     */
    public boolean isValidInteger(String input) {
        if (input == null) {
            return false;
        }
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates if the given phone number follows the Vietnamese format.
     *
     * @param phone the phone number to validate
     * @return true if valid, otherwise false
     */
    public boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^(03|04|05|07|08|09)\\d{8}$");
    }

    /**
     * Validates email format.
     *
     * @param email the email to validate
     * @return true if valid, otherwise false
     */
    public boolean isValidEmail(String email) {
        return email != null && email.matches("^[a-zA-Z0-9]+([._+-]?[a-zA-Z0-9]+)*@(?![.-])[a-zA-Z0-9-]+(?<!-)(?:\\.[a-zA-Z0-9-]+(?<!-))*(\\.[a-zA-Z]{2,6})$");
    }

    /**
     * Checks if an input is non-empty.
     *
     * @param input the input string
     * @return true if non-empty, otherwise false
     */
    public boolean isValidNonEmpty(String input) {
        return input != null && !input.trim().isEmpty();
    }

    /**
     * Validates candidate ID format.
     *
     * @param id the candidate ID
     * @return true if valid, otherwise false
     */
    public boolean isValidId(String id) {
        return id != null && !id.trim().isEmpty() && id.matches("[A-Za-z0-9]+");
    }

    /**
     * Checks if a given candidate ID already exists in the list.
     *
     * @param id the candidate ID
     * @param candidates list of candidates
     * @return true if ID exists, otherwise false
     */
//    public boolean isExisted(String id, List<Candidate> candidates) {
//        return candidates.stream()
//                .anyMatch(candidate -> candidate.getCandidateId().equalsIgnoreCase(id));
//    }
    public boolean isExisted(String id, List<Candidate> candidates) {
        return candidates.stream().anyMatch(candidate -> candidate.getCandidateId().equalsIgnoreCase(id));

    }

    /**
     * Validates name format (only letters and spaces allowed).
     *
     * @param name the name to validate
     * @return true if valid, otherwise false
     */
    public boolean isValidName(String name) {
        return isValidNonEmpty(name) && name.matches("[A-Za-z ]+");
    }

    /**
     * Validates address.
     *
     * @param address the address to validate
     * @return true if valid, otherwise false
     */
    public boolean isValidAddress(String address) {
        return isValidNonEmpty(address) && address.matches("[A-Za-z ]+");
    }

    /**
     * Validates graduation year (must not be before candidate turns 18).
     *
     * @param graduationYear the year of graduation
     * @param birthDate the candidate's birth year
     * @return true if valid, otherwise false
     */
    public boolean isValidGraduationYear(int graduationYear, int birthDate) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int earliestGraduationYear = birthDate + 18;
        return graduationYear <= currentYear && graduationYear >= earliestGraduationYear;
    }


    /**
     * Validates graduation rank (must be one of predefined values).
     *
     * @param rank graduation rank
     * @return true if valid, otherwise false
     */
    public boolean isValidGraduationRank(String rank) {
        return rank != null && (rank.equals("Excellence") || rank.equals("Good") || rank.equals("Fair") || rank.equals("Poor"));
    }

    /**
     * Validates years of experience (must be non-negative and within reasonable
     * range based on birth year).
     *
     * @param expYears years of experience
     * @param birthDate candidate's birth year
     * @return true if valid, otherwise false
     */
//    public static boolean isValidExperienceYears(int expYears, int birthDate) {
//        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
//        int age = currentYear - birthDate;
//        int maxExperience = age - Experience.MIN_AGE;
//
//        return expYears >= Experience.MIN_EXPERIENCE && expYears <= maxExperience;
//    }
    public static boolean isValidExperienceYears(int exYears, int birthDate) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int age = currentYear - birthDate;
        int maxExperience = age - Experience.MIN_AGE;

        return exYears >= Experience.MIN_EXPERIENCE && exYears <= maxExperience;
    }

    /**
     * Validates major (for Intern candidates).
     *
     * @param major the major to validate
     * @return true if valid, otherwise false
     */
    public boolean isValidMajor(String major) {
        return isValidNonEmpty(major) && major.matches("[A-Za-z ]+");
    }

    /**
     * Checks if a candidate ID already exists in the internal list and provides
     * a warning.
     *
     * @param id the candidate ID
     * @return true if ID exists, otherwise false
     */
//    private boolean isExisted(String id) {
//        for (Candidate candidate : candidates) {
//            if (candidate.getCandidateId().equalsIgnoreCase(id)) {
//                System.out.println("Canidate ID already exists. Please enter a different ID");
//                return true;
//            }
//        }
//        return false;
//    }
    private boolean isExisted(String id) {
        for (Candidate candidate : candidates) {
            if (candidate.getCandidateId().equalsIgnoreCase(id)) {
                System.out.println("Candidate ID already exists. Please enter a different ID");
                return true;
            }
        }
        return false;
    }

}
