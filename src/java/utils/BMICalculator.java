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

    public static double calculate(double weightKg, double heightCm) {
        if (weightKg < 30 || weightKg > 300) {
            throw new IllegalArgumentException("Weight must be between 30 kg and 300 kg");
        }
        if (heightCm < 100 || heightCm > 250) {
            throw new IllegalArgumentException("Height must be between 100 cm and 250 cm");
        }
        return 100 * 100 * weightKg / (heightCm * heightCm);
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
