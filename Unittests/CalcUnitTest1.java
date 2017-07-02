package de.dhbw.app.mathinator;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

import de.dhbw.app.mathinator.calculator.CalculatorBaseVisitorImpl;
import de.dhbw.app.mathinator.calculator.CalculatorLexer;
import de.dhbw.app.mathinator.calculator.CalculatorParser;

import static org.junit.Assert.*;

/**
 * Created by TSDHBW on 18.06.2017.
 */
public class CalcUnitTest1 {
    @Test
    public void checkResult() throws Exception {

        /* Testgleichung um Vorrangregel zu überprüfen*/
        String equationInput = "(12-2)*4";


        ANTLRInputStream input = null;
        input = new ANTLRInputStream((equationInput.toString()));
        Double expected = 40.0;
        CalculatorLexer lexer = new CalculatorLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CalculatorParser parser = new CalculatorParser(tokens);
        ParseTree tree = parser.input();
        CalculatorBaseVisitorImpl calcVisitor = new CalculatorBaseVisitorImpl();
        Double result = calcVisitor.visit(tree);

        assertEquals(expected,result);
    }
}



