package com.protege.fizzbuzz;

import java.util.Random;

public class FizzBuzzHelper {
	public static final int FIZZ = 0;
	public static final int BUZZ = 1;
	public static final int FIZZBUZZ = 2;
	public static final int NO_MATCH = 3;
	public static final int MAX_FIZZBUZZ_NUMBER = 1000;
	public static final int DIVISIBLE_BY_3 = 3;
	public static final int DIVISIBLE_BY_5 = 5;

	public static int calculateFizzBuzz(int i)
	{
		//Checks should always go from most specific to least specific
		if(i % DIVISIBLE_BY_5 == 0 && i % DIVISIBLE_BY_3 == 0){
			return FIZZBUZZ;
		}else if(i %  DIVISIBLE_BY_5 == 0){
			return BUZZ;
		}else if(i % DIVISIBLE_BY_3 == 0){
			return FIZZ;
		}else{
			return NO_MATCH;
		}
	}
}
