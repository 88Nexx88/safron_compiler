package gram;// Generated from C:/Users/Danila/IdeaProjects/safron++/src\safron.g4 by ANTLR 4.9.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link safronParser}.
 */
public interface safronListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link safronParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(safronParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link safronParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(safronParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link safronParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(safronParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link safronParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(safronParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link safronParser#vars_}.
	 * @param ctx the parse tree
	 */
	void enterVars_(safronParser.Vars_Context ctx);
	/**
	 * Exit a parse tree produced by {@link safronParser#vars_}.
	 * @param ctx the parse tree
	 */
	void exitVars_(safronParser.Vars_Context ctx);
	/**
	 * Enter a parse tree produced by {@link safronParser#format}.
	 * @param ctx the parse tree
	 */
	void enterFormat(safronParser.FormatContext ctx);
	/**
	 * Exit a parse tree produced by {@link safronParser#format}.
	 * @param ctx the parse tree
	 */
	void exitFormat(safronParser.FormatContext ctx);
	/**
	 * Enter a parse tree produced by {@link safronParser#function}.
	 * @param ctx the parse tree
	 */
	void enterFunction(safronParser.FunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link safronParser#function}.
	 * @param ctx the parse tree
	 */
	void exitFunction(safronParser.FunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link safronParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(safronParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link safronParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(safronParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link safronParser#assignstmt}.
	 * @param ctx the parse tree
	 */
	void enterAssignstmt(safronParser.AssignstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link safronParser#assignstmt}.
	 * @param ctx the parse tree
	 */
	void exitAssignstmt(safronParser.AssignstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link safronParser#breakstmt}.
	 * @param ctx the parse tree
	 */
	void enterBreakstmt(safronParser.BreakstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link safronParser#breakstmt}.
	 * @param ctx the parse tree
	 */
	void exitBreakstmt(safronParser.BreakstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link safronParser#continuestmt}.
	 * @param ctx the parse tree
	 */
	void enterContinuestmt(safronParser.ContinuestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link safronParser#continuestmt}.
	 * @param ctx the parse tree
	 */
	void exitContinuestmt(safronParser.ContinuestmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link safronParser#returnstmt}.
	 * @param ctx the parse tree
	 */
	void enterReturnstmt(safronParser.ReturnstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link safronParser#returnstmt}.
	 * @param ctx the parse tree
	 */
	void exitReturnstmt(safronParser.ReturnstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link safronParser#callstmt}.
	 * @param ctx the parse tree
	 */
	void enterCallstmt(safronParser.CallstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link safronParser#callstmt}.
	 * @param ctx the parse tree
	 */
	void exitCallstmt(safronParser.CallstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link safronParser#printstmt}.
	 * @param ctx the parse tree
	 */
	void enterPrintstmt(safronParser.PrintstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link safronParser#printstmt}.
	 * @param ctx the parse tree
	 */
	void exitPrintstmt(safronParser.PrintstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link safronParser#beginstmt}.
	 * @param ctx the parse tree
	 */
	void enterBeginstmt(safronParser.BeginstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link safronParser#beginstmt}.
	 * @param ctx the parse tree
	 */
	void exitBeginstmt(safronParser.BeginstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link safronParser#statements}.
	 * @param ctx the parse tree
	 */
	void enterStatements(safronParser.StatementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link safronParser#statements}.
	 * @param ctx the parse tree
	 */
	void exitStatements(safronParser.StatementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link safronParser#ifstmt}.
	 * @param ctx the parse tree
	 */
	void enterIfstmt(safronParser.IfstmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link safronParser#ifstmt}.
	 * @param ctx the parse tree
	 */
	void exitIfstmt(safronParser.IfstmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link safronParser#whilestmt}.
	 * @param ctx the parse tree
	 */
	void enterWhilestmt(safronParser.WhilestmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link safronParser#whilestmt}.
	 * @param ctx the parse tree
	 */
	void exitWhilestmt(safronParser.WhilestmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ltExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLtExpression(safronParser.LtExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ltExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLtExpression(safronParser.LtExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code gtExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterGtExpression(safronParser.GtExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code gtExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitGtExpression(safronParser.GtExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code minusExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMinusExpression(safronParser.MinusExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code minusExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMinusExpression(safronParser.MinusExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code signedIdent}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterSignedIdent(safronParser.SignedIdentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code signedIdent}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitSignedIdent(safronParser.SignedIdentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code leExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLeExpression(safronParser.LeExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code leExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLeExpression(safronParser.LeExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code modExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterModExpression(safronParser.ModExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code modExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitModExpression(safronParser.ModExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code notExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNotExpression(safronParser.NotExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code notExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNotExpression(safronParser.NotExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code divExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterDivExpression(safronParser.DivExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code divExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitDivExpression(safronParser.DivExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parenExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterParenExpression(safronParser.ParenExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parenExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitParenExpression(safronParser.ParenExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code signedFloat}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterSignedFloat(safronParser.SignedFloatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code signedFloat}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitSignedFloat(safronParser.SignedFloatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code noteqExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNoteqExpression(safronParser.NoteqExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code noteqExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNoteqExpression(safronParser.NoteqExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code orExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterOrExpression(safronParser.OrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code orExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitOrExpression(safronParser.OrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code eqExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterEqExpression(safronParser.EqExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code eqExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitEqExpression(safronParser.EqExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code andExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAndExpression(safronParser.AndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code andExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAndExpression(safronParser.AndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code signedNumber}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterSignedNumber(safronParser.SignedNumberContext ctx);
	/**
	 * Exit a parse tree produced by the {@code signedNumber}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitSignedNumber(safronParser.SignedNumberContext ctx);
	/**
	 * Enter a parse tree produced by the {@code geExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterGeExpression(safronParser.GeExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code geExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitGeExpression(safronParser.GeExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code plusExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterPlusExpression(safronParser.PlusExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code plusExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitPlusExpression(safronParser.PlusExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code starExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterStarExpression(safronParser.StarExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code starExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitStarExpression(safronParser.StarExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link safronParser#ident}.
	 * @param ctx the parse tree
	 */
	void enterIdent(safronParser.IdentContext ctx);
	/**
	 * Exit a parse tree produced by {@link safronParser#ident}.
	 * @param ctx the parse tree
	 */
	void exitIdent(safronParser.IdentContext ctx);
	/**
	 * Enter a parse tree produced by {@link safronParser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumber(safronParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link safronParser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumber(safronParser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link safronParser#num_float}.
	 * @param ctx the parse tree
	 */
	void enterNum_float(safronParser.Num_floatContext ctx);
	/**
	 * Exit a parse tree produced by {@link safronParser#num_float}.
	 * @param ctx the parse tree
	 */
	void exitNum_float(safronParser.Num_floatContext ctx);
}