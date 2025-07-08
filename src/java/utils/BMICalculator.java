/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author Hoa
 */
public class BMICalculator {
    public static double calculate(double weightKg, double heightMeters) {
        if (weightKg <= 0) {
            throw new IllegalArgumentException("Weight must be greater than 0");
        }
        if (heightMeters <= 0) {
            throw new IllegalArgumentException("Height must be greater than 0");
        }
        return weightKg / (heightMeters * heightMeters);
    }

    public static String getBMICategory(double bmi) {
        if (bmi < 16.0) {
            return "Severely underweight";
        } else if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi < 25.0) {
            return "Normal";
        } else if (bmi < 30.0) {
            return "Overweight";
        } else if (bmi < 35.0) {
            return "Obesity Class I";
        } else if (bmi < 40.0) {
            return "Obesity Class II";
        } else {
            return "Obesity Class III";
        }
    }
}