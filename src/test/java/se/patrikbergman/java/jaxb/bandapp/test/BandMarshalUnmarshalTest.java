package se.patrikbergman.java.jaxb.bandapp.test;

import org.junit.Test;
import se.patrikbergman.java.jaxb.MarshalUtils;
import se.patrikbergman.java.jaxb.schemagenerated.common.Band;
import se.patrikbergman.java.utility.resource.ResourceInputStream;
import se.patrikbergman.java.utility.resource.ResourceString;

import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class BandMarshalUnmarshalTest {

	//File XMLfile = new File("Band.xml");
	//jaxbMarshaller.marshal(band, XMLfile);
	@Test
	public void javaObjectToXmlString() throws JAXBException {
		final String bandXmlString = MarshalUtils.toXmlString(createBand());
		assertNotNull(bandXmlString);
		System.out.println(bandXmlString);
	}

	@Test
	public void xmlFileToJavaObject() throws JAXBException, IOException {
		final Band band = MarshalUtils.toJAXBObject(
				new StreamSource(new ResourceInputStream("Band.xml")),
				Band.class,
				"/Rootelements.xsd");
		assertNotNull(band);
		System.out.println(band);
	}

	@Test
	public void xmlStringToJavaObject() throws JAXBException, IOException {
		final ResourceString bandResourceString = new ResourceString("Band.xml");
		final Band band = MarshalUtils.toJAXBObject(
				bandResourceString.toString(),
				Band.class,
				"/Rootelements.xsd");
		assertNotNull(band);
		System.out.println(band);
	}

	private Band createBand() {
		Band band = new Band();
		band.setName("Accept");
		band.setDescription("Accept is a German heavy metal band originally assembled by former vocalist Udo Dirkschneider, guitarist Wolf Hoffmann and bassist Peter Baltes.");
		band.setRockFactor(10);
		return band;
	}
}
