package Singleton;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Singleton chargé de la gestion des données de synchronisation des fichiers.
 * Le registre a la responsabilité de maintenir un suivi des dates de
 * synchronisation pour chaque fichier dans le système. Il est essentiel pour la
 * détection de modifications de fichier, la gestion de conflits éventuels.
 * Il garantit que chaque fichier est synchronisé au bon moment en effectuant
 * une comparaison de sa date de synchronisation avec celle du répertoire cible.
 */
public class Registry {
    // ATTRIBUTS

    private static Registry instance;
    private Map<String, Date> data;
    private final String registryFile = "registry.xml";

    // CONSTRUCTEUR

    /**
     * Constructeur de la classe Registry
     */
    private Registry() {
        data = new HashMap<>();
        try {
            loadFromFile();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // REQUÊTES

    /**
     * Récupère l'instance unique du registre
     * @return instance du registre {@code Registry}
     */
    public static Registry getInstance() {
        if (instance == null) {
            instance = new Registry();
        }
        return instance;
    }

    /**
     * Récupère la date à laquelle le fichier donné a été synchronisé pour la
     * dernière fois. Si la date n'est pas connue, parce qu'il n'a pas été
     * encore synchronisé, {@code null} est retourné
     * @param path chemin relatif du fichier donné
     * @return date de la dernière synchronisation, {@code null} sinon
     */
    public Date getLastSyncDate(String path) {
        return data.get(path);
    }

    // COMMANDES

    /**
     * Met à jour la date de synchronisation d'un fichier donné
     * @param path chemin relatif du fichier donné
     * @param date nouvelle date de synchronisation
     */
    public void updateDate(String path, Date date) {
        data.put(path, date);
    }

    /**
     * Supprime un fichier du registre
     * @param path chemin du fichier à supprimer
     */
    public void removePath(String path) {
        data.remove(path);
    }

    /**
     * Sérialise toutes les données du registre dans un fichier local
     * @throws IOException exception levée en cas d'erreur d'écriture
     */
    public void saveToFile() throws IOException {
        try {
            DocumentBuilderFactory BuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = BuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element element = doc.createElement("registry");
            doc.appendChild(element);
            for (Map.Entry<String, Date> entry : data.entrySet()) {
                Element fileElement = doc.createElement("file");
                Element pathElement = doc.createElement("path");
                pathElement.appendChild(doc.createTextNode(entry.getKey()));
                Element dateElement = doc.createElement("date");
                dateElement.appendChild(doc.createTextNode(String.valueOf(entry.getValue().getTime())));
                fileElement.appendChild(pathElement);
                fileElement.appendChild(dateElement);
                element.appendChild(fileElement);
            }
            try {
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                DOMSource domSource = new DOMSource(doc);
                StreamResult streamResult = new StreamResult(new File(registryFile));
                transformer.transform(domSource, streamResult);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } catch (Exception e) {
            throw new IOException("cannot save xml file", e);
        }
    }

    /**
     * /!!\ Doc à définir /!!\
     */
    public void printAllData() {
        if (data.isEmpty()) {
            System.out.println("Le registre est vide.");
        } else {
            System.out.println("Contenu du registre de synchronisation :");
            for (Map.Entry<String, Date> entry : data.entrySet()) {
                System.out.println("- " + entry.getKey() + " : " + entry.getValue());
            }
        }
    }


    // OUTILS

    /**
     * Charge les données de synchronisation depuis un fichier local.
     * Si le fichier n'existe pas, aucune donnée ne sera chargée
     * @throws IOException exception levée en cas d'erreur d'écriture
     * @throws ClassNotFoundException exception levée en cas de fichier
     * non trouvé
     */
    private void loadFromFile() throws IOException, ClassNotFoundException {
        File file = new File(registryFile);
        if (!file.exists()) {
            return;
        }

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(file);
            NodeList fileNodes = doc.getElementsByTagName("file");
            for (int i = 0; i < fileNodes.getLength(); i++) {
                Node node = fileNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String path = element.getElementsByTagName("path").item(0).getTextContent();
                    String date = element.getElementsByTagName("date").item(0).getTextContent();
                    long timestamp = Long.parseLong(date);
                    data.put(path, new Date(timestamp));
                }
            }
        } catch (Exception e) {
            throw new IOException("cannot load xml file", e);
        }
    }
}
