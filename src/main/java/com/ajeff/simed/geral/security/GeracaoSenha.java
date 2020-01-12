package com.ajeff.simed.geral.security;

import java.util.Scanner;

public class GeracaoSenha {
	
	public static void main(String[] args) {
		
//		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//		System.out.println(encoder.encode("1111"));

		int n = 0;
		while(n != 42) {
			Scanner sc = new Scanner(System.in);
			n = sc.nextInt();
			System.out.println(n);
			sc.close();
		}
		
	
	}
	
}	
