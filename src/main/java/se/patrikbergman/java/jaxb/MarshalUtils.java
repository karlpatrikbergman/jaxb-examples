package se.patrikbergman.java.jaxb;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MarshalUtils {
	private final static Map<String, Schema> schemaMap = Collections.synchronizedMap(new HashMap<>());
	private final static Map<Class<?>, JAXBContext> contextMap = Collections.synchronizedMap(new HashMap<>());
	private final static XMLInputFactory XML_INPUT_FACTORY = XMLInputFactory.newInstance();

	public static String toXmlString(final Object object) throws JAXBException {
		final JAXBContext jaxbContext = getJAXBContext(object.getClass());
		final Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		final StringWriter writer = new StringWriter();
		jaxbMarshaller.marshal(object, writer);
		return writer.toString();
	}

	public static <T> T toJAXBObject(final StreamSource source, Class<T> clazz, final String schemaFile) throws JAXBException {
		final JAXBContext jaxbContext = getJAXBContext(clazz);
		final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		final Schema schema = getSchema(schemaFile, clazz);
		jaxbUnmarshaller.setSchema(schema);
		try {
			final XMLStreamReader xmlStreamReader = XML_INPUT_FACTORY.createXMLStreamReader(source);
			return jaxbUnmarshaller.unmarshal(xmlStreamReader, clazz).getValue();
		} catch (XMLStreamException e) {
			throw new JAXBException("Exception when setting up and reading from the XMLStreamReader", e);
		}
	}

	public static <T> T toJAXBObject(String xml, Class<T> clazz, String schemaFile) throws JAXBException {
		try {
			final InputStream inputStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
			return toJAXBObject(new StreamSource(inputStream), clazz, schemaFile);
		} catch (UnsupportedEncodingException e) {
			throw new JAXBException("Caught unsupported encoding exception!", e);
		}
	}

	private static JAXBContext getJAXBContext(final Class<?> clazz) throws JAXBException {
		if (contextMap.containsKey(clazz)) {
			return contextMap.get(clazz);
		} else {
			final JAXBContext context = JAXBContext.newInstance(clazz);
			contextMap.put(clazz, context);
			return context;
		}
	}

	private static Schema getSchema(String schemaFile, Class<?> clazz) {
		if (schemaMap.containsKey(schemaFile)) {
			return schemaMap.get(schemaFile);
		} else {
			return loadNewSchema(schemaFile, clazz);
		}
	}

	private static Schema loadNewSchema(String schemaFile, Class<?> clazz) {
		final SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		final URL schemaUrl = clazz.getResource(schemaFile);
		if (schemaUrl == null) {
			throw new RuntimeException("Schema file not found: " + schemaFile);
		}
		try {
			final Schema schema = sf.newSchema(schemaUrl);
			schemaMap.put(schemaFile, schema);
			return schema;
		} catch (SAXException e) {
			throw new RuntimeException("Error when setting schemaUrl: " + e.getMessage(), e);
		}
	}
}
