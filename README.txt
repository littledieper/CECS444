CECS 444 Parser - Project 3

Author: Alex Diep (alexdiep96@gmail.com)
All files written by me in IntelliJ. No external dependencies used.

Relevant files in \src\:
\runner\sct\SCTBuilder.java - Builds the scope tree from a given AST node.
\runner\sct\SCTNode.java - Represents a scope tree node.
\runner\TableEntry.java - Represents a entry in a symbol table.
\runner\ASTRunner.java - File that handles the main logic for running the AST
\runner\Main.java - Main file that gets called to run Lexer, Parser, and to run the AST.
\runner\Operations - Handles mathematical operations and various helper testing functions.

Summary:
All keywords of the A6 Grammar have been tested with small snippets of code alongside modified version of the
sample code input given in Project 1. These had to be slightly modified as the A6 grammar does not support things like
'input', so all input statements were removed with assignment statements.

To read the tree output, parents are 1 "tab" behind, and children are one "forward". Siblings are inline with each other.
The program will print the PST, AST, SCT, and the output of the actual program with whitespace in between. Large
programs with large scope trees may overflow your command prompt.

The parser is fed tokens from my own lexer/scanner built in Project 1. No external resources were used,
unless you count class notes as external.

Bugs:
1) Empty or non-existent program.txt files just output "The tokenizer really failed."
2) When looking up a non-terminal or terminal symbol that doesn't exist in the grammar, it'll result in a null-pointer
    exception. But really, why are you looking up symbols that don't exist in the grammar?
3) Incorrect syntax code will return an M2/M3 error as expected.



To run easily...
1) Open the "program.txt" file and input code you want to parse. Save. You may test with the sample code in modified.txt.
2) Open a command prompt or terminal and cd to this directory.
3) Enter in "java -jar CECS444.jar" and hit enter.

To run manually...
1) Create a text file called "program.txt" and place it in the root directory of the project.
2) Place the code you want to test in "program.txt".
3) Import project (folder CECS444Compiler) into IntelliJ/Eclipse using the default settings.  (IntelliJ ONLY: Overwrite any files in any prompts on project import).
4) Navigate to the Parser.java file in /src/parser in the project explorer.
5a) To run in IntelliJ, right click on Main.java and click "Run Main.main". To run in Eclipse, right click on Main.java and Run As->Java Application.
