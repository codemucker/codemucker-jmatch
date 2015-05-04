/* -----------------------------------------------------------------------------
 * Visitor.java
 * -----------------------------------------------------------------------------
 *
 * Producer : com.parse2.aparse.Parser 2.5
 * Produced : Mon May 04 20:57:29 CST 2015
 *
 * -----------------------------------------------------------------------------
 */

package org.codemucker.jmatch.expression.parser;

public interface Visitor
{
  public Object visit(Rule_MATCHERS rule);
  public Object visit(Rule_MATCHER rule);
  public Object visit(Rule_MTYPE rule);
  public Object visit(Rule_EXPR rule);
  public Object visit(Rule_GFILTERS rule);
  public Object visit(Rule_FILTERS rule);
  public Object visit(Rule_FILTER rule);
  public Object visit(Rule_ATTNAME rule);
  public Object visit(Rule_ATTVALEXPR rule);
  public Object visit(Rule_ATTVAL rule);
  public Object visit(Rule_INTVAL rule);
  public Object visit(Rule_QVAL rule);
  public Object visit(Rule_IVAL rule);
  public Object visit(Rule_ANTEXPR rule);
  public Object visit(Rule_VAREXPR rule);
  public Object visit(Rule_VARNAME rule);
  public Object visit(Rule_QCHAR rule);
  public Object visit(Rule_GOPEN rule);
  public Object visit(Rule_GCLOSE rule);
  public Object visit(Rule_RANGE rule);
  public Object visit(Rule_BOOL rule);
  public Object visit(Rule_NULL rule);
  public Object visit(Rule_ALPHANUM rule);
  public Object visit(Rule_CHAR rule);
  public Object visit(Rule_VCHAR_MD rule);
  public Object visit(Rule_VCHAR_MS rule);
  public Object visit(Rule_VCHAR rule);
  public Object visit(Rule_ALPHA rule);
  public Object visit(Rule_DIGIT rule);
  public Object visit(Rule_LWSP rule);
  public Object visit(Rule_WSP rule);
  public Object visit(Rule_DQUOTE rule);
  public Object visit(Rule_SQUOTE rule);
  public Object visit(Rule_OR rule);
  public Object visit(Rule_AND rule);
  public Object visit(Rule_NOT rule);
  public Object visit(Rule_NOT_EQ rule);
  public Object visit(Rule_LESS rule);
  public Object visit(Rule_GREATER rule);
  public Object visit(Rule_LESS_EQ rule);
  public Object visit(Rule_GREATER_EQ rule);
  public Object visit(Rule_EQ rule);
  public Object visit(Rule_PLUS rule);
  public Object visit(Rule_NEG rule);

  public Object visit(Terminal_StringValue value);
  public Object visit(Terminal_NumericValue value);
}

/* -----------------------------------------------------------------------------
 * eof
 * -----------------------------------------------------------------------------
 */
