package se.patrikbergman.java.jaxb.bandapp.test;

import org.junit.Before;
import org.junit.Test;
import se.patrikbergman.java.jaxb.bandapp.entity.Band;
import se.patrikbergman.java.utility.resource.ResourceInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class BandEntityTest {

	private JAXBContext jaxbContext;

	@Before
	public void setup() throws JAXBException {
		jaxbContext = JAXBContext.newInstance(Band.class);
	}

	//File XMLfile = new File("Band.xml");
	//jaxbMarshaller.marshal(band, XMLfile);
	@Test
	public void marshal() throws JAXBException {
		final Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		jaxbMarshaller.marshal(createBand(), System.out);
	}

	@Test
	public void unmarshal() throws JAXBException, IOException {
		final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		final Band band = (Band) jaxbUnmarshaller.unmarshal(
				new ResourceInputStream("Band.xml")
		);
		assertNotNull(band);
		System.out.println(band);
	}

	private Band createBand() {
		Band band = new Band();
		band.setId(0);
		band.setName("Accept");
		band.setDescription("Heavy Metal band from the eighties");
		return band;
	}
}
