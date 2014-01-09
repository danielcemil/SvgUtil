package br.com.cemil.svgutil;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Automação Cemil
 */
public class SvgUtil {
    
    private Document documento;
    private XPath xpath;
    
    public SvgUtil( File arquivo ) {
        documento = criarDocumento( arquivo );
        xpath = criarXPath();
    }
    
    private Document criarDocumento( File arquivo ) {
        
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            return docBuilder.parse( arquivo );
        }
        catch( ParserConfigurationException | SAXException | IOException ex ) {
            return null;
        }
        
    }
    
    private XPath criarXPath() {
        return XPathFactory.newInstance().newXPath();
    }
    
    private Node procurarTag( String nome ) throws XPathExpressionException {
        NodeList lista = (NodeList) xpath.evaluate( "//*[@id='"+ nome +"']", documento, XPathConstants.NODESET );
        return lista.item( 0 );
    }
    
    private void atualizarValores( Map<String, String> valores ) throws XPathExpressionException {
        
        for( String tag : valores.keySet() ) {
            Node no = procurarTag( tag );
            no.setTextContent( valores.get( tag ) );
        }
    }
    
    private void serializarDocumento( Writer saida, OutputFormat formato ) throws IOException {
        XMLSerializer serializer = new XMLSerializer( saida, formato );
        serializer.serialize( documento );
    }
    
    private Writer obterWriter() throws IOException {
        OutputFormat formato = new OutputFormat( documento );
        Writer saida = new StringWriter();
        
        serializarDocumento( saida, formato );
        
        return saida;
    }
    
    public String obterSvgAtualizado( Map<String, String> valores ) throws XPathExpressionException, IOException {
        atualizarValores( valores );
        Writer saida = obterWriter();
        
        return saida.toString();
    }
    
}