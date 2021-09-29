    /**
     * Given XML with 8 deep or more, JAXB indentation system is not working anymore.
     * {@link MarshallerImpl#createWriter(java.io.OutputStream, java.lang.String)}
     * {@link com.sun.xml.bind.v2.runtime.output.IndentingUTF8XmlOutput}
     * [...]
     * for( int i=0; i<8; i++ ) System.arraycopy(e.buf, 0, indent8.buf, unitLen*i, unitLen);
     * [...]
     * We have to use specific Transformer to fix it
     **/
    public String fromPojoToString(T pojo) throws JAXBException {
        OutputStream os = new ByteArrayOutputStream();
        marshaller.marshal(pojo, os);
        // Manual transform from https://stackoverflow.com/a/53217583/10691359
        try {
            // From String to Document
            Document document = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new InputSource(new ByteArrayInputStream(os.toString().getBytes(StandardCharsets.UTF_8))));
            // Whitespace removal
            document.normalize();
            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xPath.evaluate("//text()[normalize-space()='']", document, XPathConstants.NODESET);
            for (int i = 0; i < nodeList.getLength(); ++i) {
                Node node = nodeList.item(i);
                node.getParentNode().removeChild(node);
            }
            // Configuration
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", 4);
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); // security
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, ""); // security
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            // Results
            StringWriter stringWriter = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
