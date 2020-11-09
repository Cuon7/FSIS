package a3_1801040028.fsis;

import static utils.TextIO.getln;
import static utils.TextIO.putf;
import static utils.TextIO.putln;

import java.util.Arrays;
import java.util.Iterator;

import a3_1801040028.kengine.Doc;
import a3_1801040028.kengine.DocCnt;
import a3_1801040028.kengine.Engine;
import a3_1801040028.kengine.Query;
import utils.DomainConstraint;
import utils.EmptyException;
import utils.NotPossibleException;
import utils.TextIO;

/**
 * @overview Represents a worksheet program that enables a user to create
 *           customer objects, display a report about them, and to search for
 *           objects of interest using keywords.
 * 
 * @attributes objects SortedSet<Customer> engine Engine
 * 
 * @abstract_properties mutable(objects)=true /\ optional(objects)=false /\
 *                      mutable(engine)=false /\ optional(engine)=false /\
 *                      size(objects) > 0 -> (for all o in objects:
 *                      o.toHtmlDoc() is in engine)
 * @author dmle
 */
public class CustomerWorkSheet {
  @DomainConstraint(type = "Collection", mutable = true, optional = false)
  private SortedSet objects;
  @DomainConstraint(type = "Engine", mutable = false, optional = false)
  private Engine engine;

