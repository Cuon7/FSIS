Edit the below file in order to not get 0
...KEngine/Engine.java
Line 191 downward aka method addDoc (dont change the spec)
...KEngine/Query.java
Line 245: Add Iterator like from A2. Keep the name matchIterator cause original CustomerWorkSheet uses this name.
Can change MatchGen to anything else.
...FSIS/Customer.java
Line 26: Change from the original version to implement Document interface.
Also from 247, make compareTo method shorter.
New method: toHtmlDoc
PLEASE DONT CHANGE toHtmlDoc to anything else.
...FSIS/HighEarner
Line 33: Implement Document interface
Add spec for HighEarner and add repOk method since i forgot to put it in A1 (LOL)
Line 139 downward: toHtmlDoc
...FSIS/SortedSet
Line 22: Small change: Remove what after public class SortedSet (dont know why but CustomerWorkSheet run successfully after deleting it )
Line 183: public Iterator<Comparable> iterator() throws EmptyException { (OLD) --->> public Iterator<Comparable> elements() throws EmptyException {
(CustomerWorkSheet uses this)
...FSIS/CustomerWorkSheet
Line 298 downward: DONT CHANGE ANY OF THE SPEC. Can change the name of the variables.
