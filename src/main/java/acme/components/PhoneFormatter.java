/*
 * PhoneFormatter.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.components;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.expression.ParseException;
import org.springframework.format.Formatter;

import acme.client.helpers.MessageHelper;
import acme.client.helpers.StringHelper;
import acme.datatypes.Phone;

public class PhoneFormatter implements Formatter<Phone> {

	// Formatter interface ----------------------------------------------------

	@Override
	public String print(final Phone object, final Locale locale) {
		assert object != null;
		assert locale != null;

		String result;
		String countryCodeText, areaCodeText, numberText;
		String areaCodeFormat;

		countryCodeText = String.format("%d", object.getCountryCode());
		areaCodeFormat = locale.getCountry().equals("es") ? " %s " : " (%s) ";
		areaCodeText = object.getAreaCode() == null ? " " : //
			String.format(areaCodeFormat, object.getAreaCode());
		numberText = String.format("%s", object.getNumber());

		result = String.format("+%s%s%s", countryCodeText, areaCodeText, numberText);

		return result;
	}

	@Override
	public Phone parse(final String text, final Locale locale) throws ParseException {
		assert !StringHelper.isBlank(text);
		assert locale != null;

		Phone result;
		String countryCodeRegex, areaCodeRegex, numberRegex, phoneRegex;
		Pattern pattern;
		Matcher matcher;
		String errorMessage;
		String countryCodeText, areaCodeText;
		int countryCode, areaCode;
		String phoneNumber;

		countryCodeRegex = "\\+\\d{1,3}";
		areaCodeRegex = "\\d{1,5}";
		numberRegex = "\\d{1,9}([\\s-]\\d{1,9}){0,5}";
		phoneRegex = String.format( //
			"^\\s*(?<CC>%1$s)(\\s+(\\((?<AC1>%2$s)\\)|(?<AC2>%2$s))\\s+|\\s+)(?<N>%3$s)\\s*$", //
			countryCodeRegex, //
			areaCodeRegex, //
			numberRegex //
		);

		pattern = Pattern.compile(phoneRegex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
		matcher = pattern.matcher(text);

		if (!matcher.find()) {
			errorMessage = MessageHelper.getMessage("acme.validation.phone.message", locale);
			throw new ParseException(0, errorMessage);
		}
		countryCodeText = matcher.group("CC");
		countryCode = Integer.parseInt(countryCodeText);
		areaCodeText = matcher.group("AC1") != null ? matcher.group("AC1") : matcher.group("AC2");
		areaCode = Integer.parseInt(areaCodeText);
		phoneNumber = matcher.group("N");

		result = new Phone();
		result.setCountryCode(countryCode);
		result.setAreaCode(areaCode);
		result.setNumber(phoneNumber);

		return result;
	}

}
