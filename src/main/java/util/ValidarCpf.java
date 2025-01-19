package util;

public class ValidarCpf {
    public static boolean validarCpf(String CPF) {
		if (CPF == null || CPF.length() != 11 || CPF.matches("(\\d)\\1{10}")) {
			return false;
		}

		try {
			int sum = 0;
			for (int i = 0; i < 9; i++) {
				sum += Character.getNumericValue(CPF.charAt(i)) * (10 - i);
			}

			int firstDigit = 11 - (sum % 11);
			if (firstDigit >= 10) {
				firstDigit = 0;
			}

			sum = 0;
			for (int i = 0; i < 10; i++) {
				sum += Character.getNumericValue(CPF.charAt(i)) * (11 - i);
			}

			int secondDigit = 11 - (sum % 11);
			if (secondDigit >= 10) {
				secondDigit = 0;
			}

			return firstDigit == Character.getNumericValue(CPF.charAt(9)) &&
					secondDigit == Character.getNumericValue(CPF.charAt(10));
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