  /**
   * @effects initialise a CustomerWorkSheet ask the users to create Customer
   *          objects and display a report ask the users to perform a keyword
   *          search and display a report
   */
  public static void main(String[] args) {
    // initialise a CustomerWorkSheet
    putln("Initialising program...");
    CustomerWorkSheet worksheet = new CustomerWorkSheet();

    try {
      // create some customer objects
      worksheet.manageCustomers();

      if (!worksheet.isEmpty()) {
        // do search
        worksheet.manageSearching();
      }

      // end
      putln("\nGood bye.");
    } catch (NotPossibleException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * @effects Invoke promptForCustomer, followed by createCustomer or
   *          createHighEarner create some Customer objects. Add these objects to
   *          this. Invoke displayReport to display a report about them.
   */
  private void manageCustomers() {
    putln("\n* Customer Manager *\n");
    while (true) {
      // prompt for a customer
      String[] data = promptForCustomer();

      if (data != null) {
        // add to this
        try {
          Customer c;
          if (data.length == 4) {
            c = createCustomer(data);
          } else {
            c = createHighEarner(data);
          }

          addCustomer(c);
          // print message
          putln("Added: " + c);
        } catch (NotPossibleException e) {
          e.printStackTrace();
        }
      } else {
        break;
      }
    }

    // display a report about the objects
    displayReport();
  }

  /**
   * @effects prompt the user whether to enter details for Customer or HighEarner,
   *          then prompt the user for the data values needed to create an object
   *          for the selected type.
   * 
   *          Return the data values as String[].
   */
  private String[] promptForCustomer() {
    final String EOI = "X!";
    putln("Enter customer details (type \"" + EOI + "\" followed by ENTER to cancel at anytime)");
    putln("Choose type: [C]ustomer/[H]ighEarner? ");
    String s = getln();

    if (s.equalsIgnoreCase(EOI))
      return null;

    boolean typeHigh = false;
    if (s.equalsIgnoreCase("H"))
      typeHigh = true;
    // prompt for customer details

    String id;
    String name, phone, address;

    putln("Enter customer id: ");
    id = getln();

    if (id.equalsIgnoreCase(EOI))
      return null;

    putln("Enter customer name: ");
    name = getln();

    if (name.equalsIgnoreCase(EOI))
      return null;

    putln("Enter customer phone number: ");
    phone = getln();

    if (phone.equalsIgnoreCase(EOI))
      return null;

    putln("Enter customer address: ");
    address = getln();

    if (address.equalsIgnoreCase(EOI))
      return null;

    if (typeHigh) {
      // prompt for income to create HighEarner
      putln("Enter income: ");
      String income = getln();

      if (income.equalsIgnoreCase(EOI))
        return null;

      return new String[] { id, name, phone, address, income };
    } else {
      return new String[] { id, name, phone, address };
    }
  }

  /**
   * @modifies System.out
   * @effects if this is empty prints "No customers" else prints each object in
   *          this.objects one per line to the standard output
   */
  private void displayReport() {
    if (isEmpty()) {
      putln("No customers");
    } else {
      putln("\nDisplaying customer report (sorted by name)...");

      String textReport = toString();
      putln(textReport);
    }
  }

  /**
   * @modifies System.out
   * @effects invoke promptForKeywords() to prompt the user to enter one or more
   *          keywords if keywords != null invoke operation search(String[]) to
   *          search using keywords,
   * 
   *          if fails to execute the query throws NotPossibleException else
   *          invoke displaySearchReport to display search result to the standard
   *          output. else print "no search keywords"
   */
  private void manageSearching() {
    putln("\n* Search Manager *\n");

    // prompt the user to enter one or more keywords (separated by spaces)
    String[] keywords = promptForKeywords();

    if (keywords == null) {
      putln("no search keywords");
    } else {
      putln("\nSearching for customers using keywords: " + Arrays.toString(keywords));

      // invoke operation search(String[]) to search using these keywords
      try {
        Query q = search(keywords);

        // print the query string to the standard output
        displaySearchReport(q);
      } catch (NotPossibleException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * @effects prompt the user to enter some keywords and return an array
   *          containing these or null if no keywords were entered
   */
  private String[] promptForKeywords() {
    putln("Enter some keywords (separated by space): ");
    String s = getln();
    s.trim();
    if (s.length() == 0) {
      return null;
    } else {
      return s.split(" ");
    }
  }

  /**
   * @requires q neq null
   * @effects if q has no matches print "(empty)" else print q's matches (one per
   *          line) to the standard output
   */
  private void displaySearchReport(Query q) {
    // get matches
    Iterator matches = q.matchIterator();

    // write header
    putln("Query: " + Arrays.toString(q.keys()));
    putln("Matches:");
    if (matches == null) {
      putln("(empty)");
    } else {
      int col1Len = 0;
      while (matches.hasNext()) {
        DocCnt m = (DocCnt) matches.next();
        String t = m.getDoc().title();
        if (t.length() > col1Len)
          col1Len = t.length();
      }

      int col2Len = "Sum freqs".length();
      StringBuffer hr = new StringBuffer();
      for (int i = 0; i < col1Len + col2Len + 3; i++)
        hr.append("-");
      String rowFormat = "%-" + col1Len + "s | %s%n";
      putln(hr);
      putf(rowFormat, "Documents", "Sum freqs");
      putln(hr);
      matches = q.matchIterator();

      DocCnt m;
      while (matches.hasNext()) {
        m = (DocCnt) matches.next();
        putf(rowFormat, m.getDoc().title(), m.getCount());
      }

      putln(hr);
    }
  }

  /**
   * @effects if isEmpty() then return null else return Iterator(Comparable) of
   *          the elements in objects
   */
  public Iterator<Comparable> objectIterator() {
    if (isEmpty())
      return null;
    else
      return objects.elements();
  }

  /**
   * @effects if isEmpty() return null else return Iterator(Doc) of documents
   *          stored in engine
   */
  public Iterator<Doc> docIterator() {
    if (isEmpty())
      return null;
    else
      return engine.docIterator();
  }

  /**************************************************************
   * ABOVE THIS LINE ARE GIVEN CODE. STUDENTS SHOULD READ THE SPECS BUT MUST NOT
   * CHANGE ANYTHING! ------------------------------------------------------------
   * BELOW THIS LINE ARE CODE THAT MUST BE COMPLETED BY STUDENTS.
   **************************************************************/

  /**
   * @effects initialize this to include an empty set of objects and an empty
   *          engine
   */
  public CustomerWorkSheet() {
    objects = new SortedSet();
    engine = new Engine();
  }

  /**
   * <pre>
  
   * &#64;effects
   *       if objects is empty
   *         return "empty"
   *       else
   *         return a string containing each object in this.objects one per line
   * </pre>
   * 
   * @return
   */
  @Override
  public String toString() {
    if (isEmpty())
      throw new EmptyException("Empty object!");
    else {
      String S = " ";
      Iterator<Comparable> iterator = objects.elements();
      while (iterator.hasNext()) {
        S = S + iterator.next().toString() + "\n";
      }
      return S;
    }
  }

   /**
   * @effects 
   *  if objects is empty 
   *    return true
   *  else
   *    return false
   */ 
  public boolean isEmpty() {
    return (objects == null || objects.size() == 0);
  }

   /**
   * @requires 
   *  data neq null /\ data.length = 4 /\ 
   *  data contain id, name, phone, and address values (in that order)
   * @effects 
   *  if data contain valid values for Customer
   *    create and return a Customer object from the data
   *  else
   *    throws NotPossibleException 
   */
  public Customer createCustomer(String[] data) throws NotPossibleException {
    boolean validateID = false;
    try {
      int i = Integer.parseInt(data[0]);
      validateID = true;
    } catch (Exception e) {
      throw new NotPossibleException("Invalid input when initializing new Customer");
    }
    Customer customer;
    if (data != null && data.length == 4 && validateID) {
      String i = data[0];
      int id = Integer.valueOf(i);
      String name = data[1];
      String phone = data[2];
      String address = data[3];
      customer = new Customer(id, name, phone, address);
      customer.repOK();
    } else {
      throw new NotPossibleException("CustomerWorkSheet.createCustomer: Invalid values");
    }
    return customer;
  }

   /**
   * @requires 
   *  data neq null /\ data.length = 5 /\ 
   *  data contain id, name, phone, address, and income values (in that order)
   * @effects 
   *  if data contain valid values for HighEarner
   *    create and return a HighEarner object from the data
   *  else
   *    throws NotPossibleException 
   */
  public HighEarner createHighEarner(String[] data) throws NotPossibleException  {
    boolean validateID = false;
    try {
      int i = Integer.parseInt(data[0]);
      validateID = true;
    } catch (Exception e) {
      throw new NotPossibleException("Invalid input");
    }
    HighEarner highEarner;
    if (data != null && data.length == 5 && validateID) {
      String i = data[0];
      int id = Integer.valueOf(i);
      String name = data[1];
      String phone = data[2];
      String address = data[3];
      float income = Float.parseFloat(data[4]);
      highEarner = new HighEarner(id, name, phone, address, income);
      highEarner.repOK();
    } else {
      throw new NotPossibleException("CustomerWorkSheet.createHighEarner: Invalid values");
    }
    return highEarner;
  }

   /**
   * @effects 
   *   add c to this.objects and 
   *   add to this.engine a Doc object created from c.toHtmlDoc
   */
  public void addCustomer(Customer c) {
    this.objects.insert(c);
    String s = c.toHtmlDoc();
    Doc d = new Doc(s);
    this.engine.addDoc(d);
    TextIO.putln("Customer " + c.getName() + " added!");
  }

   /**
   * @requires words != null /\ words.length > 0
   * @effects 
   *   search for objects whose HTML documents match with the query containing words
   *   and return a Query object containing the result
   *   
   *   If fails to execute query using words
   *     throws NotPossibleException
   */
  public Query search(String[] words) throws NotPossibleException {
    if (words == null || words.length <= 0) {
      throw new NotPossibleException("Invalid keyword.");
    } else {
      System.out.println("\nSearching for objects using keywords: " + Arrays.toString(words));
      if (words.length == 1) {
        return engine.queryFirst(words[0]);
      } else {
        engine.queryFirst(words[0]);
        for (int i = 1; i < words.length - 1; i++) {
          engine.queryMore(words[i]);
        }
        return engine.queryMore(words[words.length - 1]);
      }
    }
  }
}
