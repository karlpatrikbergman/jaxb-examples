package se.patrikbergman.java.jaxb.bandapp.test;

import org.junit.Test;
import se.patrikbergman.java.jaxb.bandapp.entity.Band;

public class BandEntityTest {

	@Test
	public void test() {
		Band band = new Band();
		band.setId(0);
		band.setName("Accept");
		band.setDescription("Heavy Metal band from the eighties");
		System.out.println(band);
	}

}
