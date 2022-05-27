// Generated from C:/Users/Danila/IdeaProjects/safron++/src\safron.g4 by ANTLR 4.9.2
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link safronParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface safronVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link safronParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(safronParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link safronParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(safronParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link safronParser#vars_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVars_(safronParser.Vars_Context ctx);
	/**
	 * Visit a parse tree produced by {@link safronParser#format}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormat(safronParser.FormatContext ctx);
	/**
	 * Visit a parse tree produced by {@link safronParser#function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction(safronParser.FunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link safronParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(safronParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link safronParser#assignstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignstmt(safronParser.AssignstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link safronParser#breakstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreakstmt(safronParser.BreakstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link safronParser#continuestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinuestmt(safronParser.ContinuestmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link safronParser#returnstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnstmt(safronParser.ReturnstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link safronParser#callstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallstmt(safronParser.CallstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link safronParser#printstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrintstmt(safronParser.PrintstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link safronParser#beginstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBeginstmt(safronParser.BeginstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link safronParser#statements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatements(safronParser.StatementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link safronParser#ifstmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfstmt(safronParser.IfstmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link safronParser#whilestmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhilestmt(safronParser.WhilestmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ltExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLtExpression(safronParser.LtExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code gtExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGtExpression(safronParser.GtExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code minusExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMinusExpression(safronParser.MinusExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code signedIdent}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSignedIdent(safronParser.SignedIdentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code leExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLeExpression(safronParser.LeExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code modExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModExpression(safronParser.ModExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code notExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExpression(safronParser.NotExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code divExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDivExpression(safronParser.DivExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parenExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenExpression(safronParser.ParenExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code signedFloat}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSignedFloat(safronParser.SignedFloatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code noteqExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNoteqExpression(safronParser.NoteqExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code orExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpression(safronParser.OrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code eqExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqExpression(safronParser.EqExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code andExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpression(safronParser.AndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code signedNumber}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSignedNumber(safronParser.SignedNumberContext ctx);
	/**
	 * Visit a parse tree produced by the {@code geExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeExpression(safronParser.GeExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code plusExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlusExpression(safronParser.PlusExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code starExpression}
	 * labeled alternative in {@link safronParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStarExpression(safronParser.StarExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link safronParser#ident}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdent(safronParser.IdentContext ctx);
	/**
	 * Visit a parse tree produced by {@link safronParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(safronParser.NumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link safronParser#num_float}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNum_float(safronParser.Num_floatContext ctx);
}