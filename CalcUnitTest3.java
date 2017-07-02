package de.dhbw.app.mathinator;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

import de.dhbw.app.mathinator.calculator.CalculatorBaseVisitorImpl;
import de.dhbw.app.mathinator.calculator.CalculatorLexer;
import de.dhbw.app.mathinator.calculator.CalculatorParser;

import static org.junit.Assert.assertEquals;

/**
 * Created by TSDHBW on 02.07.2017.
 */

public class CalcUnitTest3 {
    @Test
    public void checkResult() throws Exception {

        /*Testgleichung um Vorrangregel zu überprüfen*/
        String equationInput = "(2^2)^3";

        ANTLRInputStream input = null;
        input = new ANTLRInputStream((equationInput.toString()));
        Double expected = 64.0;
        CalculatorLexer lexer = new CalculatorLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CalculatorParser parser = new CalculatorParser(tokens);
        ParseTree tree = parser.input();
        CalculatorBaseVisitorImpl calcVisitor = new CalculatorBaseVisitorImpl();
        Double result = calcVisitor.visit(tree);

        assertEquals(expected, result);
    }
}