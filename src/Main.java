import db.db.exceptions.EntityNotFoundException;
import db.db.exceptions.InvalidEntityException;
import example.*;
import db.*;

public class Main {
    public static void main(String[] args) throws InvalidEntityException {
        Document doc = new Document("Eid Eid Eid");

        Database.add(doc);

        System.out.println("Document added");

        System.out.println("id: " + doc.id);
        System.out.println("content: " + doc.getContent());
        System.out.println("creation date: " + doc.getCreationDate());
        System.out.println("last modification date: " + doc.getLastModificationDate());
        System.out.println();

        try {
            Thread.sleep(30_000);
        } catch (InterruptedException e) {
            System.out.println("Sleep interrupted!");
        }

        doc.setContent("This is the new content");

        Database.update(doc);

        System.out.println("Document updated");
        System.out.println("id: " + doc.id);
        System.out.println("content: " + doc.getContent());
        System.out.println("creation date: " + doc.getCreationDate());
        System.out.println("last modification date: " + doc.getLastModificationDate());
    }
}
