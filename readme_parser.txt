CECS 444 Parser - Project 2

Author: Alex Diep (alexdiep96@gmail.com)
All files written by me in IntelliJ.

Relevant files in \src\:
\lexer\BufferedReader.java
\lexer\Grammar.java
\lexer\Lexer.java
\lexer\Token.java
\lexer\Tokenizer.java
\parser\Parser.java
\parser\ParseTable.java
\parser\Rule.java
\parser\RuleList.java
\parser\pst\ASTCovnerter.java
\parser\pst\Node.java

Summary:
All keywords of the A6 Grammar have been tested with small snippets of code alongside modified version of the
sample code input given in Project 1. These had to be slightly modified as the A6 grammar does not support things like
'input', so all input statements were removed with assignment statements.

Parents are 1 "tab" behind, and children are one "forward". Siblings are inline with each other.

The parser is fed tokens from my own lexer/scanner built in Project 1. No external resources were used,
unless you count class notes as external.

Bugs:
1) Empty or non-existent program.txt files just output "The tokenizer really failed."
2) When looking up a non-terminal or terminal symbol that doesn't exist in the grammar, it'll result in a null-pointer
    exception. But really, why are you looking up symbols that don't exist in the grammar?

To run easily...
1) Open the "program.txt" file and input code you want to parse. Save.
2) Open a command prompt or terminal and cd to this directory.
3) Enter in "java -jar CECS444.jar" and hit enter.

To run manually...
1) Create a text file called "program.txt" and place it in the root directory of the project.
2) Place the code you want to test in "program.txt".
3) Import project (folder CECS444Compiler) into IntelliJ/Eclipse using the default settings.  (IntelliJ ONLY: Overwrite any files in any prompts on project import).
4) Navigate to the Parser.java file in /src/parser in the project explorer.
5a) To run in IntelliJ, right click on Lexer.java and click "Run Lexer.main". To run in Eclipse, right click on Lexer.java and Run As->Java Application.