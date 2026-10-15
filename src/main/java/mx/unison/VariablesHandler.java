package mx.unison;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import java.util.ArrayList;
import java.util.List;

public class VariablesHandler extends DefaultHandler {

    // 1. Añadimos las variables de estado
    private String var1;
    private String var2;
    private boolean inVar1 = false;
    private boolean inVar2 = false;

    // 2. Uso del Java Collections API
    private List<Double> listaVar1;
    private List<Double> listaVar2;
    private StringBuilder currentValue;

    public VariablesHandler(String var1, String var2) {
        super();
        this.var1 = var1;
        this.var2 = var2;
        this.listaVar1 = new ArrayList<>();
        this.listaVar2 = new ArrayList<>();
        this.currentValue = new StringBuilder();
    }

    public List<Double> getListaVar1() { return listaVar1; }
    public List<Double> getListaVar2() { return listaVar2; }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        currentValue.setLength(0);

        if (qName.equals(var1)) {
            inVar1 = true;
        } else if (var2 != null && qName.equals(var2)) {
            inVar2 = true;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);

        if (inVar1 || inVar2) {
            currentValue.append(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        if (inVar1 && qName.equals(var1)) {
            try {
                listaVar1.add(Double.parseDouble(currentValue.toString().trim()));
            } catch (NumberFormatException e) {
                throw new SAXException("Error de tipo: La variable '" + var1 + "' no es numérica.");
            }
            inVar1 = false;
        }
        else if (inVar2 && qName.equals(var2)) {
            try {
                listaVar2.add(Double.parseDouble(currentValue.toString().trim()));
            } catch (NumberFormatException e) {
                throw new SAXException("Error de tipo: La variable '" + var2 + "' no es numérica.");
            }
            inVar2 = false;
        }
    }

    @Override
    public void warning(SAXParseException e) throws SAXException {
        super.warning(e);
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
        super.error(e);
    }

    @Override
    public void fatalError(SAXParseException e) throws SAXException {
        super.fatalError(e);
    }
}