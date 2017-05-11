package by.bsu.main.project.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.junit.Test;

public class StemmerPorterImplTest {

	@Test
	public void test() {
		try {
			InputStream inputStream = new FileInputStream(new File("example.txt"));
			Scanner sc = new Scanner(inputStream);
			while (sc.hasNextLine()) {
				StringTokenizer strTok = new StringTokenizer(sc.nextLine());
				while (strTok.hasMoreTokens()) {
					String s = strTok.nextToken();
					StemmerResult stemmerResult = new StemmerResult();
					stemmerResult.setLancaster(s);
					stemmerResult.setPorter(s);
					stemmerResult.setSnowball(s);
					if (stemmerResult.check()) {
						System.out.println("Source: " + s + " " + stemmerResult);
					}
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
