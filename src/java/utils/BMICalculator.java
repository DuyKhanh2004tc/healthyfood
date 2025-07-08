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
    if (bmi < 18.5) {
        return "Underweight";
    } else if (bmi < 25.0) {
        return "Normal";
    } else if (bmi < 30.0) {
        return "Overweight";
    } else {
        return "Obese";
    }
}

public static String getBMISlugTag(double bmi) {
    if (bmi < 18.5) {
        return "gain-weight";
    } else if (bmi < 25.0) {
        return "maintain-weight";
    } else {
        return "weight-loss";
    }
}
}