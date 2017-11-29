CECS 444 Lexer -- Project 1

Author: Alex Diep (alexdiep96@gmail.com)
All files written by me in IntelliJ.

Summary:
All keywords of the A2 Lexcon are supported and tested with the given sample code.
The tokenizer will read from a text file with the given code.
No external dependencies used.

Bugs:
If the tokenizer does not have a file to read from (labeled "program.txt") in the same directory, it will
throw a FileNotFound exception. It will also only read from 1 file.

Files submitted:
All source code can be found in the /CECS444Compiler/src/lexer directory and can be imported into IntelliJ or Eclipse.

Token.java -- represents a Token produced by the lexer.
Grammar.java -- represents an ID/Keyword pair in the A2 Lexcon.
Tokenizer.java -- contains the main logic of reading from the file and producing tokens.
Lexer.java -- Simple main file to execute.
BufferedReader.java -- an extension of the java.io.BufferedReader that implements a peek() function.

To run manually:
1) Create a text file called "program.txt" and place it in the root directory of the project.
2) Place the code you want to test in "program.txt".
3) Import project (folder CECS444Compiler) into IntelliJ/Eclipse using the default settings.  (IntelliJ ONLY: Overwrite any files in any prompts on project import).
4) Navigate to the Lexer.java file in /src/lexer in the project explorer.
5a) To run in IntelliJ, right click on Lexer.java and click "Run Lexer.main". To run in Eclipse, right click on Lexer.java and Run As->Java Application.